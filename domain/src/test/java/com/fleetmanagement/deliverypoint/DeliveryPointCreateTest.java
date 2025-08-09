package com.fleetmanagement.deliverypoint;

import com.fleetmanagement.adapters.DeliveryPointFakeDataAdapter;
import com.fleetmanagement.common.exception.FleetManagementApiException;
import com.fleetmanagement.deliverypoint.model.DeliveryPoint;
import com.fleetmanagement.deliverypoint.usecase.command.DeliveryPointCreate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class DeliveryPointCreateTest {
    DeliveryPointCreateUseCaseHandler deliveryPointCreateUseCaseHandler;

    @BeforeEach
    void setUp() {
        deliveryPointCreateUseCaseHandler =
                new DeliveryPointCreateUseCaseHandler(new DeliveryPointFakeDataAdapter());
    }

    @Test
    void should_create_delivery_point() {
        // given
        DeliveryPointCreate deliveryPointCreate = new DeliveryPointCreate("DeliveryPoint", 1);
        // when

        DeliveryPoint deliveryPoint = deliveryPointCreateUseCaseHandler.handle(deliveryPointCreate);

        // then
        assertThat(deliveryPoint)
                .isNotNull()
                .returns("1", DeliveryPoint::getId)
                .returns("DeliveryPoint", DeliveryPoint::getName)
                .returns(1, DeliveryPoint::getValue);
    }

    @Test
    void should_throw_exception_when_delivery_point_exists() {
        // given
        DeliveryPointCreate deliveryPointCreate = new DeliveryPointCreate("DeliveryPoint", 2);

        // when
        assertThatExceptionOfType(FleetManagementApiException.class)
                .isThrownBy(() -> deliveryPointCreateUseCaseHandler.handle(deliveryPointCreate))
                .withMessage("Delivery Point already exists.");
    }
}
