package com.damei.scm.common.utils;

import java.beans.PropertyEditorSupport;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.util.HtmlUtils;

public class StringTrimEscapeEditor extends PropertyEditorSupport {

	private boolean isEscapeHtml;

	public StringTrimEscapeEditor(boolean isEscapeHtml) {
		this.isEscapeHtml = isEscapeHtml;
	}

	@Override
	public void setAsText(String text) {
		if (text == null) {
			setValue(null);
			return;
		}

		text = StringUtils.trimToEmpty(text);
		if (!text.isEmpty()) {
			if (isEscapeHtml) {
				setValue(HtmlUtils.htmlEscape(text));//html 特殊字符转义
			} else {
				setValue(text);
			}
		} else {
			setValue(StringUtils.EMPTY);
		}
	}
}
