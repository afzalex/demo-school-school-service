package com.fz.demoschool.schoolservice.controller;

import com.fz.demoschool.avro.SchoolEvent;
import com.fz.demoschool.core.TeacherModel;
import com.fz.demoschool.schoolservice.config.SchoolProperties;
import com.fz.demoschool.schoolservice.models.SchoolModel;
import com.netflix.discovery.shared.Application;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.ws.rs.Path;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequestMapping("/school")
@Slf4j
@RequiredArgsConstructor
public class SchoolController {

    private final KafkaTemplate<String, SchoolEvent> kafkaTemplate;
    private final SchoolProperties schoolProperties;

    private final AtomicInteger counter = new AtomicInteger();

    @GetMapping("/ping")
    public String ping() {
        log.info("I am ping information.");

        kafkaTemplate.send(schoolProperties.getTopicName(),
                SchoolEvent.newBuilder()
                        .setEvent("CREATED")
                        .setUuid(UUID.randomUUID().toString())
                        .setName("My School " + counter.incrementAndGet())
                        .build()
        );

        return "Ping is working properly";
    }

    private final List<SchoolModel> schoolList = List.of(
            SchoolModel.builder().id(UUID.randomUUID().toString()).schoolId(1).name("My School 1").build(),
                SchoolModel.builder().id(UUID.randomUUID().toString()).schoolId(2).name("My School 2").build(),
                SchoolModel.builder().id(UUID.randomUUID().toString()).schoolId(3).name("My School 3").build()
        );

    @GetMapping
    public List<SchoolModel> getSchoolList(){
        return schoolList;
    }


    private final DiscoveryClient discoveryClient;
    private final RestTemplate restTemplate;

    @GetMapping("/{schoolId}")
    public SchoolModel getSchoolInfo(@PathVariable("schoolId") Integer schoolId) {
        List<ServiceInstance> instances = discoveryClient.getInstances("teacher-service");
        ServiceInstance instance = instances.get(0);
        return schoolList.stream().filter(s -> schoolId.equals(s.getSchoolId())).findFirst()
                .map(s -> {
                    List<TeacherModel> teachers = restTemplate.getForEntity(String.format("http://%s:%s/teacher/%d", instance.getHost(), instance.getPort(), schoolId), List.class).getBody();
                    s.setTeachers(teachers);
                    return s;
                })
                .get();
    }
}
