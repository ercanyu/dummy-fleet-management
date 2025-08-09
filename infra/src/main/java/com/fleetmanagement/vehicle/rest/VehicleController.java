package com.fleetmanagement.vehicle.rest;

import com.fleetmanagement.common.rest.ApiResponse;
import com.fleetmanagement.common.rest.BaseController;
import com.fleetmanagement.common.usecase.UseCaseHandler;
import com.fleetmanagement.vehicle.model.Vehicle;
import com.fleetmanagement.vehicle.rest.dto.VehicleCreateRequest;
import com.fleetmanagement.vehicle.rest.dto.VehicleResponse;
import com.fleetmanagement.vehicle.usecase.command.VehicleCreate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/vehicles")
public class VehicleController extends BaseController {
    private final UseCaseHandler<Vehicle, VehicleCreate> createHandler;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<VehicleResponse> create(@Valid @RequestBody VehicleCreateRequest request) {
        Vehicle vehicle = createHandler.handle(request.toUseCase());
        log.info("Vehicle {} is created for request {}", vehicle, request);
        return respond(VehicleResponse.fromModel(vehicle));
    }
}
