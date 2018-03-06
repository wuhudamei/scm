package cn.damei.scm.entity.prod;

import cn.damei.scm.common.IdEntity;
import cn.damei.scm.entity.eum.CatalogTypeEnum;
import cn.damei.scm.entity.eum.StatusEnum;

import java.math.BigDecimal;

public class Catalog extends IdEntity {

	private static final long serialVersionUID = -2239465519188073988L;

	public static final String CATA_URL_JOINER = "-";


	private String name;


	private String url;


	private Catalog parent;


	private Integer seq;


	private StatusEnum status;

	private Integer checkScale;

	private String convertUnit;

	private CatalogTypeEnum catalogType;

	private Integer[] domainInfo;

	private BigDecimal lossFactor;

	private Integer useDecimal;

	private Integer mealCategory;

	private String device;

	public String getDevice() {
		return device;
	}

	public void setDevice(String device) {
		this.device = device;
	}

	public Integer[] getDomainInfo() {
		return domainInfo;
	}

	public void setDomainInfo(Integer[] domainInfo) {
		this.domainInfo = domainInfo;
	}

	public CatalogTypeEnum getCatalogType() {
		return catalogType;
	}

	public void setCatalogType(CatalogTypeEnum catalogType) {
		this.catalogType = catalogType;
	}

	public BigDecimal getLossFactor() {
		return lossFactor;
	}

	public void setLossFactor(BigDecimal lossFactor) {
		this.lossFactor = lossFactor;
	}

	public Integer getUseDecimal() {
		return useDecimal;
	}

	public void setUseDecimal(Integer useDecimal) {
		this.useDecimal = useDecimal;
	}

	public Integer getMealCategory() {
		return mealCategory;
	}

	public void setMealCategory(Integer mealCategory) {
		this.mealCategory = mealCategory;
	}


	public Catalog() {
		super();
	}

	public Catalog(String name) {
		this();
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Catalog getParent() {
		return parent;
	}

	public void setParent(Catalog parent) {
		this.parent = parent;
	}

	public Integer getSeq() {
		return seq;
	}

	public void setSeq(Integer seq) {
		this.seq = seq;
	}

	public StatusEnum getStatus() {
		return status;
	}

	public void setStatus(StatusEnum status) {
		this.status = status;
	}

	public Integer getCheckScale() {return checkScale;}

	public void setCheckScale(Integer checkScale) {this.checkScale = checkScale;}

	public String getConvertUnit() {
		return convertUnit;
	}

	public void setConvertUnit(String convertUnit) {
		this.convertUnit = convertUnit;
	}
}