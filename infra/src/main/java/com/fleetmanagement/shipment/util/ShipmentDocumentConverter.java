package com.fleetmanagement.shipment.util;

import com.fleetmanagement.shipment.bag.model.Bag;
import com.fleetmanagement.shipment.bag.model.BagStatus;
import com.fleetmanagement.shipment.model.Shipment;
import com.fleetmanagement.shipment.model.ShipmentType;
import com.fleetmanagement.shipment.mongo.document.ShipmentDocument;
import com.fleetmanagement.shipment.packet.model.Package;
import com.fleetmanagement.shipment.packet.model.PackageStatus;
import lombok.experimental.UtilityClass;

import java.util.Map;
import java.util.function.Function;

@UtilityClass
public class ShipmentDocumentConverter {
    static final Function<ShipmentDocument, Bag> bagConverter =
            shipmentDocument ->
                    Bag.builder()
                            .id(shipmentDocument.getId())
                            .deliveryPoint(shipmentDocument.getDeliveryPoint())
                            .barcode(shipmentDocument.getBarcode())
                            .bagStatus(BagStatus.of(shipmentDocument.getStatus()))
                            .build();

    static final Function<ShipmentDocument, Package> packageConverter =
            shipmentDocument ->
                    Package.builder()
                            .id(shipmentDocument.getId())
                            .deliveryPoint(shipmentDocument.getDeliveryPoint())
                            .barcode(shipmentDocument.getBarcode())
                            .packageStatus(PackageStatus.of(shipmentDocument.getStatus()))
                            .volumetricWeight(shipmentDocument.getVolumetricWeight())
                            .bagId(shipmentDocument.getBagId())
                            .build();

    static final Map<ShipmentType, Function<ShipmentDocument, ? extends Shipment>> converterMap =
            Map.of(ShipmentType.BAG, bagConverter, ShipmentType.PACKAGE, packageConverter);

    public static Shipment toModel(ShipmentDocument document) {
        return converterMap.get(document.getShipmentType()).apply(document);
    }

    public static Bag toBagModel(ShipmentDocument document) {
        return bagConverter.apply(document);
    }

    public static Package toPackageModel(ShipmentDocument document) {
        return packageConverter.apply(document);
    }
}
