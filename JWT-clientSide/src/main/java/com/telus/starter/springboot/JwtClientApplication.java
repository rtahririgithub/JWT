package com.telus.starter.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Profile;



@SpringBootApplication
@ComponentScan("com.telus.starter.springboot")
@ComponentScan("com.telus.starter.springboot.jwt.jwkey")
@ComponentScan("com.telus.starter.springboot.security")
@Profile("dv")               


public class JwtClientApplication {

		//Enable this line in order to force a failed scan
		//private String password = "blue";

	  public static void main(String[] args)  {

		  SpringApplication.run(JwtClientApplication.class, args);
	  }


}
