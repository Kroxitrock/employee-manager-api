package com.andreystrinski.employee.manager.config;

import com.andreystrinski.employee.manager.employee.model.dto.mappings.CompactEmployeeDtoMappings;
import com.andreystrinski.employee.manager.employee.model.dto.mappings.SaveEmployeeDtoMappings;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

  @Bean
  public ModelMapper modelMapper() {
    var modelMapper = new ModelMapper();

    modelMapper.addMappings(new SaveEmployeeDtoMappings());
    modelMapper.addMappings(new CompactEmployeeDtoMappings());

    return modelMapper;
  }

}
