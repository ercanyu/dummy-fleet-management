package com.fleetmanagement.deliveryerror.mongo.repository;

import com.fleetmanagement.deliveryerror.mongo.document.DeliveryErrorDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DeliveryErrorMongoRepository
        extends MongoRepository<DeliveryErrorDocument, String> {
}
