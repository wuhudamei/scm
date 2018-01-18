package com.mdni.scm.service.dataArrangement;

import com.mdni.scm.common.service.CrudService;
import com.mdni.scm.entity.dataArrangement.MaterialChange;
import com.mdni.scm.repository.dataArrangement.MaterialChangeDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 选材变更service类
 * Created by 李万财 on 2017-08-04.
 */
@Service
public class MaterialChangeService extends CrudService<MaterialChangeDao, MaterialChange> {
	private final Logger logger = LoggerFactory.getLogger(MaterialChangeService.class);

    /**
     * 根据合同id查询对应的材料列表
	 * @param contractId
     * @return
     */
	public List<MaterialChange> findByContractId(Long contractId){
		return this.entityDao.findByContractId(contractId);
	}

    public void delete(Map<String, Object> paramMap) {
		this.entityDao.delete(paramMap);
    }
}
