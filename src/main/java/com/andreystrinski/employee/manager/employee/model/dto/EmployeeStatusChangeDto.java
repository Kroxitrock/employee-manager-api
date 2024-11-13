package com.andreystrinski.employee.manager.employee.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeStatusChangeDto {

  @NotNull(message = "A new status id is required!")
  private Long newStatusId;
  private String reason;
}
