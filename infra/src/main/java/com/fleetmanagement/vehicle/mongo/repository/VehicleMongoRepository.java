package com.fleetmanagement.vehicle.mongo.repository;

import com.fleetmanagement.vehicle.mongo.document.VehicleDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface VehicleMongoRepository extends MongoRepository<VehicleDocument, String> {
    Optional<VehicleDocument> findByLicensePlate(String licensePlate);
}
