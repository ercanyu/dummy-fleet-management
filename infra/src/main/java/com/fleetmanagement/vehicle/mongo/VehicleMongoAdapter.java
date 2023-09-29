package com.fleetmanagement.vehicle.mongo;

import com.fleetmanagement.vehicle.mongo.document.VehicleDocument;
import com.fleetmanagement.vehicle.mongo.repository.VehicleMongoRepository;
import com.fleetmanagement.vehicle.model.Vehicle;
import com.fleetmanagement.vehicle.port.VehicleDataPort;
import com.fleetmanagement.vehicle.usecase.command.VehicleCreate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VehicleMongoAdapter implements VehicleDataPort {

  private final VehicleMongoRepository vehicleMongoRepository;

  @Override
  public Vehicle create(VehicleCreate vehicleCreate) {
    VehicleDocument vehicleDocument =
        VehicleDocument.builder().licensePlate(vehicleCreate.licensePlate()).build();

    return vehicleMongoRepository.save(vehicleDocument).toModel();
  }

  @Override
  public Optional<Vehicle> retrieveByLicensePlate(String licensePlate) {
    return vehicleMongoRepository.findByLicensePlate(licensePlate).map(VehicleDocument::toModel);
  }
}
