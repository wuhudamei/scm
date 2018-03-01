package com.damei.scm.service.upload;

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
import com.damei.scm.common.PropertyHolder;
import com.damei.scm.common.dto.XhEditorUploadDto;
import com.damei.scm.common.service.ServiceException;

@SuppressWarnings("all")
@Component
@Transactional(isolation = Isolation.SERIALIZABLE)
public class UploadService {
	public static final String TMP_BASE_PATH = "tmp/";// System.getProperty("user.dir")+
	public static final String REQUEST_KEY = "request";
	public static final String USER_ID = "userid";
	private static final SimpleDateFormat DATE_PATH_SDF = new SimpleDateFormat("yyyy/MM/dd");
	private static final Pattern tmpImgSrcPattern = Pattern.compile(" src=(\"|')(" + TMP_BASE_PATH
		+ "([^\\\\1]|1)+?)\\1");
	private static final Pattern imgSrcPattern = Pattern.compile(" src=(\"|')(([^\\\\1]|1)+?)\\1");
	public static ThreadLocal<Map<String, Object>> uploadThreadLocal = new ThreadLocal<Map<String, Object>>();
	protected static Map<String, ThumbnailProperty[]> thumbnailPropertyMap;// 缩略图属性
	protected static List<String> XH_EDITOR_ALLOWED_TYPE = Arrays.asList("jpg", "jpeg", "gif", "png");
	private Logger logger = LoggerFactory.getLogger(UploadService.class);
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


	protected Map<String, ThumbnailProperty[]> initThumbnailPropertyMap() {
		return Maps.newHashMap();
	}

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
			return destPath;
		}

		if (!destFile.getParentFile().exists()) {
			if (!destFile.getParentFile().mkdirs()) {
				throw new ServiceException("创建目录失败" + destFile.getParentFile().getAbsolutePath());
			}
		}
		try {

			if (parseCatalogType(tmpPath).getPath().toLowerCase().endsWith("import")) {
				FileUtils.copyFile(tmpFile, destFile);
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
			UploadCategory category = this.parseCatalogType(tmpPath);
			if (!category.getPath().toLowerCase().endsWith("import")) {
			}
			String ip = null;
			Long userId = null;
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

	public File submitPathAndGetfile(String path) {
		// 先解析excel
		String newPath = submitPath(path);
		return new File(uploadDir, newPath);
	}

	public void generateThumbnailImage(File tmpFile, String destPath) throws IOException {
		ThumbnailProperty[] propertys = getThumbnailPropertyByType(parseCatalogType(destPath));
		if (propertys != null) {
			for (ThumbnailProperty p : propertys) {
				Thumbnails.of(tmpFile).size(p.getWidth(), p.getHeight())
					.toFile(new File(uploadDir, getFilePathByType(p, destPath)));
			}
		}
	}

	private String getFilePathByType(ThumbnailProperty p, String destPath) {
		return new StringBuilder(destPath).append(".").append(p.getWidth()).append("_").append(p.getHeight())
			.append(".jpg").toString();
	}

	public UploadCategory parseCatalogType(String path) {
		path = getDbPath(path);
		String type = StringUtils.split(path, "/")[0];
		return UploadCategory.parsePathToCategory(type);
	}

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

	private String mockSubmitPath(String tmpPath) {
		if (!tmpPath.startsWith(TMP_BASE_PATH)) {
			return tmpPath;
		}
		return tmpPath.substring(TMP_BASE_PATH.length());
	}

	public String updateHtml(String oldHtml, String newHtml) {
		if (StringUtils.isNotBlank(oldHtml)) {
			List<String> pathsInOldHtml = findPathsInHtml(oldHtml);
			List<String> pathsInNewHtml = findPathsInHtml(newHtml);
			for (String oldPath : pathsInOldHtml) {
				if (!pathsInNewHtml.contains(oldPath)) {
					delete(oldPath);
				}
			}
		}
		return submitHtml(newHtml);
	}

	public List<String> deleteByPathInHtml(String html) {
		if (StringUtils.isNotBlank(html)) {
			List<String> paths = findPathsInHtml(html);
			for (String path : paths) {
				delete(path);
			}
			return paths;
		}
		return Collections.emptyList();
	}

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

	@SuppressWarnings("ConstantConditions")
	public void scheduleCleanTmpFile() throws ParseException, IOException {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		Date deleteBefore = calendar.getTime();

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

	public File getFileByPath(String path) {
		path = mockImageBasePath(path);
		return new File(uploadDir, path);
	}

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
