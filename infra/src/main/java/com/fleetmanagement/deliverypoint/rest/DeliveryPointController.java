package com.fleetmanagement.deliverypoint.rest;

import com.fleetmanagement.common.rest.ApiResponse;
import com.fleetmanagement.common.rest.BaseController;
import com.fleetmanagement.common.usecase.UseCaseHandler;
import com.fleetmanagement.deliverypoint.model.DeliveryPoint;
import com.fleetmanagement.deliverypoint.rest.dto.DeliveryPointCreateRequest;
import com.fleetmanagement.deliverypoint.rest.dto.DeliveryPointResponse;
import com.fleetmanagement.deliverypoint.usecase.command.DeliveryPointCreate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/delivery-points")
public class DeliveryPointController extends BaseController {
  private final UseCaseHandler<DeliveryPoint, DeliveryPointCreate> createHandler;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public ApiResponse<DeliveryPointResponse> create(
      @Valid @RequestBody DeliveryPointCreateRequest request) {
    DeliveryPoint deliveryPoint = createHandler.handle(request.toUseCase());
    log.info("DeliveryPoint {} is created for request {}", deliveryPoint, request);
    return respond(DeliveryPointResponse.fromModel(deliveryPoint));
  }
}
