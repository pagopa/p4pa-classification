package it.gov.pagopa.pu.classification.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import it.gov.pagopa.pu.classification.config.json.LocalDateTimeToOffsetDateTimeSerializer;
import it.gov.pagopa.pu.classification.config.json.OffsetDateTimeToLocalDateTimeDeserializer;
import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@SuperBuilder(toBuilder = true)
@MappedSuperclass
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity implements Serializable {
  @Column(updatable = false)
  @CreatedDate
  @JsonDeserialize(using = OffsetDateTimeToLocalDateTimeDeserializer.class)
  @JsonSerialize(using = LocalDateTimeToOffsetDateTimeSerializer.class)
  private LocalDateTime creationDate;
  @LastModifiedDate
  @JsonDeserialize(using = OffsetDateTimeToLocalDateTimeDeserializer.class)
  @JsonSerialize(using = LocalDateTimeToOffsetDateTimeSerializer.class)
  private LocalDateTime updateDate;
  @LastModifiedBy
  private String updateOperatorExternalId;
}
