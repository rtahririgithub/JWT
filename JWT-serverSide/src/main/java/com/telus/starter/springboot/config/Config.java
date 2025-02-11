package com.telus.starter.springboot.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties
public class Config {

	private static final Log LOG = LogFactory.getLog(Config.class);
	private Connections connections;

	public Connections getConnections() {
		return connections;
	}
	public void setConnections(Connections connections) {
		this.connections = connections;
	}
	
	public static class Connections {

		private List<WebService> webServices = new ArrayList<>();

		public List<WebService> getWebServices() {
			return webServices;
		}
		public void setWebServices(List<WebService> webServices) {
			this.webServices = webServices;
		}
	}


	public static class WebService {

		private String name;
		private String username;
		private String password;
		private String endpointAddress;

		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getUsername() {
			return username;
		}
		public void setUsername(String username) {
			this.username = username;
		}
		public String getPassword() {
			return password;
		}
		public void setPassword(String password) {
			this.password = password;
		}
		public String getEndpointAddress() {
			return endpointAddress;
		}
		public void setEndpointAddress(String endpointAddress) {
			this.endpointAddress = endpointAddress;
		}
	}

}
