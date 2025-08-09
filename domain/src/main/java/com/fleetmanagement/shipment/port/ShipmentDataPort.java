package com.fleetmanagement.shipment.port;

import com.fleetmanagement.shipment.model.Shipment;

import java.util.List;

public interface ShipmentDataPort {
    List<Shipment> retrieveByBarcodes(List<String> barcodes);

    void updateStatus(String shipmentId, Integer status);

    void updateStatus(List<String> shipmentIds, Integer status);
}
