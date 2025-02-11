package com.telus.starter.springboot.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.telus.starter.springboot.jwt.filter.JWTTokenFilter;


@Configuration
public class JwtConfig {
	@Autowired
	JWTTokenFilter jwtTokenFilter;
	
	@Bean
	public FilterRegistrationBean filterRegistrationBean() {
		FilterRegistrationBean registrationBean = new FilterRegistrationBean();

		//create filter
		//JWTTokenFilter jwtTokenFilter = new JWTTokenFilter();
		
		//registrationBean
		registrationBean.setFilter(jwtTokenFilter);
		String openAPIContextPath="/fraudManagement/v1";
		String urlPatterns= openAPIContextPath + "/*";		
		registrationBean.addUrlPatterns(urlPatterns);
		registrationBean.setOrder(1); // set precedence

		return registrationBean;
	}
   
}
