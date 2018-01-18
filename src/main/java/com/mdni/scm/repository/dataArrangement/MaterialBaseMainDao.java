package com.mdni.scm.repository.dataArrangement;

import com.mdni.scm.common.MyBatisRepository;
import com.mdni.scm.common.persistence.CrudDao;
import com.mdni.scm.entity.dataArrangement.MaterialBaseMain;
import com.mdni.scm.entity.eum.dataArrangement.MetarialTypeEnum;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 基装主材dao类
 * Created by 李万财 on 2017-08-04.
 */
@MyBatisRepository
public interface MaterialBaseMainDao extends CrudDao<MaterialBaseMain> {
    /**
     * 根据合同id以及材料类型查询
     * @param contractId
     * @return
     */
    List<MaterialBaseMain> findByContractIdAndMetarialType(@Param("contractId") Long contractId , @Param("metarialType") MetarialTypeEnum metarialType);

    /**
     * 增项、减项、基装综合统计
     * @param contractId
     * @param metarialType
     * @return
     */
    List<Map<String,Object>> findForTotal(@Param("contractId") Long contractId , @Param("metarialType") MetarialTypeEnum metarialType);

    void delete(Map<String,Object> paramMap);
}