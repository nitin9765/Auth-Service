package com.edigest.authservice.events;

import com.edigest.authservice.model.UserKafkaDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Slf4j
@Service
public class EventProducer {
    @Autowired
    private KafkaTemplate<String, UserKafkaDto> kafkaTemplate;

    @Value("${spring.kafka.topic.name}")
    private String topicName;

    public void publishToUserService(String key, UserKafkaDto userKafkaDto) throws ExecutionException, InterruptedException {
        CompletableFuture<SendResult<String, UserKafkaDto>> future=kafkaTemplate.send(topicName, key, userKafkaDto);
        RecordMetadata metadata = future.get().getRecordMetadata();
        log.info("Message sent to topic: {} partition: {} offset: {}", metadata.topic(), metadata.partition(), metadata.offset());
    }
}
