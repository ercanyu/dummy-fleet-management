package com.fleetmanagement.shipment.model;

public record ShipmentUnloadResult(Integer deliveryPoint, String barcode, Integer status) {

  public String getShipmentUnloadKey() {
    return barcode + "-" + deliveryPoint;
  }
}
