package com.henryschein.loyalty.rewards.dao;

import com.henryschein.loyalty.rewards.dao.exception.BusinessException;
import com.henryschein.loyalty.rewards.dao.exception.SystemException;
import com.henryschein.loyalty.rewards.dao.model.CustomerRegistrationDAOReq;
import com.henryschein.loyalty.rewards.dao.model.CustomerRegistrationDAORes;

public interface LoyaltyRewardsDAO {
	public CustomerRegistrationDAORes register(CustomerRegistrationDAOReq daoReq) throws BusinessException, SystemException;
}
