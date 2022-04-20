package com.fz.demoschool.schoolservice.controller;

import com.fz.demoschool.avro.SchoolEvent;
import com.fz.demoschool.schoolservice.config.SchoolProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
