package com.mdni.scm.repository.dataArrangement;


import com.mdni.scm.common.MyBatisRepository;
import com.mdni.scm.common.persistence.CrudDao;
import com.mdni.scm.entity.dataArrangement.MaterialChangeDetail;

import java.util.List;
import java.util.Map;

/**
 * <dl>
 * <dd>Description: 美得你scm  变更详情 Dao</dd>
 * <dd>@date：2017/8/5  10:30</dd>
 * <dd>@author：Ryze</dd>
 * </dl>
 */
@MyBatisRepository
public interface MaterialChangeDetailDao  extends CrudDao<MaterialChangeDetail> {
    /**
     * 通过 chang id 查询列表
     * @param changeId
     * @return
     */
    List<MaterialChangeDetail> findByChangeId(Long changeId);

    void delete(Map<String, Object> paramMap);
}