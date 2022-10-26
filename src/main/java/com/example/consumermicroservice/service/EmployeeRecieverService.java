package com.example.consumermicroservice.service;


import com.example.consumermicroservice.entity.Changes;
import com.example.consumermicroservice.entity.Employee;
import com.example.consumermicroservice.entity.Employee_Changes;
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
import java.util.Optional;

@Service
@Slf4j
@AllArgsConstructor
public class EmployeeRecieverService {
    private final EmployeeRepository employeeRepository;
    private static final String POSTS_API_URL = "http://localhost:8082/employees/consumerInitializer";

    public void sendingRequestToProducer() throws IOException, InterruptedException {
        if (employeeRepository.count() == 0) {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .GET()
                    .header("accept", "application/json")
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

        if (employee.getManager() != null) {
            if (!employeeRepository
                    .findByDn(employee.getManager().getDn())
                    .isPresent()) {
                SaveEmployeeIntialiser(employee.getManager());
            }
        }
        CreateEmployee(employee);
    }

    public void EmployeeHandler(Employee_Changes employee_changes) {

        if (employee_changes.getChangesList().contains(Changes.NEWEMPLOYEE_Created)) {
            CreateEmployee(employee_changes.getEmployee());
        } else if (employee_changes.getChangesList().contains(Changes.EMPLOYEE_DELETED)) {
            deleteEmployee(employee_changes.getEmployee());
        } else {
            UpdateEmployee(employee_changes.getEmployee(), employee_changes.getChangesList());
        }
    }

    public void deleteEmployee(Employee employee) {
        employee.setId(null);
        employeeRepository.deleteByDn(employee.getDn());
        log.info("Employee is deleted {} in the Consumer Sucessfully ", employee);

    }


    private void ProceedChange(Changes change, Employee employee, Employee employee1) {
        if (change.equals(Changes.MANAGER_Change)) {
            employee1.setManager(employeeRepository.findByDn(employee.getManager().getDn()).get());
        }
        if (change.equals(Changes.USERNAME_Change)) {
            employee1.setUsername(employee.getUsername());
        }

        if (change.equals(Changes.SURNAME_Change)) {
            employee1.setSurname(employee.getSurname());
        }
        if (change.equals(Changes.TELEPHONENUMBER_Change)) {
            employee1.setTelephoneNumber(employee.getTelephoneNumber());
        }
        if (change.equals(Changes.DEPARTMENT_Change)) {
            employee1.setDepartment(employee.getDepartment());
        }
        if (change.equals(Changes.EMAIL_Change)) {
            employee1.setEmail(employee.getEmail());
        }
        if (change.equals(Changes.MOBILENUMBER_Change)) {
            employee1.setMobileNumber(employee.getMobileNumber());
        }
        if (change.equals(Changes.FIRSTNAME_Change)) {
            employee1.setGivenName(employee.getGivenName());
        }
        if (change.equals(Changes.TITLE_Change)) {
            employee1.setTitle(employee.getTitle());
        }

    }

    public Employee UpdateEmployee(Employee employee, List<Changes> changesList) {

        Optional<Employee> employee1 = employeeRepository.findByDn(employee.getDn());
        if (employee1.isPresent()) {
            log.info("Employee is found in the Consumer Sucessfully", employee1);
            changesList.stream()
                    .forEach(x -> ProceedChange(x, employee, employee1.get()));

            log.info("Employee {} is updated Sucessfully ", employee1);
            return employeeRepository.save(employee1.get());
        } else {
            return CreateEmployee(employee);
        }


    }

    public Employee SetEmployee(Employee employee) {
        employee.setId(null);
        if (employee.getManager() != null) {
            employee.setManager(employeeRepository.findByDn(employee.getManager().getDn()).get());
        }
        Employee result = employeeRepository.save(employee);
        log.info("New Employee is saved Sucessfully {} ", result);

        return result;
    }

    public Employee CreateEmployee(Employee employee) {
        if (!employeeRepository.findByDn(employee.getDn()).isPresent()) {
            return SetEmployee(employee);
        } else {
            log.info("Employee with DN {} found already", employee.getDn());
            return employee;
        }
    }
}
