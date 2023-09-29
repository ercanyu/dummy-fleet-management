package com.fleetmanagement.shipment.bag.usecase.command;

import com.fleetmanagement.common.model.UseCase;

public record BagCreate(String barcode, Integer deliveryPoint) implements UseCase {
}
