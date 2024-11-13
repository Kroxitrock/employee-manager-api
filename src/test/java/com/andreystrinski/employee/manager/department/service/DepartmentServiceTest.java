package com.andreystrinski.employee.manager.department.service;

import com.andreystrinski.employee.manager.department.repository.DepartmentRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

@SpringJUnitConfig(DepartmentService.class)
public class DepartmentServiceTest {

  @MockBean
  private DepartmentRepository departmentRepository;

  @Autowired
  private DepartmentService departmentService;

  @Test
  void whenGettingDepartments_thenFetchDepartments() {

    // When
    departmentService.getAll();

    // ThendepartmentRepository
    Mockito.verify(departmentRepository).findAll();
  }

}
