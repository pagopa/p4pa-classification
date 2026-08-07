package it.gov.pagopa.pu.classification.config.rest;

import it.gov.pagopa.pu.classification.dto.generated.ErrorFieldDTO;

import java.util.List;

public record PuErrorDTO(
  String category,
  String code,
  String message,
  List<ErrorFieldDTO> fields
) {
}
