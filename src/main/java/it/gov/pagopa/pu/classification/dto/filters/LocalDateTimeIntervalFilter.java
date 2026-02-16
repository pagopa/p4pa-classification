package it.gov.pagopa.pu.classification.dto.filters;

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
