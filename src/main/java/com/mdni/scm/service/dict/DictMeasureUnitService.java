package com.mdni.scm.service.dict;

import org.springframework.stereotype.Service;

import com.mdni.scm.common.service.CrudService;
import com.mdni.scm.entity.dict.DictMeasureUnit;
import com.mdni.scm.repository.dict.DictMeasureUnitDao;

/**
 * <dl>
 * <dd>描述: 计量单位Service</dd>
 * <dd>公司: 大城若谷信息技术有限公司</dd>
 * <dd>创建时间：2017-6-23 14:00:39</dd>
 * <dd>创建人： 张俊奎</dd>
 * </dl>
 */
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