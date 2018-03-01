package com.damei.scm.service.dataArrangement;

import com.damei.scm.common.service.CrudService;
import com.damei.scm.entity.dataArrangement.MaterialChange;
import com.damei.scm.repository.dataArrangement.MaterialChangeDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class MaterialChangeService extends CrudService<MaterialChangeDao, MaterialChange> {
	private final Logger logger = LoggerFactory.getLogger(MaterialChangeService.class);

	public List<MaterialChange> findByContractId(Long contractId){
		return this.entityDao.findByContractId(contractId);
	}

    public void delete(Map<String, Object> paramMap) {
		this.entityDao.delete(paramMap);
    }
}
