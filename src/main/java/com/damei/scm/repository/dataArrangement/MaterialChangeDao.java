package com.damei.scm.repository.dataArrangement;

import com.damei.scm.common.MyBatisRepository;
import com.damei.scm.common.persistence.CrudDao;
import com.damei.scm.entity.dataArrangement.MaterialChange;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@MyBatisRepository
public interface MaterialChangeDao  extends CrudDao<MaterialChange> {

    List<MaterialChange> findByContractId(@Param("contractId") Long contractId );

    void delete(Map<String, Object> paramMap);
}