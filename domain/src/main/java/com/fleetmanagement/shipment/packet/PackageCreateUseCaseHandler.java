package com.fleetmanagement.shipment.packet;

import com.fleetmanagement.common.DomainComponent;
import com.fleetmanagement.common.exception.ExceptionMessage;
import com.fleetmanagement.common.exception.FleetManagementApiException;
import com.fleetmanagement.common.usecase.UseCaseHandler;
import com.fleetmanagement.deliverypoint.port.DeliveryPointDataPort;
import com.fleetmanagement.shipment.packet.model.Package;
import com.fleetmanagement.shipment.packet.port.PackageDataPort;
import com.fleetmanagement.shipment.packet.usecase.command.PackageCreate;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@DomainComponent
public class PackageCreateUseCaseHandler implements UseCaseHandler<Package, PackageCreate> {
  private final PackageDataPort packageDataPort;
  private final DeliveryPointDataPort deliveryPointDataPort;

  @Override
  public Package handle(PackageCreate useCase) {
    deliveryPointDataPort
        .retrieveByValue(useCase.deliveryPoint())
        .orElseThrow(
            () -> new FleetManagementApiException(ExceptionMessage.DELIVERY_POINT_NOT_FOUND));

    return packageDataPort.create(useCase);
  }
}
