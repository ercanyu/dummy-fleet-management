package com.fleetmanagement.rest.vehicle;

import com.fleetmanagement.common.rest.ApiResponse;
import com.fleetmanagement.common.rest.ErrorResponse;
import com.fleetmanagement.rest.BaseIntegrationTest;
import com.fleetmanagement.vehicle.mongo.document.VehicleDocument;
import com.fleetmanagement.vehicle.rest.dto.VehicleCreateRequest;
import com.fleetmanagement.vehicle.rest.dto.VehicleResponse;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

class VehicleControllerIntegrationTest extends BaseIntegrationTest {
    ParameterizedTypeReference<ApiResponse<VehicleResponse>> vehicleTypeReference =
            new ParameterizedTypeReference<>() {
            };

    @Test
    void should_create_vehicle() {
        // given
        VehicleCreateRequest vehicleCreateRequest = new VehicleCreateRequest();
        vehicleCreateRequest.setLicensePlate("34 EY 34");

        // when
        ResponseEntity<ApiResponse<VehicleResponse>> response =
                testRestTemplate.exchange(
                        "/api/vehicles",
                        HttpMethod.POST,
                        new HttpEntity<>(vehicleCreateRequest),
                        vehicleTypeReference);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().data())
                .isNotNull()
                .returns("34 EY 34", VehicleResponse::getLicensePlate);
    }

    @Test
    void should_return_error_when_vehicle_exists() {
        // given
        VehicleCreateRequest vehicleCreateRequest = new VehicleCreateRequest();
        vehicleCreateRequest.setLicensePlate("34 EY 34");
        mongoTemplate.save(VehicleDocument.builder().licensePlate("34 EY 34").build());

        // when
        ResponseEntity<ApiResponse<ErrorResponse>> errorResponse =
                testRestTemplate.exchange(
                        "/api/vehicles",
                        HttpMethod.POST,
                        new HttpEntity<>(vehicleCreateRequest),
                        errorTypeReference);

        // then
        assertThat(errorResponse.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(errorResponse.getBody()).isNotNull();
        assertThat(errorResponse.getBody().data())
                .isNotNull()
                .returns("99", ErrorResponse::errorCode)
                .returns("Vehicle already exists.", ErrorResponse::errorDescription);
    }
}
