package com.fleetmanagement.shipment.packet.strategy;

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

import java.util.Collection;
import java.util.Optional;

@Slf4j
@DomainComponent
@RequiredArgsConstructor
public class PackageStrategy implements ShipmentStrategy {
  private final ShipmentDataPort shipmentDataPort;
  private final PackageDataPort packageDataPort;
  private final DeliveryErrorDataPort deliveryErrorDataPort;

  @Override
  public void loadShipment(Shipment shipment) {
    shipmentDataPort.updateStatus(shipment.getId(), PackageStatus.LOADED.getValue());
    log.info("Loading completed for package {}", shipment);
  }

  @Override
  public ShipmentUnloadResult unloadShipment(Shipment shipment, Integer deliveryPoint) {
    Integer shipmentStatus = shipment.getStatusValue();

    if (shipment.isUnloadAvailableAt(deliveryPoint)) {
      shipmentDataPort.updateStatus(shipment.getId(), PackageStatus.UNLOADED.getValue());
      log.info("Unloading completed for package {}", shipment);

      handleBagStatus((Package) shipment);
      shipmentStatus = PackageStatus.UNLOADED.getValue();
    } else {
      log.warn(
          "Unloading is not available for package {} at delivery point {}.",
          shipment,
          deliveryPoint);
      deliveryErrorDataPort.create(
          shipment.getBarcode(), buildDeliveryErrorMessage(shipment.getId(), deliveryPoint));
    }

    return new ShipmentUnloadResult(deliveryPoint, shipment.getBarcode(), shipmentStatus);
  }

  private void handleBagStatus(Package pkg) {
    boolean allPackagesInBagAreUnloaded =
        pkg.getBagId() != null
            && Optional.ofNullable(pkg.getBagId()).map(packageDataPort::retrieveByBagId).stream()
                .flatMap(Collection::stream)
                .allMatch(Package::isUnloaded);

    if (allPackagesInBagAreUnloaded) {
      shipmentDataPort.updateStatus(pkg.getBagId(), BagStatus.UNLOADED.getValue());
      log.info(
          "All packages of bag {} is currently unloaded. Bag is now unloaded too.", pkg.getBagId());
    }
  }

  private String buildDeliveryErrorMessage(String shipmentId, Integer deliveryPoint) {
    return "Package Shipment Failed => SHIPMENT_ID = ["
        + shipmentId
        + "] DELIVERY_POINT = ["
        + deliveryPoint.toString()
        + "]";
  }
}
