package com.andreystrinski.employee.manager.employee.repository;

import com.andreystrinski.employee.manager.employee.model.EmployeeStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeStatusRepository extends JpaRepository<EmployeeStatus, Long> {

}
