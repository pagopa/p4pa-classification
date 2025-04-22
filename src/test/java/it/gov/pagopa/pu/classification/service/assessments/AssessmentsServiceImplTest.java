package it.gov.pagopa.pu.classification.service.assessments;

import it.gov.pagopa.pu.classification.connector.debtposition.DebtPositionTypeOrgService;
import it.gov.pagopa.pu.classification.connector.debtposition.InstallmentNoPIIService;
import it.gov.pagopa.pu.classification.connector.processexecutions.IngestionFlowFileService;
import it.gov.pagopa.pu.classification.enums.AssessmentStatus;
import it.gov.pagopa.pu.classification.model.Assessments;
import it.gov.pagopa.pu.classification.repository.AssessmentsRepository;
import it.gov.pagopa.pu.classification.util.faker.InstallmentNoPIIResponseFaker;
import it.gov.pagopa.pu.debtposition.dto.generated.DebtPositionTypeOrg;
import it.gov.pagopa.pu.debtposition.dto.generated.InstallmentNoPIIResponse;
import it.gov.pagopa.pu.p4paprocessexecutions.dto.generated.IngestionFlowFile;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AssessmentsServiceImplTest {
  @Mock
  private InstallmentNoPIIService installmentNoPIIServiceMock;
  @Mock
  private IngestionFlowFileService ingestionFlowFileServiceMock;
  @Mock
  private DebtPositionTypeOrgService debtPositionTypeOrgServiceMock;
  @Mock
  private AssessmentsRepository assessmentsRepositoryMock;
  @Mock
  private AssessmentsDetailService assessmentsDetailServiceMock;

  private AssessmentsServiceImpl service;

  @BeforeEach
  void init() {
    service = new AssessmentsServiceImpl(installmentNoPIIServiceMock, ingestionFlowFileServiceMock, debtPositionTypeOrgServiceMock, assessmentsRepositoryMock,assessmentsDetailServiceMock);
  }

  @AfterEach
  void verifyNoMoreInteractions() {
    Mockito.verifyNoMoreInteractions(installmentNoPIIServiceMock);
  }

  @Test
  void buildAssessment_withValidInstallmentNoPIIResponse_returnsAssessment() {
    String accessToken = "accessToken";
    InstallmentNoPIIResponse installmentNoPIIResponse = InstallmentNoPIIResponseFaker.buildInstallmentNoPIIResponse();
    IngestionFlowFile ingestionFlowFile = new IngestionFlowFile();
    ingestionFlowFile.setOrganizationId(1L);
    ingestionFlowFile.setFileName("testFile");
    DebtPositionTypeOrg debtPositionTypeOrg = new DebtPositionTypeOrg();
    debtPositionTypeOrg.setCode("testCode");
    debtPositionTypeOrg.setDebtPositionTypeOrgId(2L);

    when(ingestionFlowFileServiceMock.getIngestionFlowFile(installmentNoPIIResponse.getIngestionFlowFileId(), accessToken))
      .thenReturn(ingestionFlowFile);
    when(debtPositionTypeOrgServiceMock.getDebtPositionTypeOrgByInstallmentId(installmentNoPIIResponse.getInstallmentId(), accessToken))
      .thenReturn(debtPositionTypeOrg);

    Assessments result = service.buildAssessment(installmentNoPIIResponse, accessToken);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(debtPositionTypeOrg.getOrganizationId(), result.getOrganizationId());
    Assertions.assertEquals(debtPositionTypeOrg.getCode(), result.getDebtPositionTypeOrgCode());
    Assertions.assertEquals(AssessmentStatus.NEW, result.getStatus());
    Assertions.assertEquals(ingestionFlowFile.getFileName() + "_" + debtPositionTypeOrg.getCode(), result.getAssessmentName());
  }
  @Test
  void buildAssessmentNoIngestionFlowFileId_withValidInstallmentNoPIIResponse_returnsAssessment() {
    String accessToken = "accessToken";
    InstallmentNoPIIResponse installmentNoPIIResponse = InstallmentNoPIIResponseFaker.buildInstallmentNoPIIResponse();
    installmentNoPIIResponse.setIngestionFlowFileId(null);
    DebtPositionTypeOrg debtPositionTypeOrg = new DebtPositionTypeOrg();
    debtPositionTypeOrg.setCode("testCode");
    debtPositionTypeOrg.setDebtPositionTypeOrgId(2L);

    when(debtPositionTypeOrgServiceMock.getDebtPositionTypeOrgByInstallmentId(installmentNoPIIResponse.getInstallmentId(), accessToken))
      .thenReturn(debtPositionTypeOrg);

    Assessments result = service.buildAssessment(installmentNoPIIResponse, accessToken);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(debtPositionTypeOrg.getOrganizationId(), result.getOrganizationId());
    Assertions.assertEquals(debtPositionTypeOrg.getCode(), result.getDebtPositionTypeOrgCode());
    Assertions.assertEquals(AssessmentStatus.NEW, result.getStatus());
    Assertions.assertEquals("ACC" + LocalDate.now().format(AssessmentsServiceImpl.DATE_TIME_FORMATTER) + "_" + debtPositionTypeOrg.getCode(), result.getAssessmentName());
  }

  @Test
  void createAssessment_withValidReceiptId_returnsAssessments() {
    Long receiptId = 1L;
    String accessToken = "accessToken";
    List<InstallmentNoPIIResponse> installments = List.of(InstallmentNoPIIResponseFaker.buildInstallmentNoPIIResponse());
    Assessments assessment = new Assessments();
    IngestionFlowFile ingestionFlowFile = new IngestionFlowFile();
    ingestionFlowFile.setOrganizationId(1L);
    ingestionFlowFile.setFileName("testFile");
    DebtPositionTypeOrg debtPositionTypeOrg = new DebtPositionTypeOrg();
    debtPositionTypeOrg.setCode("testCode");
    debtPositionTypeOrg.setOrganizationId(3L);

    when(installmentNoPIIServiceMock.getByReceiptId(receiptId, accessToken)).thenReturn(installments);
    when(ingestionFlowFileServiceMock.getIngestionFlowFile(installments.getFirst().getIngestionFlowFileId(), accessToken))
            .thenReturn(ingestionFlowFile);
    when(debtPositionTypeOrgServiceMock.getDebtPositionTypeOrgByInstallmentId(installments.getFirst().getInstallmentId(), accessToken)).thenReturn(debtPositionTypeOrg);
    when(assessmentsRepositoryMock.findByOrganizationIdAndDebtPositionTypeOrgCodeAndAssessmentName(
            debtPositionTypeOrg.getOrganizationId(), debtPositionTypeOrg.getCode(), "testFile_testCode"))
            .thenReturn(null);
    when(assessmentsRepositoryMock.save(Mockito.any(Assessments.class))).thenReturn(assessment);

    List<Assessments> result = service.createAssesment(receiptId, accessToken);

    assertEquals(1, result.size());
    assertEquals(assessment, result.getFirst());
  }


  @Test
  void createAssessment_withNullBalance_skipsAssessmentCreation() {
    Long receiptId = 1L;
    String accessToken = "accessToken";
    InstallmentNoPIIResponse installment = InstallmentNoPIIResponseFaker.buildInstallmentNoPIIResponse();
    installment.setBalance(null);

    when(installmentNoPIIServiceMock.getByReceiptId(receiptId, accessToken)).thenReturn(List.of(installment));

    List<Assessments> result = service.createAssesment(receiptId, accessToken);

    assertEquals(0, result.size());
    Mockito.verify(installmentNoPIIServiceMock).getByReceiptId(receiptId, accessToken);
  }


}
