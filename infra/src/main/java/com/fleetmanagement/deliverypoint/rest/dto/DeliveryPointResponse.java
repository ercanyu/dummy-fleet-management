package com.fleetmanagement.deliverypoint.rest.dto;

import com.fleetmanagement.deliverypoint.model.DeliveryPoint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryPointResponse {
    private String id;
    private String name;
    private Integer value;

    public static DeliveryPointResponse fromModel(DeliveryPoint deliveryPoint) {
        return DeliveryPointResponse.builder()
                .id(deliveryPoint.getId())
                .name(deliveryPoint.getName())
                .value(deliveryPoint.getValue())
                .build();
    }
}
