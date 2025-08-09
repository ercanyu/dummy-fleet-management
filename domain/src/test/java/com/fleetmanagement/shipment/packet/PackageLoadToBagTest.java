package com.fleetmanagement.shipment.packet;

import com.fleetmanagement.adapters.BagFakeDataAdapter;
import com.fleetmanagement.adapters.PackageFakeDataAdapter;
import com.fleetmanagement.common.exception.FleetManagementApiException;
import com.fleetmanagement.shipment.packet.model.Package;
import com.fleetmanagement.shipment.packet.usecase.command.PackageLoadToBag;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

public class PackageLoadToBagTest {

    PackageLoadToBagUseCaseHandler packageLoadToBagUseCaseHandler;

    @BeforeEach
    void setUp() {
        packageLoadToBagUseCaseHandler =
                new PackageLoadToBagUseCaseHandler(new PackageFakeDataAdapter(), new BagFakeDataAdapter());
    }

    @Test
    void should_load_packages_to_bag() {
        // given
        PackageLoadToBag packageLoadToBag =
                new PackageLoadToBag(
                        List.of("PACKAGE_BARCODE1", "PACKAGE_BARCODE2"), "EXISTING_BAG_BARCODE");

        // when
        List<Package> packages = packageLoadToBagUseCaseHandler.handle(packageLoadToBag);

        // then
        assertThat(packages)
                .isNotNull()
                .hasSize(2)
                .extracting("barcode", "bagId")
                .containsExactlyInAnyOrder(
                        tuple("PACKAGE_BARCODE1", "EXISTING_BAG_ID"),
                        tuple("PACKAGE_BARCODE2", "EXISTING_BAG_ID"));
    }

    @Test
    void should_throw_exception_when_bag_not_exists() {
        // given
        PackageLoadToBag packageLoadToBag =
                new PackageLoadToBag(
                        List.of("PACKAGE_BARCODE1", "PACKAGE_BARCODE2"), "NOT_EXISTING_BAG_BARCODE");

        // when
        assertThatExceptionOfType(FleetManagementApiException.class)
                .isThrownBy(() -> packageLoadToBagUseCaseHandler.handle(packageLoadToBag))
                .withMessage("Bag doesn't exist.");
    }
}
