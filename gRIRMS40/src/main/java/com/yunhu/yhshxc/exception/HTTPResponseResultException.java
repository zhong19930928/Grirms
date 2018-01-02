package com.yunhu.yhshxc.exception;

import java.io.IOException;


public class HTTPResponseResultException extends IOException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6298903122912875551L;
	
	private Integer httpStatus;
	private String resultCode; 
	

	public HTTPResponseResultException() {
		super();
	}
	
	public HTTPResponseResultException(String detailMessage) {
		super(detailMessage);
	}

	public HTTPResponseResultException(String detailMessage,int httpStatus) {
		super(detailMessage);
		this.httpStatus = httpStatus;
	}
	
	public HTTPResponseResultException(String detailMessage,String resultCode) {
		super(detailMessage);
		this.resultCode = resultCode;
	}

	public Integer getHttpStatus() {
		return httpStatus;
	}

	public String getResultCode() {
		return resultCode;
	}
}
