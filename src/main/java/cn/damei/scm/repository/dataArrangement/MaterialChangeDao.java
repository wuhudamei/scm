package cn.damei.scm.repository.dataArrangement;

import cn.damei.scm.common.MyBatisRepository;
import cn.damei.scm.common.persistence.CrudDao;
import cn.damei.scm.entity.dataArrangement.MaterialChange;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@MyBatisRepository
public interface MaterialChangeDao  extends CrudDao<MaterialChange> {

    List<MaterialChange> findByContractId(@Param("contractId") Long contractId );

    void delete(Map<String, Object> paramMap);
}