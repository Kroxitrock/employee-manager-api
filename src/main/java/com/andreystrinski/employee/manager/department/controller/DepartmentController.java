package com.andreystrinski.employee.manager.department.controller;

import com.andreystrinski.employee.manager.department.model.dto.DepartmentDto;
import com.andreystrinski.employee.manager.department.service.DepartmentService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/departments")
@RequiredArgsConstructor
public class DepartmentController {

  private final ModelMapper modelMapper;

  private final DepartmentService departmentService;

  @GetMapping()
  public ResponseEntity<List<DepartmentDto>> findDepartments() {
    var departments = departmentService.getAll();

    var response = departments.stream()
        .map(department -> modelMapper.map(department, DepartmentDto.class)).toList();

    return ResponseEntity.ok(response);
  }
}
