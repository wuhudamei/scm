package com.mdni.scm.common;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mdni.scm.common.utils.DateUtil;
import com.mdni.scm.entity.account.User;

/**
 * 统一定义id的entity基类.
 * <p/>
 * 基类统一定义id的属性名称、数据类型、列名映射及生成策略. Oracle需要每个Entity独立定义id的SEQUCENCE时，不继承于本类而改为实现一个Idable的接口。
 * 
 * @author calvin
 */
// JPA 基类的标识
public abstract class IdEntity implements Serializable {

	public static final String ID_FIELD_NAME = "id";
	protected static final long serialVersionUID = -2716222356509348153L;
	protected final Class entityClass = getClass();
	protected Long id;

	//最后编辑人
	private User editor;

	//最后编辑时间
	@JsonFormat(pattern = DateUtil.DATE_TIME_PATTERN)
	private Date editTime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	//覆盖toString方法
	public String toString() {
		StringBuilder sb = new StringBuilder();
		try {
			Field[] fields = entityClass.getDeclaredFields();
			Object fieldValue = null;
			for (Field f : fields) {
				if (!f.getName().equals("serialVersionUID")) {
					f.setAccessible(true);
					fieldValue = f.get(this);
					if (fieldValue != null) {
						sb.append(f.getName()).append("=").append(fieldValue).append(",");
					}
				}

			}
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

	public User getEditor() {
		return editor;
	}

	public void setEditor(User editor) {
		this.editor = editor;
	}

	public Date getEditTime() {
		return editTime;
	}

	public void setEditTime(Date editTime) {
		this.editTime = editTime;
	}
}
