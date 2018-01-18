package com.mdni.scm.service.dataArrangement;

import com.google.common.collect.Maps;
import com.mdni.scm.common.service.CrudService;
import com.mdni.scm.common.utils.WebUtils;
import com.mdni.scm.entity.dataArrangement.MaterialBaseMain;
import com.mdni.scm.entity.eum.dataArrangement.MetarialTypeEnum;
import com.mdni.scm.repository.dataArrangement.MaterialBaseMainDao;
import com.mdni.scm.shiro.ShiroUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 基装主材service类
 * Created by 李万财 on 2017-08-04.
 */
@Service
public class MaterialBaseMainService  extends CrudService<MaterialBaseMainDao, MaterialBaseMain> {
	private final Logger logger = LoggerFactory.getLogger(MaterialBaseMainService.class);

	/**
	 * 根据合同id以及材料类别查询对应的材料列表
	 * @param contractId
	 * @param metarialType
	 * @return
	 */
	public List<MaterialBaseMain> findByContractIdAndMetarialType(Long contractId,MetarialTypeEnum metarialType){
		return this.entityDao.findByContractIdAndMetarialType(contractId,metarialType);
	}

	/**
	 * 根据合同id以及材料类型汇总增项、减项以及基装综合费用合计
	 * @param contractId
	 * @param metarialType
	 * @return
	 */
	public Map<String,Object> findForTotal(Long contractId,MetarialTypeEnum metarialType){
		List<Map<String, Object>> list = this.entityDao.findForTotal(contractId,metarialType);
		if( list != null && list.size() > 0 ){
			return list.get(0);
		}else{
			return Maps.newHashMap();
		}
	}

    public void delete(Map<String,Object> paramMap) {


		this.entityDao.delete(paramMap);
	}
}
