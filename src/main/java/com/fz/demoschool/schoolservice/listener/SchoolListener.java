package com.fz.demoschool.schoolservice.listener;

import com.fz.demoschool.avro.SchoolEvent;
import com.fz.demoschool.corekafka.config.Constants;
import com.fz.demoschool.schoolservice.models.SchoolModel;
import com.fz.demoschool.schoolservice.repostiory.SchoolRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Service
@RequiredArgsConstructor
public class SchoolListener {

    private final SchoolRepository schoolRepository;

    private final AtomicInteger counter = new AtomicInteger();

    @KafkaListener(topics = Constants.TOPIC_SCHOOL_GENERAL_EVENT, groupId = "#{'${demo.appname.school}'}")
    public void consume(SchoolEvent schoolEvent) {
        log.info("Consuming Kafka Message : {}", schoolEvent);
        if(counter.incrementAndGet() < 10) throw new RuntimeException();
        SchoolModel model = schoolRepository.add(SchoolModel.builder().name(schoolEvent.getName()).build());
        log.info("Created School Entity with ID : {}, and UUID : ", model.getSchoolId(), model.getId());
    }
}
