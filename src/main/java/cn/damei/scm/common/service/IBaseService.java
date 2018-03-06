package cn.damei.scm.common.service;

import java.util.List;
import java.util.Map;

import cn.damei.scm.common.dto.BootstrapPage;

public interface IBaseService<T> {

	public T getById(Long id);

	public void insert(T entity);

	public void update(T entity);

	public void deleteById(Long id);

	public List<T> findAll();

	public BootstrapPage<T> searchScrollPage(Map<String, Object> params);

	public void insertList(List<T> entity);
}
