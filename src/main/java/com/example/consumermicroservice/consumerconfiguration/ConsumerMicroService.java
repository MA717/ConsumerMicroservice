package com.example.consumermicroservice.consumerconfiguration;

import com.azure.spring.messaging.eventhubs.support.EventHubsHeaders;
import com.example.consumermicroservice.entity.EmployeeChanges;
import com.example.consumermicroservice.service.EmployeeRecieverService;
import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cloudevents.CloudEvent;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;

import java.io.IOException;
import java.util.function.Consumer;

@Slf4j
@Configuration
@AllArgsConstructor
public class ConsumerMicroService {
    private final EmployeeRecieverService employeeRecieverService;
    private final ObjectMapper mapper;



    @Bean
    public void dataBaseInitialiser() throws IOException, InterruptedException {
        employeeRecieverService.sendingRequestToProducer();
    }


    @Bean
    public Consumer<CloudEvent> consume() throws IOException,DatabindException,StreamReadException {
        return cloudevent -> {

            log.info(" New Cloud Event has recieved ");
            log.info("cloud event id {}", cloudevent.getId());
            log.info("cloud event type {}",cloudevent.getType());
            log.info("cloud event Content {}",cloudevent.getDataContentType());


            try {
                EmployeeChanges employee_changes = mapper.readValue(cloudevent.getData().toBytes(), new TypeReference<EmployeeChanges>() {
                });
                log.info("For Employee :{} ",employee_changes);

                employeeRecieverService.EmployeeHandler(employee_changes);


            } catch (IOException e) {
                throw new RuntimeException(e);
            }



        };

    }
}
