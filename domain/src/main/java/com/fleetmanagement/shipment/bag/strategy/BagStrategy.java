package com.fleetmanagement.shipment.bag.strategy;

import com.fleetmanagement.common.DomainComponent;
import com.fleetmanagement.deliveryerror.port.DeliveryErrorDataPort;
import com.fleetmanagement.shipment.bag.model.BagStatus;
import com.fleetmanagement.shipment.model.Shipment;
import com.fleetmanagement.shipment.model.ShipmentUnloadResult;
import com.fleetmanagement.shipment.packet.model.Package;
import com.fleetmanagement.shipment.packet.model.PackageStatus;
import com.fleetmanagement.shipment.packet.port.PackageDataPort;
import com.fleetmanagement.shipment.port.ShipmentDataPort;
import com.fleetmanagement.shipment.strategy.ShipmentStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@DomainComponent
@RequiredArgsConstructor
public class BagStrategy implements ShipmentStrategy {
    private final PackageDataPort packageDataPort;
    private final ShipmentDataPort shipmentDataPort;
    private final DeliveryErrorDataPort deliveryErrorDataPort;

    @Override
    public void loadShipment(Shipment shipment) {
        packageDataPort
                .retrieveByBagId(shipment.getId())
                .forEach(s -> shipmentDataPort.updateStatus(s.getId(), PackageStatus.LOADED.getValue()));
        log.info("Loading completed for packages of bag {}", shipment.getId());

        shipmentDataPort.updateStatus(shipment.getId(), BagStatus.LOADED.getValue());
        log.info("Loading completed for bag {}", shipment);
    }

    @Override
    public ShipmentUnloadResult unloadShipment(Shipment shipment, Integer deliveryPoint) {
        Integer shipmentStatus = shipment.getStatusValue();

        if (shipment.isUnloadAvailableAt(deliveryPoint)) {
            updateStatus(shipment);
            shipmentStatus = BagStatus.UNLOADED.getValue();
            log.info("Unloading completed for bag {} at delivery point {}", shipment, deliveryPoint);
        } else {
            log.warn(
                    "Unloading is not available for bag {} at delivery point {}.", shipment, deliveryPoint);
            deliveryErrorDataPort.create(
                    shipment.getBarcode(), buildDeliveryErrorMessage(shipment.getId(), deliveryPoint));
        }

        return new ShipmentUnloadResult(deliveryPoint, shipment.getBarcode(), shipmentStatus);
    }

    private void updateStatus(Shipment shipment) {
        shipmentDataPort.updateStatus(shipment.getId(), BagStatus.UNLOADED.getValue());
        List<String> loadedPackageIds =
                packageDataPort.retrieveByBagId(shipment.getId()).stream()
                        .filter(Package::isLoaded)
                        .map(Shipment::getId)
                        .toList();

        log.info("Unloading completed for packages of bag {}", shipment.getId());

        shipmentDataPort.updateStatus(loadedPackageIds, PackageStatus.UNLOADED.getValue());
        log.info("Unloading completed for bag {}", shipment);
    }

    private String buildDeliveryErrorMessage(String shipmentId, Integer deliveryPoint) {
        return "Bag Shipment Failed => SHIPMENT_ID = ["
                + shipmentId
                + "] DELIVERY_POINT = ["
                + deliveryPoint.toString()
                + "]";
    }
}
