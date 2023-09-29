package com.fleetmanagement.delivery.rest.dto;

import com.fleetmanagement.delivery.model.DeliveryRouteResult;
import lombok.Data;

import java.util.List;

@Data
public class DeliveryRouteResponse {
  private Integer deliveryPoint;
  private List<DeliveryShipmentResponse> deliveries;

  public static DeliveryRouteResponse fromDeliveryRouteResult(
      DeliveryRouteResult deliveryRouteResult) {

    DeliveryRouteResponse deliveryRouteResponse = new DeliveryRouteResponse();
    deliveryRouteResponse.setDeliveryPoint(deliveryRouteResult.deliveryPoint());
    deliveryRouteResponse.setDeliveries(
        deliveryRouteResult.deliveryResults().stream()
            .map(DeliveryShipmentResponse::fromShipmentDeliveryResult)
            .toList());

    return deliveryRouteResponse;
  }
}
