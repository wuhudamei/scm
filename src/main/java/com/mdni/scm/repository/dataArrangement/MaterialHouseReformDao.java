package com.mdni.scm.repository.dataArrangement;

import com.mdni.scm.common.MyBatisRepository;
import com.mdni.scm.common.persistence.CrudDao;
import com.mdni.scm.entity.dataArrangement.MaterialHouseReform;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <dl>
 * <dd>Description: 美得你scm 房屋拆改dao</dd>
 * <dd>@date：2017/8/4  18:06</dd>
 * <dd>@author：Ryze</dd>
 * </dl>
 */
    @MyBatisRepository
public interface MaterialHouseReformDao extends CrudDao<MaterialHouseReform> {
    /**
     * 根据合同id查询
     * @param contractId
     * @return
     */
    List<MaterialHouseReform>findByContractId(@Param("contractId") Long contractId );

    void delete(Map<String, Object> paramMap);
}