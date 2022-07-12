package com.fz.demoschool.schoolservice;

import com.fz.demoschool.core.CoreApplication;
import com.fz.demoschool.corekafka.CoreKafkaApplication;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cloud.openfeign.EnableFeignClients;

@ConfigurationPropertiesScan
@EnableFeignClients
@SpringBootApplication(scanBasePackageClasses = {SchoolServiceApplication.class, CoreApplication.class, CoreKafkaApplication.class})
public class SchoolServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(SchoolServiceApplication.class, args);
	}

}
