package com.fleetmanagement.shipment.model;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
public abstract class Shipment {
  private String id;
  private String barcode;
  protected Integer deliveryPoint;

  public abstract ShipmentType getShipmentType();

  public abstract Integer getStatusValue();

  public abstract boolean isUnloadAvailableAt(Integer deliveryPoint);
}
