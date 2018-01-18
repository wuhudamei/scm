package com.mdni.scm.entity.prod;

import java.util.List;

import com.mdni.scm.entity.eum.RegionSupplierEnum;
import org.springframework.data.annotation.Transient;

import com.mdni.scm.common.IdEntity;

/**
 * <dl>
 * <dd>描述: 区域供应商</dd>
 * <dd>公司: 大城若谷信息技术有限公司</dd>
 * <dd>创建时间：2017年6月22日 下午1:27:59</dd>
 * <dd>创建人： 张敏</dd>
 * </dl>
 */
public class RegionSupplier extends IdEntity {

	private static final long serialVersionUID = 3472998551819184451L;
	//区域供应商名称
	private String name;
	
	//所属门店
	private Store store;

	private RegionSupplierEnum status;//状态

	//该区域下包含的商品供货商
	@Transient
	private List<Supplier> supplierList;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Supplier> getSupplierList() {
		return supplierList;
	}

	public void setSupplierList(List<Supplier> supplierList) {
		this.supplierList = supplierList;
	}

	public Store getStore() {
		return store;
	}

	public void setStore(Store store) {
		this.store = store;
	}

	public RegionSupplierEnum getStatus() {
		return status;
	}

	public void setStatus(RegionSupplierEnum status) {
		this.status = status;
	}
}
