package com.fleetmanagement.delivery.model;

import com.fleetmanagement.shipment.model.ShipmentUnloadResult;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public record ShipmentDeliveryResult(String barcode, Integer status) {

  public static List<ShipmentDeliveryResult> of(
      Integer deliveryPoint,
      List<String> barcodes,
      List<ShipmentUnloadResult> shipmentUnloadResults) {

    Map<String, ShipmentUnloadResult> shipmentDeliveryResultMap =
        shipmentUnloadResults.stream()
            .collect(
                Collectors.toMap(ShipmentUnloadResult::getShipmentUnloadKey, Function.identity()));

    return barcodes.stream()
        .map(
            barcode ->
                new ShipmentDeliveryResult(
                    barcode, shipmentDeliveryResultMap.get(barcode + "-" + deliveryPoint).status()))
        .toList();
  }
}
