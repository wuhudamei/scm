/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.damei.scm.common.service;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.damei.scm.common.IdEntity;
import com.damei.scm.common.dto.BootstrapPage;
import com.damei.scm.common.persistence.CrudDao;


@SuppressWarnings("all")
public abstract class CrudService<D extends CrudDao<T>, T extends IdEntity> extends BaseService<T> {

	protected final Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	protected D entityDao;

	@Override
	public T getById(Long id) {
		if (id == null || id < 1)
			return null;
		return entityDao.getById(id);
	}

	@Override
	@Transactional
	public void insert(T entity) {
		if (entity == null)
			return;
		entityDao.insert(entity);
	}

	@Override
	@Transactional
	public void update(T entity) {
		if (entity == null)
			return;
		entityDao.update(entity);
	}

	@Override
	public void deleteById(Long id) {
		if (id == null || id < 1)
			return;
		this.entityDao.deleteById(id);
	}

	@Override
	public List<T> findAll() {
		return entityDao.findAll();
	}

	@Override
	public BootstrapPage<T> searchScrollPage(Map<String, Object> params) {

		List<T> pageData = Collections.emptyList();
		Long count = this.entityDao.searchTotal(params);
		if (count > 0) {
			pageData = entityDao.search(params);
		}
		return new BootstrapPage(count, pageData);
	}

	@Transactional
	public void updateList(List<T> entitys) {
		if (entitys != null && entitys.size() > 0) {
			for (T entity : entitys) {
				this.entityDao.update(entity);
			}
		}
	}

	public void insertList(List<T> entitys) {
		if (entitys != null && entitys.size() > 0) {
			final int batchSize = 5000;
			int batchTimes = entitys.size() / batchSize;
			if ((entitys.size() % batchSize) != 0) {
				batchTimes++;
			}

			for (int i = 0; i < batchTimes; i++) {
				int startIdx = i * batchSize;
				int endIdx = Math.min(entitys.size(), startIdx + batchSize);
				List<T> subList = entitys.subList(startIdx, endIdx);
				this.entityDao.batchInsertList(subList);
			}
		}
	}

	public List<T> getByEntityProperties(T entity) {
		if (entity == null)
			return Collections.emptyList();

		return entityDao.getEntityByProperties(entity);
	}

}
