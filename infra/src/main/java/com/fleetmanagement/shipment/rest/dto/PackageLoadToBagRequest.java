package com.fleetmanagement.shipment.rest.dto;

import com.fleetmanagement.shipment.packet.usecase.command.PackageLoadToBag;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class PackageLoadToBagRequest {
    @NotNull
    private List<String> packageBarcodes;
    @NotBlank
    private String bagBarcode;

    public PackageLoadToBag toUseCase() {
        return new PackageLoadToBag(packageBarcodes, bagBarcode);
    }
}
