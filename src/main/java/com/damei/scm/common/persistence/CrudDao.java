/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.damei.scm.common.persistence;

import java.util.List;
import java.util.Map;

public interface CrudDao<T> extends BaseDao {

	/**
	 * 获取单条数据
	 * 
	 * @param id
	 */
	T getById(Long id);

	/**
	 * 插入数据
	 * 
	 * @param entity
	 */
	void insert(T entity);

	/**
	 * 更新数据
	 * 
	 * @param entity
	 */
	void update(T entity);

	void deleteById(Long id);

	List<T> findAll();

	List<T> search(Map<String, Object> params);

	Long searchTotal(Map<String, Object> params);

	/**
	 * 插入数据
	 * 
	 */
	void batchInsertList(List<T> entitys);

	/**
	 * 根据实体的属性获取对象列表,除去id属性
	 * 
	 */
	List<T> getEntityByProperties(T entity);
}