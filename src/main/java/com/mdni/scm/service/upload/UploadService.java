package com.mdni.scm.service.upload;

/**
 * Created by weiys on 16/4/15.
 */

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import net.coobird.thumbnailator.Thumbnails;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.HtmlUtils;

import com.google.common.collect.Maps;
import com.mdni.scm.common.PropertyHolder;
import com.mdni.scm.common.dto.XhEditorUploadDto;
import com.mdni.scm.common.service.ServiceException;

/**
 * 文件上传服务。
 * <p/>
 * 上传处理过程: 浏览器填写表单 -> 选择文件上传 -> 服务器将文件保存到临时路径 -> 浏览器显示上传的图片 -> 浏览器提交表单 -> 服务器将文件从临时目录移至持久目录 -> 删除旧图片(如果有) -> 保存新图片路径.
 * <p/>
 * 解决以下问题： 1. 用户可能上传了图片，但没有提交表单，那么用户之前上传过的图片就永远不会被访问到。这种图片应该被删除。 2. 当富文本内容从数据库中删除时，富文本中图片路径对应的图片文件也应该被删除。 3.
 * 当更新富文本内容时，应该删除旧富文本中引用的但新富文本中不再引用的上传图片（或移至已删除目录）。
 * 
 * @author zhangmin
 */
@SuppressWarnings("all")
@Component
@Transactional(isolation = Isolation.SERIALIZABLE)
public class UploadService {
	public static final String TMP_BASE_PATH = "tmp/";// System.getProperty("user.dir")+
	public static final String REQUEST_KEY = "request";
	public static final String USER_ID = "userid";
	private static final SimpleDateFormat DATE_PATH_SDF = new SimpleDateFormat("yyyy/MM/dd");
	// 匹配 src="tmp/xxx"
	// 有点复杂，[^\\1]不匹配1，所以写成([^\\1]|1)
	private static final Pattern tmpImgSrcPattern = Pattern.compile(" src=(\"|')(" + TMP_BASE_PATH
		+ "([^\\\\1]|1)+?)\\1");
	private static final Pattern imgSrcPattern = Pattern.compile(" src=(\"|')(([^\\\\1]|1)+?)\\1");
	public static ThreadLocal<Map<String, Object>> uploadThreadLocal = new ThreadLocal<Map<String, Object>>();
	protected static Map<String, ThumbnailProperty[]> thumbnailPropertyMap;// 缩略图属性
	protected static List<String> XH_EDITOR_ALLOWED_TYPE = Arrays.asList("jpg", "jpeg", "gif", "png");
	private Logger logger = LoggerFactory.getLogger(UploadService.class);
	//文件上传 基 目录
	@Value("${upload.dir}")
	private String uploadDir;
	@Value("${image.base.url}")
	private String imageBaseUrl;
	@Value("${upload.maxfilesize}")
	private long maxUploadBytes;

	@Autowired
	private SequenceService sequenceService;

	public static void setInfoInThreadLocal(String key, Object value) {
		Map<String, Object> map = uploadThreadLocal.get();
		if (map == null) {
			map = new HashMap<String, Object>();
			uploadThreadLocal.set(map);
		}
		map.put(key, value);
	}

	public static void setRequestAndUserId(HttpServletRequest req, Long userId) {
		setInfoInThreadLocal(REQUEST_KEY, req);
		setInfoInThreadLocal(USER_ID, userId);
	}

	/**
	 * 初始化缩略图数据
	 * 
	 * @return
	 * @author： 张文山
	 * @创建时间：2015-9-1 下午5:59:32
	 */
	protected Map<String, ThumbnailProperty[]> initThumbnailPropertyMap() {
		return Maps.newHashMap();
	}

	// 获取某个图片类型下的缩略图信息
	public ThumbnailProperty[] getThumbnailPropertyByType(UploadCategory uploadCategory) {
		if (thumbnailPropertyMap == null) {
			synchronized (ThumbnailProperty.class) {
				if (thumbnailPropertyMap == null) {
					thumbnailPropertyMap = initThumbnailPropertyMap();
				}
			}
		}
		return thumbnailPropertyMap.get(uploadCategory.getPath());
	}

	/**
	 * 保存上传的文件，返回文件保存路径. 文件将保存在临时目录。
	 * 
	 * @param file 上传文件
	 * @param uploadCategory 类别.文件会存储在该类别对应的子目录下。
	 * @return 返回文件保存路径。如tmp/2014/09/04/1.jpg
	 * @throws ServiceException 发生错误时
	 */
	public String upload(MultipartFile file, UploadCategory uploadCategory) {
		validateUploadFile(file, uploadCategory);
		InputStream input;
		try {
			input = file.getInputStream();
		} catch (IOException e) {
			logger.debug("读取上传文件输入流发生错误", e);
			throw new ServiceException("读取上传文件发生错误");
		}
		String ext = FilenameUtils.getExtension(file.getOriginalFilename());
		return saveTmp(input, ext, uploadCategory.getPath());
	}

	public String upload(InputStream stream, UploadCategory uploadCategory, String ext) {
		return saveTmp(stream, ext, uploadCategory.getPath());
	}

	/**
	 * 上传图片校验
	 * 
	 * @param file
	 * @param uploadCategory
	 * @author： 张文山
	 * @创建时间：2015-9-1 下午5:47:41
	 */
	public void validateUploadFile(MultipartFile file, UploadCategory uploadCategory) {
		if (file.isEmpty()) {
			throw new ServiceException("上传文件不能为空");
		}

		if (StringUtils.isBlank(file.getOriginalFilename())) {
			throw new ServiceException("上传文件名不能为空");
		}

		String ext = FilenameUtils.getExtension(file.getOriginalFilename());
		if (StringUtils.isBlank(ext)) {
			throw new ServiceException("上传文件扩展名不能为空");
		}

		if (file.getSize() > maxUploadBytes) {
			throw new ServiceException("上传文件不能大于 " + FileUtils.byteCountToDisplaySize(maxUploadBytes));
		}
	}

	/**
	 * 将文件从临时目录移至普通目录
	 * 
	 * @param tmpPath 文件临时路径。如tmp/product/2014/09/04/1.jpg
	 * @return 如果tmpPath是临时路径，则返回新路径，如product/2014/09/04/1.jpg。否则返回原路径tmpPath
	 */
	public String submitPath(String tmpPath) {
		moveFileBefore(tmpPath);
		tmpPath = this.mockImageBasePath(tmpPath);

		if (!tmpPath.startsWith(TMP_BASE_PATH)) {
			return tmpPath;
		}
		File tmpFile = new File(uploadDir, tmpPath);
		logger.info("文件零时地址:{}", tmpFile.getAbsolutePath());
		String destPath = tmpPath.substring(TMP_BASE_PATH.length());
		File destFile = new File(uploadDir, destPath);
		if (!tmpFile.exists() && destFile.exists()) {
			// 当系统保存表单，已将一部分图片移至持久目录，处理后面事务发生错误，有可能符合业务规则或其他原因，事务回滚，
			// 用户修改表单后，再次提交，就会tmpFile不存在destFile已存在的情况。这种情况应该返回destPath。
			// 实际例子：添加商品时，填写多个sku，如果第1个sku之后的某个sku编码已存在，就会出现这种情况。
			return destPath;
		}

		if (!destFile.getParentFile().exists()) {
			if (!destFile.getParentFile().mkdirs()) {
				throw new ServiceException("创建目录失败" + destFile.getParentFile().getAbsolutePath());
			}
		}
		try {

			//generateThumbnailImage(tmpFile,
			// destPath);// 生成缩略图

			if (parseCatalogType(tmpPath).getPath().toLowerCase().endsWith("import")) {
				// 有时候excel文件被占用，内存还没有释放，移动会失败!
				FileUtils.copyFile(tmpFile, destFile);// 移动图片
			} else {
				FileUtils.moveFile(tmpFile, destFile);
			}
			moveFileAfter(destFile, tmpPath);
		} catch (IOException e) {
			logger.warn("移动文件失败", e);
			throw new ServiceException("移动文件失败");
		}
		return destPath;
	}

	protected void moveFileBefore(String tmpPath) {
	}

	protected void moveFileAfter(File file, String tmpPath) {
		try {
			// 记录日志
			UploadCategory category = this.parseCatalogType(tmpPath);
			if (!category.getPath().toLowerCase().endsWith("import")) {
				// return;
			}
			String ip = null;
			Long userId = null;
			// 说明是excel导入
			Map<String, Object> map = uploadThreadLocal.get();
			if (map != null) {
				HttpServletRequest request = (HttpServletRequest) map.get(REQUEST_KEY);
				if (request != null) {
					ip = request.getRemoteAddr();
				}
				userId = (Long) map.get(USER_ID);
			}
			logger
				.info("upload excel .上传时间【{}】,用户id【{}】.类型【{}】.用户ip【{}】.文件路径【{}】",
					DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"), userId, category, ip,
					file.getAbsolutePath());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 解析excel文件
	 */
	public File submitPathAndGetfile(String path) {
		// 先解析excel
		String newPath = submitPath(path);
		return new File(uploadDir, newPath);
	}

	/**
	 * @param tmpFile 零时图片
	 * @param destPath 移动图片的目标路径 .格式 imagetype/2015/06/12/21/32.jpg
	 * @throws IOException
	 * @author： 张文山
	 * @创建时间：2015-9-1 下午6:18:20
	 */
	public void generateThumbnailImage(File tmpFile, String destPath) throws IOException {
		// 生成缩略图
		ThumbnailProperty[] propertys = getThumbnailPropertyByType(parseCatalogType(destPath));
		// propertys== null 表示是excel文件或者一些不需要生成缩略图的照片类型
		if (propertys != null) {
			for (ThumbnailProperty p : propertys) {
				Thumbnails.of(tmpFile).size(p.getWidth(), p.getHeight())
					.toFile(new File(uploadDir, getFilePathByType(p, destPath)));
			}
		}
	}

	/**
	 * 根据缩略图对象生成文件路径
	 * 
	 * @param p
	 * @param destPath
	 * @return
	 * @author： 张文山
	 * @创建时间：2015-9-1 下午5:55:12
	 */
	private String getFilePathByType(ThumbnailProperty p, String destPath) {
		return new StringBuilder(destPath).append(".").append(p.getWidth()).append("_").append(p.getHeight())
			.append(".jpg").toString();
	}

	/**
	 * 通过路径解析文件类型
	 * 
	 * @param path
	 * @return
	 * @author： 张文山
	 * @创建时间：2015-9-10 下午1:35:48
	 */
	public UploadCategory parseCatalogType(String path) {
		path = getDbPath(path);
		String type = StringUtils.split(path, "/")[0];
		return UploadCategory.parsePathToCategory(type);
	}

	/**
	 * 将html中临时图片地址替换为永久图片地址.
	 * 
	 * @param html 含有临时图片地址img标签的html.通常是前端富文本编辑器产生的。
	 * @return 返回新html.已将临时图片地址替换为永久图片地址.
	 */
	public String submitHtml(String html) {
		if (StringUtils.isNotBlank(html)) {
			html = HtmlUtils.htmlUnescape(html);
			StringBuilder newHtml = new StringBuilder();
			int pos = 0;
			Matcher matcher = imgSrcPattern.matcher(html);
			while (matcher.find()) {
				String src = matcher.group(2);
				if (src.startsWith(imageBaseUrl)) {
					String path = mockImageBasePath(src);
					newHtml.append(html.substring(pos, matcher.start(2)));
					newHtml.append(PropertyHolder.getFullImageUrl(submitPath(path)));
					pos = matcher.end(2);
				}
			}
			newHtml.append(html.substring(pos));
			return newHtml.toString();
		}
		return html;
	}

	/**
	 * 截取地址前半部分
	 */
	private String mockImageBasePath(String url) {
		if (!url.startsWith(imageBaseUrl)) {
			return url;
		}
		return url.substring(imageBaseUrl.length());
	}

	private String getDbPath(String path) {
		path = mockImageBasePath(path);
		if (path.startsWith("/")) {
			path = path.substring(1);
		}
		if (path.startsWith(TMP_BASE_PATH)) {
			path = path.substring(TMP_BASE_PATH.length());
		}
		return path;
	}

	/**
	 * 仅测试用
	 */
	private String mockSubmitPath(String tmpPath) {
		if (!tmpPath.startsWith(TMP_BASE_PATH)) {
			return tmpPath;
		}
		return tmpPath.substring(TMP_BASE_PATH.length());
	}

	/**
	 * 该方法用于更新可能含有上传的文件路径的html。删除旧的不用的图片，保存新图片(从临时目录移至持久目录)。
	 * 
	 * @param oldHtml 旧的可能含有上传的图片地址img标签的html
	 * @param newHtml 新的可能含有上传的图片地址img标签的html
	 * @return 返回新的html
	 */
	public String updateHtml(String oldHtml, String newHtml) {
		// 删除旧的不用的图片
		if (StringUtils.isNotBlank(oldHtml)) {
			List<String> pathsInOldHtml = findPathsInHtml(oldHtml);
			List<String> pathsInNewHtml = findPathsInHtml(newHtml);
			for (String oldPath : pathsInOldHtml) {
				if (!pathsInNewHtml.contains(oldPath)) {
					delete(oldPath);
				}
			}
		}
		// 保存新图片
		return submitHtml(newHtml);
	}

	/**
	 * 删除html中图片路径对应的图片文件
	 * 
	 * @param html 可能引用上传文件的html
	 * @return 返回删除的文件路径
	 */
	public List<String> deleteByPathInHtml(String html) {
		if (StringUtils.isNotBlank(html)) {
			List<String> paths = findPathsInHtml(html);
			for (String path : paths) {
				delete(path);
			}
			return paths;
		}
		// noinspection unchecked
		return Collections.emptyList();
	}

	/**
	 * 查找html中出现的上传的文件路径
	 * 
	 * @param html 可能包含上传的文件路径的html
	 * @return 返回上传的文件路径. 不排除tmp路径
	 */
	private List<String> findPathsInHtml(String html) {
		List<String> paths = new ArrayList<String>();
		Matcher matcher = imgSrcPattern.matcher(html);
		while (matcher.find()) {
			String src = matcher.group(2);
			if (src.startsWith(imageBaseUrl)) {
				paths.add(mockImageBasePath(src));
			}
		}
		return paths;
	}

	/**
	 * 删除指定路径的单个文件
	 * 
	 * @param saveRelatePath 上传的文件路径 例如：product/2012/11/12/1.jpg
	 */
	public void delete(String saveRelatePath) {
		if (StringUtils.isBlank(saveRelatePath)) {
			return;
		}

		char first = saveRelatePath.charAt(0);
		if (first == '/' || first == '.') {
			return;
		}

		File file = new File(uploadDir, saveRelatePath);
		if (file.exists()) {
			if (!file.delete()) {
				logger.warn("删除 {} 失败", file.getAbsolutePath());
			}
		}
	}

	/**
	 * 删除缩略图
	 * 
	 * @param path
	 * @author： 张文山
	 * @创建时间：2015-9-2 上午10:17:14
	 */
	public void deleteThumbnailImage(String path) {
		ThumbnailProperty[] propertys = getThumbnailPropertyByType(parseCatalogType(path));
		File file = null;
		if (propertys != null) {
			for (ThumbnailProperty p : propertys) {
				file = new File(uploadDir, getFilePathByType(p, path));
				if (file.exists()) {
					if (!file.delete()) {
						logger.warn("删除 {} 失败", file.getAbsolutePath());
					}
				}
			}
		}
	}

	/**
	 * 保存文件内容到临时目录，返回文件路径.
	 * 
	 * @param input 文件内容
	 * @param ext 文件扩展名，如jpg，不带"."
	 * @param categoryPath 类别路径。如product
	 * @return 返回保存的文件路径. 如tmp/product/2014/09/04/1.jpg
	 * @throws ServiceException 当发生错误时
	 */
	private String saveTmp(InputStream input, String ext, String categoryPath) {
		Assert.state(StringUtils.isNotBlank(ext));
		Assert.notNull(input);

		String path = new StringBuilder(TMP_BASE_PATH).append(categoryPath).append("/")
			.append(DATE_PATH_SDF.format(new Date())).append("/")
			.append(sequenceService.getNextVal(SequenceService.SequenceTable.UPLOAD)).append(".").append(ext)
			.toString();

		save(path, input);
		return path;
	}

	/**
	 * 保存文件内容到上传目录的指定路径.
	 * 
	 * @param path 文件路径 如tmp/2014/09/04/1.png
	 * @param input 文件内容
	 * @throws ServiceException 当发生错误时
	 */
	private void save(String path, InputStream input) {
		Assert.state(StringUtils.isNotBlank(uploadDir));
		Assert.state(StringUtils.isNotBlank(path));

		BufferedInputStream bufInput = null;
		BufferedOutputStream bufOut = null;

		try {
			File dest = new File(uploadDir, path);
			if (!dest.getParentFile().exists()) {
				if (!dest.getParentFile().mkdirs()) {
					throw new ServiceException("创建目录失败 " + dest.getParentFile().getAbsolutePath());
				}
			}

			bufInput = new BufferedInputStream(input);
			bufOut = new BufferedOutputStream(new FileOutputStream(dest));

			IOUtils.copy(bufInput, bufOut);
		} catch (IOException e) {
			logger.warn("保存文件出错", e);
			throw new ServiceException("保存文件出错");
		} finally {
			IOUtils.closeQuietly(bufOut);
			IOUtils.closeQuietly(bufInput);
		}
	}

	/**
	 * 删除临时目录中1天之前上传的文件
	 */
	@SuppressWarnings("ConstantConditions")
	public void scheduleCleanTmpFile() throws ParseException, IOException {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		Date deleteBefore = calendar.getTime();

		// 目录结构如 tmp/product/2014/09/01/1.jpg
		File tmp = new File(uploadDir, TMP_BASE_PATH);
		if (tmp.exists()) {
			for (File catalogDir : tmp.listFiles()) {
				if (catalogDir.isHidden() || !catalogDir.isDirectory()) {
					continue;
				}
				for (File yearDir : catalogDir.listFiles()) {
					if (yearDir.isHidden() || !yearDir.isDirectory()) {
						continue;
					}
					for (File monthDir : yearDir.listFiles()) {
						if (monthDir.isHidden() || !monthDir.isDirectory()) {
							continue;
						}
						for (File dateDir : monthDir.listFiles()) {
							if (dateDir.isHidden() || !dateDir.isDirectory()) {
								continue;
							}
							Date dirDate = DATE_PATH_SDF.parse(yearDir.getName() + "/" + monthDir.getName() + "/"
								+ dateDir.getName());
							if (dirDate.getTime() < deleteBefore.getTime()) {
								FileUtils.deleteDirectory(dateDir);
							}
						}
					}
				}
			}
		}
	}

	/**
	 * 处理xhEditor上传。
	 * 
	 * @param file 上传文件
	 * @param uploadCategory 类别。文件会保存在该类别对应的目录下。
	 * @return 返回符合xhEditor默认返回值格式的XhEditorUploadDto对象。
	 */
	public XhEditorUploadDto xhEditorUpload(MultipartFile file, UploadCategory uploadCategory) {
		XhEditorUploadDto dto = new XhEditorUploadDto();

		String ext = FilenameUtils.getExtension(file.getOriginalFilename()).toLowerCase();
		if (!XH_EDITOR_ALLOWED_TYPE.contains(ext)) {
			dto.setErr("只可上传 " + XH_EDITOR_ALLOWED_TYPE);
			return dto;
		}

		try {
			String path = upload(file, uploadCategory);
			dto.setMsg(PropertyHolder.getFullImageUrl(path));
		} catch (ServiceException e) {
			dto.setErr(e.getMessage());
		}
		return dto;
	}

	public String updateFile(String path, String oldPath) {
		if (StringUtils.isNotBlank(oldPath)) {
			this.delete(oldPath);
		}
		return this.submitPath(path);
	}

	/**
	 * 获取文件对象
	 */
	public File getFileByPath(String path) {
		path = mockImageBasePath(path);
		return new File(uploadDir, path);
	}

	/**
	 * 上传类别枚举类。
	 */
	public static enum UploadCategory {
		PRODUCT("product"), BRAND("brand"), CATALOG("catalog"), SUPPLIER("supplier");
		private String path;// path 和name格式要一样
		private String className;

		UploadCategory(String path) {
			this.path = path;
		}

		UploadCategory(String path, String className) {
			this.path = path;
			this.className = className;
		}

		public static UploadCategory parsePathToCategory(String path) {
			for (UploadCategory category : UploadCategory.values()) {
				if (StringUtils.equalsIgnoreCase(path, category.getPath())) {
					return category;
				}
			}
			return null;
		}

		public String getPath() {
			return path;
		}

		public String getClassName() {
			return className;
		}
	}

	/**
	 * <dl>
	 * <dd>描述:缩略图属性</dd>
	 * <dd>公司: 大城若谷信息技术有限公司</dd>
	 * <dd>@创建时间：2015-9-1 下午2:09:06</dd>
	 * <dd>@author： 张文山</dd>
	 * </dl>
	 */
	public static class ThumbnailProperty {
		private int width;
		private int height;
		private int size;

		public ThumbnailProperty(int width, int height) {
			super();
			this.width = width;
			this.height = height;
		}

		public ThumbnailProperty(int width, int height, int size) {
			super();
			this.width = width;
			this.height = height;
			this.size = size;
		}

		public int getWidth() {
			return width;
		}

		public void setWidth(int width) {
			this.width = width;
		}

		public int getHeight() {
			return height;
		}

		public void setHeight(int height) {
			this.height = height;
		}

		public int getSize() {
			return size;
		}

		public void setSize(int size) {
			this.size = size;
		}
	}
}
