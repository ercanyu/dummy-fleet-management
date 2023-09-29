package com.fleetmanagement.delivery.model;

import com.fleetmanagement.shipment.model.ShipmentUnloadResult;

import java.util.List;

public record DeliveryResult (String licensePlate, List<DeliveryRouteResult> shipmentDeliveryResults){

  public static DeliveryResult of(
      String licensePlate,
      List<DeliveryRoute> deliveries,
      List<ShipmentUnloadResult> shipmentUnloadResults) {

    var deliveryRouteResults = deliveries.stream()
            .map(deliveryRoute -> DeliveryRouteResult.of(deliveryRoute, shipmentUnloadResults))
            .toList();
    return new DeliveryResult(licensePlate, deliveryRouteResults);
  }
}
