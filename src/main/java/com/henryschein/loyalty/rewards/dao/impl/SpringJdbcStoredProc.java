/*copyright 2019 HenrySchein. All rights are reserved. 
 * we should no disclose the information outside, terms and conditions will apply
 * 
 */
package com.henryschein.loyalty.rewards.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.SqlReturnResultSet;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.object.StoredProcedure;

import com.henryschein.loyalty.rewards.dao.model.CustomerRegistrationDAOReq;
import com.henryschein.loyalty.rewards.dao.model.CustomerRegistrationDAORes;

/**
 * @author user 12-Dec-2019
 *
 */
public class SpringJdbcStoredProc extends StoredProcedure{
	
	
	
	public SpringJdbcStoredProc() {
		super(getJdbcTemplateObject(),"HS_CUSTOMER_DETAILS");
		compileStoredProcedure();
	}
	
	/**
	 * 
	 */
	private void compileStoredProcedure() {
		declareParameter(new SqlParameter("IN_EMAIL_ID", Types.VARCHAR));
		declareParameter(new SqlParameter("IN_REG_DATE", Types.DATE));
		declareParameter(new SqlParameter("IN_ORG_NAME", Types.VARCHAR));
		declareParameter(new SqlParameter("IN_WEBSITE", Types.VARCHAR));
		declareParameter(new SqlParameter("IN_ZIPCODE", Types.VARCHAR));
		declareParameter(new SqlParameter("IN_ADDRESS", Types.VARCHAR));
		declareParameter(new SqlParameter("IN_COMPANY_ID", Types.VARCHAR));
		declareParameter(new SqlParameter("IN_VAT_REG_ID", Types.VARCHAR));
		
		declareParameter(new SqlOutParameter("OUT_RESP_CODE", Types.VARCHAR));
		declareParameter(new SqlOutParameter("OUT_RESP_MSG", Types.VARCHAR));
		
		declareParameter(new SqlReturnResultSet("RESULT_LIST", new CustomerRegMapRow()));
		compile();
	}

	private static JdbcTemplate getJdbcTemplateObject(){
		JdbcTemplate jdbcTemplate = new JdbcTemplate();
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
		dataSource.setUrl("jdbc:mysql://localhost:3306/henryschein");
		dataSource.setUsername("root");
		dataSource.setPassword("root");
		jdbcTemplate.setDataSource(dataSource);
		return jdbcTemplate;
		
	}

	public static void main(String[] args) {
		SpringJdbcStoredProc sp = new SpringJdbcStoredProc();
		CustomerRegistrationDAOReq req = sp.prepareCustomerReq();
		CustomerRegistrationDAORes res = sp.register(req);
		System.out.println(res);
	}

	
	CustomerRegistrationDAOReq prepareCustomerReq(){
		CustomerRegistrationDAOReq req = new CustomerRegistrationDAOReq();
		req.setAddress("hyd");
		req.setDob(Calendar.getInstance());
		req.setEmailId("abc@gmail.com");
		req.setFirstName("firstName");
		req.setLastName("lastName");
		req.setOrganizationName("sreenu tech");
		req.setWebsite("abc.com");
		req.setClientId("111");
		req.setRequestId("111_0001");
		return req;
	}
	
	public Calendar convertStringToCalander(String stringDate){
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		Calendar calender = Calendar.getInstance();
		try {
			Date date = formatter.parse(stringDate);
			calender.setTime(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return calender;
	}
	
	CustomerRegistrationDAORes register(CustomerRegistrationDAOReq req){
		Map<String, Object> inputMap = new HashMap<String, Object>();
		inputMap.put("IN_EMAIL_ID", req.getEmailId());
		inputMap.put("IN_REG_DATE", req.getDob());
		inputMap.put("IN_ORG_NAME", req.getOrganizationName());
		inputMap.put("IN_WEBSITE", req.getWebsite());
		inputMap.put("IN_ZIPCODE", req.getPincode());
		inputMap.put("IN_ADDRESS", req.getAddress());
		inputMap.put("IN_COMPANY_ID", req.getClientId());
		inputMap.put("IN_VAT_REG_ID", req.getRequestId());
		
		Map<String, Object> outputMap = super.execute(inputMap);
		
		String respCode = outputMap.get("OUT_RESP_CODE").toString();
		String respMsg = outputMap.get("OUT_RESP_MSG").toString();
		
		CustomerRegistrationDAORes resp=new CustomerRegistrationDAORes();
		if("0".equals(respCode)){
			 @SuppressWarnings("unchecked")
			List<CustomerRegistrationDAORes> list = (List<CustomerRegistrationDAORes>)outputMap.get("RESULT_LIST");
			 for (CustomerRegistrationDAORes customerRegistrationDAORes : list) {
				 resp.setStatus(customerRegistrationDAORes.getStatus());
				 resp.setRefNum(customerRegistrationDAORes.getRefNum());
				 resp.setRespCode(respCode);
				 resp.setRespMsg(respMsg);
			}
			 
		}
		else if("100".equals(respCode)){
			 resp.setRespCode(respCode);
			 resp.setRespMsg(respMsg);
		}
		return resp;
		
		}
	
	private class CustomerRegMapRow implements RowMapper<CustomerRegistrationDAORes>{
		public CustomerRegistrationDAORes mapRow(ResultSet rs, int rowNum) throws SQLException {
			
			CustomerRegistrationDAORes daoRes = new CustomerRegistrationDAORes();
			daoRes.setRefNum(rs.getString("accountId"));
			daoRes.setStatus(rs.getString("STATUS"));
			return daoRes;
		}
	}

}
