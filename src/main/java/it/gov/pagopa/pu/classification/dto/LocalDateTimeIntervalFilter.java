package it.gov.pagopa.pu.classification.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LocalDateTimeIntervalFilter {
  private LocalDateTime from;
  private LocalDateTime to;
}
