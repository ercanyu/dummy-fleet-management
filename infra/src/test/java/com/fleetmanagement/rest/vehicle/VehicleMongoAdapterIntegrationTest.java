package com.fleetmanagement.rest.vehicle;

import com.fleetmanagement.rest.BaseIntegrationTest;
import com.fleetmanagement.vehicle.model.Vehicle;
import com.fleetmanagement.vehicle.mongo.VehicleMongoAdapter;
import com.fleetmanagement.vehicle.mongo.document.VehicleDocument;
import com.fleetmanagement.vehicle.mongo.repository.VehicleMongoRepository;
import com.fleetmanagement.vehicle.usecase.command.VehicleCreate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class VehicleMongoAdapterIntegrationTest extends BaseIntegrationTest {

  @Autowired VehicleMongoAdapter vehicleMongoAdapter;
  @Autowired VehicleMongoRepository vehicleMongoRepository;

  @Test
  void should_create_vehicle() {
    // given
    VehicleCreate vehicleCreate = new VehicleCreate("34 EY 34");

    // when
    Vehicle vehicle = vehicleMongoAdapter.create(vehicleCreate);

    // then
    Optional<VehicleDocument> vehicleDocument = vehicleMongoRepository.findById(vehicle.getId());
    assertThat(vehicleDocument).isPresent();
    assertThat(vehicleDocument.get().getLicensePlate()).isEqualTo("34 EY 34");
  }

  @Test
  void should_retrieve_vehicle() {
    // given
    mongoTemplate.save(VehicleDocument.builder().licensePlate("34 EY 34").build());

    // when
    Optional<Vehicle> vehicle = vehicleMongoAdapter.retrieveByLicensePlate("34 EY 34");

    // then
    assertThat(vehicle).isPresent();
    assertThat(vehicle.get().getLicensePlate()).isEqualTo("34 EY 34");
  }
}
