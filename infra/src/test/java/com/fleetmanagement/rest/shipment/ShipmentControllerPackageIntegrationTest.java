package com.fleetmanagement.rest.shipment;

import com.fleetmanagement.common.rest.ApiResponse;
import com.fleetmanagement.common.rest.DataResponse;
import com.fleetmanagement.common.rest.ErrorResponse;
import com.fleetmanagement.deliverypoint.model.DeliveryPointType;
import com.fleetmanagement.deliverypoint.mongo.document.DeliveryPointDocument;
import com.fleetmanagement.shipment.packet.model.PackageStatus;
import com.fleetmanagement.shipment.rest.dto.PackageCreateRequest;
import com.fleetmanagement.shipment.rest.dto.PackageLoadToBagRequest;
import com.fleetmanagement.shipment.rest.dto.PackageResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

@Slf4j
class ShipmentControllerPackageIntegrationTest extends BaseShipmentIntegrationTest {
    ParameterizedTypeReference<ApiResponse<PackageResponse>> packageResponseTypeReference =
            new ParameterizedTypeReference<>() {
            };

    ParameterizedTypeReference<ApiResponse<DataResponse<PackageResponse>>>
            packageDataResponseTypeReference = new ParameterizedTypeReference<>() {
    };

    @Test
    void should_create_package() {
        // given
        mongoTemplate.save(DeliveryPointDocument.builder().name("DELIVERY_POINT").value(1).build());
        PackageCreateRequest request = new PackageCreateRequest();
        request.setBarcode("PACKAGE_BARCODE");
        request.setDeliveryPoint(DeliveryPointType.BRANCH.getValue());
        request.setVolumetricWeight(22);

        // when
        ResponseEntity<ApiResponse<PackageResponse>> response =
                testRestTemplate.exchange(
                        "/api/shipments/packages",
                        HttpMethod.POST,
                        new HttpEntity<>(request),
                        packageResponseTypeReference);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().data())
                .isNotNull()
                .returns(DeliveryPointType.BRANCH.getValue(), PackageResponse::getDeliveryPoint)
                .returns("PACKAGE_BARCODE", PackageResponse::getBarcode)
                .returns(PackageStatus.CREATED, PackageResponse::getPackageStatus)
                .returns(22, PackageResponse::getVolumetricWeight);
    }

    @Test
    void should_return_error_when_delivery_point_not_exists() {
        // given
        PackageCreateRequest request = new PackageCreateRequest();
        request.setBarcode("PACKAGE_BARCODE");
        request.setDeliveryPoint(DeliveryPointType.BRANCH.getValue());
        request.setVolumetricWeight(22);

        // when
        ResponseEntity<ApiResponse<ErrorResponse>> errorResponse =
                testRestTemplate.exchange(
                        "/api/shipments/packages",
                        HttpMethod.POST,
                        new HttpEntity<>(request),
                        errorTypeReference);

        // then
        assertThat(errorResponse.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(errorResponse.getBody()).isNotNull();
        assertThat(errorResponse.getBody().data())
                .isNotNull()
                .returns("99", ErrorResponse::errorCode)
                .returns("Delivery Point doesn't exist.", ErrorResponse::errorDescription);
    }

    @Test
    void should_load_packages_to_bag() {
        // given
        saveBag("BAG_BARCODE", DeliveryPointType.DISTRIBUTION_CENTER.getValue());
        savePackage("PACKAGE_BARCODE1", DeliveryPointType.DISTRIBUTION_CENTER.getValue());
        savePackage("PACKAGE_BARCODE2", DeliveryPointType.DISTRIBUTION_CENTER.getValue());

        PackageLoadToBagRequest request = new PackageLoadToBagRequest();
        request.setBagBarcode("BAG_BARCODE");
        request.setPackageBarcodes(List.of("PACKAGE_BARCODE1", "PACKAGE_BARCODE2"));

        // when
        ResponseEntity<ApiResponse<DataResponse<PackageResponse>>> response =
                testRestTemplate.exchange(
                        "/api/shipments/packages",
                        HttpMethod.PUT,
                        new HttpEntity<>(request),
                        packageDataResponseTypeReference);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().data()).isNotNull();
        assertThat(response.getBody().data().itemList())
                .isNotNull()
                .hasSize(2)
                .extracting("barcode", "deliveryPoint", "packageStatus")
                .containsExactlyInAnyOrder(
                        tuple(
                                "PACKAGE_BARCODE1",
                                DeliveryPointType.DISTRIBUTION_CENTER.getValue(),
                                PackageStatus.LOADED_INTO_BAG),
                        tuple(
                                "PACKAGE_BARCODE2",
                                DeliveryPointType.DISTRIBUTION_CENTER.getValue(),
                                PackageStatus.LOADED_INTO_BAG));
    }

    @Test
    void should_return_error_when_bag_not_exists() {
        // given
        PackageLoadToBagRequest request = new PackageLoadToBagRequest();
        request.setBagBarcode("BAG_BARCODE");
        request.setPackageBarcodes(List.of("PACKAGE_BARCODE1", "PACKAGE_BARCODE2"));

        // when
        ResponseEntity<ApiResponse<ErrorResponse>> errorResponse =
                testRestTemplate.exchange(
                        "/api/shipments/packages",
                        HttpMethod.PUT,
                        new HttpEntity<>(request),
                        errorTypeReference);

        // then
        assertThat(errorResponse.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(errorResponse.getBody()).isNotNull();
        assertThat(errorResponse.getBody().data())
                .isNotNull()
                .returns("99", ErrorResponse::errorCode)
                .returns("Bag doesn't exist.", ErrorResponse::errorDescription);
    }
}
