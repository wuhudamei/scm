package com.damei.scm.common.dto;

import java.util.Collections;
import java.util.List;

public final class BootstrapPage<T> {

	private static final BootstrapPage EMPTY_PAGE = new BootstrapPage(0L, Collections.emptyList());

	private Long total;

	private List<T> rows;

	public static final <T> BootstrapPage<T> emptyPage() {
		return (BootstrapPage<T>) EMPTY_PAGE;
	}

	public BootstrapPage(Long total, List<T> rows) {
		this.total = total;
		this.rows = rows;
	}

	public BootstrapPage() {
	}

	public Long getTotal() {
		return total;
	}

	public void setTotal(Long total) {
		this.total = total;
	}

	public List<T> getRows() {
		return rows;
	}

	public void setRows(List<T> rows) {
		this.rows = rows;
	}
}
