package com.example.consumermicroservice.repository;

import com.example.consumermicroservice.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee , Long> {




 Optional<Employee>  findByUsername( String Username );


    @Transactional

    void deleteByUsername(String username);
}
