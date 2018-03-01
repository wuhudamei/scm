package com.damei.scm.common;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.damei.scm.common.utils.DateUtil;
import com.damei.scm.entity.account.User;

public abstract class IdEntity implements Serializable {

	public static final String ID_FIELD_NAME = "id";
	protected static final long serialVersionUID = -2716222356509348153L;
	protected final Class entityClass = getClass();
	protected Long id;

	private User editor;

	@JsonFormat(pattern = DateUtil.DATE_TIME_PATTERN)
	private Date editTime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

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
