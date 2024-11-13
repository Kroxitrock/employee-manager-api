package com.andreystrinski.employee.manager.employee.repository;

import com.andreystrinski.employee.manager.employee.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long>,
    QuerydslPredicateExecutor<Employee> {

}
