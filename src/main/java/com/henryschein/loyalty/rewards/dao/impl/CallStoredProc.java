/*copyright 2019 HenrySchein. All rights are reserved. 
 * we should no disclose the information outside, terms and conditions will apply
 * 
 */
package com.henryschein.loyalty.rewards.dao.impl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

/**
 * @author user 06-Dec-2019
 *
 */
public class CallStoredProc {

	
	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.cj.jdbc.Driver");
		Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/henryschein?useSSL=false&serverTimezone=UTC","root", "root");
		String sql = "{call HS_CUSTOMER_DETAILS(?,?,?,?,?,?,?,?,?,?)}";
		CallableStatement prepareCall = connection.prepareCall(sql);
		/*prepareCall.setString("IN_EMAIL_ID", "abc@gmail.com");
		prepareCall.setDate("IN_REG_DATE", new Date(2019, 12, 05));
		prepareCall.setString("IN_ORG_NAME", "org1");
		prepareCall.setString("IN_WEBSITE", "abc.com");
		prepareCall.setString("IN_ZIPCODE", "1000001");
		prepareCall.setString("IN_ADDRESS", "abc");
		prepareCall.setString("IN_COMPANY_ID", "111");
		prepareCall.setString("IN_VAT_REG_ID", "10001");
		*/
		prepareCall.registerOutParameter("OUT_RESP_CODE", Types.VARCHAR);
		prepareCall.registerOutParameter("OUT_RESP_MSG", Types.VARCHAR);
		//boolean execute = prepareCall.execute();
		//System.out.println(execute);
		
		prepareCall.execute();
		ResultSet resultSet = prepareCall.getResultSet();
		String respcode = prepareCall.getString(9);
		String respmsg = prepareCall.getString(10);
		System.out.println(respcode);
		System.out.println(respmsg);
		//System.out.println(resultSet);
		if (resultSet!=null){
			while(resultSet.next()){
				
				System.out.println(resultSet.getString(8));
			}
		}
		if(resultSet!=null){
			resultSet.close();	
		}
		
		connection.close();
	}

}
