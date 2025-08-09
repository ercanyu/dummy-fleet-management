package com.fleetmanagement.shipment.rest.dto;

import com.fleetmanagement.shipment.bag.usecase.command.BagCreate;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class BagCreateRequest {
    @NotBlank
    private String barcode;
    @NotNull
    private Integer deliveryPoint;

    public BagCreate toUseCase() {
        return new BagCreate(barcode, deliveryPoint);
    }
}
