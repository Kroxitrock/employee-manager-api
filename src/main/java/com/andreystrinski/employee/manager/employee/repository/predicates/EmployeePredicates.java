package com.andreystrinski.employee.manager.employee.repository.predicates;

import com.andreystrinski.employee.manager.employee.model.QEmployee;
import com.querydsl.core.BooleanBuilder;
import org.springframework.stereotype.Component;

@Component
public class EmployeePredicates {

  private final QEmployee qEmployee;

  public EmployeePredicates() {
    qEmployee = QEmployee.employee;
  }

  public BooleanBuilder filterPredicate(Long statusId, Long positionId, Long seniorityId) {
    var builder = new BooleanBuilder();

    if (statusId != null) {
      builder.and(qEmployee.status.id.eq(statusId));
    }
    if (positionId != null) {
      builder.and(qEmployee.position.id.eq(positionId));
    }
    if (seniorityId != null) {
      builder.and(qEmployee.seniority.id.eq(seniorityId));
    }

    return builder;
  }
}
