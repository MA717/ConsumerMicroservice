package com.example.consumermicroservice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ConsumerMicroserviceApplicationTests {

    @Test
    void contextLoads() {
    }
    //    public void SaveEmployeeIntialiser(Employee employee) {
//
//        if (employee.getManager() != null) {
//            employeeRepository.findByDn(employee.getManager().getDn()).flatMap(
//                    (manager) -> {
//                        SaveEmployeeIntialiser(employee.getManager());
//                        return employeeRepository.findByDn(employee.getManager().getDn());
//                    }).ifPresent((manager2) -> employee.setManager(manager2));
//
//        }
//        if (!employeeRepository.findByDn(employee.getDn()).isPresent()) {
//            CreateEmployee(employee);
//        }
//
//    }

}
