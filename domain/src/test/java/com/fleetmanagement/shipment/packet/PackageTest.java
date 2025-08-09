package com.fleetmanagement.shipment.packet;

import com.fleetmanagement.deliverypoint.model.DeliveryPointType;
import com.fleetmanagement.shipment.packet.model.Package;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PackageTest {

    @Test
    void should_unload_when_at_distribution_center() {
        // given
        Package pkg =
                Package.builder().deliveryPoint(DeliveryPointType.DISTRIBUTION_CENTER.getValue()).build();

        // then
        assertThat(pkg.isUnloadAvailableAt(DeliveryPointType.DISTRIBUTION_CENTER.getValue())).isTrue();
    }

    @Test
    void should_not_unload_when_at_different_delivery_point() {
        // given
        Package pkg =
                Package.builder().deliveryPoint(DeliveryPointType.DISTRIBUTION_CENTER.getValue()).build();

        // then
        assertThat(pkg.isUnloadAvailableAt(DeliveryPointType.BRANCH.getValue())).isFalse();
    }

    @Test
    void should_not_unload_when_at_branch_and_in_a_bag() {
        // given
        Package pkg =
                Package.builder()
                        .deliveryPoint(DeliveryPointType.BRANCH.getValue())
                        .bagId("EXISTING_BAG_ID")
                        .build();

        // then
        assertThat(pkg.isUnloadAvailableAt(DeliveryPointType.BRANCH.getValue())).isFalse();
    }

    @Test
    void should_unload_when_at_branch_and_not_in_a_bag() {
        // given
        Package pkg = Package.builder().deliveryPoint(DeliveryPointType.BRANCH.getValue()).build();

        // then
        assertThat(pkg.isUnloadAvailableAt(DeliveryPointType.BRANCH.getValue())).isTrue();
    }

    @Test
    void should_unload_when_at_transfer_center_and_in_a_bag() {
        // given
        Package pkg =
                Package.builder()
                        .deliveryPoint(DeliveryPointType.TRANSFER_CENTER.getValue())
                        .bagId("EXISTING_BAG_ID")
                        .build();

        // then
        assertThat(pkg.isUnloadAvailableAt(DeliveryPointType.TRANSFER_CENTER.getValue())).isTrue();
    }

    @Test
    void should_not_unload_when_at_transfer_center_and_not_in_a_bag() {
        // given
        Package pkg =
                Package.builder().deliveryPoint(DeliveryPointType.TRANSFER_CENTER.getValue()).build();

        // then
        assertThat(pkg.isUnloadAvailableAt(DeliveryPointType.TRANSFER_CENTER.getValue())).isFalse();
    }
}
