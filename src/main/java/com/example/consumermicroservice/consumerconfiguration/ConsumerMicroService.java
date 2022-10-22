package com.example.consumermicroservice.consumerconfiguration;

import com.azure.spring.messaging.eventhubs.support.EventHubsHeaders;
import com.example.consumermicroservice.entity.Employee;
import com.example.consumermicroservice.entity.Employee_Changes;
import com.example.consumermicroservice.service.EmployeeRecieverService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;

import java.io.IOException;
import java.util.Collection;
import java.util.function.Consumer;

@Slf4j
@Configuration
@AllArgsConstructor
public class ConsumerMicroService {
    EmployeeRecieverService employeeRecieverService;

    public void messageRecieverLogger(Message<Employee_Changes> message) {
        log.info("New message received: '{}', partition key: {}, sequence number: {}, offset: {}, enqueued time: {}",
                message.getPayload(),
                message.getHeaders().get(EventHubsHeaders.PARTITION_KEY),
                message.getHeaders().get(EventHubsHeaders.SEQUENCE_NUMBER),
                message.getHeaders().get(EventHubsHeaders.OFFSET),
                message.getHeaders().get(EventHubsHeaders.ENQUEUED_TIME)
        );
    }




    @Bean
    public void dataBaseInitialiser ( ) throws IOException, InterruptedException {
        employeeRecieverService.sendingRequestToProducer();
    }


    @Bean
    public Consumer<Message<Employee_Changes>> consume() {
        return message -> {
            log.info("Employee : {} Changes has recieved ", message.getPayload().getEmployee());
            messageRecieverLogger(message);
            employeeRecieverService.EmployeeHandler(message.getPayload());
        };
    }


}
