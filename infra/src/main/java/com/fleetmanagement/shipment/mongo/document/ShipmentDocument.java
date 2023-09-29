package com.fleetmanagement.shipment.mongo.document;

import com.fleetmanagement.shipment.model.ShipmentType;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("shipments")
@Data
@Builder
public class ShipmentDocument {
  @Id private String id;
  @Version private Long version;
  private String barcode;
  private Integer deliveryPoint;
  private Integer status;
  private Integer volumetricWeight;
  private String bagId;
  private ShipmentType shipmentType;
}
