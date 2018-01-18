package com.mdni.scm.repository.dataArrangement;

import com.mdni.scm.common.MyBatisRepository;
import com.mdni.scm.common.persistence.CrudDao;
import com.mdni.scm.entity.dataArrangement.MaterialStandardConfig;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <dl>
 * <dd>Description: 美得你 标配录入Dao</dd>
 * <dd>@date：2017/8/4  18:39</dd>
 * <dd>@author：Ryze</dd>
 * </dl>
 */
@MyBatisRepository
public interface MaterialStandardConfigDao  extends CrudDao<MaterialStandardConfig> {
    /**
     * 根据合同id查询
     * @param contractId
     * @return
     */
    List<MaterialStandardConfig> findByContractId(@Param("contractId") Long contractId );

    void delete(Map<String, Object> paramMap);
}
