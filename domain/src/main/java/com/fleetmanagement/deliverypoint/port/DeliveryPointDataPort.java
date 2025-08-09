package com.fleetmanagement.deliverypoint.port;

import com.fleetmanagement.deliverypoint.model.DeliveryPoint;
import com.fleetmanagement.deliverypoint.usecase.command.DeliveryPointCreate;

import java.util.Optional;

public interface DeliveryPointDataPort {
    DeliveryPoint create(DeliveryPointCreate deliveryPointCreate);

    Optional<DeliveryPoint> retrieveByValue(Integer value);
}
