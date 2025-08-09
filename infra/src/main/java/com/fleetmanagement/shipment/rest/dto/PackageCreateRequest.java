package com.fleetmanagement.shipment.rest.dto;

import com.fleetmanagement.shipment.packet.usecase.command.PackageCreate;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class PackageCreateRequest {
    @NotBlank
    private String barcode;
    @NotNull
    private Integer deliveryPoint;

    @NotNull
    @Min(0)
    @Max(Integer.MAX_VALUE)
    private Integer volumetricWeight;

    public PackageCreate toUseCase() {
        return new PackageCreate(barcode, deliveryPoint, volumetricWeight);
    }
}
