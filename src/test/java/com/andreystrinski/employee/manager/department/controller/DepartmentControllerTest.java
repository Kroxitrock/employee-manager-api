package com.andreystrinski.employee.manager.department.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.andreystrinski.employee.manager.department.model.Department;
import com.andreystrinski.employee.manager.department.model.dto.DepartmentDto;
import com.andreystrinski.employee.manager.department.service.DepartmentService;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(DepartmentController.class)
public class DepartmentControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private DepartmentService departmentService;

  @MockBean
  private ModelMapper modelMapper;

  @Test
  public void whenFindingDepartments_thenDepartmentListIsReturned() throws Exception {
    var department = Department.builder().id(1L).build();
    var departments = List.of(department);
    var departmentDto = DepartmentDto.builder().id(department.getId()).build();

    Mockito.when(departmentService.getAll()).thenReturn(departments);
    Mockito.when(modelMapper.map(department, DepartmentDto.class)).thenReturn(departmentDto);

    // When
    mockMvc.perform(get("/departments"))
        // Then
        .andExpect(status().isOk()).andExpect(jsonPath("$.[0].id").value(departmentDto.getId()));
  }
}
