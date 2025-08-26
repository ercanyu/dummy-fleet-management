package com.fleetmanagement.delivery.rest;

import com.fleetmanagement.common.rest.BaseController;
import com.fleetmanagement.common.usecase.UseCaseHandler;
import com.fleetmanagement.delivery.model.DeliveryResult;
import com.fleetmanagement.delivery.rest.dto.DeliveryRequest;
import com.fleetmanagement.delivery.rest.dto.DeliveryResponse;
import com.fleetmanagement.delivery.usecase.command.MakeDelivery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/deliveries")
public class DeliveryController extends BaseController {
    private final UseCaseHandler<DeliveryResult, MakeDelivery> makeDeliveryUseCaseHandler;

    @PostMapping
    public DeliveryResponse deliver(@RequestBody DeliveryRequest request) {
        DeliveryResult deliveryResult = makeDeliveryUseCaseHandler.handle(request.toUseCase());
        log.info("DeliveryResult {} is created for request {}", deliveryResult, request);
        return DeliveryResponse.fromModel(deliveryResult);
    }
}
