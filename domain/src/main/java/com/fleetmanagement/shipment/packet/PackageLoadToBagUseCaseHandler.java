package com.fleetmanagement.shipment.packet;

import com.fleetmanagement.common.DomainComponent;
import com.fleetmanagement.common.exception.ExceptionMessage;
import com.fleetmanagement.common.exception.FleetManagementApiException;
import com.fleetmanagement.common.usecase.UseCaseHandler;
import com.fleetmanagement.shipment.bag.model.Bag;
import com.fleetmanagement.shipment.bag.port.BagDataPort;
import com.fleetmanagement.shipment.packet.model.Package;
import com.fleetmanagement.shipment.packet.port.PackageDataPort;
import com.fleetmanagement.shipment.packet.usecase.command.PackageLoadToBag;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
@DomainComponent
public class PackageLoadToBagUseCaseHandler
    implements UseCaseHandler<List<Package>, PackageLoadToBag> {
  private final PackageDataPort packageDataPort;
  private final BagDataPort bagDataPort;

  @Override
  public List<Package> handle(PackageLoadToBag useCase) {
    Bag bag =
        bagDataPort
            .retrieveByBarcode(useCase.bagBarcode())
            .orElseThrow(() -> new FleetManagementApiException(ExceptionMessage.BAG_NOT_FOUND));

    return packageDataPort.loadPackagesToBag(useCase.packageBarcodes(), bag.getId());
  }
}
