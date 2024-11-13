package com.andreystrinski.employee.manager.employee.repository;

import com.andreystrinski.employee.manager.employee.model.EmployeeSeniority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeSeniorityRepository extends JpaRepository<EmployeeSeniority, Long> {

}
