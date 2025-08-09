package com.fleetmanagement.delivery.rest.dto;

import com.fleetmanagement.delivery.model.DeliveryRoute;
import lombok.Data;

import java.util.List;

@Data
public class DeliveryStep {
    private Integer deliveryPoint;
    private List<Barcode> deliveries;

    public DeliveryRoute toDeliveryRoute() {
        return new DeliveryRoute(deliveryPoint, deliveries.stream().map(Barcode::getBarcode).toList());
    }
}
