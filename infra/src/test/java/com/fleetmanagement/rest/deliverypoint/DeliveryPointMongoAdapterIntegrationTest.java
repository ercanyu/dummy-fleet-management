package com.fleetmanagement.rest.deliverypoint;

import com.fleetmanagement.deliverypoint.model.DeliveryPoint;
import com.fleetmanagement.deliverypoint.model.DeliveryPointType;
import com.fleetmanagement.deliverypoint.mongo.DeliveryPointMongoAdapter;
import com.fleetmanagement.deliverypoint.mongo.document.DeliveryPointDocument;
import com.fleetmanagement.deliverypoint.mongo.repository.DeliveryPointMongoRepository;
import com.fleetmanagement.deliverypoint.usecase.command.DeliveryPointCreate;
import com.fleetmanagement.rest.BaseIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class DeliveryPointMongoAdapterIntegrationTest extends BaseIntegrationTest {
  @Autowired DeliveryPointMongoRepository deliveryPointMongoRepository;
  @Autowired DeliveryPointMongoAdapter deliveryPointMongoAdapter;

  @Test
  void should_create_delivery_point() {
    // given
    DeliveryPointCreate deliveryPointCreate = new DeliveryPointCreate("DELIVERY_POINT", 1);

    // when
    DeliveryPoint deliveryPoint = deliveryPointMongoAdapter.create(deliveryPointCreate);

    // then
    Optional<DeliveryPointDocument> deliveryPointDocument =
        deliveryPointMongoRepository.findById(deliveryPoint.getId());
    assertThat(deliveryPointDocument).isPresent();
    assertThat(deliveryPointDocument.get())
        .returns("DELIVERY_POINT", DeliveryPointDocument::getName)
        .returns(DeliveryPointType.BRANCH.getValue(), DeliveryPointDocument::getValue);
  }

  @Test
  void should_retrieve_delivery_point() {
    // given
    mongoTemplate.save(DeliveryPointDocument.builder().name("DELIVERY_POINT").value(1).build());

    // when
    Optional<DeliveryPoint> deliveryPoint = deliveryPointMongoAdapter.retrieveByValue(1);

    // then
    assertThat(deliveryPoint).isPresent();
    assertThat(deliveryPoint.get())
        .returns("DELIVERY_POINT", DeliveryPoint::getName)
        .returns(DeliveryPointType.BRANCH.getValue(), DeliveryPoint::getValue);
  }
}
