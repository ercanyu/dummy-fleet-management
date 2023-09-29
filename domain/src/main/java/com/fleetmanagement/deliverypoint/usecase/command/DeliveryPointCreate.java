package com.fleetmanagement.deliverypoint.usecase.command;

import com.fleetmanagement.common.model.UseCase;

public record DeliveryPointCreate(String name, Integer value) implements UseCase {
}
