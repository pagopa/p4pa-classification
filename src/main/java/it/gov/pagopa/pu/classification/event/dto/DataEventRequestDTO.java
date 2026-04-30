package it.gov.pagopa.pu.classification.event.dto;

import it.gov.pagopa.pu.classification.enums.DataEventType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Data
public class DataEventRequestDTO implements Serializable {
  private DataEventType dataEventType;
  private String eventDescription;
}
