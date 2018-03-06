package cn.damei.scm.service.prod;

import cn.damei.scm.entity.prod.DomainInfo;
import cn.damei.scm.repository.prod.DomainInfoDao;
import cn.damei.scm.common.service.CrudService;
import org.springframework.stereotype.Service;

@Service
public class DomainInfoService extends CrudService<DomainInfoDao,DomainInfo>{
}
