package cn.damei.scm.repository.prod;

import cn.damei.scm.common.MyBatisRepository;
import cn.damei.scm.common.persistence.CrudDao;
import cn.damei.scm.entity.prod.DomainInfo;

@MyBatisRepository
public interface DomainInfoDao extends CrudDao<DomainInfo>{
}
