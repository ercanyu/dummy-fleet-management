package com.fleetmanagement.shipment.bag.port;

import com.fleetmanagement.shipment.bag.model.Bag;
import com.fleetmanagement.shipment.bag.usecase.command.BagCreate;

import java.util.Optional;

public interface BagDataPort {
  Bag create(BagCreate bagCreate);

  Optional<Bag> retrieveByBarcode(String barcode);
}
