package com.fleetmanagement.deliverypoint.rest.dto;

import com.fleetmanagement.deliverypoint.usecase.command.DeliveryPointCreate;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class DeliveryPointCreateRequest {
    @NotBlank
    private String name;

    @NotNull
    @Min(1)
    @Max(3)
    private Integer value;

    public DeliveryPointCreate toUseCase() {
        return new DeliveryPointCreate(name, value);
    }
}
