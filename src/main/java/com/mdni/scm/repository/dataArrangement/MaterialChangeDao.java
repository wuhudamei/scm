package com.mdni.scm.repository.dataArrangement;

import com.mdni.scm.common.MyBatisRepository;
import com.mdni.scm.common.persistence.CrudDao;
import com.mdni.scm.entity.dataArrangement.MaterialChange;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 基装主材dao类
 * Created by 李万财 on 2017-08-04.
 */
@MyBatisRepository
public interface MaterialChangeDao  extends CrudDao<MaterialChange> {

    List<MaterialChange> findByContractId(@Param("contractId") Long contractId );

    void delete(Map<String, Object> paramMap);
}