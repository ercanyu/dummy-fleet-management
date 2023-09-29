package com.fleetmanagement.vehicle.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Vehicle {
  private String id;
  private String licensePlate;
}
