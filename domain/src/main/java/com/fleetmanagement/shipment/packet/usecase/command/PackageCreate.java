package com.fleetmanagement.shipment.packet.usecase.command;

import com.fleetmanagement.common.model.UseCase;

public record PackageCreate(String barcode, Integer deliveryPoint, Integer volumetricWeight) implements UseCase {
}
