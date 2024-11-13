package com.andreystrinski.employee.manager.employee.model.dto.mappings;

import com.andreystrinski.employee.manager.employee.model.Employee;
import com.andreystrinski.employee.manager.employee.model.dto.SaveEmployeeDto;
import org.modelmapper.PropertyMap;

public class SaveEmployeeDtoMappings extends PropertyMap<SaveEmployeeDto, Employee> {

  @Override
  protected void configure() {
    skip(destination.getId());
  }
}
