package com.fz.demoschool.schoolservice;

import com.fz.demoschool.schoolservice.config.SchoolProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Bean;

@Slf4j
@ConfigurationPropertiesScan
@SpringBootApplication
public class SchoolServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(SchoolServiceApplication.class, args);
	}


	@Bean
	public NewTopic newTopic(SchoolProperties schoolProperties) {
		return new NewTopic(schoolProperties.getTopicName(), 1, (short)1);
	}
}
