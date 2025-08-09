package com.fleetmanagement.vehicle.mongo.document;

import com.fleetmanagement.vehicle.model.Vehicle;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("vehicles")
@Data
@Builder
public class VehicleDocument {
    @Id
    private String id;
    @Version
    private Long version;
    private String licensePlate;

    public Vehicle toModel() {
        return Vehicle.builder().id(id).licensePlate(licensePlate).build();
    }
}
