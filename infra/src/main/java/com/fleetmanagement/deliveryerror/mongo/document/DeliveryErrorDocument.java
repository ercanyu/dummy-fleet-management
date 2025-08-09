package com.fleetmanagement.deliveryerror.mongo.document;

import com.fleetmanagement.deliveryerror.model.DeliveryError;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document("delivery_errors")
@Builder
@Data
public class DeliveryErrorDocument {
    @Id
    private String id;
    private String shipmentBarcode;
    private String message;
    private LocalDateTime errorDate;

    public DeliveryError toModel() {
        return DeliveryError.builder()
                .id(id)
                .shipmentBarcode(shipmentBarcode)
                .message(message)
                .errorDate(errorDate)
                .build();
    }
}
