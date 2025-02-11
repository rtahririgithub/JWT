package com.telus.starter.springboot.service;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class GreetingServiceTest {
	
	private GreetingService greetingSvc;
	
	
	
	@Before
    public void setUp() {
		greetingSvc = new GreetingService();
	}
	
	
	@Test
	public void testGreetingMessageContainsProvidedName() {
		
		String inputName = "Nole";
		String expectedGreetingMsg = String.format("Hello, %s!", inputName);
		
		assertEquals(expectedGreetingMsg, greetingSvc.getGreetingMessage(inputName));
	}

}
