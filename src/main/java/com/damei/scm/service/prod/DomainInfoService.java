package com.damei.scm.service.prod;

import com.damei.scm.common.service.CrudService;
import com.damei.scm.entity.prod.DomainInfo;
import com.damei.scm.repository.prod.DomainInfoDao;
import org.springframework.stereotype.Service;

@Service
public class DomainInfoService extends CrudService<DomainInfoDao,DomainInfo>{
}
