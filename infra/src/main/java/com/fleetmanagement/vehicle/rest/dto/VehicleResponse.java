package com.fleetmanagement.vehicle.rest.dto;

import com.fleetmanagement.vehicle.model.Vehicle;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class VehicleResponse {
  private String id;
  private String licensePlate;

  public static VehicleResponse fromModel(Vehicle vehicle) {
    return new VehicleResponse(vehicle.getId(), vehicle.getLicensePlate());
  }
}
