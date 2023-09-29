package com.fleetmanagement.shipment.bag;

import com.fleetmanagement.deliverypoint.model.DeliveryPointType;
import com.fleetmanagement.shipment.bag.model.Bag;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class BagTest {

  @Test
  void should_not_unload_when_at_different_delivery_point() {
    // given
    Bag bag = Bag.builder().deliveryPoint(DeliveryPointType.DISTRIBUTION_CENTER.getValue()).build();

    // then
    assertThat(bag.isUnloadAvailableAt(DeliveryPointType.TRANSFER_CENTER.getValue())).isFalse();
  }

  @Test
  void should_not_unload_when_at_branch() {
    // given
    Bag bag = Bag.builder().deliveryPoint(DeliveryPointType.BRANCH.getValue()).build();

    // then
    assertThat(bag.isUnloadAvailableAt(DeliveryPointType.BRANCH.getValue())).isFalse();
  }

  @Test
  void should_unload_when_at_distribution_center() {
    // given
    Bag bag = Bag.builder().deliveryPoint(DeliveryPointType.DISTRIBUTION_CENTER.getValue()).build();

    // then
    assertThat(bag.isUnloadAvailableAt(DeliveryPointType.DISTRIBUTION_CENTER.getValue())).isTrue();
  }

  @Test
  void should_unload_when_at_transfer_center() {
    // given
    Bag bag = Bag.builder().deliveryPoint(DeliveryPointType.TRANSFER_CENTER.getValue()).build();

    // then
    assertThat(bag.isUnloadAvailableAt(DeliveryPointType.TRANSFER_CENTER.getValue())).isTrue();
  }
}
