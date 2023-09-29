package com.fleetmanagement.deliveryerror.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class DeliveryError {
  private String id;
  private String shipmentBarcode;
  private String message;
  private LocalDateTime errorDate;
}
