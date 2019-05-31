package com.ahgj.community.canal.request.exception;

import lombok.Data;

/**
 * 系统异常类
 *
 * @author:Hohn
 */
@Data
public class GjSystemException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	private int code;

	public GjSystemException(String message) {
		super(message);
	}

	public GjSystemException(String message, Throwable cause) {
		super(message, cause);
	}

	public GjSystemException(String message, int status) {
		super(message);
		this.code = status;
	}

}
