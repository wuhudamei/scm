package com.mdni.scm.service.dataArrangement;

import com.mdni.scm.common.service.CrudService;
import com.mdni.scm.entity.dataArrangement.MaterialChange;
import com.mdni.scm.entity.dataArrangement.MaterialChangeDetail;
import com.mdni.scm.repository.dataArrangement.MaterialChangeDao;
import com.mdni.scm.repository.dataArrangement.MaterialChangeDetailDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <dl>
 * <dd>Description: 美得你scm  变更详情 Service</dd>
 * <dd>@date：2017/8/5  10:30</dd>
 * <dd>@author：Ryze</dd>
 * </dl>
 */
@Service
public class MaterialChangeDetailService extends CrudService<MaterialChangeDetailDao, MaterialChangeDetail> {

	/**
	 * 通过 chang id 查询列表
	 * @param changeId
	 * @return
	 */
	public List<MaterialChangeDetail> findByChangeId(Long changeId){
		return entityDao.findByChangeId(changeId);
	}

    public void delete(Map<String, Object> paramMap) {
		this.entityDao.delete(paramMap);
    }
}
