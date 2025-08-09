package com.fleetmanagement.rest.shipment;

import com.fleetmanagement.common.rest.ApiResponse;
import com.fleetmanagement.common.rest.ErrorResponse;
import com.fleetmanagement.deliverypoint.model.DeliveryPointType;
import com.fleetmanagement.deliverypoint.mongo.document.DeliveryPointDocument;
import com.fleetmanagement.shipment.bag.model.BagStatus;
import com.fleetmanagement.shipment.rest.dto.BagCreateRequest;
import com.fleetmanagement.shipment.rest.dto.BagResponse;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

class ShipmentControllerBagIntegrationTest extends BaseShipmentIntegrationTest {
    ParameterizedTypeReference<ApiResponse<BagResponse>> bagTypeReference =
            new ParameterizedTypeReference<>() {
            };

    @Test
    void should_create_bag() {
        // given
        mongoTemplate.save(DeliveryPointDocument.builder().name("DELIVERY_POINT").value(1).build());
        BagCreateRequest request = new BagCreateRequest();
        request.setBarcode("BAG_BARCODE");
        request.setDeliveryPoint(DeliveryPointType.BRANCH.getValue());

        // when
        ResponseEntity<ApiResponse<BagResponse>> response =
                testRestTemplate.exchange(
                        "/api/shipments/bags", HttpMethod.POST, new HttpEntity<>(request), bagTypeReference);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().data())
                .isNotNull()
                .returns(DeliveryPointType.BRANCH.getValue(), BagResponse::getDeliveryPoint)
                .returns("BAG_BARCODE", BagResponse::getBarcode)
                .returns(BagStatus.CREATED, BagResponse::getBagStatus);
    }

    @Test
    void should_return_error_when_delivery_point_not_exists() {
        // given
        BagCreateRequest request = new BagCreateRequest();
        request.setBarcode("BAG_BARCODE");
        request.setDeliveryPoint(DeliveryPointType.BRANCH.getValue());

        // when
        ResponseEntity<ApiResponse<ErrorResponse>> errorResponse =
                testRestTemplate.exchange(
                        "/api/shipments/bags", HttpMethod.POST, new HttpEntity<>(request), errorTypeReference);

        // then
        assertThat(errorResponse.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(errorResponse.getBody()).isNotNull();
        assertThat(errorResponse.getBody().data())
                .isNotNull()
                .returns("99", ErrorResponse::errorCode)
                .returns("Delivery Point doesn't exist.", ErrorResponse::errorDescription);
    }
}
