package com.fleetmanagement.delivery.rest.dto;

import com.fleetmanagement.delivery.model.DeliveryResult;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryResponse {
    private String plate;
    private List<DeliveryRouteResponse> route;

    public static DeliveryResponse fromModel(DeliveryResult deliveryResult) {
        DeliveryResponse deliveryResponse = new DeliveryResponse();
        deliveryResponse.setPlate(deliveryResult.licensePlate());
        deliveryResponse.setRoute(
                deliveryResult.shipmentDeliveryResults().stream()
                        .map(DeliveryRouteResponse::fromDeliveryRouteResult)
                        .toList());

        return deliveryResponse;
    }
}
