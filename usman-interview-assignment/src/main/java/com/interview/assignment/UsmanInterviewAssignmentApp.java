package com.interview.assignment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class UsmanInterviewAssignmentApp {

	public static void main(String[] args) {
		SpringApplication.run(UsmanInterviewAssignmentApp.class, args);
	}

}
