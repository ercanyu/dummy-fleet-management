package com.fleetmanagement.shipment.strategy;

import com.fleetmanagement.common.DomainComponent;
import com.fleetmanagement.shipment.bag.strategy.BagStrategy;
import com.fleetmanagement.shipment.model.ShipmentType;
import com.fleetmanagement.shipment.packet.strategy.PackageStrategy;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@DomainComponent
@RequiredArgsConstructor
public class ShipmentStrategyRegistry {
  private Map<ShipmentType, ShipmentStrategy> shipmentStrategyMap;
  private final BagStrategy bagStrategy;
  private final PackageStrategy packageStrategy;

  public ShipmentStrategy getStrategy(ShipmentType shipmentType) {
    if (shipmentStrategyMap == null || shipmentStrategyMap.isEmpty()) {
      shipmentStrategyMap =
          Map.of(ShipmentType.BAG, bagStrategy, ShipmentType.PACKAGE, packageStrategy);
    }

    return shipmentStrategyMap.get(shipmentType);
  }
}
