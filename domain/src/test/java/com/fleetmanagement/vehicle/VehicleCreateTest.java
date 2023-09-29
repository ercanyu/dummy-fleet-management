package com.fleetmanagement.vehicle;

import com.fleetmanagement.adapters.VehicleFakeDataAdapter;
import com.fleetmanagement.common.exception.FleetManagementApiException;
import com.fleetmanagement.vehicle.model.Vehicle;
import com.fleetmanagement.vehicle.usecase.command.VehicleCreate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class VehicleCreateTest {
  VehicleCreateUseCaseHandler vehicleCreateUseCaseHandler;

  @BeforeEach
  void setUp() {
    vehicleCreateUseCaseHandler = new VehicleCreateUseCaseHandler(new VehicleFakeDataAdapter());
  }

  @Test
  void should_create_vehicle() {
    // given
    VehicleCreate vehicleCreate = new VehicleCreate("34 EY 34");

    // when
    Vehicle vehicle = vehicleCreateUseCaseHandler.handle(vehicleCreate);

    // then
    assertThat(vehicle)
        .isNotNull()
        .returns("1", Vehicle::getId)
        .returns("34 EY 34", Vehicle::getLicensePlate);
  }

  @Test
  void should_throw_exception_when_license_plate_exists() {
    // given
    VehicleCreate vehicleCreate = new VehicleCreate("EXISTING_LICENSE_PLATE");

    // when
    assertThatExceptionOfType(FleetManagementApiException.class)
        .isThrownBy(() -> vehicleCreateUseCaseHandler.handle(vehicleCreate))
        .withMessage("Vehicle already exists.");
  }
}
