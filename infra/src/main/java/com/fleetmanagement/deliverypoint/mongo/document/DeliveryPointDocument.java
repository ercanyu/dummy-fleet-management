package com.fleetmanagement.deliverypoint.mongo.document;

import com.fleetmanagement.deliverypoint.model.DeliveryPoint;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("delivery_points")
@Data
@Builder
public class DeliveryPointDocument {
  @Id private String id;
  @Version private Long version;
  private String name;
  private Integer value;

  public DeliveryPoint toModel() {
    return DeliveryPoint.builder().id(id).name(name).value(value).build();
  }
}
