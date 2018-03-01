package com.damei.scm.service.prod;

import com.damei.scm.common.service.CrudService;
import com.damei.scm.entity.prod.DomainInfoCataLog;
import com.damei.scm.repository.prod.DomainInfoCatalogDao;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DomainInfoCatalogService extends CrudService<DomainInfoCatalogDao, DomainInfoCataLog> {


    public List<DomainInfoCataLog> findDomainInfoByParentId(Long parentId) {
        return this.entityDao.findDomainInfoByParentId(parentId);
    }
}
