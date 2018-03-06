package cn.damei.scm.rest.prod;

import cn.damei.scm.common.BaseComController;
import cn.damei.scm.common.dto.StatusDto;
import cn.damei.scm.entity.prod.DomainInfo;
import cn.damei.scm.entity.prod.DomainInfoCataLog;
import cn.damei.scm.service.prod.DomainInfoCatalogService;
import cn.damei.scm.service.prod.DomainInfoService;
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
