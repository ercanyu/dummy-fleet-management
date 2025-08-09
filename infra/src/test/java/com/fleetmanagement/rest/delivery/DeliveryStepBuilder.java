package com.fleetmanagement.rest.delivery;

import com.fleetmanagement.delivery.rest.dto.Barcode;
import com.fleetmanagement.delivery.rest.dto.DeliveryStep;

import java.util.List;

public class DeliveryStepBuilder {

    public static DeliveryStep from(Integer deliveryPoint, List<String> barcodes) {
        List<Barcode> barcodeList = barcodes.stream().map(DeliveryStepBuilder::createBarcode).toList();
        DeliveryStep deliveryStep = new DeliveryStep();
        deliveryStep.setDeliveryPoint(deliveryPoint);
        deliveryStep.setDeliveries(barcodeList);

        return deliveryStep;
    }

    private static Barcode createBarcode(String barcodeStr) {
        Barcode barcode = new Barcode();
        barcode.setBarcode(barcodeStr);
        return barcode;
    }
}
