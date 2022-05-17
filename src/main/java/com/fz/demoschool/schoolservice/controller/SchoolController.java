package com.fz.demoschool.schoolservice.controller;

import com.fz.demoschool.avro.SchoolEvent;
import com.fz.demoschool.schoolservice.config.SchoolProperties;
import com.fz.demoschool.schoolservice.feign.TeacherFeignClient;
import com.fz.demoschool.schoolservice.models.SchoolModel;
import com.fz.demoschool.schoolservice.repostiory.SchoolRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequestMapping("/school")
@Slf4j
@RequiredArgsConstructor
public class SchoolController {

    private static final String SERVICE_UUID = UUID.randomUUID().toString();
    private final KafkaTemplate<String, SchoolEvent> kafkaTemplate;
    private final SchoolProperties schoolProperties;
    private final SchoolRepository schoolRepository;

    @Autowired
    private TeacherFeignClient teacherFeignClient;

    private final AtomicInteger counter = new AtomicInteger();

    @GetMapping("/ping")
    public String ping() {
        log.info("{} : Ping is working properly in School Service.", SERVICE_UUID);
        return String.format("%s : Ping is working properly School Service", SERVICE_UUID);
    }

    @PostMapping
    public void createSchoolEntity(@RequestBody SchoolModel schoolModel) {
        kafkaTemplate.send(schoolProperties.getTopicName(),
                SchoolEvent.newBuilder()
                        .setEvent("CREATED")
                        .setUuid(UUID.randomUUID().toString())
                        .setName(schoolModel.getName())
                        .build()
        );
    }

    @GetMapping
    public List<SchoolModel> getSchoolList(){
        return schoolRepository.getList();
    }

    @GetMapping("/{schoolId}")
    public SchoolModel getSchoolInfo(@PathVariable("schoolId") Integer schoolId) {
        return schoolRepository.getList().stream()
                .filter(s -> schoolId.equals(s.getSchoolId())).findFirst()
                .map(s -> s.toBuilder().teachers(teacherFeignClient.getTeacherList(schoolId)).build())
                .get();
    }
}
