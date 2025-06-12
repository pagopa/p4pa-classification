package it.gov.pagopa.pu.classification.service.assessments;

import it.gov.pagopa.pu.classification.connector.debtposition.DebtPositionTypeOrgService;
import it.gov.pagopa.pu.classification.connector.debtposition.InstallmentService;
import it.gov.pagopa.pu.classification.connector.debtposition.ReceiptService;
import it.gov.pagopa.pu.classification.connector.processexecutions.IngestionFlowFileService;
import it.gov.pagopa.pu.classification.dto.LocalDateTimeIntervalFilter;
import it.gov.pagopa.pu.classification.dto.generated.PagedAssessmentsView;
import it.gov.pagopa.pu.classification.enums.AssessmentStatus;
import it.gov.pagopa.pu.classification.mapper.PagedAssessmentsViewMapper;
import it.gov.pagopa.pu.classification.model.Assessments;
import it.gov.pagopa.pu.classification.repository.AssessmentsRepository;
import it.gov.pagopa.pu.classification.util.TestUtils;
import it.gov.pagopa.pu.classification.util.faker.InstallmentNoPIIFaker;
import it.gov.pagopa.pu.debtposition.dto.generated.DebtPositionTypeOrg;
import it.gov.pagopa.pu.debtposition.dto.generated.InstallmentNoPII;
import it.gov.pagopa.pu.debtposition.dto.generated.ReceiptNoPII;
import it.gov.pagopa.pu.p4paprocessexecutions.dto.generated.IngestionFlowFile;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AssessmentsServiceImplTest {
  @Mock
  private InstallmentService installmentServiceMock;
  @Mock
  private ReceiptService receiptServiceMock;
  @Mock
  private IngestionFlowFileService ingestionFlowFileServiceMock;
  @Mock
  private DebtPositionTypeOrgService debtPositionTypeOrgServiceMock;
  @Mock
  private AssessmentsRepository assessmentsRepositoryMock;
  @Mock
  private AssessmentsDetailService assessmentsDetailServiceMock;
  @Mock
  private PagedAssessmentsViewMapper pagedAssessmentsViewMapperMock;

  private AssessmentsServiceImpl service;
  private PodamFactory podamFactory;

  @BeforeEach
  void init() {
    service = new AssessmentsServiceImpl(
      installmentServiceMock,
      receiptServiceMock,
      ingestionFlowFileServiceMock,
      debtPositionTypeOrgServiceMock,
      assessmentsRepositoryMock,
      assessmentsDetailServiceMock,
      pagedAssessmentsViewMapperMock);
    podamFactory = new PodamFactoryImpl();
  }

  @AfterEach
  void verifyNoMoreInteractions() {
    Mockito.verifyNoMoreInteractions(
      installmentServiceMock,
      receiptServiceMock,
      ingestionFlowFileServiceMock,
      debtPositionTypeOrgServiceMock,
      assessmentsRepositoryMock,
      assessmentsDetailServiceMock,
      pagedAssessmentsViewMapperMock);
  }

  @Test
  void buildAssessment_withValidInstallmentNoPII_returnsAssessment() {
    String accessToken = "accessToken";
    InstallmentNoPII installment = InstallmentNoPIIFaker.buildInstallmentNoPII();
    IngestionFlowFile ingestionFlowFile = TestUtils.getPodamFactory().manufacturePojo(IngestionFlowFile.class);
    ingestionFlowFile.setOrganizationId(1L);
    ingestionFlowFile.setFileName("testFile");
    DebtPositionTypeOrg debtPositionTypeOrg = TestUtils.getPodamFactory().manufacturePojo(DebtPositionTypeOrg.class);
    debtPositionTypeOrg.setCode("testCode");
    debtPositionTypeOrg.setDebtPositionTypeOrgId(2L);

    when(ingestionFlowFileServiceMock.getIngestionFlowFile(installment.getIngestionFlowFileId(), accessToken))
      .thenReturn(ingestionFlowFile);
    when(debtPositionTypeOrgServiceMock.getDebtPositionTypeOrgByInstallmentId(installment.getInstallmentId(), accessToken))
      .thenReturn(debtPositionTypeOrg);

    Assessments result = service.buildAssessment(installment, accessToken);

    assertNotNull(result);
    Assertions.assertEquals(debtPositionTypeOrg.getOrganizationId(), result.getOrganizationId());
    Assertions.assertEquals(debtPositionTypeOrg.getCode(), result.getDebtPositionTypeOrgCode());
    Assertions.assertEquals(AssessmentStatus.NEW, result.getStatus());
    Assertions.assertEquals(ingestionFlowFile.getFileName() + "_" + debtPositionTypeOrg.getCode(), result.getAssessmentName());

    TestUtils.checkNotNullFields(result, "assessmentId","creationDate","updateDate","updateOperatorExternalId","updateTraceId");
  }

  @Test
  void buildAssessmentNoIngestionFlowFileId_withValidInstallmentNoPII_returnsAssessment() {
    String accessToken = "accessToken";
    InstallmentNoPII installmentNoPII = InstallmentNoPIIFaker.buildInstallmentNoPII();
    installmentNoPII.setIngestionFlowFileId(null);
    DebtPositionTypeOrg debtPositionTypeOrg = TestUtils.getPodamFactory().manufacturePojo(DebtPositionTypeOrg.class);
    debtPositionTypeOrg.setCode("testCode");
    debtPositionTypeOrg.setDebtPositionTypeOrgId(2L);

    when(debtPositionTypeOrgServiceMock.getDebtPositionTypeOrgByInstallmentId(installmentNoPII.getInstallmentId(), accessToken))
      .thenReturn(debtPositionTypeOrg);

    Assessments result = service.buildAssessment(installmentNoPII, accessToken);

    assertNotNull(result);
    Assertions.assertEquals(debtPositionTypeOrg.getOrganizationId(), result.getOrganizationId());
    Assertions.assertEquals(debtPositionTypeOrg.getCode(), result.getDebtPositionTypeOrgCode());
    Assertions.assertEquals(AssessmentStatus.NEW, result.getStatus());
    Assertions.assertEquals("ACC" + LocalDate.now().format(AssessmentsServiceImpl.DATE_TIME_FORMATTER) + "_" + debtPositionTypeOrg.getCode(), result.getAssessmentName());

    TestUtils.checkNotNullFields(result, "assessmentId","creationDate","updateDate","updateOperatorExternalId","updateTraceId");
  }

  @Test
  void createAssessment_withValidReceiptId_returnsAssessments() {
    Long receiptId = 1L;
    String accessToken = "accessToken";
    List<InstallmentNoPII> installments = List.of(InstallmentNoPIIFaker.buildInstallmentNoPII());
    ReceiptNoPII receipt = new ReceiptNoPII();
    Assessments assessment = new Assessments();
    IngestionFlowFile ingestionFlowFile = new IngestionFlowFile();
    ingestionFlowFile.setOrganizationId(1L);
    ingestionFlowFile.setFileName("testFile");
    DebtPositionTypeOrg debtPositionTypeOrg = new DebtPositionTypeOrg();
    debtPositionTypeOrg.setCode("testCode");
    debtPositionTypeOrg.setOrganizationId(3L);

    when(receiptServiceMock.getById(receiptId, accessToken)).thenReturn(receipt);
    when(installmentServiceMock.getByReceiptId(receiptId, accessToken)).thenReturn(installments);
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

    Mockito.verify(assessmentsDetailServiceMock).createAssessmentDetail(Mockito.same(assessment), Mockito.same(receipt), Mockito.same(installments.getFirst()));
  }


  @Test
  void createAssessment_withNullBalance_skipsAssessmentCreation() {
    Long receiptId = 1L;
    String accessToken = "accessToken";
    ReceiptNoPII receipt = new ReceiptNoPII();
    InstallmentNoPII installment = InstallmentNoPIIFaker.buildInstallmentNoPII();
    installment.setBalance(null);

    when(receiptServiceMock.getById(receiptId, accessToken)).thenReturn(receipt);
    when(installmentServiceMock.getByReceiptId(receiptId, accessToken)).thenReturn(List.of(installment));

    List<Assessments> result = service.createAssesment(receiptId, accessToken);

    assertEquals(0, result.size());
  }


  @Test
  void givenParamsWhenGetPagedAssessmentsViewThenReturnPagedAssessmentsView() {
    //given
    String assessmentName = "ASSESSMENT_NAME";
    OffsetDateTime from = OffsetDateTime.now();
    OffsetDateTime to = OffsetDateTime.now().plusDays(1L);
    String iuv = "IUV";
    String debtPositionTypeOrg = "DEBT_POSITION_TYPE_ORG";
    String accessToken = "accessToken";
    LocalDateTimeIntervalFilter localDateTimeIntervalFilter = new LocalDateTimeIntervalFilter(from.toLocalDateTime(), to.toLocalDateTime());

    List<Assessments> content = List.of(
      podamFactory.manufacturePojo(Assessments.class));
    Pageable pageable = PageRequest.of(0, 10);
    Page<Assessments> pagedAssessments = new PageImpl<>(
      content, pageable, 1);

    PagedAssessmentsView expected = podamFactory.manufacturePojo(PagedAssessmentsView.class);

    Mockito.when(assessmentsRepositoryMock.findPagedAssessments(assessmentName, localDateTimeIntervalFilter, iuv, debtPositionTypeOrg, AssessmentStatus.NEW, Pageable.ofSize(1))).thenReturn(pagedAssessments);
    Mockito.when(pagedAssessmentsViewMapperMock.map(pagedAssessments)).thenReturn(expected);
    //when
    PagedAssessmentsView result = service.getPagedAssessmentsView(assessmentName, localDateTimeIntervalFilter, iuv, debtPositionTypeOrg, AssessmentStatus.NEW, Pageable.ofSize(1), accessToken);
    //then
    assertNotNull(result);
    assertEquals(expected, result);
  }
}
