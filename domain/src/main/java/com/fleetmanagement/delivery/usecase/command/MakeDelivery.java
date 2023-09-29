package com.fleetmanagement.delivery.usecase.command;

import com.fleetmanagement.common.model.UseCase;
import com.fleetmanagement.delivery.model.DeliveryRoute;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class MakeDelivery implements UseCase {
  private String licensePlate;
  private List<DeliveryRoute> deliveryRoutes;
}
