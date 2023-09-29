package com.fleetmanagement.delivery.model;

import com.fleetmanagement.shipment.model.ShipmentUnloadResult;

import java.util.List;


public record DeliveryRouteResult (Integer deliveryPoint, List<ShipmentDeliveryResult> deliveryResults) {

  public static DeliveryRouteResult of(
          DeliveryRoute deliveryRoute, List<ShipmentUnloadResult> shipmentUnloadResults) {
    List<ShipmentDeliveryResult> shipmentDeliveryResults = ShipmentDeliveryResult.of(
            deliveryRoute.getDeliveryPoint(),
            deliveryRoute.getShipmentBarcodes(),
            shipmentUnloadResults);

    return new DeliveryRouteResult(deliveryRoute.getDeliveryPoint(), shipmentDeliveryResults);
  }
}
