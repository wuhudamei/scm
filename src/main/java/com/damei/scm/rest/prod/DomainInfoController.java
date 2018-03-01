package com.damei.scm.rest.prod;

import com.damei.scm.common.BaseComController;
import com.damei.scm.common.dto.StatusDto;
import com.damei.scm.entity.prod.DomainInfo;
import com.damei.scm.entity.prod.DomainInfoCataLog;
import com.damei.scm.service.prod.DomainInfoCatalogService;
import com.damei.scm.service.prod.DomainInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/domaininfo")
public class DomainInfoController extends BaseComController<DomainInfoService,DomainInfo> {

    @Autowired
    private DomainInfoCatalogService domainInfoCatalogService;

    @RequestMapping("/finddomaininfoname")
    public Object domainInfoName(){
       return StatusDto.buildDataSuccessStatusDto(service.findAll());
    }

    @RequestMapping("/finddomaininfocatalogbyid")
    public  Object findDomainInfoCatalogById(@RequestParam(required = false) Long parentId){
       List<DomainInfoCataLog> domainInfoCataLogList =  domainInfoCatalogService.findDomainInfoByParentId(parentId);
       return StatusDto.buildDataSuccessStatusDto(domainInfoCataLogList);
    }
}
