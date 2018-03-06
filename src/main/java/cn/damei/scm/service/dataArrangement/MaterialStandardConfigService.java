package cn.damei.scm.service.dataArrangement;

import cn.damei.scm.common.service.CrudService;
import cn.damei.scm.entity.dataArrangement.MaterialStandardConfig;
import cn.damei.scm.repository.dataArrangement.MaterialStandardConfigDao;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class MaterialStandardConfigService extends CrudService<MaterialStandardConfigDao, MaterialStandardConfig> {

   public List<MaterialStandardConfig> findByContractId( Long contractId ){
        return entityDao.findByContractId(contractId);
    }

    public void delete(Map<String, Object> paramMap) {
       this.entityDao.delete(paramMap);
    }
}
