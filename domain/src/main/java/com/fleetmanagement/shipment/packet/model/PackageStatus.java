package com.fleetmanagement.shipment.packet.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.stream.Stream;

@AllArgsConstructor
@Getter
public enum PackageStatus {
  CREATED(1),
  LOADED_INTO_BAG(2),
  LOADED(3),
  UNLOADED(4);

  private final Integer value;

  public static PackageStatus of(Integer status) {
    return Stream.of(PackageStatus.values())
        .filter(packageStatus -> packageStatus.value.equals(status))
        .findFirst()
        .orElseThrow();
  }
}
