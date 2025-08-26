package com.fleetmanagement.rest.delivery;

import com.fleetmanagement.common.rest.ApiResponse;
import com.fleetmanagement.common.rest.ErrorResponse;
import com.fleetmanagement.delivery.rest.dto.DeliveryRequest;
import com.fleetmanagement.delivery.rest.dto.DeliveryResponse;
import com.fleetmanagement.delivery.rest.dto.DeliveryShipmentResponse;
import com.fleetmanagement.deliverypoint.model.DeliveryPointType;
import com.fleetmanagement.rest.shipment.BaseShipmentIntegrationTest;
import com.fleetmanagement.shipment.packet.model.PackageStatus;
import com.fleetmanagement.vehicle.mongo.document.VehicleDocument;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

public class DeliveryControllerIntegrationTest extends BaseShipmentIntegrationTest {
    ParameterizedTypeReference<DeliveryResponse> deliveryPointTypeReference =
            new ParameterizedTypeReference<>() {
            };

    @Test
    void should_make_delivery() {
        // given
        mongoTemplate.save(VehicleDocument.builder().licensePlate("34 EY 34").build());
        savePackage("PACKAGE_BARCODE1", DeliveryPointType.BRANCH.getValue());
        savePackage("PACKAGE_BARCODE2", DeliveryPointType.BRANCH.getValue());

        DeliveryRequest request = new DeliveryRequest();
        request.setPlate("34 EY 34");
        request.setRoute(
                List.of(
                        DeliveryStepBuilder.from(
                                DeliveryPointType.BRANCH.getValue(),
                                List.of("PACKAGE_BARCODE1", "PACKAGE_BARCODE2"))));

        // when
        ResponseEntity<DeliveryResponse> response =
                testRestTemplate.exchange(
                        "/api/deliveries",
                        HttpMethod.POST,
                        new HttpEntity<>(request),
                        deliveryPointTypeReference);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).returns("34 EY 34", DeliveryResponse::getPlate);
        assertThat(response.getBody().getRoute())
                .extracting("deliveryPoint", "deliveries")
                .containsExactlyInAnyOrder(
                        tuple(
                                DeliveryPointType.BRANCH.getValue(),
                                List.of(
                                        new DeliveryShipmentResponse(
                                                "PACKAGE_BARCODE1", PackageStatus.UNLOADED.getValue()),
                                        new DeliveryShipmentResponse(
                                                "PACKAGE_BARCODE2", PackageStatus.UNLOADED.getValue()))));
    }

    @Test
    void should_return_error_when_vehicle_not_exists() {
        // given
        DeliveryRequest request = new DeliveryRequest();
        request.setPlate("NOT_EXISTING_PLATE");
        request.setRoute(
                List.of(
                        DeliveryStepBuilder.from(
                                DeliveryPointType.BRANCH.getValue(),
                                List.of("PACKAGE_BARCODE1", "PACKAGE_BARCODE2"))));

        // when
        ResponseEntity<ApiResponse<ErrorResponse>> errorResponse =
                testRestTemplate.exchange(
                        "/api/deliveries", HttpMethod.PUT, new HttpEntity<>(request), errorTypeReference);

        // then
        assertThat(errorResponse.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(errorResponse.getBody()).isNotNull();
        assertThat(errorResponse.getBody().data())
                .isNotNull()
                .returns("99", ErrorResponse::errorCode)
                .returns("Vehicle doesn't exist.", ErrorResponse::errorDescription);
    }
}
