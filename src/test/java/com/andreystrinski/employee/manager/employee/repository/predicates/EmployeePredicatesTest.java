package com.andreystrinski.employee.manager.employee.repository.predicates;

import com.andreystrinski.employee.manager.employee.model.QEmployee;
import com.querydsl.core.BooleanBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

@SpringJUnitConfig(EmployeePredicates.class)
public class EmployeePredicatesTest {

  private final QEmployee qEmployee;

  @Autowired
  private EmployeePredicates employeePredicates;

  public EmployeePredicatesTest() {
    qEmployee = QEmployee.employee;
  }

  @Test
  void givenNoFilters_whenBuildingAFilterPredicate_thenThePredicateIsEmpty() {
    var expectedResult = new BooleanBuilder();

    // When
    var actualResult = employeePredicates.filterPredicate( /* Given */ null, null, null);

    // Then
    Assertions.assertEquals(expectedResult.hashCode(), actualResult.hashCode());
  }

  @Test
  void givenAllFilters_whenBuildingAFilterPredicate_thenThePredicateHasAllFilter() {
    // Given
    var statusId = 1L;
    var positionId = 2L;
    var seniorityId = 3L;

    var expectedResult = new BooleanBuilder();
    expectedResult.and(qEmployee.status.id.eq(statusId));
    expectedResult.and(qEmployee.position.id.eq(positionId));
    expectedResult.and(qEmployee.seniority.id.eq(seniorityId));

    // When
    var actualResult = employeePredicates.filterPredicate(statusId, positionId, seniorityId);

    // Then
    Assertions.assertEquals(expectedResult.hashCode(), actualResult.hashCode());
  }
}
