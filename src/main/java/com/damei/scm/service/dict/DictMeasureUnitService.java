package com.damei.scm.service.dict;

import org.springframework.stereotype.Service;

import com.damei.scm.common.service.CrudService;
import com.damei.scm.entity.dict.DictMeasureUnit;
import com.damei.scm.repository.dict.DictMeasureUnitDao;

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