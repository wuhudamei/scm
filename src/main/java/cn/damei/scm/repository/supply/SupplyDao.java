package cn.damei.scm.repository.supply;


import cn.damei.scm.common.persistence.CrudDao;
import cn.damei.scm.common.MyBatisRepository;
import cn.damei.scm.entity.supply.Supply;

import java.util.List;

@MyBatisRepository
public interface SupplyDao extends CrudDao<Supply> {
    List<Supply> findSupplyInfoByContractNo(String contractNo);
}