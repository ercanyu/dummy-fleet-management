package com.fleetmanagement.vehicle.port;

import com.fleetmanagement.vehicle.model.Vehicle;
import com.fleetmanagement.vehicle.usecase.command.VehicleCreate;

import java.util.Optional;

public interface VehicleDataPort {
    Vehicle create(VehicleCreate vehicleCreate);

    Optional<Vehicle> retrieveByLicensePlate(String licensePlate);
}
