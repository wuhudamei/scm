package com.damei.scm.service.prod;

import java.util.Iterator;
import java.util.List;

import com.damei.scm.entity.prod.DomainInfoCataLog;
import com.damei.scm.repository.prod.DomainInfoCatalogDao;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.damei.scm.common.service.CrudService;
import com.damei.scm.entity.eum.StatusEnum;
import com.damei.scm.entity.prod.Catalog;
import com.damei.scm.repository.prod.CatalogDao;
import com.damei.scm.service.upload.UploadService;

@Service
public class CatalogService extends CrudService<CatalogDao, Catalog> {

    @Autowired
    private UploadService uploadService;

    @Autowired
    private DomainInfoCatalogDao domainInfoCatalogDao;

    public List<Catalog> findSubCatalogList(final Long parentId, final StatusEnum status) {
        return this.entityDao.findSubCatalogList(parentId, status);
    }

    public List<Catalog> findLeafCatalogList(final StatusEnum status) {
        return this.entityDao.findLeafCatalogList(status);
    }

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
            catalog.getParent().setId(parentId);
        }

        if (catalog.getId() != null) {
            url = url + catalog.getId() + Catalog.CATA_URL_JOINER;
            catalog.setUrl(url);

            String[] domains = null;
            if (org.apache.commons.lang3.StringUtils.isNotBlank(catalog.getDevice())) {
                domainInfoCatalogDao.deleteById(catalog.getId());
                domains = catalog.getDevice().split(",");
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