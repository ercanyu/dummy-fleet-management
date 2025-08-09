package com.fleetmanagement.delivery;

import com.fleetmanagement.common.DomainComponent;
import com.fleetmanagement.common.exception.ExceptionMessage;
import com.fleetmanagement.common.exception.FleetManagementApiException;
import com.fleetmanagement.common.usecase.UseCaseHandler;
import com.fleetmanagement.delivery.model.DeliveryResult;
import com.fleetmanagement.delivery.model.DeliveryRoute;
import com.fleetmanagement.delivery.usecase.command.MakeDelivery;
import com.fleetmanagement.shipment.model.Shipment;
import com.fleetmanagement.shipment.model.ShipmentUnloadResult;
import com.fleetmanagement.shipment.port.ShipmentDataPort;
import com.fleetmanagement.shipment.strategy.ShipmentStrategyRegistry;
import com.fleetmanagement.vehicle.port.VehicleDataPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@DomainComponent
@RequiredArgsConstructor
public class MakeDeliveryUseCaseHandler implements UseCaseHandler<DeliveryResult, MakeDelivery> {
    private final VehicleDataPort vehicleDataPort;
    private final ShipmentDataPort shipmentDataPort;
    private final ShipmentStrategyRegistry shipmentStrategyRegistry;

    @Override
    public DeliveryResult handle(MakeDelivery useCase) {
        log.info("Loading will start for the vehicle: {}", useCase.getLicensePlate());
        checkVehicle(useCase.getLicensePlate());
        load(useCase.getDeliveryRoutes());
        List<ShipmentUnloadResult> unloadResults = unload(useCase.getDeliveryRoutes());

        return DeliveryResult.of(useCase.getLicensePlate(), useCase.getDeliveryRoutes(), unloadResults);
    }

    private void load(List<DeliveryRoute> deliveryRoutes) {
        log.debug("Starting load process for deliveries: {}", deliveryRoutes);
        deliveryRoutes.forEach(this::handleShipmentLoading);
    }

    private void checkVehicle(String licensePlate) {
        vehicleDataPort
                .retrieveByLicensePlate(licensePlate)
                .orElseThrow(() -> new FleetManagementApiException(ExceptionMessage.VEHICLE_NOT_FOUND));
        log.info("License Plate {} is registered to the system, delivery can continue.", licensePlate);
    }

    private void handleShipmentLoading(DeliveryRoute deliveryRoute) {
        retrieveShipmentsByBarcode(deliveryRoute.getShipmentBarcodes()).forEach(this::loadShipment);
    }

    private void loadShipment(Shipment shipment) {
        shipmentStrategyRegistry.getStrategy(shipment.getShipmentType()).loadShipment(shipment);
    }

    private List<ShipmentUnloadResult> unload(List<DeliveryRoute> deliveryRoutes) {
        log.debug("Starting unload process for deliveries: {}", deliveryRoutes);
        return deliveryRoutes.stream().map(this::handleShipmentUnLoading).toList().stream()
                .flatMap(List::stream)
                .toList();
    }

    private List<ShipmentUnloadResult> handleShipmentUnLoading(DeliveryRoute deliveryRoute) {
        Integer deliveryPoint = deliveryRoute.getDeliveryPoint();
        List<Shipment> shipments = retrieveShipmentsByBarcode(deliveryRoute.getShipmentBarcodes());
        return shipments.stream().map(shipment -> unloadShipment(shipment, deliveryPoint)).toList();
    }

    private ShipmentUnloadResult unloadShipment(Shipment shipment, Integer deliveryPoint) {
        return shipmentStrategyRegistry
                .getStrategy(shipment.getShipmentType())
                .unloadShipment(shipment, deliveryPoint);
    }

    private List<Shipment> retrieveShipmentsByBarcode(List<String> barcodes) {
        return shipmentDataPort.retrieveByBarcodes(barcodes);
    }
}
