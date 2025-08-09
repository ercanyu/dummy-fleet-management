package com.fleetmanagement.shipment.bag.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.stream.Stream;

@AllArgsConstructor
@Getter
public enum BagStatus {
    CREATED(1),
    LOADED(3),
    UNLOADED(4);

    private final Integer value;

    public static BagStatus of(Integer status) {
        return Stream.of(BagStatus.values())
                .filter(bagStatus -> bagStatus.value.equals(status))
                .findFirst()
                .orElseThrow();
    }
}
