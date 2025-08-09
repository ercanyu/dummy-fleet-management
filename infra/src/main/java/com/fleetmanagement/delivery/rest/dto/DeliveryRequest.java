package com.fleetmanagement.delivery.rest.dto;

import com.fleetmanagement.delivery.usecase.command.MakeDelivery;
import lombok.Data;

import java.util.List;

@Data
public class DeliveryRequest {
    private String plate;
    private List<DeliveryStep> route;

    public MakeDelivery toUseCase() {
        return MakeDelivery.builder()
                .licensePlate(plate)
                .deliveryRoutes(route.stream().map(DeliveryStep::toDeliveryRoute).toList())
                .build();
    }
}
