package com.fleetmanagement.shipment.packet.model;

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
public class Package extends Shipment {
    private PackageStatus packageStatus;
    private Integer volumetricWeight;
    private String bagId;

    @Override
    public ShipmentType getShipmentType() {
        return ShipmentType.PACKAGE;
    }

    @Override
    public Integer getStatusValue() {
        return packageStatus.getValue();
    }

    @Override
    public boolean isUnloadAvailableAt(Integer deliveryPoint) {
        return deliveryPoint.equals(this.deliveryPoint)
                && (deliveryPoint.equals(DeliveryPointType.DISTRIBUTION_CENTER.getValue())
                || ((bagId != null
                && deliveryPoint.equals(DeliveryPointType.TRANSFER_CENTER.getValue()))
                || (bagId == null && deliveryPoint.equals(DeliveryPointType.BRANCH.getValue()))));
    }

    public boolean isUnloaded() {
        return packageStatus == PackageStatus.UNLOADED;
    }

    public boolean isLoaded() {
        return packageStatus == PackageStatus.LOADED;
    }
}
