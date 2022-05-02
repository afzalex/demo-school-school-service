package com.fz.demoschool.schoolservice.controller;

import com.fz.demoschool.avro.SchoolEvent;
import com.fz.demoschool.core.dto.TeacherModel;
import com.fz.demoschool.schoolservice.config.SchoolProperties;
import com.fz.demoschool.schoolservice.feign.TeacherFeignClient;
import com.fz.demoschool.schoolservice.models.SchoolModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
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

    @Autowired
    private TeacherFeignClient teacherFeignClient;

    @GetMapping("/{schoolId}")
    public SchoolModel getSchoolInfo(@PathVariable("schoolId") Integer schoolId) {
        return schoolList.stream().filter(s -> schoolId.equals(s.getSchoolId())).findFirst()
                .map(s -> {
                    s.setTeachers(teacherFeignClient.getTeacherList(schoolId));
                    return s;
                })
                .get();
    }
}
