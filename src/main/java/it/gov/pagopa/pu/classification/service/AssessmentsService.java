package it.gov.pagopa.pu.classification.service;

import it.gov.pagopa.pu.classification.model.Assessments;

import java.util.List;

public interface AssessmentsService {
  List<Assessments> createAssesment(Long receiptId, String accessToken);
}
