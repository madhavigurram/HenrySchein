package com.henryschein.loyalty.rewards.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
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

import com.henryschein.loyalty.rewards.dao.LoyaltyRewardsDAO;
import com.henryschein.loyalty.rewards.dao.exception.BusinessException;
import com.henryschein.loyalty.rewards.dao.exception.SystemException;
import com.henryschein.loyalty.rewards.dao.model.CustomerRegistrationDAOReq;
import com.henryschein.loyalty.rewards.dao.model.CustomerRegistrationDAORes;

public class LoyaltyRewardsDAOImpl extends StoredProcedure
		implements LoyaltyRewardsDAO, RowMapper<CustomerRegistrationDAORes> {

	CustomerRegistrationDAORes resp = null;

	public LoyaltyRewardsDAOImpl() {
		super(getJdbcTemplateObject(), "HS_CUSTOMER_DETAILS");
		compileStoredProcedure();
	}

	private static JdbcTemplate getJdbcTemplateObject() {
		JdbcTemplate jdbcTemplate = new JdbcTemplate();
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
		dataSource.setUrl("jdbc:mysql://localhost:3306/henryschein");
		dataSource.setUsername("root");
		dataSource.setPassword("root");
		jdbcTemplate.setDataSource(dataSource);
		return jdbcTemplate;

	}

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

		declareParameter(new SqlReturnResultSet("RESULT_LIST", this));
		compile();
	}

	@SuppressWarnings("unchecked")
	public CustomerRegistrationDAORes register(CustomerRegistrationDAOReq daoReq)
			throws BusinessException, SystemException {
		// get the daorequest from process layer

		try {
			Map<String, Object> inputMap = new HashMap<String, Object>();
			inputMap.put("IN_EMAIL_ID", daoReq.getEmailId());
			inputMap.put("IN_REG_DATE", daoReq.getDob());
			inputMap.put("IN_ORG_NAME", daoReq.getOrganizationName());
			inputMap.put("IN_WEBSITE", daoReq.getWebsite());
			inputMap.put("IN_ZIPCODE", daoReq.getPincode());
			inputMap.put("IN_ADDRESS", daoReq.getAddress());
			inputMap.put("IN_COMPANY_ID", daoReq.getClientId());
			inputMap.put("IN_VAT_REG_ID", daoReq.getRequestId());

			Map<String, Object> outputMap = super.execute(inputMap);

			String respCode = outputMap.get("OUT_RESP_CODE").toString();
			String respMsg = outputMap.get("OUT_RESP_MSG").toString();

			resp = new CustomerRegistrationDAORes();
			resp.setRespMsg(respMsg);
			resp.setRespCode(respCode);
			// prepare the db resp
			if ("0".equals(respCode)) {
				List<CustomerRegistrationDAORes> list = (List<CustomerRegistrationDAORes>) outputMap.get("RESULT_LIST");
				if (list != null && list.size() > 0) {
					for (CustomerRegistrationDAORes customerRegistrationDAORes : list) {
						resp.setStatus(customerRegistrationDAORes.getStatus());
						resp.setRefNum(customerRegistrationDAORes.getRefNum());
					}
				} else {
					resp.setRespCode("100");
					resp.setRespMsg("No records available");
				}

			} else if ("101".equals(respCode) || ("102".equals(respCode))) {
				// respMsg = "Invalid Company/Vat Reg Id";
				throw new BusinessException(respCode, respMsg);
			} else {
				respCode = "103";
				respMsg = "Unable to process req";
				resp.setRespMsg(respMsg);
				resp.setRespCode(respCode);
				throw new SystemException(respMsg, respCode);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resp;
	}

	@Override
	public CustomerRegistrationDAORes mapRow(ResultSet rs, int rowNum) throws SQLException {
		CustomerRegistrationDAORes daoRes = new CustomerRegistrationDAORes();
		daoRes.setRefNum(rs.getString("accountId"));
		daoRes.setStatus(rs.getString("status"));
		return daoRes;
	}

}
