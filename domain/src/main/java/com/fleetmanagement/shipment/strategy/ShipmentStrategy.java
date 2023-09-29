package com.fleetmanagement.shipment.strategy;

import com.fleetmanagement.shipment.model.Shipment;
import com.fleetmanagement.shipment.model.ShipmentUnloadResult;

public interface ShipmentStrategy {
  void loadShipment(Shipment shipment);

  ShipmentUnloadResult unloadShipment(Shipment shipment, Integer deliveryPoint);
}
