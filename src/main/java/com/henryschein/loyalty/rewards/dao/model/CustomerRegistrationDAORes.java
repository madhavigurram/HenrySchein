/*copyright 2019 HenrySchein. All rights are reserved. 
 * we should no disclose the information outside, terms and conditions will apply
 * 
 */
package com.henryschein.loyalty.rewards.dao.model;

import lombok.Data;

/**
 * @author user 18-Nov-2019
 *
 */
@Data
public class CustomerRegistrationDAORes {

	private String respCode;
	private String respMsg;
	private String status;
	private String refNum;
}
