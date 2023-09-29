package com.fleetmanagement.vehicle.rest.dto;

import com.fleetmanagement.vehicle.usecase.command.VehicleCreate;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class VehicleCreateRequest {
  @NotBlank
  @Size(min = 5, max = 11)
  private String licensePlate;

  public VehicleCreate toUseCase() {
    return new VehicleCreate(licensePlate);
  }
}
