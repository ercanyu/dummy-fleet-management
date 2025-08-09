package com.fleetmanagement.deliverypoint.mongo;

import com.fleetmanagement.deliverypoint.model.DeliveryPoint;
import com.fleetmanagement.deliverypoint.mongo.document.DeliveryPointDocument;
import com.fleetmanagement.deliverypoint.mongo.repository.DeliveryPointMongoRepository;
import com.fleetmanagement.deliverypoint.port.DeliveryPointDataPort;
import com.fleetmanagement.deliverypoint.usecase.command.DeliveryPointCreate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DeliveryPointMongoAdapter implements DeliveryPointDataPort {
    private final DeliveryPointMongoRepository deliveryPointMongoRepository;

    @Override
    public DeliveryPoint create(DeliveryPointCreate deliveryPointCreate) {
        DeliveryPointDocument deliveryPointDocument =
                DeliveryPointDocument.builder()
                        .name(deliveryPointCreate.name())
                        .value(deliveryPointCreate.value())
                        .build();

        return deliveryPointMongoRepository.save(deliveryPointDocument).toModel();
    }

    @Override
    public Optional<DeliveryPoint> retrieveByValue(Integer value) {
        return deliveryPointMongoRepository.findByValue(value).map(DeliveryPointDocument::toModel);
    }
}
