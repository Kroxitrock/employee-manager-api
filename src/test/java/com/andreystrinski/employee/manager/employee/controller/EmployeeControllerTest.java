package com.andreystrinski.employee.manager.employee.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.andreystrinski.employee.manager.employee.model.Employee;
import com.andreystrinski.employee.manager.employee.model.EmployeePosition;
import com.andreystrinski.employee.manager.employee.model.EmployeeSeniority;
import com.andreystrinski.employee.manager.employee.model.EmployeeStatus;
import com.andreystrinski.employee.manager.employee.model.dto.CompactEmployeeDto;
import com.andreystrinski.employee.manager.employee.model.dto.EmployeeDetailsDto;
import com.andreystrinski.employee.manager.employee.model.dto.EmployeePositionDto;
import com.andreystrinski.employee.manager.employee.model.dto.EmployeeSeniorityDto;
import com.andreystrinski.employee.manager.employee.model.dto.EmployeeStatusChangeDto;
import com.andreystrinski.employee.manager.employee.model.dto.EmployeeStatusDto;
import com.andreystrinski.employee.manager.employee.model.dto.SaveEmployeeDto;
import com.andreystrinski.employee.manager.employee.service.EmployeeService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(EmployeeController.class)
public class EmployeeControllerTest {

  private final ObjectMapper objectMapper;

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private EmployeeService employeeService;

  @MockBean
  private ModelMapper modelMapper;

  public EmployeeControllerTest() {
    objectMapper = new ObjectMapper();
  }

  @Test
  public void givenAnInvalidFirstName_whenCreatingAnEmployee_thenReturnErrorCode()
      throws Exception {
    var employeeDto = SaveEmployeeDto.builder().firstName("") // Given
        .lastName("Strinski").email("andrey.strinski@email.com").statusId(1L).positionId(1L)
        .seniorityId(1L).build();

    // When
    mockMvc.perform(post("/employees").contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(employeeDto)))
        // Then
        .andExpect(status().is(HttpStatus.BAD_REQUEST.value()));
  }

  @Test
  public void givenAnInvalidLastName_whenCreatingAnEmployee_thenReturnErrorCode() throws Exception {
    var employeeDto = SaveEmployeeDto.builder().firstName("Andrey").lastName("") // Given
        .email("andrey.strinski@email.com").statusId(1L).positionId(1L).seniorityId(1L).build();

    // When
    mockMvc.perform(post("/employees").contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(employeeDto)))
        // Then
        .andExpect(status().is(HttpStatus.BAD_REQUEST.value()));
  }

  @Test
  public void givenAnInvalidEmail_whenCreatingAnEmployee_thenReturnErrorCode() throws Exception {
    var employeeDto = SaveEmployeeDto.builder().firstName("Andrey").lastName("Strinski")
        .email("andrey.strinski[at]email.com") // Given
        .statusId(1L).positionId(1L).seniorityId(1L).build();

    // When
    mockMvc.perform(post("/employees").contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(employeeDto)))
        // Then
        .andExpect(status().is(HttpStatus.BAD_REQUEST.value()));
  }

  @Test
  public void givenAnInvalidStatusId_whenCreatingAnEmployee_thenReturnErrorCode() throws Exception {
    var employeeDto = SaveEmployeeDto.builder().firstName("Andrey").lastName("Strinski")
        .email("andrey.strinski@email.com").statusId(null) // Given
        .positionId(1L).seniorityId(1L).build();

    // When
    mockMvc.perform(post("/employees").contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(employeeDto)))
        // Then
        .andExpect(status().is(HttpStatus.BAD_REQUEST.value()));
  }

  @Test
  public void givenAnInvalidPositionId_whenCreatingAnEmployee_thenReturnErrorCode()
      throws Exception {
    var employeeDto = SaveEmployeeDto.builder().firstName("Andrey").lastName("Strinski")
        .email("andrey.strinski@email.com").statusId(1L).positionId(null) // Given
        .seniorityId(1L).build();

    // When
    mockMvc.perform(post("/employees").contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(employeeDto)))
        // Then
        .andExpect(status().is(HttpStatus.BAD_REQUEST.value()));
  }

  @Test
  public void givenAnInvalidSeniorityId_whenCreatingAnEmployee_thenReturnErrorCode()
      throws Exception {
    var employeeDto = SaveEmployeeDto.builder().firstName("Andrey").lastName("Strinski")
        .email("andrey.strinski@email.com").statusId(1L).positionId(1L).seniorityId(null) // Given
        .build();

    // When
    mockMvc.perform(post("/employees").contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(employeeDto)))
        // Then
        .andExpect(status().is(HttpStatus.BAD_REQUEST.value()));
  }

  @Test
  public void givenAValidDto_whenCreatingAnEmployee_thenTheRequestIsProcessed() throws Exception {
    // Given
    var employeeDto = SaveEmployeeDto.builder().firstName("Andrey").lastName("Strinski")
        .email("andrey.strinski@email.com").statusId(1L).positionId(1L).seniorityId(1L).build();

    var employee = Employee.builder().build();

    Mockito.when(modelMapper.map(employeeDto, Employee.class)).thenReturn(employee);

    // When
    mockMvc.perform(post("/employees").contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(employeeDto)))
        // Then
        .andExpect(status().is(HttpStatus.CREATED.value()));

    Mockito.verify(employeeService).saveEmployee(employee);
  }

  @Test
  public void givenNoFilterParameters_whenFindingEmployees_thenTheRequestIsProcessed()
      throws Exception {
    // Given
    var defaultSort = Sort.by(Sort.Direction.ASC, "id");
    var pageRequest = PageRequest.of(0, 5, defaultSort);

    var employee = Employee.builder().firstName("Andrey").lastName("Strinski").build();
    var employeePage = new PageImpl<>(List.of(employee), pageRequest, 1);
    var fullName = String.format("%s %s", employee.getFirstName(), employee.getLastName());
    var employeeDto = CompactEmployeeDto.builder().fullName(fullName).build();

    Mockito.when(employeeService.findEmployees(Mockito.isNull(), Mockito.isNull(), Mockito.isNull(),
        Mockito.eq(pageRequest))).thenReturn(employeePage);
    Mockito.when(modelMapper.map(employee, CompactEmployeeDto.class)).thenReturn(employeeDto);

    // When
    mockMvc.perform(get("/employees"))
        // Then
        .andExpect(status().isOk()).andExpect(jsonPath("$.content[0].fullName").value(fullName));

  }

  @Test
  public void givenFilterParameters_whenFindingEmployees_thenTheRequestIsProcessed()
      throws Exception {
    // Given
    var customSort = Sort.by(Sort.Direction.DESC, "email");
    var pageRequest = PageRequest.of(1, 4, customSort);
    var statusId = 1L;
    var positionId = 2L;
    var seniorityId = 3L;

    var employee = Employee.builder().firstName("Andrey").lastName("Strinski").build();
    var employeePage = new PageImpl<>(List.of(employee), pageRequest, 1);
    var fullName = String.format("%s %s", employee.getFirstName(), employee.getLastName());
    var employeeDto = CompactEmployeeDto.builder().fullName(fullName).build();

    Mockito.when(employeeService.findEmployees(statusId, positionId, seniorityId, pageRequest))
        .thenReturn(employeePage);
    Mockito.when(modelMapper.map(employee, CompactEmployeeDto.class)).thenReturn(employeeDto);

    // When
    mockMvc.perform(get("/employees").param("statusId", String.valueOf(statusId))
            .param("positionId", String.valueOf(positionId))
            .param("seniorityId", String.valueOf(seniorityId)).param("sort", "email,desc")
            .param("size", "4").param("page", "1"))
        // Then
        .andExpect(status().isOk()).andExpect(jsonPath("$.content[0].fullName").value(fullName));
  }

  @Test
  public void givenEntityId_whenGettingAnEmployee_thenTheRequestIsProcessed() throws Exception {
    // Given
    var employeeId = 1L;

    var employee = Employee.builder().id(employeeId).build();
    var employeeDto = EmployeeDetailsDto.builder().id(employeeId).build();

    Mockito.when(employeeService.getEmployee(employeeId)).thenReturn(employee);
    Mockito.when(modelMapper.map(employee, EmployeeDetailsDto.class)).thenReturn(employeeDto);

    // When
    mockMvc.perform(get(String.format("/employees/%d", employeeId)))
        // Then
        .andExpect(status().isOk()).andExpect(jsonPath("$.id").value(employeeId));
  }

  @Test
  public void givenAnInvalidFirstName_whenUpdatingAnEmployee_thenReturnErrorCode()
      throws Exception {
    var employeeId = 1L;

    var employeeDto = SaveEmployeeDto.builder().firstName("") // Given
        .lastName("Strinski").email("andrey.strinski@email.com").statusId(1L).positionId(1L)
        .seniorityId(1L).build();

    // When
    mockMvc.perform(
            put(String.format("/employees/%d", employeeId)).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employeeDto)))
        // Then
        .andExpect(status().is(HttpStatus.BAD_REQUEST.value()));
  }

  @Test
  public void givenAnInvalidLastName_whenUpdatingAnEmployee_thenReturnErrorCode() throws Exception {
    var employeeId = 1L;

    var employeeDto = SaveEmployeeDto.builder().firstName("Andrey").lastName("") // Given
        .email("andrey.strinski@email.com").statusId(1L).positionId(1L).seniorityId(1L).build();

    // When
    mockMvc.perform(
            put(String.format("/employees/%d", employeeId)).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employeeDto)))
        // Then
        .andExpect(status().is(HttpStatus.BAD_REQUEST.value()));
  }

  @Test
  public void givenAnInvalidEmail_whenUpdatingAnEmployee_thenReturnErrorCode() throws Exception {
    var employeeId = 1L;

    var employeeDto = SaveEmployeeDto.builder().firstName("Andrey").lastName("Strinski")
        .email("andrey.strinski[at]email.com") // Given
        .statusId(1L).positionId(1L).seniorityId(1L).build();

    // When
    mockMvc.perform(
            put(String.format("/employees/%d", employeeId)).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employeeDto)))
        // Then
        .andExpect(status().is(HttpStatus.BAD_REQUEST.value()));
  }

  @Test
  public void givenAnInvalidStatusId_whenUpdatingAnEmployee_thenReturnErrorCode() throws Exception {
    var employeeId = 1L;

    var employeeDto = SaveEmployeeDto.builder().firstName("Andrey").lastName("Strinski")
        .email("andrey.strinski@email.com").statusId(null) // Given
        .positionId(1L).seniorityId(1L).build();

    // When
    mockMvc.perform(
            put(String.format("/employees/%d", employeeId)).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employeeDto)))
        // Then
        .andExpect(status().is(HttpStatus.BAD_REQUEST.value()));
  }

  @Test
  public void givenAnInvalidPositionId_whenUpdatingAnEmployee_thenReturnErrorCode()
      throws Exception {
    var employeeId = 1L;

    var employeeDto = SaveEmployeeDto.builder().firstName("Andrey").lastName("Strinski")
        .email("andrey.strinski@email.com").statusId(1L).positionId(null) // Given
        .seniorityId(1L).build();

    // When
    mockMvc.perform(
            put(String.format("/employees/%d", employeeId)).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employeeDto)))
        // Then
        .andExpect(status().is(HttpStatus.BAD_REQUEST.value()));
  }

  @Test
  public void givenAnInvalidSeniorityId_whenUpdatingAnEmployee_thenReturnErrorCode()
      throws Exception {
    var employeeId = 1L;

    var employeeDto = SaveEmployeeDto.builder().firstName("Andrey").lastName("Strinski")
        .email("andrey.strinski@email.com").statusId(1L).positionId(1L).seniorityId(null) // Given
        .build();

    // When
    mockMvc.perform(
            put(String.format("/employees/%d", employeeId)).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employeeDto)))
        // Then
        .andExpect(status().is(HttpStatus.BAD_REQUEST.value()));
  }

  @Test
  public void givenAValidDto_whenUpdatingAnEmployee_thenTheRequestIsProcessed() throws Exception {
    // Given
    var employeeId = 1L;

    var employeeDto = SaveEmployeeDto.builder().firstName("Andrey").lastName("Strinski")
        .email("andrey.strinski@email.com").statusId(1L).positionId(1L).seniorityId(1L).build();

    var employee = Employee.builder().build();

    Mockito.when(modelMapper.map(employeeDto, Employee.class)).thenReturn(employee);

    // When
    mockMvc.perform(
            put(String.format("/employees/%d", employeeId)).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employeeDto)))
        // Then
        .andExpect(status().is(HttpStatus.NO_CONTENT.value()));

    Mockito.verify(employeeService).updateEmployee(employeeId, employee);
  }

  @Test
  public void givenAnInvalidStatus_whenUpdatingEmployeeStatus_thenReturnErrorCode()
      throws Exception {
    // Given
    var employeeId = 1L;

    var employeeDto = EmployeeStatusChangeDto.builder().build();

    // When
    mockMvc.perform(patch(String.format("/employees/%d/status", employeeId)).contentType(
            MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(employeeDto)))
        .andExpect(status().is(HttpStatus.BAD_REQUEST.value())); // Then
  }

  @Test
  public void givenAValidStatus_whenUpdatingEmployeeStatus_thenRequestIsProcessed()
      throws Exception {
    // Given
    var employeeId = 1L;
    var newStatusId = 1L;

    var employeeDto = EmployeeStatusChangeDto.builder().newStatusId(newStatusId).build();

    // When
    mockMvc.perform(patch(String.format("/employees/%d/status", employeeId)).contentType(
            MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(employeeDto)))
        // Then
        .andExpect(status().is(HttpStatus.NO_CONTENT.value()));

    Mockito.verify(employeeService)
        .updateEmployeeStatus(employeeDto.getNewStatusId(), employeeDto.getReason(), employeeId);
  }

  @Test
  public void givenAValidId_whenDeletingAnEmployee_thenRequestIsProcessed() throws Exception {
    // Given
    var employeeId = 1L;

    // When
    mockMvc.perform(delete(String.format("/employees/%d", employeeId)))
        // Then
        .andExpect(status().is(HttpStatus.NO_CONTENT.value()));

    Mockito.verify(employeeService).deleteEmployee(employeeId);
  }

  @Test
  public void givenNoPerformance_whenCalculatingRecommendedPromotion_thenReturnErrorCode()
      throws Exception {
    // Given
    var employeeId = 1L;

    // When
    mockMvc.perform(get(String.format("/employees/%d/promotion", employeeId)))
        // Then
        .andExpect(status().is(HttpStatus.BAD_REQUEST.value()));

  }

  @Test
  public void givenPerformanceTooLow_whenCalculatingRecommendedPromotion_thenReturnErrorCode()
      throws Exception {
    // Given
    var employeeId = 1L;

    // When
    mockMvc.perform(
            get(String.format("/employees/%d/promotion", employeeId)).param("performance", "0"))
        // Then
        .andExpect(status().is(HttpStatus.BAD_REQUEST.value()));

  }

  @Test
  public void givenPerformanceTooHigh_whenCalculatingRecommendedPromotion_thenReturnErrorCode()
      throws Exception {
    // Given
    var employeeId = 1L;

    // When
    mockMvc.perform(
            get(String.format("/employees/%d/promotion", employeeId)).param("performance", "6"))
        // Then
        .andExpect(status().is(HttpStatus.BAD_REQUEST.value()));

  }

  @Test
  public void givenAValidPerformance_whenCalculatingRecommendedPromotion_thenProcessRequest()
      throws Exception {
    // Given
    var employeeId = 1L;
    var performance = (short) 5;
    var expectedResult = 8;

    Mockito.when(employeeService.calculateRecommendedPromotion(employeeId, performance))
        .thenReturn(expectedResult);

    // When
    mockMvc.perform(get(String.format("/employees/%d/promotion", employeeId)).param("performance",
            String.valueOf(performance)))
        // Then
        .andExpect(status().isOk()).andExpect(jsonPath("$").value(String.valueOf(expectedResult)));

  }

  @Test
  public void whenFindingSeniorities_thenSeniorityListIsReturned() throws Exception {
    var seniority = EmployeeSeniority.builder().id(1L).build();
    var seniorities = List.of(seniority);
    var seniorityDto = EmployeeSeniorityDto.builder().id(seniority.getId()).build();

    Mockito.when(employeeService.getSeniorities()).thenReturn(seniorities);
    Mockito.when(modelMapper.map(seniority, EmployeeSeniorityDto.class)).thenReturn(seniorityDto);

    // When
    mockMvc.perform(get("/employees/seniorities"))
        // Then
        .andExpect(status().isOk()).andExpect(jsonPath("$.[0].id").value(seniorityDto.getId()));
  }

  @Test
  public void whenFindingStatuses_thenStatusListIsReturned() throws Exception {
    var status = EmployeeStatus.builder().id(1L).build();
    var statuses = List.of(status);
    var statusDto = EmployeeStatusDto.builder().id(status.getId()).build();

    Mockito.when(employeeService.getStatuses()).thenReturn(statuses);
    Mockito.when(modelMapper.map(status, EmployeeStatusDto.class)).thenReturn(statusDto);

    // When
    mockMvc.perform(get("/employees/statuses"))
        // Then
        .andExpect(status().isOk()).andExpect(jsonPath("$.[0].id").value(statusDto.getId()));
  }

  @Test
  public void whenFindingPositions_thenSeniorityListIsReturned() throws Exception {
    var position = EmployeePosition.builder().id(1L).build();
    var positions = List.of(position);
    var positionDto = EmployeePositionDto.builder().id(position.getId()).build();

    Mockito.when(employeeService.getPositions()).thenReturn(positions);
    Mockito.when(modelMapper.map(position, EmployeePositionDto.class)).thenReturn(positionDto);

    // When
    mockMvc.perform(get("/employees/positions"))
        // Then
        .andExpect(status().isOk()).andExpect(jsonPath("$.[0].id").value(positionDto.getId()));
  }
}
