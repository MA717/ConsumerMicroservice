package com.example.consumermicroservice.service;


import com.example.consumermicroservice.entity.Changes;
import com.example.consumermicroservice.entity.Employee;
import com.example.consumermicroservice.entity.Employee_Changes;
import com.example.consumermicroservice.repository.EmployeeRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@AllArgsConstructor
public class EmployeeRecieverService {
    EmployeeRepository employeeRepository;

    public void EmployeesInitializer(Collection<Employee> employees) {

        employees.stream().forEach(x -> SaveEmployeeIntialiser(x));
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

//
//    public void  SaveEmployeeIntialiserOpt ( Employee employee){
//        Optional<Employee> employee1 = employeeRepository.findByDn( employee.getDn()).f
//
//    }
    public void SaveEmployeeIntialiser(Employee employee) {

        if (employee.getManager() != null) {
            Employee manager = employeeRepository.findByDn(employee.getManager().getDn());
            if (manager == null) {
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


    public Employee UpdateEmployee(Employee employee, List<Changes> changesList) {
        Employee employee1 = employeeRepository.findByDn(employee.getDn());
        if (employee1 != null) {
            log.info("Employee is found in the Consumer Sucessfully ");


            for (Changes change : changesList) {
                if (change.equals(Changes.MANAGER_Change)) {
                    employee1.setManager(employeeRepository.findByDn(employee.getManager().getDn()));
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
            log.info("Employee {} is updated Sucessfully ", employee1);
            return employeeRepository.save(employee1);
        } else {
            return CreateEmployee(employee);
        }
    }

    public Employee SetEmployee(Employee employee) {
        employee.setId(null);
        if (employee.getManager() != null) {
            employee.setManager(employeeRepository.findByDn(employee.getManager().getDn()));
        }
        Employee result = employeeRepository.save(employee);
        log.info("New Employee is saved Sucessfully {} ", result);

        return result;
    }

    public Employee CreateEmployee(Employee employee) {
        if (employeeRepository.findByDn(employee.getDn()) == null) {
            return SetEmployee(employee);
        } else {
            log.info("Employee with DN {} found already", employee.getDn());
            return employee;
        }
    }

}
