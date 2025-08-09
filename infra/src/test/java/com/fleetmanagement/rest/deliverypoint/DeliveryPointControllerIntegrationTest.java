package com.fleetmanagement.rest.deliverypoint;

import com.fleetmanagement.common.rest.ApiResponse;
import com.fleetmanagement.common.rest.ErrorResponse;
import com.fleetmanagement.deliverypoint.model.DeliveryPointType;
import com.fleetmanagement.deliverypoint.mongo.document.DeliveryPointDocument;
import com.fleetmanagement.deliverypoint.rest.dto.DeliveryPointCreateRequest;
import com.fleetmanagement.deliverypoint.rest.dto.DeliveryPointResponse;
import com.fleetmanagement.rest.BaseIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

class DeliveryPointControllerIntegrationTest extends BaseIntegrationTest {
    ParameterizedTypeReference<ApiResponse<DeliveryPointResponse>> deliveryPointTypeReference =
            new ParameterizedTypeReference<>() {
            };

    @Test
    void should_create_delivery_point() {
        // given
        DeliveryPointCreateRequest request = new DeliveryPointCreateRequest();
        request.setName("DELIVERY_POINT");
        request.setValue(DeliveryPointType.BRANCH.getValue());

        // when
        ResponseEntity<ApiResponse<DeliveryPointResponse>> response =
                testRestTemplate.exchange(
                        "/api/delivery-points",
                        HttpMethod.POST,
                        new HttpEntity<>(request),
                        deliveryPointTypeReference);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().data())
                .isNotNull()
                .returns("DELIVERY_POINT", DeliveryPointResponse::getName)
                .returns(DeliveryPointType.BRANCH.getValue(), DeliveryPointResponse::getValue);
    }

    @Test
    void should_return_error_when_delivery_point_exists() {
        // given
        DeliveryPointCreateRequest request = new DeliveryPointCreateRequest();
        request.setName("DELIVERY_POINT");
        request.setValue(DeliveryPointType.BRANCH.getValue());
        mongoTemplate.save(DeliveryPointDocument.builder().name("DELIVERY_POINT").value(1).build());

        // when
        ResponseEntity<ApiResponse<ErrorResponse>> errorResponse =
                testRestTemplate.exchange(
                        "/api/delivery-points", HttpMethod.POST, new HttpEntity<>(request), errorTypeReference);

        // then
        assertThat(errorResponse.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(errorResponse.getBody()).isNotNull();
        assertThat(errorResponse.getBody().data())
                .isNotNull()
                .returns("99", ErrorResponse::errorCode)
                .returns("Delivery Point already exists.", ErrorResponse::errorDescription);
    }
}
