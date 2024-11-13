package com.andreystrinski.employee.manager.employee.repository;

import com.andreystrinski.employee.manager.employee.model.EmployeeStatusChange;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeStatusChangeRepository extends JpaRepository<EmployeeStatusChange, Long> {

}
