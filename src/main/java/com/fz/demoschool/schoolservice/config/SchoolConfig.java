package com.fz.demoschool.schoolservice.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaOperations;
import org.springframework.kafka.listener.CommonErrorHandler;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.FixedBackOff;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Configuration
public class SchoolConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    public static final String KAFKA_HEADER_RETRIES = "retries";

    @Bean
    public CommonErrorHandler eh(KafkaOperations<String, String> template) {
        return new DefaultErrorHandler(new DeadLetterPublishingRecoverer(template, (rec, ex) -> {
            String topic = rec.topic();
            Header retries = rec.headers().lastHeader(KAFKA_HEADER_RETRIES);
            if (retries == null) {
                retries = new RecordHeader(KAFKA_HEADER_RETRIES, new byte[]{1});
                rec.headers().add(retries);
            } else {
                retries.value()[0]++;
            }
            log.info("ErrorHandler - topic:\"{}\", retry: {}", topic, retries.value()[0]);
            return retries.value()[0] > 5
                    ? new TopicPartition(topic + ".DLT", rec.partition())
                    : new TopicPartition(topic, rec.partition());
        }), new FixedBackOff(0L, 0L));
    }
}
