package com.andreystrinski.employee.manager.employee.controller;

import com.andreystrinski.employee.manager.employee.model.Employee;
import com.andreystrinski.employee.manager.employee.model.dto.CompactEmployeeDto;
import com.andreystrinski.employee.manager.employee.model.dto.EmployeeDetailsDto;
import com.andreystrinski.employee.manager.employee.model.dto.EmployeePositionDto;
import com.andreystrinski.employee.manager.employee.model.dto.EmployeeSeniorityDto;
import com.andreystrinski.employee.manager.employee.model.dto.EmployeeStatusChangeDto;
import com.andreystrinski.employee.manager.employee.model.dto.EmployeeStatusDto;
import com.andreystrinski.employee.manager.employee.model.dto.SaveEmployeeDto;
import com.andreystrinski.employee.manager.employee.service.EmployeeService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/employees")
@RequiredArgsConstructor
public class EmployeeController {

  private final ModelMapper modelMapper;

  private final EmployeeService employeeService;

  @PostMapping()
  public ResponseEntity<Void> createEmployee(@RequestBody @Valid SaveEmployeeDto employeeDto) {
    var employee = modelMapper.map(employeeDto, Employee.class);

    employeeService.saveEmployee(employee);

    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  @GetMapping
  public ResponseEntity<Page<CompactEmployeeDto>> findEmployees(
      @RequestParam(required = false) Long statusId,
      @RequestParam(required = false) Long positionId,
      @RequestParam(required = false) Long seniorityId,
      @PageableDefault(sort = "id", direction = Sort.Direction.ASC, value = 5) Pageable pageable) {
    var employees = employeeService.findEmployees(statusId, positionId, seniorityId, pageable);
    var response = employees.map(employee -> modelMapper.map(employee, CompactEmployeeDto.class));

    return ResponseEntity.ok(response);
  }

  @GetMapping("/{id}")
  public ResponseEntity<EmployeeDetailsDto> getEmployee(@PathVariable Long id) {
    var employee = employeeService.getEmployee(id);
    var response = modelMapper.map(employee, EmployeeDetailsDto.class);

    return ResponseEntity.ok(response);
  }

  @PutMapping("/{id}")
  public ResponseEntity<Void> updateEmployee(@PathVariable Long id,
      @RequestBody @Valid SaveEmployeeDto employeeDto) {
    var employee = modelMapper.map(employeeDto, Employee.class);

    employeeService.updateEmployee(id, employee);

    return ResponseEntity.noContent().build();
  }

  @PatchMapping("/{id}/status")
  public ResponseEntity<Void> updateEmployeeStatus(@PathVariable Long id,
      @RequestBody @Valid EmployeeStatusChangeDto statusChangeDto) {
    employeeService.updateEmployeeStatus(statusChangeDto.getNewStatusId(),
        statusChangeDto.getReason(), id);

    return ResponseEntity.noContent().build();
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
    employeeService.deleteEmployee(id);

    return ResponseEntity.noContent().build();
  }

  @GetMapping("/{id}/promotion")
  public ResponseEntity<Integer> calculateRecommendedPromotion(@PathVariable Long id,
      @RequestParam @Min(value = 1, message = "Performance needs to be between 1 and 5!") @Max(value = 5, message = "Performance needs to be between 1 and 5!") Short performance) {
    var recommendedPromotion = employeeService.calculateRecommendedPromotion(id, performance);

    return ResponseEntity.ok(recommendedPromotion);
  }

  @GetMapping("/seniorities")
  public ResponseEntity<List<EmployeeSeniorityDto>> getEmployeeSeniorities() {
    var seniorities = employeeService.getSeniorities();
    var response = seniorities.stream()
        .map(seniority -> modelMapper.map(seniority, EmployeeSeniorityDto.class)).toList();

    return ResponseEntity.ok(response);
  }

  @GetMapping("/statuses")
  public ResponseEntity<List<EmployeeStatusDto>> getEmployeeStatuses() {
    var statuses = employeeService.getStatuses();
    var response = statuses.stream()
        .map(status -> modelMapper.map(status, EmployeeStatusDto.class)).toList();

    return ResponseEntity.ok(response);
  }

  @GetMapping("/positions")
  public ResponseEntity<List<EmployeePositionDto>> getEmployeePositions() {
    var positions = employeeService.getPositions();
    var response = positions.stream()
        .map(position -> modelMapper.map(position, EmployeePositionDto.class)).toList();

    return ResponseEntity.ok(response);
  }
}
