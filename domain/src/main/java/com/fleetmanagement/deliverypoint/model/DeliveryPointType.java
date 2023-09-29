package com.fleetmanagement.deliverypoint.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum DeliveryPointType {
  BRANCH(1),
  DISTRIBUTION_CENTER(2),
  TRANSFER_CENTER(3);

  private final Integer value;
}
