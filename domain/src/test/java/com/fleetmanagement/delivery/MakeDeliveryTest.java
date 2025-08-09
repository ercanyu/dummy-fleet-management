package com.fleetmanagement.delivery;

import com.fleetmanagement.adapters.DeliveryErrorFakeDataAdapter;
import com.fleetmanagement.adapters.PackageFakeDataAdapter;
import com.fleetmanagement.adapters.ShipmentFakeDataAdapter;
import com.fleetmanagement.adapters.VehicleFakeDataAdapter;
import com.fleetmanagement.common.exception.FleetManagementApiException;
import com.fleetmanagement.delivery.model.DeliveryResult;
import com.fleetmanagement.delivery.model.DeliveryRoute;
import com.fleetmanagement.delivery.model.ShipmentDeliveryResult;
import com.fleetmanagement.delivery.usecase.command.MakeDelivery;
import com.fleetmanagement.deliverypoint.model.DeliveryPointType;
import com.fleetmanagement.shipment.bag.strategy.BagStrategy;
import com.fleetmanagement.shipment.packet.model.PackageStatus;
import com.fleetmanagement.shipment.packet.strategy.PackageStrategy;
import com.fleetmanagement.shipment.strategy.ShipmentStrategyRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

public class MakeDeliveryTest {
    MakeDeliveryUseCaseHandler makeDeliveryUseCaseHandler;

    @BeforeEach
    void setUp() {
        VehicleFakeDataAdapter vehicleFakeDataAdapter = new VehicleFakeDataAdapter();
        ShipmentFakeDataAdapter shipmentFakeDataAdapter = new ShipmentFakeDataAdapter();
        PackageFakeDataAdapter packageFakeDataAdapter = new PackageFakeDataAdapter();
        DeliveryErrorFakeDataAdapter deliveryErrorFakeDataAdapter = new DeliveryErrorFakeDataAdapter();

        BagStrategy bagStrategy =
                new BagStrategy(
                        packageFakeDataAdapter, shipmentFakeDataAdapter, deliveryErrorFakeDataAdapter);

        PackageStrategy packageStrategy =
                new PackageStrategy(
                        shipmentFakeDataAdapter, packageFakeDataAdapter, deliveryErrorFakeDataAdapter);

        makeDeliveryUseCaseHandler =
                new MakeDeliveryUseCaseHandler(
                        vehicleFakeDataAdapter,
                        shipmentFakeDataAdapter,
                        new ShipmentStrategyRegistry(bagStrategy, packageStrategy));
    }

    @Test
    void should_throw_exception_when_vehicle_not_exists() {
        // given
        MakeDelivery makeDelivery =
                MakeDelivery.builder().licensePlate("NOT_EXISTING_LICENSE_PLATE").build();

        // when
        assertThatExceptionOfType(FleetManagementApiException.class)
                .isThrownBy(() -> makeDeliveryUseCaseHandler.handle(makeDelivery))
                .withMessage("Vehicle doesn't exist.");
    }

    @Test
    void should_unload_package_at_branch() {
        // given
        MakeDelivery makeDelivery =
                MakeDelivery.builder()
                        .licensePlate("EXISTING_LICENSE_PLATE")
                        .deliveryRoutes(
                                List.of(
                                        new DeliveryRoute(
                                                DeliveryPointType.BRANCH.getValue(), List.of("PACKAGE_BARCODE1"))))
                        .build();

        // when
        DeliveryResult deliveryResult = makeDeliveryUseCaseHandler.handle(makeDelivery);

        // then
        assertThat(deliveryResult)
                .isNotNull()
                .returns("EXISTING_LICENSE_PLATE", DeliveryResult::licensePlate);

        assertThat(deliveryResult.shipmentDeliveryResults())
                .isNotNull()
                .hasSize(1)
                .extracting("deliveryPoint", "deliveryResults")
                .containsExactlyInAnyOrder(
                        tuple(
                                DeliveryPointType.BRANCH.getValue(),
                                List.of(
                                        new ShipmentDeliveryResult(
                                                "PACKAGE_BARCODE1", PackageStatus.UNLOADED.getValue()))));
    }
}
