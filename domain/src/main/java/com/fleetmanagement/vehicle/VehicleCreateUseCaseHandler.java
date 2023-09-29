package com.fleetmanagement.vehicle;

import com.fleetmanagement.common.DomainComponent;
import com.fleetmanagement.common.exception.ExceptionMessage;
import com.fleetmanagement.common.exception.FleetManagementApiException;
import com.fleetmanagement.common.usecase.UseCaseHandler;
import com.fleetmanagement.vehicle.model.Vehicle;
import com.fleetmanagement.vehicle.port.VehicleDataPort;
import com.fleetmanagement.vehicle.usecase.command.VehicleCreate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@DomainComponent
public class VehicleCreateUseCaseHandler implements UseCaseHandler<Vehicle, VehicleCreate> {
  private final VehicleDataPort vehicleDataPort;

  @Override
  public Vehicle handle(VehicleCreate useCase) {
    if (vehicleDataPort.retrieveByLicensePlate(useCase.licensePlate()).isPresent()) {
      log.warn("License Plate {} already exists!", useCase.licensePlate());
      throw new FleetManagementApiException(ExceptionMessage.VEHICLE_ALREADY_EXISTS);
    }

    return vehicleDataPort.create(useCase);
  }
}
