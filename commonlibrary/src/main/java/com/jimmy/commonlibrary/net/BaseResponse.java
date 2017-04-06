package com.jimmy.commonlibrary.net;

import java.io.Serializable;

/**
 * 登录接口
 * @author hq3
 *
 */

public class BaseResponse implements Serializable{
	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	/**

	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String status;
	private String msg;

	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	
}
