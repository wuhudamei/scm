package cn.damei.scm.repository.prod;

import cn.damei.scm.common.MyBatisRepository;
import cn.damei.scm.common.persistence.CrudDao;
import cn.damei.scm.entity.prod.DomainInfoCataLog;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@MyBatisRepository
public interface DomainInfoCatalogDao extends CrudDao<DomainInfoCataLog> {

    List<DomainInfoCataLog> findDomainInfoByParentId(@Param("parentId") long cataLogId);
}
