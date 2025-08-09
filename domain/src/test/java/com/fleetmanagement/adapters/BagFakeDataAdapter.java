package com.fleetmanagement.adapters;

import com.fleetmanagement.shipment.bag.model.Bag;
import com.fleetmanagement.shipment.bag.model.BagStatus;
import com.fleetmanagement.shipment.bag.port.BagDataPort;
import com.fleetmanagement.shipment.bag.usecase.command.BagCreate;

import java.util.Optional;

public class BagFakeDataAdapter implements BagDataPort {
    @Override
    public Bag create(BagCreate bagCreate) {
        return Bag.builder()
                .id("1")
                .barcode(bagCreate.barcode())
                .bagStatus(BagStatus.CREATED)
                .deliveryPoint(bagCreate.deliveryPoint())
                .build();
    }

    @Override
    public Optional<Bag> retrieveByBarcode(String barcode) {
        if ("EXISTING_BAG_BARCODE".equals(barcode)) {
            return Optional.of(
                    Bag.builder()
                            .id("EXISTING_BAG_ID")
                            .bagStatus(BagStatus.CREATED)
                            .barcode(barcode)
                            .build());
        }

        return Optional.empty();
    }
}
