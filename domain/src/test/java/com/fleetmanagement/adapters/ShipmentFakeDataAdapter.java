package com.fleetmanagement.adapters;

import com.fleetmanagement.deliverypoint.model.DeliveryPointType;
import com.fleetmanagement.shipment.model.Shipment;
import com.fleetmanagement.shipment.packet.model.Package;
import com.fleetmanagement.shipment.packet.model.PackageStatus;
import com.fleetmanagement.shipment.port.ShipmentDataPort;

import java.util.List;

public class ShipmentFakeDataAdapter implements ShipmentDataPort {
  @Override
  public List<Shipment> retrieveByBarcodes(List<String> barcodes) {
    Shipment shipment =
        Package.builder()
            .barcode("PACKAGE_BARCODE1")
            .packageStatus(PackageStatus.CREATED)
            .deliveryPoint(DeliveryPointType.BRANCH.getValue())
            .build();

    return List.of(shipment);
  }

  @Override
  public void updateStatus(String shipmentId, Integer status) {
    // do nothing
  }

  @Override
  public void updateStatus(List<String> shipmentIds, Integer status) {
    // do nothing
  }
}
