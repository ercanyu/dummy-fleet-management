package com.fleetmanagement.adapters;

import com.fleetmanagement.vehicle.model.Vehicle;
import com.fleetmanagement.vehicle.port.VehicleDataPort;
import com.fleetmanagement.vehicle.usecase.command.VehicleCreate;

import java.util.Optional;

public class VehicleFakeDataAdapter implements VehicleDataPort {
    @Override
    public Vehicle create(VehicleCreate vehicleCreate) {
        return Vehicle.builder().id("1").licensePlate(vehicleCreate.licensePlate()).build();
    }

    @Override
    public Optional<Vehicle> retrieveByLicensePlate(String licensePlate) {
        if ("EXISTING_LICENSE_PLATE".equals(licensePlate)) {
            return Optional.of(Vehicle.builder().id("1").licensePlate("EXISTING_LICENSE_PLATE").build());
        }

        return Optional.empty();
    }
}
