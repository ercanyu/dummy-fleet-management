package com.fleetmanagement.shipment.packet;

import com.fleetmanagement.adapters.DeliveryPointFakeDataAdapter;
import com.fleetmanagement.adapters.PackageFakeDataAdapter;
import com.fleetmanagement.common.exception.FleetManagementApiException;
import com.fleetmanagement.deliverypoint.model.DeliveryPointType;
import com.fleetmanagement.shipment.model.Shipment;
import com.fleetmanagement.shipment.model.ShipmentType;
import com.fleetmanagement.shipment.packet.model.Package;
import com.fleetmanagement.shipment.packet.model.PackageStatus;
import com.fleetmanagement.shipment.packet.usecase.command.PackageCreate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class PackageCreateTest {
  PackageCreateUseCaseHandler packageCreateUseCaseHandler;

  @BeforeEach
  void setUp() {
    packageCreateUseCaseHandler =
        new PackageCreateUseCaseHandler(
            new PackageFakeDataAdapter(), new DeliveryPointFakeDataAdapter());
  }

  @Test
  void should_create_package() {
    // given
    PackageCreate packageCreate =
        new PackageCreate("PACKET_BARCODE", DeliveryPointType.DISTRIBUTION_CENTER.getValue(), 5);

    // when
    Package pkg = packageCreateUseCaseHandler.handle(packageCreate);

    // then
    assertThat(pkg)
        .isNotNull()
        .returns("1", Shipment::getId)
        .returns("PACKET_BARCODE", Shipment::getBarcode)
        .returns(PackageStatus.CREATED, Package::getPackageStatus)
        .returns(ShipmentType.PACKAGE, Package::getShipmentType)
        .returns(DeliveryPointType.DISTRIBUTION_CENTER.getValue(), Shipment::getDeliveryPoint)
        .returns(5, Package::getVolumetricWeight);
  }

  @Test
  void should_throw_exception_when_delivery_point_not_exists() {
    // given
    PackageCreate packageCreate = new PackageCreate("PACKET_BARCODE", 54, 5);

    // when
    assertThatExceptionOfType(FleetManagementApiException.class)
        .isThrownBy(() -> packageCreateUseCaseHandler.handle(packageCreate))
        .withMessage("Delivery Point doesn't exist.");
  }
}
