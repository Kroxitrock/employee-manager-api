package com.andreystrinski.employee.manager.employee.model.dto.mappings;

import com.andreystrinski.employee.manager.employee.model.Employee;
import com.andreystrinski.employee.manager.employee.model.dto.CompactEmployeeDto;
import org.modelmapper.PropertyMap;

public class CompactEmployeeDtoMappings extends PropertyMap<Employee, CompactEmployeeDto> {

  @Override
  protected void configure() {
    using(ctx -> String.format("%s %s", ((Employee) ctx.getSource()).getFirstName(),
        ((Employee) ctx.getSource()).getLastName())).map(source, destination.getFullName());
  }
}
