package com.fleetmanagement.shipment.mongo;

import com.fleetmanagement.shipment.mongo.repository.ShipmentMongoRepository;
import com.fleetmanagement.shipment.bag.model.Bag;
import com.fleetmanagement.shipment.bag.model.BagStatus;
import com.fleetmanagement.shipment.bag.port.BagDataPort;
import com.fleetmanagement.shipment.bag.usecase.command.BagCreate;
import com.fleetmanagement.shipment.model.Shipment;
import com.fleetmanagement.shipment.model.ShipmentType;
import com.fleetmanagement.shipment.mongo.document.ShipmentDocument;
import com.fleetmanagement.shipment.packet.model.Package;
import com.fleetmanagement.shipment.packet.model.PackageStatus;
import com.fleetmanagement.shipment.packet.port.PackageDataPort;
import com.fleetmanagement.shipment.packet.usecase.command.PackageCreate;
import com.fleetmanagement.shipment.port.ShipmentDataPort;
import com.fleetmanagement.shipment.util.ShipmentDocumentConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ShipmentMongoAdapter implements ShipmentDataPort, BagDataPort, PackageDataPort {
  private final ShipmentMongoRepository shipmentMongoRepository;

  @Override
  public Bag create(BagCreate bagCreate) {
    ShipmentDocument shipmentDocument =
        ShipmentDocument.builder()
            .status(BagStatus.CREATED.getValue())
            .barcode(bagCreate.barcode())
            .deliveryPoint(bagCreate.deliveryPoint())
            .shipmentType(ShipmentType.BAG)
            .build();
    ShipmentDocument savedDocument = shipmentMongoRepository.save(shipmentDocument);

    return ShipmentDocumentConverter.toBagModel(savedDocument);
  }

  @Override
  public Optional<Bag> retrieveByBarcode(String barcode) {
    return shipmentMongoRepository
        .findByBarcode(barcode)
        .map(ShipmentDocumentConverter::toBagModel);
  }

  @Override
  public Package create(PackageCreate packageCreate) {
    ShipmentDocument shipmentDocument =
        ShipmentDocument.builder()
            .barcode(packageCreate.barcode())
            .deliveryPoint(packageCreate.deliveryPoint())
            .status(PackageStatus.CREATED.getValue())
            .volumetricWeight(packageCreate.volumetricWeight())
            .shipmentType(ShipmentType.PACKAGE)
            .build();
    ShipmentDocument savedDocument = shipmentMongoRepository.save(shipmentDocument);

    return ShipmentDocumentConverter.toPackageModel(savedDocument);
  }

  @Override
  public List<Package> retrieveByBagId(String bagId) {
    return shipmentMongoRepository.findByBagId(bagId).stream()
        .map(ShipmentDocumentConverter::toPackageModel)
        .toList();
  }

  @Override
  public List<Package> loadPackagesToBag(List<String> packageBarcodes, String bagId) {
    List<ShipmentDocument> shipmentDocuments =
        shipmentMongoRepository.findByBarcodeIn(packageBarcodes).stream()
            .map(shipment -> assignBagIdToPackageAndChangeStatusAsLoadedIntoBag(shipment, bagId))
            .toList();

    return shipmentMongoRepository.saveAll(shipmentDocuments).stream()
        .map(ShipmentDocumentConverter::toPackageModel)
        .toList();
  }

  private ShipmentDocument assignBagIdToPackageAndChangeStatusAsLoadedIntoBag(
      ShipmentDocument shipmentDocument, String bagId) {
    shipmentDocument.setBagId(bagId);
    shipmentDocument.setStatus(PackageStatus.LOADED_INTO_BAG.getValue());
    return shipmentDocument;
  }

  @Override
  public List<Shipment> retrieveByBarcodes(List<String> barcodes) {
    return shipmentMongoRepository.findByBarcodeIn(barcodes).stream()
        .map(ShipmentDocumentConverter::toModel)
        .toList();
  }

  @Override
  public void updateStatus(String shipmentId, Integer status) {
    shipmentMongoRepository
        .findById(shipmentId)
        .ifPresent(s -> updateShipmentDocumentStatusAndSave(s, status));
  }

  private void updateShipmentDocumentStatusAndSave(
      ShipmentDocument shipmentDocument, Integer status) {
    shipmentDocument.setStatus(status);
    shipmentMongoRepository.save(shipmentDocument);
  }

  @Override
  public void updateStatus(List<String> shipmentIds, Integer status) {
    List<ShipmentDocument> shipments =
        shipmentMongoRepository.findByIdIn(shipmentIds).stream()
            .map(shipmentDocument -> setShipmentDocumentStatus(shipmentDocument, status))
            .toList();

    shipmentMongoRepository.saveAll(shipments);
  }

  private ShipmentDocument setShipmentDocumentStatus(
      ShipmentDocument shipmentDocument, Integer status) {
    shipmentDocument.setStatus(status);
    return shipmentDocument;
  }
}
