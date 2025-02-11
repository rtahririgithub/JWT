package com.telus.starter.springboot.controller;
import static org.junit.Assert.fail;

import java.io.UnsupportedEncodingException;

import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.telus.starter.springboot.service.GreetingService;


@RunWith(SpringRunner.class)
public class HelloWorldControllerTest {

	
	private MockMvc helloWorldMockMvc;
	
	@MockBean
	private GreetingService greetingSvcMock;
	
	@InjectMocks
	private HelloWorldController helloWorld;

	
	@Before
    public void setUp() {
		
		MockitoAnnotations.initMocks(this);
		
        helloWorldMockMvc = MockMvcBuilders.standaloneSetup(helloWorld).build();
	}
	
	
	@Test
	public void testGreetingReturnsResponseWithProvidedName() {
		
		
		// Arrange
		String inputName = "BTO";
		String expectedGreetingMsg = String.format("Hello, %s!", inputName);
		String expectedJsonResp = "{\"id\":1,\"content\":\"" + expectedGreetingMsg + "\"}";
		
		Mockito.when(
				greetingSvcMock.getGreetingMessage(
						Mockito.anyString())).thenReturn(expectedGreetingMsg);
		
		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.get("/greeting")
				.param("name", "BTO")
				.accept(MediaType.APPLICATION_JSON);
		
		
		// Act
		MvcResult mvcResult = invokeGreetingRequest(requestBuilder);
		
		
		// Assert
		assertJsonResponse(expectedJsonResp, mvcResult);
	}
	
	
	@Test
	public void testGreetingReturnsResponseWithDefaultName() {
		
		// Arrange
		String expectedGreetingMsg = String.format("Hello, World!");
		String expectedJsonResp = "{\"id\":1,\"content\":\"" + expectedGreetingMsg + "\"}";
		
		Mockito.when(
				greetingSvcMock.getGreetingMessage(
						Mockito.anyString())).thenReturn(expectedGreetingMsg);
		
		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.get("/greeting")
				.accept(MediaType.APPLICATION_JSON);
		
		
		// Act
		MvcResult mvcResult = invokeGreetingRequest(requestBuilder);
		
		
		// Assert
		assertJsonResponse(expectedJsonResp, mvcResult);
	}

	
	/*
	 * 
	 */
	private MvcResult invokeGreetingRequest(RequestBuilder requestBuilder) {
		
		MvcResult result = null;
		try {
			result = helloWorldMockMvc
					.perform(requestBuilder)
					.andReturn();
	
		} catch (Exception e) {
			fail("Unable to execute MockMvc");
		}
		return result;
	}
	
	
	/*
	 * 
	 */
	private void assertJsonResponse(String expectedJsonResp, MvcResult mvcResult) {
		
		try {
			JSONAssert.assertEquals(expectedJsonResp, 
					mvcResult.getResponse().getContentAsString(), false);
			
		} catch (UnsupportedEncodingException | JSONException e) {
			fail("JSONAssert.assertEquals failed: " + e.getMessage());
		} 
	}

}
