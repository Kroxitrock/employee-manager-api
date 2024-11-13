package com.andreystrinski.employee.manager.employee.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompactEmployeeDto {

  private Long id;
  private String fullName;
  private String email;
  private String statusName;
  private String positionName;
  private String seniorityName;
  private String departmentName;
}
