package com.andreystrinski.employee.manager.config;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.andreystrinski.employee.manager.employee.controller.EmployeeController;
import com.andreystrinski.employee.manager.employee.exception.SameStatusException;
import com.andreystrinski.employee.manager.employee.service.EmployeeService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

public class GlobalExceptionHandlerTest {

  private final MockMvc mockMvc;

  private final EmployeeService employeeService;

  public GlobalExceptionHandlerTest() {
    employeeService = Mockito.mock(EmployeeService.class);
    ModelMapper modelMapper = Mockito.mock(ModelMapper.class);

    var employeeController = new EmployeeController(modelMapper, employeeService);

    mockMvc = MockMvcBuilders.standaloneSetup(employeeController)
        .setControllerAdvice(new GlobalExceptionHandler())
        .build();
  }

  @Test
  public void whenNoEntityFoundException_thenHttpStatusNotFound() throws Exception {
    var exceptionMessage = "Message";

    Mockito.when(employeeService.getPositions())
        .thenThrow(new EntityNotFoundException(exceptionMessage));

    // When
    mockMvc.perform(get("/employees/positions"))
        // Then
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$").value(exceptionMessage));
  }

  @Test
  public void whenSameStatusException_thenHttpStatusBadRequest() throws Exception {
    var exceptionMessage = "Message";

    Mockito.when(employeeService.getPositions())
        .thenThrow(new SameStatusException(exceptionMessage));

    // When
    mockMvc.perform(get("/employees/positions"))
        // Then
        .andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
        .andExpect(jsonPath("$").value(
            String.format("Status change error: the employee was already in that status %s!",
                exceptionMessage)));
  }

  @Test
  public void whenMethodArgumentNotValidException_thenSameHttpStatusCode() throws Exception {
    // When
    mockMvc.perform(post("/employees"))
        // Then
        .andExpect(status().is(HttpStatus.BAD_REQUEST.value()));
  }

}
