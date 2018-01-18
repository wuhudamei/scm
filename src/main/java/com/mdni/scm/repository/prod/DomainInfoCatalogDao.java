package com.mdni.scm.repository.prod;

import com.mdni.scm.common.MyBatisRepository;
import com.mdni.scm.common.persistence.CrudDao;
import com.mdni.scm.entity.prod.DomainInfoCataLog;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by 刘铎 on 2017/11/2.
 */
@MyBatisRepository
public interface DomainInfoCatalogDao extends CrudDao<DomainInfoCataLog>{

    List<DomainInfoCataLog> findDomainInfoByParentId(@Param("parentId") long cataLogId);
}
