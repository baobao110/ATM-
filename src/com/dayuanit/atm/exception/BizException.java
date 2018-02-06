package com.dayuanit.atm.exception;

public class BizException extends RuntimeException {
	
	private static final long serialVersionUID = -4998348346486897664L;

	public BizException() {
		
	}
	
	public BizException(String errorMsg) {
		super(errorMsg);
	}

}
