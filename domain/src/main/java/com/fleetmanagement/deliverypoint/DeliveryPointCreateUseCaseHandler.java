package com.fleetmanagement.deliverypoint;

import com.fleetmanagement.common.DomainComponent;
import com.fleetmanagement.common.exception.ExceptionMessage;
import com.fleetmanagement.common.exception.FleetManagementApiException;
import com.fleetmanagement.common.usecase.UseCaseHandler;
import com.fleetmanagement.deliverypoint.model.DeliveryPoint;
import com.fleetmanagement.deliverypoint.port.DeliveryPointDataPort;
import com.fleetmanagement.deliverypoint.usecase.command.DeliveryPointCreate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@DomainComponent
public class DeliveryPointCreateUseCaseHandler
    implements UseCaseHandler<DeliveryPoint, DeliveryPointCreate> {
  private final DeliveryPointDataPort deliveryPointDataPort;

  @Override
  public DeliveryPoint handle(DeliveryPointCreate useCase) {
    if (deliveryPointDataPort.retrieveByValue(useCase.value()).isPresent()) {
      log.warn("Delivery Point {} already exists!", useCase.value());
      throw new FleetManagementApiException(ExceptionMessage.DELIVERY_POINT_ALREADY_EXISTS);
    }

    return deliveryPointDataPort.create(useCase);
  }
}
