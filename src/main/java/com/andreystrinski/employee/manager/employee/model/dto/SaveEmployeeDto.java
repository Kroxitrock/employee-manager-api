package com.andreystrinski.employee.manager.employee.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SaveEmployeeDto {

  @NotBlank(message = "First name is required!")
  private String firstName;
  @NotBlank(message = "Last name is required!")
  private String lastName;
  @NotBlank(message = "Email is required!")
  @Email(regexp = "[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]+", message = "Email should be in the correct format!")
  private String email;
  @NotNull(message = "Status id is required!")
  private Long statusId;
  @NotNull(message = "Position id is required!")
  private Long positionId;
  @NotNull(message = "Seniority id is required!")
  private Long seniorityId;
  private Long departmentId;
}
