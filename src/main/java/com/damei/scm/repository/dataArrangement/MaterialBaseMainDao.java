package com.damei.scm.repository.dataArrangement;

import com.damei.scm.common.MyBatisRepository;
import com.damei.scm.common.persistence.CrudDao;
import com.damei.scm.entity.dataArrangement.MaterialBaseMain;
import com.damei.scm.entity.eum.dataArrangement.MetarialTypeEnum;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@MyBatisRepository
public interface MaterialBaseMainDao extends CrudDao<MaterialBaseMain> {

    List<MaterialBaseMain> findByContractIdAndMetarialType(@Param("contractId") Long contractId , @Param("metarialType") MetarialTypeEnum metarialType);


    List<Map<String,Object>> findForTotal(@Param("contractId") Long contractId , @Param("metarialType") MetarialTypeEnum metarialType);

    void delete(Map<String,Object> paramMap);
}