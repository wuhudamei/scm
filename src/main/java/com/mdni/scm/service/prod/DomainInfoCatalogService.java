package com.mdni.scm.service.prod;

import com.mdni.scm.common.service.CrudService;
import com.mdni.scm.entity.prod.DomainInfoCataLog;
import com.mdni.scm.repository.prod.DomainInfoCatalogDao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by 刘铎 on 2017/11/2.
 */
@Service
public class DomainInfoCatalogService extends CrudService<DomainInfoCatalogDao, DomainInfoCataLog> {


    public List<DomainInfoCataLog> findDomainInfoByParentId(Long parentId) {
        return this.entityDao.findDomainInfoByParentId(parentId);
    }
}
