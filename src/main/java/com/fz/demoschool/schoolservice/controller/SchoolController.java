package com.fz.demoschool.schoolservice.controller;

import com.fz.demoschool.avro.SchoolEvent;
import com.fz.demoschool.corekafka.config.Constants;
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

@Slf4j
@RestController
@RequestMapping("/school")
@RequiredArgsConstructor
public class SchoolController {

    private final KafkaTemplate<String, SchoolEvent> kafkaTemplate;
    private final SchoolProperties schoolProperties;
    private final SchoolRepository schoolRepository;
    private final String serviceUUID;


    @Autowired
    private TeacherFeignClient teacherFeignClient;

    @GetMapping("/ping")
    public String ping() {
        String message = String.format("%s : Ping is working properly School Service : %s", serviceUUID, schoolProperties.getPingMessage());
        log.info(message);
        return message;
    }

    @PostMapping
    public void createSchoolEntity(@RequestBody SchoolModel schoolModel) {
        kafkaTemplate.send(Constants.TOPIC_SCHOOL_GENERAL_EVENT,
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
                .orElseThrow();
    }
}
