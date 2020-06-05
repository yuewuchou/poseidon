package com.xw.poseidon.entity;

public class ResultEntity<E> {
    private String code;
    private String message;
    private E data;
    
	public ResultEntity() {
		super();
	}
	public ResultEntity(String code, String message) {
		super();
		this.code = code;
		this.message = message;
	}
	public ResultEntity(String code, String message, E data) {
		super();
		this.code = code;
		this.message = message;
		this.data = data;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public E getData() {
		return data;
	}
	public void setData(E data) {
		this.data = data;
	}
    
    
}
