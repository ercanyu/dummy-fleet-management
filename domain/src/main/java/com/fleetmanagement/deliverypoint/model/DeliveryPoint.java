package com.fleetmanagement.deliverypoint.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DeliveryPoint {
  private String id;
  private String name;
  private Integer value;
}
