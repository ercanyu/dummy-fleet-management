package com.fleetmanagement.adapters;

import com.fleetmanagement.deliverypoint.model.DeliveryPoint;
import com.fleetmanagement.deliverypoint.model.DeliveryPointType;
import com.fleetmanagement.deliverypoint.port.DeliveryPointDataPort;
import com.fleetmanagement.deliverypoint.usecase.command.DeliveryPointCreate;

import java.util.Optional;

public class DeliveryPointFakeDataAdapter implements DeliveryPointDataPort {
    @Override
    public DeliveryPoint create(DeliveryPointCreate deliveryPointCreate) {
        return DeliveryPoint.builder()
                .id("1")
                .name(deliveryPointCreate.name())
                .value(deliveryPointCreate.value())
                .build();
    }

    @Override
    public Optional<DeliveryPoint> retrieveByValue(Integer value) {
        if (value.equals(DeliveryPointType.DISTRIBUTION_CENTER.getValue())) {
            return Optional.of(
                    DeliveryPoint.builder()
                            .id("1")
                            .name("DeliveryPoint")
                            .value(DeliveryPointType.DISTRIBUTION_CENTER.getValue())
                            .build());
        }

        return Optional.empty();
    }
}
