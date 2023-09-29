package com.fleetmanagement.shipment.packet.port;

import com.fleetmanagement.shipment.packet.model.Package;
import com.fleetmanagement.shipment.packet.usecase.command.PackageCreate;

import java.util.List;

public interface PackageDataPort {
  Package create(PackageCreate packageCreate);

  List<Package> retrieveByBagId(String bagId);

  List<Package> loadPackagesToBag(List<String> packageBarcodes, String bagId);
}
