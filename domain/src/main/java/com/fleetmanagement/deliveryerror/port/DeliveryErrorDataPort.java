package com.fleetmanagement.deliveryerror.port;

import com.fleetmanagement.deliveryerror.model.DeliveryError;

public interface DeliveryErrorDataPort {
    DeliveryError create(String shipmentBarcode, String message);
}
