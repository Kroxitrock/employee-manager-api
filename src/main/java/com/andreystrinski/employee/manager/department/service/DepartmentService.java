package com.andreystrinski.employee.manager.department.service;

import com.andreystrinski.employee.manager.department.model.Department;
import com.andreystrinski.employee.manager.department.repository.DepartmentRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DepartmentService {

  private final DepartmentRepository departmentRepository;

  public List<Department> getAll() {
    return departmentRepository.findAll();
  }
}
