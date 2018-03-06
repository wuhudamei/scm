package cn.damei.scm.service.dataArrangement;

import cn.damei.scm.common.service.CrudService;
import cn.damei.scm.entity.dataArrangement.MaterialChangeDetail;
import cn.damei.scm.repository.dataArrangement.MaterialChangeDetailDao;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class MaterialChangeDetailService extends CrudService<MaterialChangeDetailDao, MaterialChangeDetail> {

	public List<MaterialChangeDetail> findByChangeId(Long changeId){
		return entityDao.findByChangeId(changeId);
	}

    public void delete(Map<String, Object> paramMap) {
		this.entityDao.delete(paramMap);
    }
}
