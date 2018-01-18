package com.mdni.scm.service.prod;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import com.mdni.scm.entity.prod.DomainInfo;
import com.mdni.scm.entity.prod.DomainInfoCataLog;
import com.mdni.scm.repository.prod.DomainInfoCatalogDao;
import com.mdni.scm.repository.prod.DomainInfoDao;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.openxmlformats.schemas.drawingml.x2006.main.STAdjAngle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.mdni.scm.common.service.CrudService;
import com.mdni.scm.entity.eum.StatusEnum;
import com.mdni.scm.entity.prod.Catalog;
import com.mdni.scm.repository.prod.CatalogDao;
import com.mdni.scm.service.upload.UploadService;

/**
 * <dl>
 * <dd>描述: 商品分类Service</dd>
 * <dd>公司: 大城若谷信息技术有限公司</dd>
 * <dd>创建时间：2017年5月10日 下午2:09:05</dd>
 * <dd>创建人： 张敏</dd>
 * </dl>
 */
@Service
public class CatalogService extends CrudService<CatalogDao, Catalog> {

    @Autowired
    private UploadService uploadService;

    @Autowired
    private DomainInfoCatalogDao domainInfoCatalogDao;

    /**
     * @param parentId 父分类Id,如果为null,则返回顶级分类
     * @param status   启用状态,如果status状态为null,则返回所有状态
     * @return 返回子分类列表, 分类列表按照 seq 倒序排列
     */
    public List<Catalog> findSubCatalogList(final Long parentId, final StatusEnum status) {
        return this.entityDao.findSubCatalogList(parentId, status);
    }

    /**
     * 返回所有的叶子节点
     *
     * @param status
     */
    public List<Catalog> findLeafCatalogList(final StatusEnum status) {
        return this.entityDao.findLeafCatalogList(status);
    }

    /**
     * 查询某种状态的所有分类列表
     *
     * @param status 分类状态,null则返回所有状态
     */
    public List<Catalog> finalAllByStatus(final StatusEnum status) {
        return this.entityDao.finalAllByStatus(status);
    }

    public Catalog getByName(String name) {
        return entityDao.getByName(new Catalog(name));
    }

    public boolean isNameExists(Catalog catalog) {
        if (catalog == null)
            return false;

        return this.entityDao.getByName(catalog) != null;
    }

    /**
     * 创建或更新分类 1.处理url 2.将图片由临时文件转移
     */
    @Transactional
    public void saveOrUpdate(final Catalog catalog) {
        if (catalog == null) {
            return;
        }

        String url = StringUtils.EMPTY;
        if (catalog.getParent() != null && StringUtils.isNotBlank(catalog.getParent().getUrl())) {
            url = catalog.getParent().getUrl();
            Iterable<String> cataIdIter = Splitter.on(Catalog.CATA_URL_JOINER).omitEmptyStrings().split(url);
            long parentId = NumberUtils.toLong(Iterables.getLast(cataIdIter));
            //通过url获得分类Id
            catalog.getParent().setId(parentId);
        }

        if (catalog.getId() != null) {
            url = url + catalog.getId() + Catalog.CATA_URL_JOINER;
            catalog.setUrl(url);

            String[] domains = null;
            if (org.apache.commons.lang3.StringUtils.isNotBlank(catalog.getDevice())) {
                domainInfoCatalogDao.deleteById(catalog.getId());
                domains = catalog.getDevice().split(",");//获取到选择的功能区
                for (String domain : domains) {
                    DomainInfoCataLog domainInfoCataLog = new DomainInfoCataLog();
                    domainInfoCataLog.setCatalogId(catalog.getId());
                    domainInfoCataLog.setDomainInfoId(Long.parseLong(domain));
                    domainInfoCatalogDao.insert(domainInfoCataLog);
                }
            }
            entityDao.update(catalog);
        } else {
            entityDao.insert(catalog);
            url = url + catalog.getId() + Catalog.CATA_URL_JOINER;
            catalog.setUrl(url);
            entityDao.update(catalog);
        }
    }

    /**
     * 构建分类树
     */
    public List<Catalog> buildCatalogTree() {
        final List<Catalog> allCatalogs = findAll();
        if (!CollectionUtils.isEmpty(allCatalogs)) {
            final List<Catalog> prodTypeTree = Lists.newArrayList();

            for (final Catalog prodType : findTopCatalogs(allCatalogs)) {
                recurse(prodType, prodTypeTree, allCatalogs, 0);
            }
            return prodTypeTree;
        }

        return allCatalogs;
    }

    /**
     * 所有的顶级分类
     *
     * @param allCatalogs 系统中所有的分类
     */
    private List<Catalog> findTopCatalogs(List<Catalog> allCatalogs) {
        if (CollectionUtils.isEmpty(allCatalogs)) {
            return allCatalogs;
        }
        List<Catalog> topCatalogs = Lists.newArrayList();
        for (final Catalog cate : allCatalogs) {
            if (cate != null && (cate.getParent() == null || cate.getParent().getId() < 1)) {
                topCatalogs.add(cate);
            }
        }
        return topCatalogs;
    }

    private List<Catalog> findSubCatelogs(final long parentId, final List<Catalog> allCatalogs) {
        final List<Catalog> subCateList = Lists.newArrayList();
        for (final Catalog catalog : allCatalogs) {
            if (catalog != null && catalog.getParent() != null && catalog.getParent().getId() == parentId) {
                subCateList.add(catalog);
            }
        }
        return subCateList;
    }

    private void recurse(final Catalog curCatalog, final List<Catalog> tree, final List<Catalog> allCatalog, int depth) {
        if (curCatalog != null) {
            String name = curCatalog.getName();
            if (depth == 0) {
                name = "╋" + name;
            } else {
                String blank = "";
                for (int i = 0; i < depth; i++) {
                    blank += "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
                }
                name = blank + "├" + name;
            }
            curCatalog.setName(name);
            tree.add(curCatalog);

            List<Catalog> childCateList = findSubCatelogs(curCatalog.getId(), allCatalog);
            Iterator<Catalog> iter = childCateList.iterator();
            while (iter.hasNext()) {
                recurse(iter.next(), tree, allCatalog, depth + 1);
            }
        }
    }

    @Transactional
    public void open(Catalog catalog) {
        this.entityDao.open(catalog);
    }

    @Transactional
    public void lock(Catalog catalog) {
        this.entityDao.lock(catalog);
    }

    public Catalog getByUrl(String catalogUrl) {
        return this.entityDao.getByUrl(catalogUrl);
    }

    public List<Catalog> findCatalogByIsReject() {
        List<Catalog> allCatalogs = this.entityDao.findCatalogByIsReject();
        if (!CollectionUtils.isEmpty(allCatalogs)) {
            final List<Catalog> prodTypeTree = Lists.newArrayList();

            for (final Catalog prodType : findTopCatalogs(allCatalogs)) {
                recurse(prodType, prodTypeTree, allCatalogs, 0);
            }
            return prodTypeTree;
        }

        return allCatalogs;
    }

    public Integer findCatalogParent(Long parentId) {
       return this.entityDao.findCatalogParent(parentId);
    }
}