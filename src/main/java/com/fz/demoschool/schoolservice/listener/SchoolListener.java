package com.fz.demoschool.schoolservice.listener;

import com.fz.demoschool.avro.SchoolEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SchoolListener {

//    @KafkaListener(topics = "#{'${school.topicName}'}", groupId = "simple-consumer")
//    public void consume(SchoolEvent schoolEvent) {
//        log.info("Consuming Kafka Message : {}", schoolEvent);
//    }
}
