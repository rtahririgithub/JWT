package com.telus.starter.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;



@SpringBootApplication
@ComponentScan("com.telus.starter.springboot")
@ComponentScan("com.telus.starter.springboot.jwt.jwkey")



public class HelloWorldApplication {

		//Enable this line in order to force a failed scan
		//private String password = "blue";

	  public static void main(String[] args)  {

		  SpringApplication.run(HelloWorldApplication.class, args);
	  }


}
