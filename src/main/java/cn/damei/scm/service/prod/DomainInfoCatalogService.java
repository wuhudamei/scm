package cn.damei.scm.service.prod;

import cn.damei.scm.common.service.CrudService;
import cn.damei.scm.entity.prod.DomainInfoCataLog;
import cn.damei.scm.repository.prod.DomainInfoCatalogDao;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DomainInfoCatalogService extends CrudService<DomainInfoCatalogDao, DomainInfoCataLog> {


    public List<DomainInfoCataLog> findDomainInfoByParentId(Long parentId) {
        return this.entityDao.findDomainInfoByParentId(parentId);
    }
}
