package com.mdni.scm.service.prod;

import com.mdni.scm.common.service.CrudService;
import com.mdni.scm.entity.prod.DomainInfo;
import com.mdni.scm.repository.prod.DomainInfoDao;
import org.springframework.stereotype.Service;

/**
 * Created by 刘铎 on 2017/11/2.
 */
@Service
public class DomainInfoService extends CrudService<DomainInfoDao,DomainInfo>{
}
