package com.damei.scm.repository.dataArrangement;

import com.damei.scm.common.MyBatisRepository;
import com.damei.scm.common.persistence.CrudDao;
import com.damei.scm.entity.dataArrangement.MaterialStandardConfig;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@MyBatisRepository
public interface MaterialStandardConfigDao  extends CrudDao<MaterialStandardConfig> {

    List<MaterialStandardConfig> findByContractId(@Param("contractId") Long contractId );

    void delete(Map<String, Object> paramMap);
}
