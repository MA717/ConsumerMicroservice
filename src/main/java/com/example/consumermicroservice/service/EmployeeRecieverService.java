package com.example.consumermicroservice.service;


import com.example.consumermicroservice.entity.Changes;
import com.example.consumermicroservice.entity.Employee;
import com.example.consumermicroservice.entity.EmployeeChanges;
import com.example.consumermicroservice.repository.EmployeeRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class EmployeeRecieverService {
    private static final String POSTS_API_URL = "http://localhost:8080/employees";
    private final EmployeeRepository employeeRepository;

    public void sendingRequestToProducer() throws IOException, InterruptedException {
        if (employeeRepository.count() == 0) {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .GET()
                    .header("accept", "application/json")
                    .header("Authorization", "bearer eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJhakFoM3M1TkU1SUh4WENSNkNlUzhIQV9GcWhPYV96M2lfYXVEV0Q2SHZzIn0.eyJleHAiOjE2NjgzNjEzNDIsImlhdCI6MTY2ODM1OTU0MiwianRpIjoiNzA1ZjZiOTItNDhmOS00NjVjLTkyYTktNWFlMmZiYjYxYWU1IiwiaXNzIjoiaHR0cHM6Ly9rZXljbG9hay5tdC1hZy5jb20vYXV0aC9yZWFsbXMvZGV2ZWxvcG1lbnQiLCJhdWQiOlsianViaWxlZXMiLCJlbXBsb3llZS1saXN0LXNlcnZpY2UiLCJtb29kIiwiYXR0ZW5kYW5jZSIsImFjY291bnQiXSwic3ViIjoiNWU3MjQ4MjktNTc0Zi00OWY2LTllYjYtY2Q1YTc0YzAxZDNkIiwidHlwIjoiQmVhcmVyIiwiYXpwIjoiZGV2ZWxvcGVyLXRvb2xzIiwic2Vzc2lvbl9zdGF0ZSI6IjM0Nzg3YmZhLWE4YzAtNGI5Ni04M2U1LTM3MzE3NDg3M2E4MyIsImFjciI6IjEiLCJyZWFsbV9hY2Nlc3MiOnsicm9sZXMiOlsib2ZmbGluZV9hY2Nlc3MiLCJ1bWFfYXV0aG9yaXphdGlvbiIsImVtcGxveWVlIl19LCJyZXNvdXJjZV9hY2Nlc3MiOnsianViaWxlZXMiOnsicm9sZXMiOlsiZW1wbG95ZWUiXX0sImVtcGxveWVlLWxpc3Qtc2VydmljZSI6eyJyb2xlcyI6WyJlbXBsb3llZSJdfSwibW9vZCI6eyJyb2xlcyI6WyJlbXBsb3llZSJdfSwiYXR0ZW5kYW5jZSI6eyJyb2xlcyI6WyJlbXBsb3llZSJdfSwiYWNjb3VudCI6eyJyb2xlcyI6WyJtYW5hZ2UtYWNjb3VudCIsIm1hbmFnZS1hY2NvdW50LWxpbmtzIiwidmlldy1wcm9maWxlIl19fSwic2NvcGUiOiJlbXBsb3llZV9hdHRyaWJ1dGVzIHByb2ZpbGUgZW1haWwiLCJzaWQiOiIzNDc4N2JmYS1hOGMwLTRiOTYtODNlNS0zNzMxNzQ4NzNhODMiLCJlbWFpbF92ZXJpZmllZCI6dHJ1ZSwibmFtZSI6IlN0ZWZmZW4gSMO8bHN3aXR0IiwicHJlZmVycmVkX3VzZXJuYW1lIjoiYXR0ZW5kYW5jZS1lbXBsb3llZSIsImRlcGFydG1lbnQiOiJPcmFjbGUgSmF2YSBEZXZlbG9wbWVudCIsInRpdGxlIjoiQmVyYXRlciIsImdpdmVuX25hbWUiOiJTdGVmZmVuIiwiZmFtaWx5X25hbWUiOiJIw7xsc3dpdHQiLCJlbWFpbCI6ImF0dGVuZGFuY2UtZW1wbG95ZWVAZGV2Lm10LWFnLmNvbSJ9.DCJuYDSn97eBEOuQNYGR_kHnwmOYwCtqa0ZadXC6q2P8NtVMp0XVTZ_sAWK62bFSHBr99c9aYC8WkG6yF9xPMBocT6vae_dP13eztltGLZLQAYb1yN2T42aQGressUq4klY0SpRbVvrj0ov4EwB7CVSErZrkADDz1BB9dg1N90X0C--PNupxpoRgsEJg756zjPtK8I4QxvELgvcqbLTPau0CJp11HgrawYfHSk_L5SKHwqzQZfUiNrdRk0rlPALorijSbBeFsR7Ulw1OE7abqi17lZJaId6IXZ0Z6erWnje6-yctzBMlEwdtcVjR3nVBbc7-Ve3jOXc9uFM9f_R9Hg")
                    .uri(URI.create(POSTS_API_URL))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            ObjectMapper mapper = new ObjectMapper();
            List<Employee> employeeList = mapper.readValue(response.body(), new TypeReference<List<Employee>>() {
            });
            EmployeesInitializer(employeeList);
            log.info(employeeList.toString());
        }


    }

    public void EmployeesInitializer(List<Employee> employees) {

        employees.stream().forEach(x -> SaveEmployeeIntialiser(x));
    }


    public void SaveEmployeeIntialiser(Employee employee) {

        if (!employeeRepository
                .findByUsername(employee.getUsername())
                .isPresent()) {
            CreateEmployee(employee);
        }


    }

    public void EmployeeHandler(EmployeeChanges employeeChanges) {

        if (employeeChanges.getChanges().equals(Changes.EMPLOYEE_CREATED)) {
            CreateEmployee(employeeChanges.getEmployee());
        } else if (employeeChanges.getChanges().equals(Changes.EMPLOYEE_DELETED)) {
            deleteEmployee(employeeChanges.getEmployee());
        } else {
            UpdateEmployee(employeeChanges.getEmployee());
        }
    }

    public void deleteEmployee(Employee employee) {
        employee.setId(null);
        employeeRepository.deleteByUsername(employee.getUsername());
        log.info("Employee is deleted {} in the Consumer Sucessfully ", employee);

    }


    private void proceedChange(Employee employeeEntityOld, Employee employeeEntityNew) {

        employeeEntityOld.setGivenName(employeeEntityNew.getGivenName());
        employeeEntityOld.setSurname(employeeEntityNew.getSurname());
        employeeEntityOld.setEmail(employeeEntityNew.getEmail());
        employeeEntityOld.setMobileNumber(employeeEntityNew.getMobileNumber());
        employeeEntityOld.setDepartment(employeeEntityNew.getDepartment());
        employeeEntityOld.setTelephoneNumber(employeeEntityNew.getTelephoneNumber());
        employeeEntityOld.setTitle(employeeEntityNew.getTitle());
        employeeEntityOld.setUsername(employeeEntityNew.getUsername());

    }


    public Employee UpdateEmployee(Employee employee) {

        employeeRepository.findByUsername(employee.getUsername()).ifPresentOrElse(
                (employeeOld) -> {
                    log.info("Employee is found in the Consumer Sucessfully", employeeOld);
                    proceedChange(employeeOld, employee);
                    log.info("Employee {} is updated Sucessfully ", employeeOld);
                    employeeRepository.save(employeeOld);
                },

                () -> CreateEmployee(employee)

        );

        return employee;

    }

    public Employee SetEmployee(Employee employee) {
        employee.setId(null);
        Employee result = employeeRepository.save(employee);
        log.info("New Employee is saved Sucessfully {} ", result);

        return result;
    }

    public Employee CreateEmployee(Employee employee) {
        if (!employeeRepository.findByUsername(employee.getUsername()).isPresent()) {
            return SetEmployee(employee);
        } else {
            log.info("Employee with DN {} found already", employee.getUsername());
            return employee;
        }
    }
}
