package com.andreystrinski.employee.manager.employee.model;

import com.andreystrinski.employee.manager.department.model.Department;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Employee {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String firstName;

  private String lastName;

  private String email;

  @ManyToOne
  private EmployeeStatus status;

  @ManyToOne
  private EmployeePosition position;

  @ManyToOne
  private EmployeeSeniority seniority;

  @ManyToOne
  private Department department;

  @OneToMany(
      cascade = CascadeType.REMOVE,
      mappedBy = "employee"
  )
  private List<EmployeeStatusChange> statusChanges;
}

