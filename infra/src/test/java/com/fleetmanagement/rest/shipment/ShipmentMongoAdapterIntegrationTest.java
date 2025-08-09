package com.fleetmanagement.rest.shipment;

import com.fleetmanagement.deliverypoint.model.DeliveryPointType;
import com.fleetmanagement.shipment.bag.model.Bag;
import com.fleetmanagement.shipment.bag.model.BagStatus;
import com.fleetmanagement.shipment.bag.usecase.command.BagCreate;
import com.fleetmanagement.shipment.model.Shipment;
import com.fleetmanagement.shipment.model.ShipmentType;
import com.fleetmanagement.shipment.mongo.ShipmentMongoAdapter;
import com.fleetmanagement.shipment.mongo.document.ShipmentDocument;
import com.fleetmanagement.shipment.mongo.repository.ShipmentMongoRepository;
import com.fleetmanagement.shipment.packet.model.Package;
import com.fleetmanagement.shipment.packet.model.PackageStatus;
import com.fleetmanagement.shipment.packet.usecase.command.PackageCreate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

public class ShipmentMongoAdapterIntegrationTest extends BaseShipmentIntegrationTest {
    @Autowired
    ShipmentMongoRepository shipmentMongoRepository;
    @Autowired
    ShipmentMongoAdapter shipmentMongoAdapter;

    @Test
    void should_create_bag() {
        // given
        BagCreate bagCreate = new BagCreate("BAG_BARCODE", 1);

        // when
        Bag bag = shipmentMongoAdapter.create(bagCreate);

        // then
        Optional<ShipmentDocument> bagDocument = shipmentMongoRepository.findById(bag.getId());

        assertThat(bagDocument).isPresent();
        assertThat(bagDocument.get())
                .returns("BAG_BARCODE", ShipmentDocument::getBarcode)
                .returns(ShipmentType.BAG, ShipmentDocument::getShipmentType)
                .returns(1, ShipmentDocument::getDeliveryPoint)
                .returns(BagStatus.CREATED.getValue(), ShipmentDocument::getStatus);
    }

    @Test
    void should_retrieve_bag_by_barcode() {
        // given
        saveBag("BAG_BARCODE", 1);

        // when
        Optional<Bag> bag = shipmentMongoAdapter.retrieveByBarcode("BAG_BARCODE");

        // then
        assertThat(bag).isPresent();
        assertThat(bag.get()).returns("BAG_BARCODE", Bag::getBarcode).returns(1, Bag::getDeliveryPoint);
    }

    @Test
    void should_create_package() {
        // given
        PackageCreate packageCreate = new PackageCreate("PACKAGE_BARCODE", 1, 33);

        // when
        Package pkg = shipmentMongoAdapter.create(packageCreate);

        // then
        Optional<ShipmentDocument> packageDocument = shipmentMongoRepository.findById(pkg.getId());
        assertThat(packageDocument).isPresent();
        assertThat(packageDocument.get())
                .returns("PACKAGE_BARCODE", ShipmentDocument::getBarcode)
                .returns(ShipmentType.PACKAGE, ShipmentDocument::getShipmentType)
                .returns(DeliveryPointType.BRANCH.getValue(), ShipmentDocument::getDeliveryPoint)
                .returns(33, ShipmentDocument::getVolumetricWeight)
                .returns(PackageStatus.CREATED.getValue(), ShipmentDocument::getStatus);
    }

    @Test
    void should_retrieve_packages_by_bag_id() {
        // given
        String bagId = saveBag("BAG_BARCODE", DeliveryPointType.DISTRIBUTION_CENTER.getValue()).getId();
        savePackage("PACKAGE_BARCODE1", DeliveryPointType.DISTRIBUTION_CENTER.getValue(), bagId);
        savePackage("PACKAGE_BARCODE2", DeliveryPointType.DISTRIBUTION_CENTER.getValue(), bagId);

        // when
        List<Package> packages = shipmentMongoAdapter.retrieveByBagId(bagId);

        // then
        assertThat(packages)
                .isNotNull()
                .hasSize(2)
                .extracting("barcode", "bagId")
                .containsExactlyInAnyOrder(
                        tuple("PACKAGE_BARCODE1", bagId), tuple("PACKAGE_BARCODE2", bagId));
    }

    @Test
    void should_load_packages_to_bag() {
        // given
        String bagId = saveBag("BAG_BARCODE", DeliveryPointType.DISTRIBUTION_CENTER.getValue()).getId();
        savePackage("PACKAGE_BARCODE1", DeliveryPointType.DISTRIBUTION_CENTER.getValue());
        savePackage("PACKAGE_BARCODE2", DeliveryPointType.DISTRIBUTION_CENTER.getValue());

        // when
        List<Package> packages =
                shipmentMongoAdapter.loadPackagesToBag(
                        List.of("PACKAGE_BARCODE1", "PACKAGE_BARCODE2"), bagId);

        // then
        assertThat(packages)
                .isNotNull()
                .hasSize(2)
                .extracting("barcode", "packageStatus", "bagId")
                .containsExactlyInAnyOrder(
                        tuple("PACKAGE_BARCODE1", PackageStatus.LOADED_INTO_BAG, bagId),
                        tuple("PACKAGE_BARCODE2", PackageStatus.LOADED_INTO_BAG, bagId));
    }

    @Test
    void should_retrieve_shipments_by_barcodes() {
        // given
        saveBag("BAG_BARCODE", DeliveryPointType.DISTRIBUTION_CENTER.getValue());
        savePackage("PACKAGE_BARCODE1", DeliveryPointType.DISTRIBUTION_CENTER.getValue());
        savePackage("PACKAGE_BARCODE2", DeliveryPointType.DISTRIBUTION_CENTER.getValue());

        // when
        List<Shipment> shipments =
                shipmentMongoAdapter.retrieveByBarcodes(
                        List.of("BAG_BARCODE", "PACKAGE_BARCODE1", "PACKAGE_BARCODE2"));

        // then
        assertThat(shipments)
                .isNotNull()
                .hasSize(3)
                .extracting("barcode", "shipmentType")
                .containsExactlyInAnyOrder(
                        tuple("BAG_BARCODE", ShipmentType.BAG),
                        tuple("PACKAGE_BARCODE2", ShipmentType.PACKAGE),
                        tuple("PACKAGE_BARCODE1", ShipmentType.PACKAGE));
    }

    @Test
    void should_update_single_shipment_status() {
        // given
        String bagId = saveBag("BAG_BARCODE", DeliveryPointType.DISTRIBUTION_CENTER.getValue()).getId();

        // when
        shipmentMongoAdapter.updateStatus(bagId, BagStatus.LOADED.getValue());

        // then
        Optional<ShipmentDocument> bag = shipmentMongoRepository.findById(bagId);

        assertThat(bag).isPresent();
        assertThat(bag.get()).returns(BagStatus.LOADED.getValue(), ShipmentDocument::getStatus);
    }

    @Test
    void should_update_many_shipments_status() {
        // given
        String packageId =
                savePackage("PACKAGE_BARCODE1", DeliveryPointType.DISTRIBUTION_CENTER.getValue()).getId();
        String packageId2 =
                savePackage("PACKAGE_BARCODE2", DeliveryPointType.DISTRIBUTION_CENTER.getValue()).getId();

        // when
        shipmentMongoAdapter.updateStatus(
                List.of(packageId, packageId2), PackageStatus.LOADED.getValue());

        // then
        List<ShipmentDocument> shipments =
                shipmentMongoRepository.findByIdIn(List.of(packageId, packageId2));
        assertThat(shipments)
                .isNotNull()
                .hasSize(2)
                .extracting("id", "barcode", "status")
                .containsExactlyInAnyOrder(
                        tuple(packageId, "PACKAGE_BARCODE1", PackageStatus.LOADED.getValue()),
                        tuple(packageId2, "PACKAGE_BARCODE2", PackageStatus.LOADED.getValue()));
    }
}
