package com.fleetmanagement.shipment.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ShipmentType {
  BAG("B"),
  PACKAGE("P");

  private final String value;
}
