package com.example.consumermicroservice.repository;

import com.example.consumermicroservice.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee , Long> {


    //Optional<Employee> findByDn(String dn);

 Optional<Employee>  findByDn( String dn );

    @Transactional
    void deleteByDn(String dn);
}
