package it.gov.pagopa.pu.classification.service.assessments;

import it.gov.pagopa.pu.classification.connector.debtposition.DebtPositionTypeOrgService;
import it.gov.pagopa.pu.classification.connector.debtposition.InstallmentService;
import it.gov.pagopa.pu.classification.connector.debtposition.ReceiptService;
import it.gov.pagopa.pu.classification.connector.organization.service.OrganizationService;
import it.gov.pagopa.pu.classification.dto.LocalDateTimeIntervalFilter;
import it.gov.pagopa.pu.classification.dto.generated.PagedAssessmentsView;
import it.gov.pagopa.pu.classification.enums.AssessmentStatus;
import it.gov.pagopa.pu.classification.exception.custom.AssessmentConflictException;
import it.gov.pagopa.pu.classification.exception.custom.NotFoundException;
import it.gov.pagopa.pu.classification.mapper.PagedAssessmentsViewMapper;
import it.gov.pagopa.pu.classification.model.Assessments;
import it.gov.pagopa.pu.classification.repository.AssessmentsRepository;
import it.gov.pagopa.pu.classification.util.faker.InstallmentNoPIIFaker;
import it.gov.pagopa.pu.debtposition.dto.generated.DebtPositionTypeOrg;
import it.gov.pagopa.pu.debtposition.dto.generated.InstallmentNoPII;
import it.gov.pagopa.pu.debtposition.dto.generated.ReceiptNoPII;
import it.gov.pagopa.pu.organization.dto.generated.Organization;
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

import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AssessmentsServiceImplTest {
  @Mock
  private InstallmentService installmentServiceMock;
  @Mock
  private ReceiptService receiptServiceMock;
  @Mock
  private OrganizationService organizationServiceMock;
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
      organizationServiceMock,
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
      organizationServiceMock,
      debtPositionTypeOrgServiceMock,
      assessmentsRepositoryMock,
      assessmentsDetailServiceMock,
      pagedAssessmentsViewMapperMock);
  }

  @Test
  void createAssessment_withNotExistentOrg_throwNotFoundException() {
    Long receiptId = 1L;
    String accessToken = "accessToken";
    ReceiptNoPII receipt = new ReceiptNoPII();
    receipt.setOrgFiscalCode("ORGFC");

    when(receiptServiceMock.getById(receiptId, accessToken)).thenReturn(receipt);
    when(organizationServiceMock.getOrganizationByFiscalCode(receipt.getOrgFiscalCode(), accessToken))
      .thenReturn(Optional.empty());

    Assertions.assertThrows(NotFoundException.class, () -> service.createAssessment(receiptId, accessToken));
  }

  @Test
  void createAssessment_withValidReceiptId_returnsAssessments() {
    Long receiptId = 1L;
    Long organizationId = 3L;
    String accessToken = "accessToken";
    List<InstallmentNoPII> installments = List.of(InstallmentNoPIIFaker.buildInstallmentNoPII());
    ReceiptNoPII receipt = new ReceiptNoPII();
    receipt.setReceiptId(receiptId);
    receipt.setOrgFiscalCode("ORGFC");
    Organization organization = new Organization();
    organization.setOrganizationId(organizationId);
    Assessments assessment = new Assessments();
    DebtPositionTypeOrg debtPositionTypeOrg = new DebtPositionTypeOrg();
    debtPositionTypeOrg.setCode("testCode");
    debtPositionTypeOrg.setOrganizationId(organizationId);

    when(receiptServiceMock.getById(receiptId, accessToken)).thenReturn(receipt);
    when(organizationServiceMock.getOrganizationByFiscalCode(receipt.getOrgFiscalCode(), accessToken)).thenReturn(Optional.of(organization));
    when(installmentServiceMock.getByReceiptId(organizationId, receiptId, accessToken)).thenReturn(installments);
    when(debtPositionTypeOrgServiceMock.getDebtPositionTypeOrgByInstallmentId(installments.getFirst().getInstallmentId(), accessToken)).thenReturn(debtPositionTypeOrg);
    when(assessmentsRepositoryMock.findByOrganizationIdAndDebtPositionTypeOrgCodeAndAssessmentName(
            debtPositionTypeOrg.getOrganizationId(), debtPositionTypeOrg.getCode(), "sourceFlowName"))
            .thenReturn(null);
    when(assessmentsRepositoryMock.save(Mockito.any(Assessments.class))).thenReturn(assessment);

    List<Assessments> result = service.createAssessment(receiptId, accessToken);

    assertEquals(1, result.size());
    assertEquals(assessment, result.getFirst());

    Mockito.verify(assessmentsDetailServiceMock).createAssessmentDetail(Mockito.same(assessment), Mockito.same(receipt), Mockito.same(installments.getFirst()));
  }

  @Test
  void createAssessment_withValidReceiptId_withExistingAssessment_returnsAssessments() {
    Long receiptId = 1L;
    Long organizationId = 3L;
    String accessToken = "accessToken";
    List<InstallmentNoPII> installments = List.of(InstallmentNoPIIFaker.buildInstallmentNoPII());
    ReceiptNoPII receipt = new ReceiptNoPII();
    receipt.setReceiptId(receiptId);
    receipt.setOrgFiscalCode("ORGFC");
    Organization organization = new Organization();
    organization.setOrganizationId(organizationId);
    Assessments assessment = podamFactory.manufacturePojo(Assessments.class);
    DebtPositionTypeOrg debtPositionTypeOrg = new DebtPositionTypeOrg();
    debtPositionTypeOrg.setCode("testCode");
    debtPositionTypeOrg.setOrganizationId(3L);

    when(receiptServiceMock.getById(receiptId, accessToken)).thenReturn(receipt);
    when(organizationServiceMock.getOrganizationByFiscalCode(receipt.getOrgFiscalCode(), accessToken)).thenReturn(Optional.of(organization));
    when(installmentServiceMock.getByReceiptId(organizationId, receiptId, accessToken)).thenReturn(installments);
    when(debtPositionTypeOrgServiceMock.getDebtPositionTypeOrgByInstallmentId(installments.getFirst().getInstallmentId(), accessToken)).thenReturn(debtPositionTypeOrg);
    when(assessmentsRepositoryMock.findByOrganizationIdAndDebtPositionTypeOrgCodeAndAssessmentName(
      debtPositionTypeOrg.getOrganizationId(), debtPositionTypeOrg.getCode(), "sourceFlowName"))
      .thenReturn(assessment);

    List<Assessments> result = service.createAssessment(receiptId, accessToken);

    assertEquals(1, result.size());
    assertEquals(assessment, result.getFirst());

    Mockito.verify(assessmentsDetailServiceMock).createAssessmentDetail(Mockito.same(assessment), Mockito.same(receipt), Mockito.same(installments.getFirst()));
    Mockito.verify(assessmentsRepositoryMock, times(0)).save(Mockito.any());
  }


  @Test
  void createAssessment_withNullBalance_skipsAssessmentCreation() {
    Long receiptId = 1L;
    Long organizationId = 3L;
    String accessToken = "accessToken";
    ReceiptNoPII receipt = new ReceiptNoPII();
    receipt.setReceiptId(receiptId);
    receipt.setOrgFiscalCode("ORGFC");
    Organization organization = new Organization();
    organization.setOrganizationId(organizationId);
    InstallmentNoPII installment = InstallmentNoPIIFaker.buildInstallmentNoPII();
    installment.setBalance(null);

    when(receiptServiceMock.getById(receiptId, accessToken)).thenReturn(receipt);
    when(organizationServiceMock.getOrganizationByFiscalCode(receipt.getOrgFiscalCode(), accessToken)).thenReturn(Optional.of(organization));
    when(installmentServiceMock.getByReceiptId(organizationId, receiptId, accessToken)).thenReturn(List.of(installment));

    List<Assessments> result = service.createAssessment(receiptId, accessToken);

    assertEquals(0, result.size());
  }


  @Test
  void givenParamsWhenGetPagedAssessmentsViewThenReturnPagedAssessmentsView() {
    //given
    String assessmentName = "ASSESSMENT_NAME";
    OffsetDateTime from = OffsetDateTime.now();
    OffsetDateTime to = OffsetDateTime.now().plusDays(1L);
    String iuv = "IUV";
    List<String> debtPositionTypeOrgCodes = List.of("DEBT_POSITION_TYPE_ORG_CODE", "DEBT_POSITION_TYPE_ORG_CODE1");
    LocalDateTimeIntervalFilter localDateTimeIntervalFilter = new LocalDateTimeIntervalFilter(from.toLocalDateTime(), to.toLocalDateTime());

    List<Assessments> content = List.of(
      podamFactory.manufacturePojo(Assessments.class));
    Pageable pageable = PageRequest.of(0, 10);
    Page<Assessments> pagedAssessments = new PageImpl<>(
      content, pageable, 1);

    PagedAssessmentsView expected = podamFactory.manufacturePojo(PagedAssessmentsView.class);

    Mockito.when(assessmentsRepositoryMock.findPagedAssessments(assessmentName, localDateTimeIntervalFilter, iuv, new HashSet<>(debtPositionTypeOrgCodes), AssessmentStatus.ACTIVE, Pageable.ofSize(1))).thenReturn(pagedAssessments);
    Mockito.when(pagedAssessmentsViewMapperMock.map(pagedAssessments)).thenReturn(expected);
    //when
    PagedAssessmentsView result = service.getPagedAssessmentsView(assessmentName, localDateTimeIntervalFilter, iuv, debtPositionTypeOrgCodes, AssessmentStatus.ACTIVE, Pageable.ofSize(1));
    //then
    assertNotNull(result);
    assertEquals(expected, result);
  }

  @Test
  void givenNullDebtPositionTypeOrgCodesWhenGetPagedAssessmentsViewThenReturnPagedAssessmentsView() {
    // given
    String assessmentName = "ASSESSMENT_NAME";
    OffsetDateTime from = OffsetDateTime.now();
    OffsetDateTime to = OffsetDateTime.now().plusDays(1L);
    String iuv = "IUV";
    List<String> debtPositionTypeOrgCodes = null;
    LocalDateTimeIntervalFilter localDateTimeIntervalFilter = new LocalDateTimeIntervalFilter(from.toLocalDateTime(), to.toLocalDateTime());
    Pageable pageable = PageRequest.of(0, 10);
    List<Assessments> content = List.of(podamFactory.manufacturePojo(Assessments.class));
    Page<Assessments> pagedAssessments = new PageImpl<>(content, pageable, 1);
    PagedAssessmentsView expected = podamFactory.manufacturePojo(PagedAssessmentsView.class);

    Mockito.when(assessmentsRepositoryMock.findPagedAssessments(assessmentName, localDateTimeIntervalFilter, iuv, null, AssessmentStatus.ACTIVE, pageable)).thenReturn(pagedAssessments);
    Mockito.when(pagedAssessmentsViewMapperMock.map(pagedAssessments)).thenReturn(expected);

    // when
    PagedAssessmentsView result = service.getPagedAssessmentsView(assessmentName, localDateTimeIntervalFilter, iuv, debtPositionTypeOrgCodes, AssessmentStatus.ACTIVE, pageable);

    // then
    assertNotNull(result);
    assertEquals(expected, result);
  }

  @Test
  void givenEmptyDebtPositionTypeOrgCodesWhenGetPagedAssessmentsViewThenReturnPagedAssessmentsView() {
    // given
    String assessmentName = "ASSESSMENT_NAME";
    OffsetDateTime from = OffsetDateTime.now();
    OffsetDateTime to = OffsetDateTime.now().plusDays(1L);
    String iuv = "IUV";
    List<String> debtPositionTypeOrgCodes = List.of();
    LocalDateTimeIntervalFilter localDateTimeIntervalFilter = new LocalDateTimeIntervalFilter(from.toLocalDateTime(), to.toLocalDateTime());
    Pageable pageable = PageRequest.of(0, 10);
    List<Assessments> content = List.of(podamFactory.manufacturePojo(Assessments.class));
    Page<Assessments> pagedAssessments = new PageImpl<>(content, pageable, 1);
    PagedAssessmentsView expected = podamFactory.manufacturePojo(PagedAssessmentsView.class);

    Mockito.when(assessmentsRepositoryMock.findPagedAssessments(assessmentName, localDateTimeIntervalFilter, iuv, null, AssessmentStatus.ACTIVE, pageable)).thenReturn(pagedAssessments);
    Mockito.when(pagedAssessmentsViewMapperMock.map(pagedAssessments)).thenReturn(expected);

    // when
    PagedAssessmentsView result = service.getPagedAssessmentsView(assessmentName, localDateTimeIntervalFilter, iuv, debtPositionTypeOrgCodes, AssessmentStatus.ACTIVE, pageable);

    // then
    assertNotNull(result);
    assertEquals(expected, result);
  }

  @Test
  void givenDebtPositionTypeOrgCodesWithBlankValuesWhenGetPagedAssessmentsViewThenReturnPagedAssessmentsView() {
    // given
    String assessmentName = "ASSESSMENT_NAME";
    OffsetDateTime from = OffsetDateTime.now();
    OffsetDateTime to = OffsetDateTime.now().plusDays(1L);
    String iuv = "IUV";
    List<String> debtPositionTypeOrgCodes = List.of("  ", "VALID_CODE", "");
    LocalDateTimeIntervalFilter localDateTimeIntervalFilter = new LocalDateTimeIntervalFilter(from.toLocalDateTime(), to.toLocalDateTime());
    Pageable pageable = PageRequest.of(0, 10);
    List<Assessments> content = List.of(podamFactory.manufacturePojo(Assessments.class));
    Page<Assessments> pagedAssessments = new PageImpl<>(content, pageable, 1);
    PagedAssessmentsView expected = podamFactory.manufacturePojo(PagedAssessmentsView.class);

    Mockito.when(assessmentsRepositoryMock.findPagedAssessments(assessmentName, localDateTimeIntervalFilter, iuv, Set.of("VALID_CODE"), AssessmentStatus.ACTIVE, pageable)).thenReturn(pagedAssessments);
    Mockito.when(pagedAssessmentsViewMapperMock.map(pagedAssessments)).thenReturn(expected);

    // when
    PagedAssessmentsView result = service.getPagedAssessmentsView(assessmentName, localDateTimeIntervalFilter, iuv, debtPositionTypeOrgCodes, AssessmentStatus.ACTIVE, pageable);

    // then
    assertNotNull(result);
    assertEquals(expected, result);
  }

  @Test
  void givenParamsWhenCreateAssessmentThenReturnAssessments() {
    //given
    Long organizationId = 3L;
    String assessmentName = "ASSESSMENT_NAME";
    String debtPositionTypeOrgCode = "CODE";
    Assessments assessments = Assessments.builder()
        .organizationId(organizationId)
          .assessmentName(assessmentName)
            .status(AssessmentStatus.ACTIVE)
              .debtPositionTypeOrgCode(debtPositionTypeOrgCode)
                .flagManualGeneration(true)
                  .printed(false).build();
    Mockito.when(assessmentsRepositoryMock.findByOrganizationIdAndDebtPositionTypeOrgCodeAndAssessmentName(organizationId, debtPositionTypeOrgCode, assessmentName)).thenReturn(null);
    Mockito.when(assessmentsRepositoryMock.save(assessments)).thenReturn(assessments);
    //when

    Assessments result = service.createAssessment(organizationId, assessmentName, debtPositionTypeOrgCode);
    //then
    assertNotNull(result);
    assertEquals(assessments, result);
  }

  @Test
  void givenExistingNameWhenCreateAssessmentThenThrowException() {
    //given
    Long organizationId = 3L;
    String assessmentName = "ASSESSMENT_NAME";
    String debtPositionTypeOrgCode = "CODE";
    Assessments assessments = Assessments.builder()
      .assessmentId(1L)
      .organizationId(organizationId)
      .assessmentName(assessmentName)
      .status(AssessmentStatus.ACTIVE)
      .debtPositionTypeOrgCode(debtPositionTypeOrgCode)
      .flagManualGeneration(true)
      .printed(false).build();
    Mockito.when(assessmentsRepositoryMock.findByOrganizationIdAndDebtPositionTypeOrgCodeAndAssessmentName(organizationId, debtPositionTypeOrgCode, assessmentName)).thenReturn(assessments);
    //when

    AssessmentConflictException ex = assertThrows(AssessmentConflictException.class, () -> service.createAssessment(organizationId, assessmentName, debtPositionTypeOrgCode));
    Assertions.assertEquals("Assessment with the same name ASSESSMENT_NAME and debtPositionTypeOrgCode CODE already exists for the current organizationId 3", ex.getMessage());
  }
}
