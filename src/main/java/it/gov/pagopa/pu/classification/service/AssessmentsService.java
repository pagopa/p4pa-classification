package it.gov.pagopa.pu.classification.service;

import it.gov.pagopa.pu.classification.model.Assessments;
import it.gov.pagopa.pu.debtposition.dto.generated.InstallmentNoPIIResponse;

import java.util.List;

public interface AssessmentsService {
  List<InstallmentNoPIIResponse> getInstallmentsByReceiptId(Long receiptId, String accessToken);
  List<Assessments> createAssesment(Long receiptId, String accessToken);
}
