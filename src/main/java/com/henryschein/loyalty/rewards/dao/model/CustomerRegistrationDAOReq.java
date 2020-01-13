/*copyright 2019 HenrySchein. All rights are reserved. 
 * we should no disclose the information outside, terms and conditions will apply
 * 
 */
package com.henryschein.loyalty.rewards.dao.model;

import java.util.Calendar;

import lombok.Data;

/**
 * @author user 18-Nov-2019
 *
 */
@Data
public class CustomerRegistrationDAOReq {
	private String clientId;
	private String requestId;
	private String firstName;
	private String lastName;
	private String emailId;
	private Calendar dob;
	// private String dob;
	private String address;
	private String website;
	private String organizationName;
	private String pincode;

}
