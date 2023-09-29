package com.fleetmanagement.shipment.packet.usecase.command;

import com.fleetmanagement.common.model.UseCase;

import java.util.List;

public record PackageLoadToBag(List<String> packageBarcodes, String bagBarcode) implements UseCase {
}
