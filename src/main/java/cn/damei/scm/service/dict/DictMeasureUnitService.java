package cn.damei.scm.service.dict;

import org.springframework.stereotype.Service;

import cn.damei.scm.common.service.CrudService;
import cn.damei.scm.entity.dict.DictMeasureUnit;
import cn.damei.scm.repository.dict.DictMeasureUnitDao;

@Service
public class DictMeasureUnitService extends CrudService<DictMeasureUnitDao, DictMeasureUnit> {

	public void saveOrUpdate(DictMeasureUnit measureUnit) {
		if (measureUnit == null) {
			return;
		}

		if (measureUnit.getId() == null) {
			this.entityDao.insert(measureUnit);
		} else {
			this.entityDao.update(measureUnit);
		}
	}
}