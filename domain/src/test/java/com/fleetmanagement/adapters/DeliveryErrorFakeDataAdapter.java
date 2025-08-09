package com.fleetmanagement.adapters;

import com.fleetmanagement.deliveryerror.model.DeliveryError;
import com.fleetmanagement.deliveryerror.port.DeliveryErrorDataPort;

import java.time.LocalDateTime;

public class DeliveryErrorFakeDataAdapter implements DeliveryErrorDataPort {
    @Override
    public DeliveryError create(String shipmentBarcode, String message) {
        return DeliveryError.builder()
                .id("1")
                .message(message)
                .shipmentBarcode(shipmentBarcode)
                .errorDate(LocalDateTime.now())
                .build();
    }
}
