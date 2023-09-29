package com.fleetmanagement.shipment.rest.dto;

import com.fleetmanagement.shipment.bag.model.Bag;
import com.fleetmanagement.shipment.bag.model.BagStatus;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BagResponse {
  private String id;
  private String barcode;
  private Integer deliveryPoint;
  private BagStatus bagStatus;

  public static BagResponse fromModel(Bag bag) {
    return BagResponse.builder()
        .id(bag.getId())
        .barcode(bag.getBarcode())
        .deliveryPoint(bag.getDeliveryPoint())
        .bagStatus(bag.getBagStatus())
        .build();
  }
}
