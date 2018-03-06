package cn.damei.scm.repository.dataArrangement;


import cn.damei.scm.common.MyBatisRepository;
import cn.damei.scm.common.persistence.CrudDao;
import cn.damei.scm.entity.dataArrangement.MaterialChangeDetail;

import java.util.List;
import java.util.Map;

@MyBatisRepository
public interface MaterialChangeDetailDao  extends CrudDao<MaterialChangeDetail> {

    List<MaterialChangeDetail> findByChangeId(Long changeId);

    void delete(Map<String, Object> paramMap);
}