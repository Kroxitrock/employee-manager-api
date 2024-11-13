package com.andreystrinski.employee.manager.employee.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeStatusChange {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String reason;

  @ManyToOne
  private Employee employee;

  @ManyToOne
  private EmployeeStatus oldStatus;

  @ManyToOne
  private EmployeeStatus newStatus;

  @CreationTimestamp
  private LocalDateTime dateCreated;

  public EmployeeStatusChange(String reason, Employee employee, EmployeeStatus oldStatus,
      EmployeeStatus newStatus) {
    this.reason = reason;
    this.employee = employee;
    this.oldStatus = oldStatus;
    this.newStatus = newStatus;
  }
}
