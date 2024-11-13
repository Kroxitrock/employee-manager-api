package com.andreystrinski.employee.manager.employee.repository;

import com.andreystrinski.employee.manager.employee.model.EmployeePosition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeePositionRepository extends JpaRepository<EmployeePosition, Long> {

}
