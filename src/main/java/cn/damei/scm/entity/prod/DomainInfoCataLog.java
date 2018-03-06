package cn.damei.scm.entity.prod;

import cn.damei.scm.common.IdEntity;

public class DomainInfoCataLog extends IdEntity {


    private Long domainInfoId;

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
