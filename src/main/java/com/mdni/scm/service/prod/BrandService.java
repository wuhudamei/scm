package com.mdni.scm.service.prod;

import com.mdni.scm.common.service.CrudService;
import com.mdni.scm.common.utils.PingYinUtil;
import com.mdni.scm.entity.eum.StatusEnum;
import com.mdni.scm.entity.prod.Brand;
import com.mdni.scm.repository.prod.BrandDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 
 * <dl>
 * <dd>描述: 品牌Service</dd>
 * <dd>公司: 大城若谷信息技术有限公司</dd>
 * <dd>创建时间：2017-6-23 14:46:11</dd>
 * <dd>创建人： 张俊奎</dd>
 * </dl>
 */
@Service
public class BrandService extends CrudService<BrandDao, Brand> {

	/**
	 * 返回某种状态的top N条品牌,品牌列表按照id升序排列
	 * 
	 * @param newStatus 品牌状态,可以为null,如果为null,则返回所有状态的品牌
	 * @param topLimit 前N条品牌,可以为null,如果为null,则返回所有品牌
	 */
	public List<Brand> findTopBrandByStatusOrderByIdAsc(final StatusEnum status, final Integer topLimit) {
		return this.entityDao.findTopBrandByStatusOrderByIdAsc(status, topLimit);
	}

	public boolean isCodeExists(Brand brand) {
		return this.entityDao.getByCode(brand) != null;
	}

	public boolean isNameExists(Brand brand) {
		return this.entityDao.getByName(brand) != null;
	}

	/**
	 * 修改品牌状态
	 * 
	 * @param brandId
	 */
	public void changeStatus(Long brandId, StatusEnum newStatus) {
		Brand brand = new Brand();
		brand.setId(brandId);
		brand.setStatus(newStatus);
		entityDao.update(brand);
	}

	/**
	 * 增加或者更新品牌信息
	 * 
	 * @param brand
	 */
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