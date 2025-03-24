package it.gov.pagopa.pu.classification.service;

import it.gov.pagopa.pu.classification.model.Assessments;

import java.util.List;

/**
 * Service interface for managing assessments.
 */
public interface AssessmentsService {

  /**
   * Creates assessments based on the given receipt ID and access token.
   *
   * @param receiptId the ID of the receipt
   * @param accessToken the access token for authentication
   * @return a list of created assessments
   */
  List<Assessments> createAssesment(Long receiptId, String accessToken);
}
