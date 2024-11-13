package com.andreystrinski.employee.manager.employee.service;

import com.andreystrinski.employee.manager.employee.exception.SameStatusException;
import com.andreystrinski.employee.manager.employee.model.Employee;
import com.andreystrinski.employee.manager.employee.model.EmployeePosition;
import com.andreystrinski.employee.manager.employee.model.EmployeeSeniority;
import com.andreystrinski.employee.manager.employee.model.EmployeeStatus;
import com.andreystrinski.employee.manager.employee.model.EmployeeStatusChange;
import com.andreystrinski.employee.manager.employee.repository.EmployeePositionRepository;
import com.andreystrinski.employee.manager.employee.repository.EmployeeRepository;
import com.andreystrinski.employee.manager.employee.repository.EmployeeSeniorityRepository;
import com.andreystrinski.employee.manager.employee.repository.EmployeeStatusChangeRepository;
import com.andreystrinski.employee.manager.employee.repository.EmployeeStatusRepository;
import com.andreystrinski.employee.manager.employee.repository.predicates.EmployeePredicates;
import com.querydsl.core.BooleanBuilder;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

@SpringJUnitConfig(EmployeeService.class)
public class EmployeeServiceTest {

  @MockBean
  private EmployeeRepository employeeRepository;
  @MockBean
  private EmployeePredicates employeePredicates;

  @MockBean
  private EmployeeStatusChangeRepository employeeStatusChangeRepository;

  @MockBean
  private EmployeeStatusRepository employeeStatusRepository;
  @MockBean
  private EmployeeSeniorityRepository employeeSeniorityRepository;
  @MockBean
  private EmployeePositionRepository employeePositionRepository;

  @Autowired
  private EmployeeService employeeService;

  @Test
  void givenNoFilters_whenGettingEmployees_thenUnfilteredEmployeesAreReturned() {
    var pageable = PageRequest.ofSize(10);
    var predicate = new BooleanBuilder();
    var employee = new Employee();
    var expectedResult = new PageImpl<>(List.of(employee));

    // When
    Mockito.when(employeePredicates.filterPredicate(Mockito.isNull(), Mockito.isNull(),
        Mockito.isNull() /* Given */)).thenReturn(predicate);
    Mockito.when(employeeRepository.findAll(predicate, pageable)).thenReturn(expectedResult);

    // Then
    var actualResult = employeeService.findEmployees(null, null, null, pageable);
    Assertions.assertEquals(expectedResult, actualResult);
  }

  @Test
  void givenFilters_whenGettingEmployees_thenFilteredEmployeesAreReturned() {
    // Given
    var statusId = 1L;
    var positionId = 2L;
    var seniorityId = 3L;

    var pageable = PageRequest.ofSize(10);
    var predicate = new BooleanBuilder();
    var employee = new Employee();
    var expectedResult = new PageImpl<>(List.of(employee));

    // When
    Mockito.when(employeePredicates.filterPredicate(statusId, positionId, seniorityId))
        .thenReturn(predicate);
    Mockito.when(employeeRepository.findAll(predicate, pageable)).thenReturn(expectedResult);

    // Then
    var actualResult = employeeService.findEmployees(statusId, positionId, seniorityId, pageable);
    Assertions.assertEquals(expectedResult, actualResult);
  }

  @Test
  void givenSameEmployeeStatus_whenUpdatingEmployeeStatus_thenAnExceptionIsThrown() {
    // Given
    var newStatusId = 1L;
    var reason = "Some reason";
    var employeeId = 1L;

    var oldStatus = EmployeeStatus.builder().id(newStatusId).build();
    var employee = Employee.builder().status(oldStatus).build();

    // When
    Mockito.when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employee));

    // Then
    Assertions.assertThrows(SameStatusException.class,
        () -> employeeService.updateEmployeeStatus(newStatusId, reason, employeeId));
  }

  @Test
  void givenNonExistentNewStatus_whenUpdatingEmployeeStatus_thenAnExceptionIsThrown() {
    // Given
    var oldStatusId = 1L;
    var newStatusId = 2L;
    var reason = "Some reason";
    var employeeId = 1L;

    var oldStatus = EmployeeStatus.builder().id(oldStatusId).build();
    var employee = Employee.builder().status(oldStatus).build();

    // When
    Mockito.when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employee));
    Mockito.when(employeeStatusRepository.findById(newStatusId)).thenReturn(Optional.empty());

    // Then
    Assertions.assertThrows(EntityNotFoundException.class,
        () -> employeeService.updateEmployeeStatus(newStatusId, reason, employeeId));
  }

  @Test
  void givenCorrectUserAndStatus_whenUpdatingEmployeeStatus_thenEmployeeIsUpdatedAndEmployeeStatusChangeIsCreated() {
    // Given
    var oldStatusId = 1L;
    var newStatusId = 2L;
    var reason = "Some reason";
    var employeeId = 1L;

    var oldStatus = EmployeeStatus.builder().id(oldStatusId).build();
    var employee = Employee.builder().status(oldStatus).build();

    var newStatus = EmployeeStatus.builder().id(newStatusId).build();
    var employeeStatusChange = new EmployeeStatusChange(reason, employee, newStatus, oldStatus);

    // When
    Mockito.when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employee));
    Mockito.when(employeeStatusRepository.findById(newStatusId)).thenReturn(Optional.of(newStatus));

    // Then
    employeeService.updateEmployeeStatus(newStatusId, reason, employeeId);

    Mockito.verify(employeeStatusChangeRepository).save(employeeStatusChange);
    Mockito.verify(employeeRepository).save(employee);
  }

  @Test
  void givenIdAndPerformance_whenCalculatingRecommendedPromotion_thenCalculationsAreCorrect() {
    // Given
    var employeeId = 1L;
    var performance = (short) 5;
    var expectedResult = 8;
    var weight = (short) 1;

    var employee = Employee.builder()
        .status(EmployeeStatus.builder().promotionWeight(weight).build())
        .position(EmployeePosition.builder().promotionWeight(weight).build())
        .seniority(EmployeeSeniority.builder().promotionWeight(weight).build()).build();

    // When
    Mockito.when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employee));

    // Then
    var actualResult = employeeService.calculateRecommendedPromotion(employeeId, performance);
    Assertions.assertEquals(expectedResult, actualResult);
  }

  @Test
  void givenIncorrectEmployeeId_whenGettingAnEmployee_thenAnExceptionIsThrown() {
    // Given
    var employeeId = 1L;

    // When
    Mockito.when(employeeRepository.findById(employeeId)).thenReturn(Optional.empty());

    // Then
    Assertions.assertThrows(EntityNotFoundException.class,
        () -> employeeService.getEmployee(employeeId));
  }

  @Test
  void givenCorrectEmployeeId_whenGettingAnEmployee_thenAnEmployeeIsReturned() {
    // Given
    var employeeId = 1L;
    var expectedResult = new Employee();

    // When
    Mockito.when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(expectedResult));

    // Then
    var actualResult = employeeService.getEmployee(employeeId);
    Assertions.assertEquals(expectedResult, actualResult);
  }

  @Test
  void givenWrongUserId_whenUpdatingAnEmployee_thenAnExceptionIsThrown() {
    // Given
    var employeeId = 1L;
    var employee = new Employee();

    Mockito.when(employeeRepository.existsById(employeeId)).thenReturn(false);

    // Then
    Assertions.assertThrows(EntityNotFoundException.class,
        // When
        () -> employeeService.updateEmployee(employeeId, employee));
  }

  @Test
  void givenCorrectUserId_whenUpdatingAnEmployee_thenUserIsUpdated() {
    // Given
    var employeeId = 1L;
    var employee = new Employee();

    Mockito.when(employeeRepository.existsById(employeeId)).thenReturn(true);

    // When
    employeeService.updateEmployee(employeeId, employee);

    // Then
    Assertions.assertEquals(employeeId, employee.getId());
    Mockito.verify(employeeRepository).save(employee);
  }

  @Test
  void givenEmployee_whenSavingAnEmployee_thenTheEmployeeRepositoryIsCalled() {
    // Given
    var employee = new Employee();

    // When

    employeeService.saveEmployee(employee);

    // Then

    Mockito.verify(employeeRepository).save(employee);
  }

  @Test
  void givenIncorrectEmployeeId_whenDeletingAnEmployee_thenThrowAnError() {
    // Given
    var employeeId = 1L;

    Mockito.when(employeeRepository.existsById(employeeId)).thenReturn(false);

    // Then
    Assertions.assertThrows(EntityNotFoundException.class,
        () -> /* When */ employeeService.deleteEmployee(employeeId));
  }

  @Test
  void givenCorrectEmployeeId_whenDeletingAnEmployee_thenEmployeeIsDeleted() {
    // Given
    var employeeId = 1L;

    Mockito.when(employeeRepository.existsById(employeeId)).thenReturn(true);

    // When

    employeeService.deleteEmployee(employeeId);

    // Then
    Mockito.verify(employeeRepository).deleteById(employeeId);
  }

  @Test
  void whenGettingSeniorities_thenFetchDepartments() {

    // When
    employeeService.getSeniorities();

    // Then
    Mockito.verify(employeeSeniorityRepository).findAll();
  }

  @Test
  void whenGettingStatuses_thenFetchDepartments() {

    // When
    employeeService.getStatuses();

    // Then
    Mockito.verify(employeeStatusRepository).findAll();
  }

  @Test
  void whenFindingPositions_thenFetchPositions() {

    // When
    employeeService.getPositions();

    // Then
    Mockito.verify(employeePositionRepository).findAll();
  }
}
