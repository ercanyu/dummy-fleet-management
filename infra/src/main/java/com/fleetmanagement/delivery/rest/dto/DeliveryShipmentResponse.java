package com.fleetmanagement.delivery.rest.dto;

import com.fleetmanagement.delivery.model.ShipmentDeliveryResult;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class DeliveryShipmentResponse {
    private String barcode;
    private Integer state;

    public static DeliveryShipmentResponse fromShipmentDeliveryResult(
            ShipmentDeliveryResult shipmentDeliveryResult) {
        return new DeliveryShipmentResponse(
                shipmentDeliveryResult.barcode(), shipmentDeliveryResult.status());
    }
}
