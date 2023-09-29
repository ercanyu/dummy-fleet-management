package com.fleetmanagement.rest.shipment;

import com.fleetmanagement.rest.BaseIntegrationTest;
import com.fleetmanagement.shipment.bag.model.BagStatus;
import com.fleetmanagement.shipment.model.ShipmentType;
import com.fleetmanagement.shipment.mongo.document.ShipmentDocument;
import com.fleetmanagement.shipment.packet.model.PackageStatus;

public abstract class BaseShipmentIntegrationTest extends BaseIntegrationTest {
  public ShipmentDocument savePackage(String barcode, Integer deliveryPoint) {
    return mongoTemplate.save(
        ShipmentDocument.builder()
            .shipmentType(ShipmentType.PACKAGE)
            .barcode(barcode)
            .deliveryPoint(deliveryPoint)
            .status(PackageStatus.CREATED.getValue())
            .build());
  }

  public ShipmentDocument savePackage(String barcode, Integer deliveryPoint, String bagId) {
    return mongoTemplate.save(
        ShipmentDocument.builder()
            .shipmentType(ShipmentType.PACKAGE)
            .barcode(barcode)
            .deliveryPoint(deliveryPoint)
            .status(PackageStatus.CREATED.getValue())
            .bagId(bagId)
            .build());
  }

  public ShipmentDocument saveBag(String barcode, Integer deliveryPoint) {
    return mongoTemplate.save(
        ShipmentDocument.builder()
            .shipmentType(ShipmentType.BAG)
            .barcode(barcode)
            .deliveryPoint(deliveryPoint)
            .status(BagStatus.CREATED.getValue())
            .build());
  }
}
