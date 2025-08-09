package com.fleetmanagement.adapters;

import com.fleetmanagement.deliverypoint.model.DeliveryPointType;
import com.fleetmanagement.shipment.packet.model.Package;
import com.fleetmanagement.shipment.packet.model.PackageStatus;
import com.fleetmanagement.shipment.packet.port.PackageDataPort;
import com.fleetmanagement.shipment.packet.usecase.command.PackageCreate;

import java.util.List;
import java.util.UUID;

public class PackageFakeDataAdapter implements PackageDataPort {
    @Override
    public Package create(PackageCreate packageCreate) {
        return Package.builder()
                .id("1")
                .packageStatus(PackageStatus.CREATED)
                .volumetricWeight(packageCreate.volumetricWeight())
                .deliveryPoint(packageCreate.deliveryPoint())
                .barcode(packageCreate.barcode())
                .build();
    }

    @Override
    public List<Package> retrieveByBagId(String bagId) {
        if ("NOT_LOADED_BAG".equals(bagId)) {
            Package pkg1 = createPackage("PACKAGE_BARCODE1", bagId, PackageStatus.LOADED_INTO_BAG);
            Package pkg2 = createPackage("PACKAGE_BARCODE2", bagId, PackageStatus.LOADED_INTO_BAG);

            return List.of(pkg1, pkg2);
        }

        if ("LOADED_BAG".equals(bagId)) {
            Package pkg1 = createPackage("PACKAGE_BARCODE1", bagId, PackageStatus.LOADED);
            Package pkg2 = createPackage("PACKAGE_BARCODE2", bagId, PackageStatus.LOADED);

            return List.of(pkg1, pkg2);
        }

        Package pkg1 = createPackage("PACKAGE_BARCODE1", bagId, PackageStatus.CREATED);
        Package pkg2 = createPackage("PACKAGE_BARCODE2", bagId, PackageStatus.CREATED);

        return List.of(pkg1, pkg2);
    }

    @Override
    public List<Package> loadPackagesToBag(List<String> packageBarcodes, String bagId) {
        return packageBarcodes.stream()
                .map(s -> createPackage(s, bagId, PackageStatus.LOADED_INTO_BAG))
                .toList();
    }

    private Package createPackage(String barcode, String bagId, PackageStatus packageStatus) {
        return Package.builder()
                .id(UUID.randomUUID().toString())
                .packageStatus(packageStatus)
                .volumetricWeight(1)
                .deliveryPoint(DeliveryPointType.BRANCH.getValue())
                .bagId(bagId)
                .barcode(barcode)
                .build();
    }
}
