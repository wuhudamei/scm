package com.damei.scm.service.dataArrangement;

import com.google.common.collect.Maps;
import com.damei.scm.common.service.CrudService;
import com.damei.scm.entity.dataArrangement.MaterialBaseMain;
import com.damei.scm.entity.eum.dataArrangement.MetarialTypeEnum;
import com.damei.scm.repository.dataArrangement.MaterialBaseMainDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class MaterialBaseMainService  extends CrudService<MaterialBaseMainDao, MaterialBaseMain> {
	private final Logger logger = LoggerFactory.getLogger(MaterialBaseMainService.class);

	public List<MaterialBaseMain> findByContractIdAndMetarialType(Long contractId,MetarialTypeEnum metarialType){
		return this.entityDao.findByContractIdAndMetarialType(contractId,metarialType);
	}

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
