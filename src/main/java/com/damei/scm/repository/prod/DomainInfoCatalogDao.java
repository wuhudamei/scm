package com.damei.scm.repository.prod;

import com.damei.scm.common.MyBatisRepository;
import com.damei.scm.common.persistence.CrudDao;
import com.damei.scm.entity.prod.DomainInfoCataLog;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@MyBatisRepository
public interface DomainInfoCatalogDao extends CrudDao<DomainInfoCataLog>{

    List<DomainInfoCataLog> findDomainInfoByParentId(@Param("parentId") long cataLogId);
}
