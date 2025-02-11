package com.telus.starter.springboot.service;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.telus.starter.springboot.config.Config;

@Service
public class ConfigService {

	@Autowired
	private Config config;


	@Autowired
	private ObjectMapper mapper;

	public String outputConfig(boolean htmlFormat) {

		String htmlPrefix = "<pre><code>";
		String htmlSuffix = "</pre></code>";

		String jsonConfigProps = "ERROR";


		Map<String, Object> configOutputMap = new LinkedHashMap<>();
		configOutputMap.put("connections", config.getConnections());


		try {
			jsonConfigProps = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(configOutputMap);

		} catch (JsonProcessingException e) {
			String errMsg = "Unable to marshall WebServices list into JSON: " + e.getMessage();
			System.out.println(errMsg);
		}

		if (htmlFormat) {
			jsonConfigProps = htmlPrefix + jsonConfigProps + htmlSuffix;
		}

		System.out.println("jsonConfigProps="+ jsonConfigProps);
		return jsonConfigProps;
	}

}
