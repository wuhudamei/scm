package com.mdni.scm.repository.supply;


import com.mdni.scm.common.MyBatisRepository;
import com.mdni.scm.common.persistence.CrudDao;
import com.mdni.scm.entity.supply.Supply;

import java.util.List;

/**
 * <dl>
 * <dd>描述: 意向单Item</dd>
 * <dd>公司: 大城若谷信息技术有限公司</dd>
 * <dd>创建时间：2017年5月11日 上午11:06:59</dd>
 * <dd>创建人： 张敏</dd>
 * </dl>
 */
@MyBatisRepository
public interface SupplyDao extends CrudDao<Supply> {
    List<Supply> findSupplyInfoByContractNo(String contractNo);
}