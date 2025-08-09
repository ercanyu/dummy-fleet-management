package com.fleetmanagement.shipment.bag;

import com.fleetmanagement.adapters.BagFakeDataAdapter;
import com.fleetmanagement.adapters.DeliveryPointFakeDataAdapter;
import com.fleetmanagement.common.exception.FleetManagementApiException;
import com.fleetmanagement.deliverypoint.model.DeliveryPointType;
import com.fleetmanagement.shipment.bag.model.Bag;
import com.fleetmanagement.shipment.bag.model.BagStatus;
import com.fleetmanagement.shipment.bag.usecase.command.BagCreate;
import com.fleetmanagement.shipment.model.Shipment;
import com.fleetmanagement.shipment.model.ShipmentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class BagCreateTest {
    BagCreateUseCaseHandler bagCreateUseCaseHandler;

    @BeforeEach
    void setUp() {
        bagCreateUseCaseHandler =
                new BagCreateUseCaseHandler(new BagFakeDataAdapter(), new DeliveryPointFakeDataAdapter());
    }

    @Test
    void should_create_bag() {
        // given
        BagCreate bagCreate =
                new BagCreate("BAG_BARCODE", DeliveryPointType.DISTRIBUTION_CENTER.getValue());

        // when
        Bag bag = bagCreateUseCaseHandler.handle(bagCreate);

        // then
        assertThat(bag)
                .isNotNull()
                .returns("1", Shipment::getId)
                .returns("BAG_BARCODE", Shipment::getBarcode)
                .returns(BagStatus.CREATED, Bag::getBagStatus)
                .returns(ShipmentType.BAG, Bag::getShipmentType)
                .returns(DeliveryPointType.DISTRIBUTION_CENTER.getValue(), Shipment::getDeliveryPoint);
    }

    @Test
    void should_throw_exception_when_delivery_point_not_exists() {
        // given
        BagCreate bagCreate = new BagCreate("BAG_BARCODE", 54);

        // when
        assertThatExceptionOfType(FleetManagementApiException.class)
                .isThrownBy(() -> bagCreateUseCaseHandler.handle(bagCreate))
                .withMessage("Delivery Point doesn't exist.");
    }
}
