package com.henryschein.loyalty.rewards.dao.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import com.henryschein.loyalty.rewards.dao.exception.BusinessException;
import com.henryschein.loyalty.rewards.dao.exception.SystemException;
import com.henryschein.loyalty.rewards.dao.model.CustomerRegistrationDAOReq;
import com.henryschein.loyalty.rewards.dao.model.CustomerRegistrationDAORes;

@Configuration
public class SpringIntgConfig {
	@Bean
	public JdbcTemplate jdbcTemplate(DriverManagerDataSource dataSource){
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		return jdbcTemplate;
	}
	
	@Bean
	public DriverManagerDataSource dataSource(){
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
		dataSource.setUrl("jdbc:mysql://localhost:3306/henryschein?useSSL=false&serverTimezone=UTC");
		dataSource.setUsername("root");
		dataSource.setPassword("root");
		return dataSource;
	}
}
