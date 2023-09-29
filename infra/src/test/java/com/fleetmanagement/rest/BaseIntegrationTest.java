package com.fleetmanagement.rest;

import com.fleetmanagement.common.rest.ApiResponse;
import com.fleetmanagement.common.rest.ErrorResponse;
import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = "spring.mongodb.embedded.version=3.5.5")
@AutoConfigureDataMongo
public class BaseIntegrationTest {
  protected @Autowired TestRestTemplate testRestTemplate;
  protected @Autowired MongoTemplate mongoTemplate;
  protected ParameterizedTypeReference<ApiResponse<ErrorResponse>> errorTypeReference =
      new ParameterizedTypeReference<>() {};

  @AfterEach
  void setUp() {
    mongoTemplate.remove(new Query(), "vehicles");
    mongoTemplate.remove(new Query(), "delivery_points");
    mongoTemplate.remove(new Query(), "delivery_errors");
    mongoTemplate.remove(new Query(), "shipments");
  }
}
