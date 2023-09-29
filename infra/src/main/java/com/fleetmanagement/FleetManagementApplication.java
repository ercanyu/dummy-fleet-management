package com.fleetmanagement;

import com.fleetmanagement.common.DomainComponent;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

@SpringBootApplication
@ComponentScan(
    includeFilters = {
      @ComponentScan.Filter(
          type = FilterType.ANNOTATION,
          value = {DomainComponent.class})
    })
public class FleetManagementApplication {
  public static void main(String[] args) {
    SpringApplication.run(FleetManagementApplication.class, args);
  }
}
