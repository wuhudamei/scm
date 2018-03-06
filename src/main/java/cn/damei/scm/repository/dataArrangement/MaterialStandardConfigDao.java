package cn.damei.scm.repository.dataArrangement;

import cn.damei.scm.common.persistence.CrudDao;
import cn.damei.scm.entity.dataArrangement.MaterialStandardConfig;
import cn.damei.scm.common.MyBatisRepository;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@MyBatisRepository
public interface MaterialStandardConfigDao  extends CrudDao<MaterialStandardConfig> {

    List<MaterialStandardConfig> findByContractId(@Param("contractId") Long contractId );

    void delete(Map<String, Object> paramMap);
}
