package com.mdni.scm.service.dataArrangement;

import com.mdni.scm.common.service.CrudService;
import com.mdni.scm.entity.dataArrangement.MaterialStandardConfig;
import com.mdni.scm.repository.dataArrangement.MaterialStandardConfigDao;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <dl>
 * <dd>Description: 美得你scm 标配录入service</dd>
 * <dd>@date：2017/8/4  18:06</dd>
 * <dd>@author：Ryze</dd>
 * </dl>
 */
@Service
public class MaterialStandardConfigService extends CrudService<MaterialStandardConfigDao, MaterialStandardConfig> {
    /**
     * 根据合同id以及材料类型查询
     * @param contractId
     * @return
     */
   public List<MaterialStandardConfig> findByContractId( Long contractId ){
        return entityDao.findByContractId(contractId);
    }

    public void delete(Map<String, Object> paramMap) {
       this.entityDao.delete(paramMap);
    }
}
