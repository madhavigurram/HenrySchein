/*copyright 2019 HenrySchein. All rights are reserved. 
 * we should no disclose the information outside, terms and conditions will apply
 * 
 */
package com.henryschein.loyalty.rewards.dao.exception;

/**
 * @author user 18-Nov-2019
 *
 */
public class BusinessException extends Exception{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String respMsg;
	private String respCode;
	
	public BusinessException(String respMsg, String respCode) {
		this.respMsg = respMsg;
	this.respCode = respCode;
	}

	/**
	 * @return the respMsg
	 */
	public String getRespMsg() {
		return respMsg;
	}

	/**
	 * @return the respCode
	 */
	public String getRespCode() {
		return respCode;
	}
	
	
}
