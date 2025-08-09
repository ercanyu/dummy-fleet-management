package com.fleetmanagement.deliverypoint.mongo.repository;

import com.fleetmanagement.deliverypoint.mongo.document.DeliveryPointDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface DeliveryPointMongoRepository
        extends MongoRepository<DeliveryPointDocument, String> {
    Optional<DeliveryPointDocument> findByValue(Integer value);
}
