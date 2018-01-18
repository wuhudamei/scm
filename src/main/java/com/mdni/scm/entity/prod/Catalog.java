package com.mdni.scm.entity.prod;

import com.mdni.scm.common.IdEntity;
import com.mdni.scm.entity.eum.CatalogTypeEnum;
import com.mdni.scm.entity.eum.StatusEnum;

import java.math.BigDecimal;
import java.util.List;

/**
 * 
 * <dl>
 * <dd>描述: 商品分类</dd>
 * <dd>公司: 大城若谷信息技术有限公司</dd>
 * <dd>创建时间：2017年5月10日 下午2:04:04</dd>
 * <dd>创建人： 张敏</dd>
 * </dl>
 */
public class Catalog extends IdEntity {

	private static final long serialVersionUID = -2239465519188073988L;

	public static final String CATA_URL_JOINER = "-";

	// 分类名称
	private String name;

	//分类路径
	private String url;

	//父分类
	private Catalog parent;

	//排序：数字越大越靠前
	private Integer seq;

	// 启用状态
	private StatusEnum status;
	//是否复尺
	private Integer checkScale;
	/**计算单位转换**/
	private String convertUnit;
	//类目的类型
	private CatalogTypeEnum catalogType;

	private Integer[] domainInfo;
	//损耗系数
	private BigDecimal lossFactor;
	//是否允许为小数
	private Integer useDecimal;
	//是否套餐类目
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