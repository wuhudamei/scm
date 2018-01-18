package com.mdni.scm.repository.prod;

import com.mdni.scm.common.MyBatisRepository;
import com.mdni.scm.common.persistence.CrudDao;
import com.mdni.scm.entity.prod.DomainInfo;

/**
 * Created by 刘铎 on 2017/11/2.
 */
@MyBatisRepository
public interface DomainInfoDao extends CrudDao<DomainInfo>{
}
