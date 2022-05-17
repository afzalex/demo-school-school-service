package com.fz.demoschool.schoolservice.listener;

import com.fz.demoschool.avro.SchoolEvent;
import com.fz.demoschool.schoolservice.models.SchoolModel;
import com.fz.demoschool.schoolservice.repostiory.SchoolRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SchoolListener {

    private final SchoolRepository schoolRepository;

    @KafkaListener(topics = "#{'${school.topicName}'}", groupId = "simple-consumer")
    public void consume(SchoolEvent schoolEvent) {
        log.info("Consuming Kafka Message : {}", schoolEvent);
        SchoolModel model = schoolRepository.add(SchoolModel.builder().name(schoolEvent.getName()).build());
        log.info("Created School Entity with ID : {}, and UUID : ", model.getSchoolId(), model.getId());
    }
}
