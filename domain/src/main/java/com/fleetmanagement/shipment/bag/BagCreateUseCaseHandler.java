package com.fleetmanagement.shipment.bag;

import com.fleetmanagement.common.DomainComponent;
import com.fleetmanagement.common.exception.ExceptionMessage;
import com.fleetmanagement.common.exception.FleetManagementApiException;
import com.fleetmanagement.common.usecase.UseCaseHandler;
import com.fleetmanagement.deliverypoint.port.DeliveryPointDataPort;
import com.fleetmanagement.shipment.bag.model.Bag;
import com.fleetmanagement.shipment.bag.port.BagDataPort;
import com.fleetmanagement.shipment.bag.usecase.command.BagCreate;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@DomainComponent
public class BagCreateUseCaseHandler implements UseCaseHandler<Bag, BagCreate> {
    private final BagDataPort bagDataPort;
    private final DeliveryPointDataPort deliveryPointDataPort;

    @Override
    public Bag handle(BagCreate useCase) {
        deliveryPointDataPort
                .retrieveByValue(useCase.deliveryPoint())
                .orElseThrow(
                        () -> new FleetManagementApiException(ExceptionMessage.DELIVERY_POINT_NOT_FOUND));

        return bagDataPort.create(useCase);
    }
}
