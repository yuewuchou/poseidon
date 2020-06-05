package com.xw.poseidon.entity;

import java.util.List;

public class PagedResultEntity<E> {
    private Integer pageNo;
    private Integer pageSize;
    private Integer rowCount;
    private Integer pageCount;
    
    private List<E> list;

	public PagedResultEntity() {
		super();
	}

	public PagedResultEntity(Integer pageNo, Integer pageSize, Integer rowCount, List<E> list) {
		super();
		this.pageNo = pageNo;
		this.pageSize = pageSize;
		this.rowCount = rowCount;
		this.list = list;
	}

	public PagedResultEntity(Integer pageNo, Integer pageSize, Integer rowCount, Integer pageCount, List<E> list) {
		super();
		this.pageNo = pageNo;
		this.pageSize = pageSize;
		this.rowCount = rowCount;
		this.pageCount = pageCount;
		this.list = list;
	}

	public Integer getPageNo() {
		return pageNo;
	}

	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Integer getRowCount() {
		return rowCount;
	}

	public void setRowCount(Integer rowCount) {
		this.rowCount = rowCount;
	}

	public Integer getPageCount() {
		return pageCount;
	}

	public void setPageCount(Integer pageCount) {
		this.pageCount = pageCount;
	}

	public List<E> getList() {
		return list;
	}

	public void setList(List<E> list) {
		this.list = list;
	}
    
    
}
