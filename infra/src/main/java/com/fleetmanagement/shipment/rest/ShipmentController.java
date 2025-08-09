package com.fleetmanagement.shipment.rest;

import com.fleetmanagement.common.rest.ApiResponse;
import com.fleetmanagement.common.rest.BaseController;
import com.fleetmanagement.common.rest.DataResponse;
import com.fleetmanagement.common.usecase.UseCaseHandler;
import com.fleetmanagement.shipment.bag.model.Bag;
import com.fleetmanagement.shipment.bag.usecase.command.BagCreate;
import com.fleetmanagement.shipment.packet.model.Package;
import com.fleetmanagement.shipment.packet.usecase.command.PackageCreate;
import com.fleetmanagement.shipment.packet.usecase.command.PackageLoadToBag;
import com.fleetmanagement.shipment.rest.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/shipments")
public class ShipmentController extends BaseController {
    private final UseCaseHandler<Bag, BagCreate> bagCreateUseCaseHandler;
    private final UseCaseHandler<Package, PackageCreate> packageCreateUseCaseHandler;
    private final UseCaseHandler<List<Package>, PackageLoadToBag> packageLoadToBagUseCaseHandler;

    @PostMapping("/bags")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<BagResponse> createBag(@Valid @RequestBody BagCreateRequest bagCreateRequest) {
        Bag bag = bagCreateUseCaseHandler.handle(bagCreateRequest.toUseCase());
        log.info("Bag {} is created for request {}", bag, bagCreateRequest);
        return respond(BagResponse.fromModel(bag));
    }

    @PostMapping("/packages")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<PackageResponse> createPackage(
            @Valid @RequestBody PackageCreateRequest packageCreateRequest) {
        Package pkg = packageCreateUseCaseHandler.handle(packageCreateRequest.toUseCase());
        log.info("Package {} is created for request {}", pkg, packageCreateRequest);
        return respond(PackageResponse.fromModel(pkg));
    }

    @PutMapping("/packages")
    public ApiResponse<DataResponse<PackageResponse>> loadPackagesToBag(
            @Valid @RequestBody PackageLoadToBagRequest request) {
        List<Package> packages = packageLoadToBagUseCaseHandler.handle(request.toUseCase());
        log.info("Package(s) {} loaded in to bag for request {}", packages, request);
        return respond(packages.stream().map(PackageResponse::fromModel).toList());
    }
}
