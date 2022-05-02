package com.fz.demoschool.schoolservice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cloud.openfeign.EnableFeignClients;

@ConfigurationPropertiesScan
@EnableFeignClients
@SpringBootApplication
public class SchoolServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(SchoolServiceApplication.class, args);
	}

}
