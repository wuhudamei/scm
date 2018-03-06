package cn.damei.scm.service.dataArrangement;

import cn.damei.scm.entity.dataArrangement.MaterialHouseReform;
import cn.damei.scm.common.service.CrudService;
import cn.damei.scm.repository.dataArrangement.MaterialHouseReformDao;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class MaterialHouseReformService extends CrudService<MaterialHouseReformDao, MaterialHouseReform> {

   public List<MaterialHouseReform> findByContractId( Long contractId ){
        return entityDao.findByContractId(contractId);
    }

    public void delete(Map<String, Object> paramMap) {
       this.entityDao.delete(paramMap);
    }
}
