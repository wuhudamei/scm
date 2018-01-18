package com.mdni.scm.entity.prod;

import com.mdni.scm.common.IdEntity;

/**
 * Created by 刘铎 on 2017/11/2.
 */
public class DomainInfoCataLog extends IdEntity{

    //功能区
    private Long domainInfoId;
    //类目
    private Long catalogId;

    private String domainName;

    public String getDomainName() {
        return domainName;
    }

    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }

    public Long getDomainInfoId() {
        return domainInfoId;
    }

    public void setDomainInfoId(Long domainInfoId) {
        this.domainInfoId = domainInfoId;
    }

    public Long getCatalogId() {
        return catalogId;
    }

    public void setCatalogId(Long catalogId) {
        this.catalogId = catalogId;
    }
}
