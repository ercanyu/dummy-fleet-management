package com.fleetmanagement.vehicle.usecase.command;

import com.fleetmanagement.common.model.UseCase;

public record VehicleCreate(String licensePlate) implements UseCase {
}
