package com.fleetmanagement.shipment.mongo.repository;

import com.fleetmanagement.shipment.mongo.document.ShipmentDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ShipmentMongoRepository extends MongoRepository<ShipmentDocument, String> {
  List<ShipmentDocument> findByBagId(String bagId);

  List<ShipmentDocument> findByBarcodeIn(List<String> barcodes);

  List<ShipmentDocument> findByIdIn(List<String> ids);

  Optional<ShipmentDocument> findByBarcode(String barcode);
}
