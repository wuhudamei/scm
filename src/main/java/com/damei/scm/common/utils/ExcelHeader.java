package com.damei.scm.common.utils;

import java.lang.reflect.Field;

/**
 * 用来存储Excel标题的对象，通过该对象可以获取标题和方法的对应关系
 * 
 * @author zhangmin
 */
public class ExcelHeader implements Comparable<ExcelHeader> {
	/**
	 * excel的标题名称
	 */
	private String title;

	/**
	 * 每一个标题的顺序
	 */
	private int order;

	/**
	 * 对应属性名称
	 */
	private String propertyName;

	private Field field;

	public ExcelHeader(String title, int order, String propertyName) {
		super();
		this.title = title;
		this.order = order;
		this.propertyName = propertyName;
	}

	public String getPropertyName() {
		return propertyName;
	}

	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	@Override
	public int compareTo(ExcelHeader o) {
		return order > o.order ? 1 : (order < o.order ? -1 : 0);
	}

	public Field getField() {
		return field;
	}

	public void setField(Field field) {
		this.field = field;
	}
}