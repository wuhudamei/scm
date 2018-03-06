package cn.damei.scm.entity.prod;

import java.util.List;

import cn.damei.scm.common.IdEntity;
import cn.damei.scm.entity.eum.RegionSupplierEnum;
import org.springframework.data.annotation.Transient;

public class RegionSupplier extends IdEntity {

	private static final long serialVersionUID = 3472998551819184451L;

	private String name;
	

	private Store store;

	private RegionSupplierEnum status;

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
