package com.mdni.scm.rest.prod;

import com.mdni.scm.common.BaseComController;
import com.mdni.scm.common.dto.StatusDto;
import com.mdni.scm.entity.prod.DomainInfo;
import com.mdni.scm.entity.prod.DomainInfoCataLog;
import com.mdni.scm.service.prod.DomainInfoCatalogService;
import com.mdni.scm.service.prod.DomainInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by 刘铎 on 2017/11/2.
 */
@RestController
@RequestMapping("/api/domaininfo")
public class DomainInfoController extends BaseComController<DomainInfoService,DomainInfo> {

    @Autowired
    private DomainInfoCatalogService domainInfoCatalogService;

    //查询功能区
    @RequestMapping("/finddomaininfoname")
    public Object domainInfoName(){
       return StatusDto.buildDataSuccessStatusDto(service.findAll());
    }

    //根据类目id查询功能区
    @RequestMapping("/finddomaininfocatalogbyid")
    public  Object findDomainInfoCatalogById(@RequestParam(required = false) Long parentId){
       List<DomainInfoCataLog> domainInfoCataLogList =  domainInfoCatalogService.findDomainInfoByParentId(parentId);
       return StatusDto.buildDataSuccessStatusDto(domainInfoCataLogList);
    }
}
