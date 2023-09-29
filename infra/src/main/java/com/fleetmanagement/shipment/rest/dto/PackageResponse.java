package com.fleetmanagement.shipment.rest.dto;

import com.fleetmanagement.shipment.packet.model.Package;
import com.fleetmanagement.shipment.packet.model.PackageStatus;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PackageResponse {
  private String id;
  private String barcode;
  private Integer deliveryPoint;
  private PackageStatus packageStatus;
  private Integer volumetricWeight;
  private String bagId;

  public static PackageResponse fromModel(Package pkg) {
    return PackageResponse.builder()
        .id(pkg.getId())
        .barcode(pkg.getBarcode())
        .deliveryPoint(pkg.getDeliveryPoint())
        .packageStatus(pkg.getPackageStatus())
        .volumetricWeight(pkg.getVolumetricWeight())
        .bagId(pkg.getBagId())
        .build();
  }
}
