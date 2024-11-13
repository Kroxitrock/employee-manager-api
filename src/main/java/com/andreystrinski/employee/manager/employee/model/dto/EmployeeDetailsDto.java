package com.andreystrinski.employee.manager.employee.model.dto;

import com.andreystrinski.employee.manager.department.model.dto.DepartmentDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDetailsDto {

  private Long id;
  private String firstName;
  private String lastName;
  private String email;
  private EmployeeStatusDto status;
  private EmployeePositionDto position;
  private EmployeeSeniorityDto seniority;
  private DepartmentDto department;
}
