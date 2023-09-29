package com.fleetmanagement.deliveryerror.mongo;

import com.fleetmanagement.deliveryerror.mongo.document.DeliveryErrorDocument;
import com.fleetmanagement.deliveryerror.model.DeliveryError;
import com.fleetmanagement.deliveryerror.mongo.repository.DeliveryErrorMongoRepository;
import com.fleetmanagement.deliveryerror.port.DeliveryErrorDataPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class DeliveryErrorMongoAdapter implements DeliveryErrorDataPort {
  private final DeliveryErrorMongoRepository deliveryErrorMongoRepository;

  @Override
  public DeliveryError create(String shipmentBarcode, String message) {
    DeliveryErrorDocument deliveryErrorDocument =
        DeliveryErrorDocument.builder()
            .shipmentBarcode(shipmentBarcode)
            .errorDate(LocalDateTime.now())
            .message(message)
            .build();
    return deliveryErrorMongoRepository.save(deliveryErrorDocument).toModel();
  }
}
