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
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EmployeeService {

  private final EmployeeRepository employeeRepository;
  private final EmployeePredicates employeePredicates;

  private final EmployeeStatusChangeRepository employeeStatusChangeRepository;

  private final EmployeeStatusRepository employeeStatusRepository;
  private final EmployeeSeniorityRepository employeeSeniorityRepository;
  private final EmployeePositionRepository employeePositionRepository;

  public Page<Employee> findEmployees(Long statusId, Long positionId, Long seniorityId,
      Pageable pageable) {
    return employeeRepository.findAll(
        employeePredicates.filterPredicate(statusId, positionId, seniorityId), pageable);
  }


  @Transactional
  public void updateEmployeeStatus(Long newStatusId, String reason, Long employeeId) {
    var employee = getEmployee(employeeId);

    if (employee.getStatus().getId().equals(newStatusId)) {
      throw new SameStatusException(employee.getStatus().getName());
    }

    var newStatus = employeeStatusRepository.findById(newStatusId).orElseThrow(
        () -> new EntityNotFoundException(
            String.format("Error setting status: Status with id %d doesn't exist!", newStatusId)));

    var employeeStatusChange = new EmployeeStatusChange(reason, employee, newStatus,
        employee.getStatus());
    employeeStatusChangeRepository.save(employeeStatusChange);

    employee.setStatus(newStatus);
    saveEmployee(employee);
  }

  public int calculateRecommendedPromotion(Long id, Short performance) {
    var employee = getEmployee(id);

    return employee.getStatus().getPromotionWeight() + employee.getPosition().getPromotionWeight()
        + employee.getSeniority().getPromotionWeight() + performance;
  }

  public Employee getEmployee(Long employeeId) {
    return employeeRepository.findById(employeeId).orElseThrow(() -> new EntityNotFoundException(
        String.format("Employee with id %d does not exist!", employeeId)));
  }

  @Transactional
  public void updateEmployee(Long employeeId, Employee employee) {
    validateEmployeeExists(employeeId);

    employee.setId(employeeId);

    saveEmployee(employee);
  }

  @Transactional
  public void saveEmployee(Employee employee) {
    employeeRepository.save(employee);
  }

  @Transactional
  public void deleteEmployee(Long employeeId) {
    validateEmployeeExists(employeeId);

    employeeRepository.deleteById(employeeId);
  }

  private void validateEmployeeExists(Long employeeId) {

    if (!employeeRepository.existsById(employeeId)) {
      throw new EntityNotFoundException(
          String.format("Employee with id %d does not exist!", employeeId));
    }
  }

  public List<EmployeeSeniority> getSeniorities() {
    return employeeSeniorityRepository.findAll();
  }

  public List<EmployeeStatus> getStatuses() {
    return employeeStatusRepository.findAll();
  }

  public List<EmployeePosition> getPositions() {
    return employeePositionRepository.findAll();
  }
}
