package com.damei.scm.repository.supply;


import com.damei.scm.common.MyBatisRepository;
import com.damei.scm.common.persistence.CrudDao;
import com.damei.scm.entity.supply.Supply;

import java.util.List;

@MyBatisRepository
public interface SupplyDao extends CrudDao<Supply> {
    List<Supply> findSupplyInfoByContractNo(String contractNo);
}