package com.damei.scm.service.prod;

import com.damei.scm.common.service.CrudService;
import com.damei.scm.common.utils.PingYinUtil;
import com.damei.scm.entity.eum.StatusEnum;
import com.damei.scm.entity.prod.Brand;
import com.damei.scm.repository.prod.BrandDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class BrandService extends CrudService<BrandDao, Brand> {

	public List<Brand> findTopBrandByStatusOrderByIdAsc(final StatusEnum status, final Integer topLimit) {
		return this.entityDao.findTopBrandByStatusOrderByIdAsc(status, topLimit);
	}

	public boolean isCodeExists(Brand brand) {
		return this.entityDao.getByCode(brand) != null;
	}

	public boolean isNameExists(Brand brand) {
		return this.entityDao.getByName(brand) != null;
	}

	public void changeStatus(Long brandId, StatusEnum newStatus) {
		Brand brand = new Brand();
		brand.setId(brandId);
		brand.setStatus(newStatus);
		entityDao.update(brand);
	}

	@Transactional
	public void saveOrUpdate(final Brand brand) {
		if (brand == null) {
			return;
		}

		brand.setPinyinInitial(PingYinUtil.getFirstSpell(brand.getBrandName()));
		if (brand.getId() != null) {
			entityDao.update(brand);
		} else {
			entityDao.insert(brand);
		}
	}

	public List<Map<String,Object>> findAllForMap(){
		return this.entityDao.findAllForMap();
	}
	
	public List<Map<String,Object>> findByName(String name){
		return this.entityDao.findByName(name);
	}
	
    public String getBrandNameById(String brandId) {
		String brandName = entityDao.getBrandNameById(brandId);
		return brandName;
	}
}