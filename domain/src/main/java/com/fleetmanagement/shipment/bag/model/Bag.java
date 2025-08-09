package com.fleetmanagement.shipment.bag.model;

import com.fleetmanagement.deliverypoint.model.DeliveryPointType;
import com.fleetmanagement.shipment.model.Shipment;
import com.fleetmanagement.shipment.model.ShipmentType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
@Setter
@ToString
public class Bag extends Shipment {
    private BagStatus bagStatus;

    @Override
    public ShipmentType getShipmentType() {
        return ShipmentType.BAG;
    }

    @Override
    public Integer getStatusValue() {
        return bagStatus.getValue();
    }

    @Override
    public boolean isUnloadAvailableAt(Integer deliveryPoint) {
        return deliveryPoint.equals(this.deliveryPoint)
                && !deliveryPoint.equals(DeliveryPointType.BRANCH.getValue());
    }
}
