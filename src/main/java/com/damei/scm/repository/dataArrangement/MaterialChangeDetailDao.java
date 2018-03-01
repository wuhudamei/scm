package com.damei.scm.repository.dataArrangement;


import com.damei.scm.common.MyBatisRepository;
import com.damei.scm.common.persistence.CrudDao;
import com.damei.scm.entity.dataArrangement.MaterialChangeDetail;

import java.util.List;
import java.util.Map;

@MyBatisRepository
public interface MaterialChangeDetailDao  extends CrudDao<MaterialChangeDetail> {

    List<MaterialChangeDetail> findByChangeId(Long changeId);

    void delete(Map<String, Object> paramMap);
}