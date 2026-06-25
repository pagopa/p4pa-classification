package it.gov.pagopa.pu.classification.service.assessments;

import it.gov.pagopa.pu.classification.connector.debtposition.DebtPositionTypeOrgBalanceCostService;
import it.gov.pagopa.pu.classification.connector.debtposition.InstallmentService;
import it.gov.pagopa.pu.classification.connector.debtposition.ReceiptService;
import it.gov.pagopa.pu.classification.connector.debtposition.TransferService;
import it.gov.pagopa.pu.classification.dto.generated.CreateAssessmentsDetail;
import it.gov.pagopa.pu.classification.exception.custom.InvalidRequestBodyException;
import it.gov.pagopa.pu.classification.exception.custom.NotFoundException;
import it.gov.pagopa.pu.classification.model.Assessments;
import it.gov.pagopa.pu.classification.model.AssessmentsDetail;
import it.gov.pagopa.pu.classification.model.AssessmentsRegistry;
import it.gov.pagopa.pu.classification.repository.AssessmentsDetailRepository;
import it.gov.pagopa.pu.classification.repository.AssessmentsRegistryRepository;
import it.gov.pagopa.pu.classification.repository.AssessmentsRepository;
import it.gov.pagopa.pu.classification.service.BalanceMarshallingService;
import it.gov.pagopa.pu.classification.util.Constants;
import it.gov.pagopa.pu.classification.util.TestUtils;
import it.gov.pagopa.pu.debtposition.dto.generated.*;
import it.veneto.regione.schemas._2012.pagamenti.ente.CtAccertamento;
import it.veneto.regione.schemas._2012.pagamenti.ente.CtBilancio;
import it.veneto.regione.schemas._2012.pagamenti.ente.CtCapitolo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.co.jemos.podam.api.PodamFactory;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AssessmentsDetailServiceImplTest {

  @Mock
  private AssessmentsDetailRepository assessmentsDetailRepositoryMock;
  @Mock
  private BalanceMarshallingService balanceMarshallingServiceMock;
  @Mock
  private AssessmentsRepository assessmentsRepositoryMock;
  @Mock
  private AssessmentsRegistryRepository assessmentsRegistryRepositoryMock;
  @Mock
  private InstallmentService installmentServiceMock;
  @Mock
  private ReceiptService receiptServiceMock;
  @Mock
  private TransferService transferServiceMock;
  @Mock
  private DebtPositionTypeOrgBalanceCostService debtPositionTypeOrgBalanceCostServiceMock;

  private static final PodamFactory podamFactory = TestUtils.getPodamFactory();

  private AssessmentsDetailServiceImpl assessmentsDetailService;

  private static final String BALANCE =
    "<bilancio xmlns=\"http://www.regione.veneto.it/schemas/2012/Pagamenti/Ente/\">>" +
      "<capitolo>" +
      "<codCapitolo>CAP1</codCapitolo>" +
      "<codUfficio>UFF1</codUfficio>" +
      "<accertamento>" +
      "<codAccertamento>ACC1</codAccertamento>" +
      "<importo>100.00</importo>" +
      "</accertamento>" +
      "</capitolo>" +
      "</bilancio>";

  @BeforeEach
  void init() {
    assessmentsDetailService = new AssessmentsDetailServiceImpl(assessmentsDetailRepositoryMock, balanceMarshallingServiceMock,
            assessmentsRepositoryMock, assessmentsRegistryRepositoryMock, installmentServiceMock, receiptServiceMock, transferServiceMock, debtPositionTypeOrgBalanceCostServiceMock);
  }

  @AfterEach
  void verifyNoMoreInteractions(){
    Mockito.verifyNoMoreInteractions(assessmentsDetailRepositoryMock, balanceMarshallingServiceMock,
            assessmentsRepositoryMock, assessmentsRegistryRepositoryMock, installmentServiceMock, receiptServiceMock,
      transferServiceMock, debtPositionTypeOrgBalanceCostServiceMock);
  }

  @Test
  void buildAssessmentDetail_returnsAssessmentDetails() {
    InstallmentNoPII installment = TestUtils.getPodamFactory().manufacturePojo(InstallmentNoPII.class);
    installment.setBalance(BALANCE);
    ReceiptNoPII receipt = new ReceiptNoPII();
    OffsetDateTime offsetDateTime = OffsetDateTime.of(2026, 6, 24, 12, 0, 0, 0, ZoneOffset.UTC);
    receipt.setPaymentDateTime(offsetDateTime);
    receipt.setReceiptId(9999L);
    receipt.setCreationDate(offsetDateTime);
    receipt.setTransferDate(offsetDateTime);
    Assessments assessment = new Assessments();
    assessment.setAssessmentId(1L);
    assessment.setOrganizationId(1L);
    assessment.setDebtPositionTypeOrgCode("DPTC");
    assessment.setDebtPositionTypeOrgId(1L);

    CtBilancio bilancio = new CtBilancio();
    CtCapitolo capitolo = new CtCapitolo();
    capitolo.setCodCapitolo("CAP1");
    capitolo.setCodUfficio("UFF1");
    CtAccertamento accertamento = new CtAccertamento();
    accertamento.setCodAccertamento("ACC1");
    accertamento.setImporto(new BigDecimal("100.00"));
    capitolo.getAccertamento().add(accertamento);
    bilancio.getCapitolo().add(capitolo);

    AssessmentsRegistry assessmentsRegistry = new AssessmentsRegistry();
    assessmentsRegistry.setAssessmentDescription("assessmentDescription");
    assessmentsRegistry.setOfficeDescription("officeDescription");
    assessmentsRegistry.setSectionDescription("sectionDescription");

    when(assessmentsRegistryRepositoryMock.findByOrganizationIdAndCodes(assessment.getOrganizationId(), assessment.getDebtPositionTypeOrgCode(), capitolo.getCodCapitolo(), capitolo.getCodUfficio(), accertamento.getCodAccertamento(), String.valueOf(offsetDateTime.getYear()))).thenReturn(Optional.of(assessmentsRegistry));
    when(balanceMarshallingServiceMock.unmarshal(BALANCE,null)).thenReturn(bilancio);

    List<AssessmentsDetail> result = assessmentsDetailService.buildAssessmentDetail(receipt, installment, assessment);

    assertNotNull(result);
    assertEquals(1, result.size());
    AssessmentsDetail detail = result.getFirst();
    assertEquals(1L, detail.getAssessmentId());
    assertEquals(1L, detail.getOrganizationId());
    assertEquals("DPTC", detail.getDebtPositionTypeOrgCode());
    assertEquals("CAP1", detail.getSectionCode());
    assertEquals("UFF1", detail.getOfficeCode());
    assertEquals("ACC1", detail.getAssessmentCode());
    assertEquals(10000L, detail.getAmountCents());
    assertEquals(9999L, detail.getReceiptId());
    assertEquals(1L, detail.getDebtPositionTypeOrgId());
    assertSame(installment.getIur(), detail.getIur());
    assertSame(installment.getDebtorFiscalCodeHash(), detail.getDebtorFiscalCodeHash());
    assertSame(receipt.getPaymentDateTime(), detail.getPaymentDateTime());

    TestUtils.checkNotNullFields(detail, "assessmentDetailId","creationDate","updateDate","updateOperatorExternalId","updateTraceId", "classificationLabel", "dateReceipt", "dateReporting", "dateTreasury");
  }

  @Test
  void buildAssessmentDetail_returnsEmptyListOnEmptyCapitolo() {
    InstallmentNoPII installment = new InstallmentNoPII();
    installment.setBalance(BALANCE);
    ReceiptNoPII receipt = new ReceiptNoPII();
    Assessments assessment = new Assessments();

    CtBilancio bilancio = new CtBilancio();

    when(balanceMarshallingServiceMock.unmarshal(BALANCE,null)).thenReturn(bilancio);

    List<AssessmentsDetail> result = assessmentsDetailService.buildAssessmentDetail(receipt, installment, assessment);

    assertNotNull(result);
    assertTrue(result.isEmpty());
  }

  @Test
  void createAssessmentDetail_savesNewDetails() {
    InstallmentNoPII installment = new InstallmentNoPII();
    installment.setBalance(BALANCE);
    installment.setIuv("IUV");
    installment.setIud("IUD");
    ReceiptNoPII receipt = new ReceiptNoPII();
    Assessments assessment = new Assessments();
    assessment.setAssessmentId(1L);
    assessment.setOrganizationId(1L);
    assessment.setDebtPositionTypeOrgCode("DPTC");

    CtBilancio bilancio = new CtBilancio();
    CtCapitolo capitolo = new CtCapitolo();
    capitolo.setCodCapitolo("CAP1");
    capitolo.setCodUfficio("UFF1");
    CtAccertamento accertamento = new CtAccertamento();
    accertamento.setCodAccertamento("ACC1");
    accertamento.setImporto(new BigDecimal("100.00"));
    capitolo.getAccertamento().add(accertamento);
    bilancio.getCapitolo().add(capitolo);

    AssessmentsRegistry assessmentsRegistry = new AssessmentsRegistry();
    assessmentsRegistry.setAssessmentDescription("assessmentDescription");
    assessmentsRegistry.setOfficeDescription("officeDescription");
    assessmentsRegistry.setSectionDescription("sectionDescription");

    OffsetDateTime offsetDateTime = OffsetDateTime.of(2026, 6, 24, 12, 0, 0, 0, ZoneOffset.UTC);
    try(MockedStatic<OffsetDateTime> offsetDateTimeMock = Mockito.mockStatic(OffsetDateTime.class)) {
      offsetDateTimeMock.when(() -> OffsetDateTime.now(Constants.ZONEID)).thenReturn(offsetDateTime);
      when(balanceMarshallingServiceMock.unmarshal(BALANCE,null)).thenReturn(bilancio);
      doReturn(null).when(assessmentsDetailRepositoryMock).findByDebtPositionTypeOrgCodeAndIuvAndIudAndOfficeCodeAndSectionCodeAndAssessmentCode(
        "DPTC", "IUV", "IUD", "UFF1", "CAP1", "ACC1");
      when(assessmentsRegistryRepositoryMock.findByOrganizationIdAndCodes(assessment.getOrganizationId(), assessment.getDebtPositionTypeOrgCode(), "CAP1", "UFF1", "ACC1", String.valueOf(offsetDateTime.getYear()))).thenReturn(Optional.of(assessmentsRegistry));

      assessmentsDetailService.createAssessmentDetail(assessment, receipt, installment);

      verify(assessmentsDetailRepositoryMock, times(1)).save(any(AssessmentsDetail.class));
    }
  }

  @Test
  void createAssessmentDetail_updatesExistingDetails() {
    InstallmentNoPII installment = new InstallmentNoPII();
    installment.setBalance(BALANCE);
    installment.setIuv("IUV");
    installment.setIud("IUD");
    ReceiptNoPII receipt = new ReceiptNoPII();
    Assessments assessment = new Assessments();
    assessment.setAssessmentId(1L);
    assessment.setOrganizationId(1L);
    assessment.setDebtPositionTypeOrgCode("DPTC");

    CtBilancio bilancio = new CtBilancio();
    CtCapitolo capitolo = new CtCapitolo();
    capitolo.setCodCapitolo("CAP1");
    capitolo.setCodUfficio("UFF1");
    CtAccertamento accertamento = new CtAccertamento();
    accertamento.setCodAccertamento("ACC1");
    accertamento.setImporto(new BigDecimal("100.00"));
    capitolo.getAccertamento().add(accertamento);
    bilancio.getCapitolo().add(capitolo);

    AssessmentsDetail existingDetail = AssessmentsDetail.builder()
      .assessmentId(1L)
      .organizationId(1L)
      .debtPositionTypeOrgCode("DPTC")
      .iuv("IUV")
      .iud("IUD")
      .officeCode("UFF1")
      .sectionCode("CAP1")
      .assessmentCode("ACC1")
      .amountCents(5000L)
      .officeDescription("officeDescription")
      .sectionDescription("sectionDescription")
      .assessmentDescription("assessmentDescription")
      .build();

    AssessmentsRegistry assessmentsRegistry = new AssessmentsRegistry();
    assessmentsRegistry.setAssessmentDescription("assessmentDescription");
    assessmentsRegistry.setOfficeDescription("officeDescription");
    assessmentsRegistry.setSectionDescription("sectionDescription");

    OffsetDateTime offsetDateTime = OffsetDateTime.of(2026, 6, 24, 12, 0, 0, 0, ZoneOffset.UTC);
    try(MockedStatic<OffsetDateTime> offsetDateTimeMock = Mockito.mockStatic(OffsetDateTime.class)) {
      offsetDateTimeMock.when(() -> OffsetDateTime.now(Constants.ZONEID)).thenReturn(offsetDateTime);
      when(balanceMarshallingServiceMock.unmarshal(BALANCE,null)).thenReturn(bilancio);
      doReturn(existingDetail).when(assessmentsDetailRepositoryMock).findByDebtPositionTypeOrgCodeAndIuvAndIudAndOfficeCodeAndSectionCodeAndAssessmentCode(
        "DPTC", "IUV", "IUD", "UFF1", "CAP1", "ACC1");
      when(assessmentsRegistryRepositoryMock.findByOrganizationIdAndCodes(assessment.getOrganizationId(), assessment.getDebtPositionTypeOrgCode(), capitolo.getCodCapitolo(), capitolo.getCodUfficio(), accertamento.getCodAccertamento(), String.valueOf(offsetDateTime.getYear()))).thenReturn(Optional.of(assessmentsRegistry));

      assessmentsDetailService.createAssessmentDetail(assessment, receipt, installment);

      verify(assessmentsDetailRepositoryMock, times(1)).save(existingDetail);
      assertEquals(10000L, existingDetail.getAmountCents());
    }
  }

  @Test
  void whenCreateAssessmentsDetailThenOk(){
    Long organizationId = 1L;
    String accessToken = "accessToken";
    Assessments assessments = podamFactory.manufacturePojo(Assessments.class);
    assessments.setOrganizationId(organizationId);
    AssessmentsRegistry assessmentsRegistry = podamFactory.manufacturePojo(AssessmentsRegistry.class);
    assessmentsRegistry.setOrganizationId(organizationId);
    assessmentsRegistry.setAssessmentDescription("assessmentDescription");
    assessmentsRegistry.setSectionDescription("sectionDescription");
    assessmentsRegistry.setOfficeDescription("officeDescription");
    List<InstallmentNoPII> installments = podamFactory.manufacturePojo(List.class,InstallmentNoPII.class);
    installments.forEach(i->i.setNotificationFeeCents(null));
    Map<String,InstallmentNoPII> installmentsMap = installments.stream().collect(Collectors.toMap(InstallmentNoPII::getIud,Function.identity()));
    ReceiptNoPII receipt = podamFactory.manufacturePojo(ReceiptNoPII.class);
    installments.forEach(i->i.setReceiptId(receipt.getReceiptId()));
    List<Transfer> transfers = podamFactory.manufacturePojo(List.class,Transfer.class);
    transfers.forEach(t->t.setOrgFiscalCode(receipt.getOrgFiscalCode()));
    CreateAssessmentsDetail createAssessmentsDetail = new CreateAssessmentsDetail(assessmentsRegistry.getAssessmentRegistryId(), installmentsMap.keySet());

    when(assessmentsRepositoryMock.findById(assessments.getAssessmentId()))
            .thenReturn(Optional.of(assessments));
    when(installmentServiceMock.findByOrganizationIdAndIuds(organizationId, installmentsMap.keySet(), accessToken))
            .thenReturn(installments);
    when(receiptServiceMock.getByReceiptIdAndDebtPositionTypeOrgCode(receipt.getReceiptId(), assessments.getDebtPositionTypeOrgCode(), accessToken))
            .thenReturn(receipt);
    when(transferServiceMock.getByInstallmentId(argThat(i->installments.stream().map(InstallmentNoPII::getInstallmentId).collect(Collectors.toSet()).contains(i)), eq(accessToken)))
            .thenReturn(transfers);
    when(assessmentsRegistryRepositoryMock.findById(assessmentsRegistry.getAssessmentRegistryId()))
            .thenReturn(Optional.of(assessmentsRegistry));
    ArgumentCaptor<AssessmentsDetail> assessmentsDetailArgumentCaptor = ArgumentCaptor.forClass(AssessmentsDetail.class);
    when(assessmentsDetailRepositoryMock.save(assessmentsDetailArgumentCaptor.capture()))
            .thenReturn(new AssessmentsDetail());

    List<AssessmentsDetail> result = assessmentsDetailService.createAssessmentsDetail(organizationId,assessments.getAssessmentId(),
            createAssessmentsDetail, accessToken);

    assertNotNull(result);
    assertEquals(installmentsMap.size(),result.size());
    List<AssessmentsDetail> assessmentsDetails = assessmentsDetailArgumentCaptor.getAllValues();
    for (AssessmentsDetail assessmentsDetail : assessmentsDetails) {
      InstallmentNoPII installment = installmentsMap.get(assessmentsDetail.getIud());
      assertEquals(assessments.getAssessmentId(),assessmentsDetail.getAssessmentId());
      assertEquals(assessments.getOrganizationId(),assessmentsDetail.getOrganizationId());
      assertEquals(assessments.getDebtPositionTypeOrgCode(),assessmentsDetail.getDebtPositionTypeOrgCode());
      assertEquals(installment.getIuv(),assessmentsDetail.getIuv());
      assertEquals(installment.getIud(),assessmentsDetail.getIud());
      assertEquals(installment.getIur(),assessmentsDetail.getIur());
      assertEquals(installment.getDebtorFiscalCodeHash(),assessmentsDetail.getDebtorFiscalCodeHash());
      assertEquals(receipt.getPaymentDateTime(),assessmentsDetail.getPaymentDateTime());
      assertEquals(assessmentsRegistry.getOfficeCode(),assessmentsDetail.getOfficeCode());
      assertEquals(assessmentsRegistry.getSectionCode(),assessmentsDetail.getSectionCode());
      assertEquals(assessmentsRegistry.getAssessmentCode(),assessmentsDetail.getAssessmentCode());
      assertEquals(assessmentsRegistry.getOfficeDescription(), assessmentsDetail.getOfficeDescription());
      assertEquals(assessmentsRegistry.getSectionDescription(), assessmentsDetail.getSectionDescription());
      assertEquals(assessmentsRegistry.getAssessmentDescription(), assessmentsDetail.getAssessmentDescription());
      assertEquals(transfers.stream().map(Transfer::getAmountCents).reduce(0L,Long::sum),assessmentsDetail.getAmountCents());
      assertEquals(receipt.getReceiptId(),assessmentsDetail.getReceiptId());
    }
  }

  @Test
  void givenReceiptOrgFiscalCodeNotMatchingWhenCreateAssessmentsDetailThenAmountCentsZero(){
    Long organizationId = 1L;
    String accessToken = "accessToken";
    Assessments assessments = podamFactory.manufacturePojo(Assessments.class);
    assessments.setOrganizationId(organizationId);
    AssessmentsRegistry assessmentsRegistry = podamFactory.manufacturePojo(AssessmentsRegistry.class);
    assessmentsRegistry.setOrganizationId(organizationId);
    List<InstallmentNoPII> installments = podamFactory.manufacturePojo(List.class,InstallmentNoPII.class);
    installments.forEach(i->i.setNotificationFeeCents(0L));
    Map<String,InstallmentNoPII> installmentsMap = installments.stream().collect(Collectors.toMap(InstallmentNoPII::getIud,Function.identity()));
    ReceiptNoPII receipt = podamFactory.manufacturePojo(ReceiptNoPII.class);
    installments.forEach(i->i.setReceiptId(receipt.getReceiptId()));
    List<Transfer> transfers = podamFactory.manufacturePojo(List.class,Transfer.class);
    transfers.forEach(t->t.setOrgFiscalCode("wrongOrgFiscalCode"));
    CreateAssessmentsDetail createAssessmentsDetail = new CreateAssessmentsDetail(assessmentsRegistry.getAssessmentRegistryId(), installmentsMap.keySet());

    when(assessmentsRepositoryMock.findById(assessments.getAssessmentId()))
            .thenReturn(Optional.of(assessments));
    when(installmentServiceMock.findByOrganizationIdAndIuds(organizationId, installmentsMap.keySet(), accessToken))
            .thenReturn(installments);
    when(receiptServiceMock.getByReceiptIdAndDebtPositionTypeOrgCode(receipt.getReceiptId(), assessments.getDebtPositionTypeOrgCode(), accessToken))
            .thenReturn(receipt);
    when(transferServiceMock.getByInstallmentId(argThat(i->installments.stream().map(InstallmentNoPII::getInstallmentId).collect(Collectors.toSet()).contains(i)), eq(accessToken)))
            .thenReturn(transfers);
    when(assessmentsRegistryRepositoryMock.findById(assessmentsRegistry.getAssessmentRegistryId()))
            .thenReturn(Optional.of(assessmentsRegistry));
    ArgumentCaptor<AssessmentsDetail> assessmentsDetailArgumentCaptor = ArgumentCaptor.forClass(AssessmentsDetail.class);
    when(assessmentsDetailRepositoryMock.save(assessmentsDetailArgumentCaptor.capture()))
            .thenReturn(new AssessmentsDetail());

    List<AssessmentsDetail> result = assessmentsDetailService.createAssessmentsDetail(organizationId,assessments.getAssessmentId(),
            createAssessmentsDetail, accessToken);

    assertNotNull(result);
    assertEquals(installmentsMap.size(),result.size());
    List<AssessmentsDetail> assessmentsDetails = assessmentsDetailArgumentCaptor.getAllValues();
    for (AssessmentsDetail assessmentsDetail : assessmentsDetails) {
      InstallmentNoPII installment = installmentsMap.get(assessmentsDetail.getIud());
      assertEquals(assessments.getAssessmentId(),assessmentsDetail.getAssessmentId());
      assertEquals(assessments.getOrganizationId(),assessmentsDetail.getOrganizationId());
      assertEquals(assessments.getDebtPositionTypeOrgCode(),assessmentsDetail.getDebtPositionTypeOrgCode());
      assertEquals(installment.getIuv(),assessmentsDetail.getIuv());
      assertEquals(installment.getIud(),assessmentsDetail.getIud());
      assertEquals(installment.getIur(),assessmentsDetail.getIur());
      assertEquals(installment.getDebtorFiscalCodeHash(),assessmentsDetail.getDebtorFiscalCodeHash());
      assertEquals(receipt.getPaymentDateTime(),assessmentsDetail.getPaymentDateTime());
      assertEquals(assessmentsRegistry.getOfficeCode(),assessmentsDetail.getOfficeCode());
      assertEquals(assessmentsRegistry.getSectionCode(),assessmentsDetail.getSectionCode());
      assertEquals(assessmentsRegistry.getAssessmentCode(),assessmentsDetail.getAssessmentCode());
      assertEquals(assessmentsRegistry.getOfficeDescription(), assessmentsDetail.getOfficeDescription());
      assertEquals(assessmentsRegistry.getSectionDescription(), assessmentsDetail.getSectionDescription());
      assertEquals(assessmentsRegistry.getAssessmentDescription(), assessmentsDetail.getAssessmentDescription());
      assertEquals(0L,assessmentsDetail.getAmountCents());
      assertEquals(receipt.getReceiptId(),assessmentsDetail.getReceiptId());
    }
  }

  @Test
  void givenNoTransfersWhenCreateAssessmentsDetailThenAssessmentsDetailAmountZero(){
    Long organizationId = 1L;
    String accessToken = "accessToken";
    Assessments assessments = podamFactory.manufacturePojo(Assessments.class);
    assessments.setOrganizationId(organizationId);
    AssessmentsRegistry assessmentsRegistry = podamFactory.manufacturePojo(AssessmentsRegistry.class);
    assessmentsRegistry.setOrganizationId(organizationId);
    List<InstallmentNoPII> installments = podamFactory.manufacturePojo(List.class,InstallmentNoPII.class);
    installments.forEach(i->i.setNotificationFeeCents(0L));
    Map<String,InstallmentNoPII> installmentsMap = installments.stream().collect(Collectors.toMap(InstallmentNoPII::getIud,Function.identity()));
    ReceiptNoPII receipt = podamFactory.manufacturePojo(ReceiptNoPII.class);
    installments.forEach(i->i.setReceiptId(receipt.getReceiptId()));
    CreateAssessmentsDetail createAssessmentsDetail = new CreateAssessmentsDetail(assessmentsRegistry.getAssessmentRegistryId(), installmentsMap.keySet());

    when(assessmentsRepositoryMock.findById(assessments.getAssessmentId()))
            .thenReturn(Optional.of(assessments));
    when(installmentServiceMock.findByOrganizationIdAndIuds(organizationId, installmentsMap.keySet(), accessToken))
            .thenReturn(installments);
    when(receiptServiceMock.getByReceiptIdAndDebtPositionTypeOrgCode(receipt.getReceiptId(), assessments.getDebtPositionTypeOrgCode(), accessToken))
            .thenReturn(receipt);
    when(transferServiceMock.getByInstallmentId(argThat(i->installments.stream().map(InstallmentNoPII::getInstallmentId).collect(Collectors.toSet()).contains(i)), eq(accessToken)))
            .thenReturn(Collections.emptyList());
    when(assessmentsRegistryRepositoryMock.findById(assessmentsRegistry.getAssessmentRegistryId()))
            .thenReturn(Optional.of(assessmentsRegistry));
    ArgumentCaptor<AssessmentsDetail> assessmentsDetailArgumentCaptor = ArgumentCaptor.forClass(AssessmentsDetail.class);
    when(assessmentsDetailRepositoryMock.save(assessmentsDetailArgumentCaptor.capture()))
            .thenReturn(new AssessmentsDetail());

    List<AssessmentsDetail> result = assessmentsDetailService.createAssessmentsDetail(organizationId,assessments.getAssessmentId(),
            createAssessmentsDetail, accessToken);

    assertNotNull(result);
    assertEquals(installmentsMap.size(),result.size());
    List<AssessmentsDetail> assessmentsDetails = assessmentsDetailArgumentCaptor.getAllValues();
    for (AssessmentsDetail assessmentsDetail : assessmentsDetails) {
      InstallmentNoPII installment = installmentsMap.get(assessmentsDetail.getIud());
      assertEquals(assessments.getAssessmentId(),assessmentsDetail.getAssessmentId());
      assertEquals(assessments.getOrganizationId(),assessmentsDetail.getOrganizationId());
      assertEquals(assessments.getDebtPositionTypeOrgCode(),assessmentsDetail.getDebtPositionTypeOrgCode());
      assertEquals(installment.getIuv(),assessmentsDetail.getIuv());
      assertEquals(installment.getIud(),assessmentsDetail.getIud());
      assertEquals(installment.getIur(),assessmentsDetail.getIur());
      assertEquals(installment.getDebtorFiscalCodeHash(),assessmentsDetail.getDebtorFiscalCodeHash());
      assertEquals(receipt.getPaymentDateTime(),assessmentsDetail.getPaymentDateTime());
      assertEquals(assessmentsRegistry.getOfficeCode(),assessmentsDetail.getOfficeCode());
      assertEquals(assessmentsRegistry.getSectionCode(),assessmentsDetail.getSectionCode());
      assertEquals(assessmentsRegistry.getAssessmentCode(),assessmentsDetail.getAssessmentCode());
      assertEquals(assessmentsRegistry.getOfficeDescription(), assessmentsDetail.getOfficeDescription());
      assertEquals(assessmentsRegistry.getSectionDescription(), assessmentsDetail.getSectionDescription());
      assertEquals(assessmentsRegistry.getAssessmentDescription(), assessmentsDetail.getAssessmentDescription());
      assertEquals(0L,assessmentsDetail.getAmountCents());
      assertEquals(receipt.getReceiptId(),assessmentsDetail.getReceiptId());
    }
  }

  @Test
  void givenNoAssessmentsRegistryWhenCreateAssessmentsDetailThenResourceNotFoundException(){
    Long organizationId = 1L;
    Long assessmentsRegistryId = 2L;
    String accessToken = "accessToken";
    Assessments assessments = podamFactory.manufacturePojo(Assessments.class);
    assessments.setOrganizationId(organizationId);
    List<InstallmentNoPII> installments = podamFactory.manufacturePojo(List.class,InstallmentNoPII.class);
    Map<String,InstallmentNoPII> installmentsMap = installments.stream().collect(Collectors.toMap(InstallmentNoPII::getIud,Function.identity()));
    ReceiptNoPII receipt = podamFactory.manufacturePojo(ReceiptNoPII.class);
    installments.forEach(i->i.setReceiptId(receipt.getReceiptId()));
    CreateAssessmentsDetail createAssessmentsDetail = new CreateAssessmentsDetail(assessmentsRegistryId, installmentsMap.keySet());
    Long assessmentId = assessments.getAssessmentId();

    when(assessmentsRepositoryMock.findById(assessmentId))
            .thenReturn(Optional.of(assessments));
    when(installmentServiceMock.findByOrganizationIdAndIuds(organizationId, installmentsMap.keySet(), accessToken))
            .thenReturn(installments);
    when(receiptServiceMock.getByReceiptIdAndDebtPositionTypeOrgCode(receipt.getReceiptId(), assessments.getDebtPositionTypeOrgCode(),accessToken))
            .thenReturn(receipt);
    when(assessmentsRegistryRepositoryMock.findById(assessmentsRegistryId))
            .thenReturn(Optional.empty());

    NotFoundException resultException = assertThrows(NotFoundException.class, () -> assessmentsDetailService.createAssessmentsDetail(organizationId, assessmentId,
      createAssessmentsDetail, accessToken));

    Assertions.assertEquals("ASSESSMENT_REGISTRY_NOT_FOUND",resultException.getCode());
    Assertions.assertEquals("AssessmentRegistry with id 2 not found",resultException.getMessage());
  }

  @Test
  void givenAssessmentsRegistryWithWrongOrganizationIdWhenCreateAssessmentsDetailThenResourceNotFoundException(){
    Long organizationId = 1L;
    String accessToken = "accessToken";
    Assessments assessments = podamFactory.manufacturePojo(Assessments.class);
    assessments.setOrganizationId(organizationId);
    List<InstallmentNoPII> installments = podamFactory.manufacturePojo(List.class,InstallmentNoPII.class);
    installments.forEach(i->i.setNotificationFeeCents(0L));
    Map<String,InstallmentNoPII> installmentsMap = installments.stream().collect(Collectors.toMap(InstallmentNoPII::getIud,Function.identity()));
    ReceiptNoPII receipt = podamFactory.manufacturePojo(ReceiptNoPII.class);
    installments.forEach(i->i.setReceiptId(receipt.getReceiptId()));
    AssessmentsRegistry assessmentsRegistry = podamFactory.manufacturePojo(AssessmentsRegistry.class);
    assessmentsRegistry.setOrganizationId(organizationId+1);
    CreateAssessmentsDetail createAssessmentsDetail = new CreateAssessmentsDetail(assessmentsRegistry.getAssessmentRegistryId(), installmentsMap.keySet());
    Long assessmentId = assessments.getAssessmentId();

    when(assessmentsRepositoryMock.findById(assessmentId))
            .thenReturn(Optional.of(assessments));
    when(installmentServiceMock.findByOrganizationIdAndIuds(organizationId, installmentsMap.keySet(), accessToken))
            .thenReturn(installments);
    when(receiptServiceMock.getByReceiptIdAndDebtPositionTypeOrgCode(receipt.getReceiptId(), assessments.getDebtPositionTypeOrgCode(),accessToken))
            .thenReturn(receipt);
    when(assessmentsRegistryRepositoryMock.findById(assessmentsRegistry.getAssessmentRegistryId()))
            .thenReturn(Optional.of(assessmentsRegistry));

    NotFoundException resultException = assertThrows(NotFoundException.class, () -> assessmentsDetailService.createAssessmentsDetail(organizationId, assessmentId,
      createAssessmentsDetail, accessToken));

    Assertions.assertEquals("ASSESSMENT_REGISTRY_NOT_FOUND",resultException.getCode());
    Assertions.assertEquals("AssessmentRegistry with id "+assessmentsRegistry.getAssessmentRegistryId()+" not found",resultException.getMessage());
  }

  @Test
  void givenInstallmentWithNoReceiptWhenCreateAssessmentsDetailThenInvalidRequestBodyException(){
    Long organizationId = 1L;
    Long assessmentsRegistryId = 2L;
    String accessToken = "accessToken";
    Assessments assessments = podamFactory.manufacturePojo(Assessments.class);
    assessments.setOrganizationId(organizationId);
    InstallmentNoPII installment = podamFactory.manufacturePojo(InstallmentNoPII.class);
    installment.setReceiptId(null);
    Set<String> iudSet = Collections.singleton(installment.getIud());
    CreateAssessmentsDetail createAssessmentsDetail = new CreateAssessmentsDetail(assessmentsRegistryId, iudSet);
    Long assessmentId = assessments.getAssessmentId();

    when(assessmentsRepositoryMock.findById(assessmentId))
            .thenReturn(Optional.of(assessments));
    when(installmentServiceMock.findByOrganizationIdAndIuds(organizationId, iudSet, accessToken))
            .thenReturn(Collections.singletonList(installment));

    assertThrows(InvalidRequestBodyException.class, ()->assessmentsDetailService.createAssessmentsDetail(organizationId, assessmentId,
            createAssessmentsDetail, accessToken));

    verifyNoInteractions(receiptServiceMock,assessmentsRegistryRepositoryMock,assessmentsDetailRepositoryMock,transferServiceMock);
  }

  @Test
  void givenNoReceiptWhenCreateAssessmentsDetailThenResourceNotFoundException(){
    Long organizationId = 1L;
    Long assessmentsRegistryId = 2L;
    Long receiptId = 3L;
    String accessToken = "accessToken";
    Assessments assessments = podamFactory.manufacturePojo(Assessments.class);
    assessments.setOrganizationId(organizationId);
    List<InstallmentNoPII> installments = podamFactory.manufacturePojo(List.class,InstallmentNoPII.class);
    Map<String,InstallmentNoPII> installmentsMap = installments.stream().collect(Collectors.toMap(InstallmentNoPII::getIud,Function.identity()));
    installments.forEach(i->i.setReceiptId(receiptId));
    CreateAssessmentsDetail createAssessmentsDetail = new CreateAssessmentsDetail(assessmentsRegistryId,installmentsMap.keySet());
    Long assessmentId = assessments.getAssessmentId();

    when(assessmentsRepositoryMock.findById(assessmentId))
            .thenReturn(Optional.of(assessments));
    when(installmentServiceMock.findByOrganizationIdAndIuds(organizationId, installmentsMap.keySet(), accessToken))
            .thenReturn(installments);
    when(receiptServiceMock.getByReceiptIdAndDebtPositionTypeOrgCode(receiptId, assessments.getDebtPositionTypeOrgCode(),accessToken))
            .thenReturn(null);


    NotFoundException resultException = assertThrows(NotFoundException.class, () -> assessmentsDetailService.createAssessmentsDetail(organizationId, assessmentId,
      createAssessmentsDetail, accessToken));

    Assertions.assertEquals("RECEIPT_NOT_FOUND",resultException.getCode());
    Assertions.assertEquals("Receipt with id 3 not found",resultException.getMessage());
  }

  @Test
  void givenInvalidIudWhenCreateAssessmentsDetailThenInvalidRequestBodyException(){
    Long organizationId = 1L;
    Long assessmentsRegistryId = 2L;
    Long receiptId = 3L;
    String accessToken = "accessToken";
    Assessments assessments = podamFactory.manufacturePojo(Assessments.class);
    assessments.setOrganizationId(organizationId);
    List<InstallmentNoPII> installments = podamFactory.manufacturePojo(List.class,InstallmentNoPII.class);
    Map<String,InstallmentNoPII> installmentsMap = installments.stream().collect(Collectors.toMap(InstallmentNoPII::getIud,Function.identity()));
    installments.forEach(i->i.setReceiptId(receiptId));
    Set<String> iuds = Stream.concat(installmentsMap.keySet().stream(), Stream.of("iud")).collect(Collectors.toSet());
    CreateAssessmentsDetail createAssessmentsDetail = new CreateAssessmentsDetail(assessmentsRegistryId,
            iuds);
    Long assessmentId = assessments.getAssessmentId();

    when(assessmentsRepositoryMock.findById(assessmentId))
            .thenReturn(Optional.of(assessments));
    when(installmentServiceMock.findByOrganizationIdAndIuds(organizationId, iuds, accessToken))
            .thenReturn(installments);

    assertThrows(InvalidRequestBodyException.class, ()->assessmentsDetailService.createAssessmentsDetail(organizationId, assessmentId,
            createAssessmentsDetail, accessToken));
  }

  @Test
  void givenNoInstallmentsWhenCreateAssessmentsDetailThenInvalidRequestBodyException(){
    Long organizationId = 1L;
    Long assessmentsRegistryId = 2L;
    String accessToken = "accessToken";
    Assessments assessments = podamFactory.manufacturePojo(Assessments.class);
    assessments.setOrganizationId(organizationId);
    Set<String> iuds = Collections.singleton("iud");
    CreateAssessmentsDetail createAssessmentsDetail = new CreateAssessmentsDetail(assessmentsRegistryId, iuds);
    Long assessmentId = assessments.getAssessmentId();

    when(assessmentsRepositoryMock.findById(assessmentId))
            .thenReturn(Optional.of(assessments));
    when(installmentServiceMock.findByOrganizationIdAndIuds(organizationId, iuds, accessToken))
            .thenReturn(Collections.emptyList());

    assertThrows(InvalidRequestBodyException.class, ()->assessmentsDetailService.createAssessmentsDetail(organizationId, assessmentId,
            createAssessmentsDetail, accessToken));
  }

  @Test
  void givenNoInstallmentsAndNoIudsWhenCreateAssessmentsDetailThenEmptyList(){
    Long organizationId = 1L;
    Long assessmentsRegistryId = 2L;
    String accessToken = "accessToken";
    Assessments assessments = podamFactory.manufacturePojo(Assessments.class);
    assessments.setOrganizationId(organizationId);
    CreateAssessmentsDetail createAssessmentsDetail = new CreateAssessmentsDetail(assessmentsRegistryId, Collections.emptySet());
    Long assessmentId = assessments.getAssessmentId();

    when(assessmentsRepositoryMock.findById(assessmentId))
            .thenReturn(Optional.of(assessments));
    when(installmentServiceMock.findByOrganizationIdAndIuds(organizationId, Collections.emptySet(), accessToken))
            .thenReturn(Collections.emptyList());

    List<AssessmentsDetail> result = assessmentsDetailService.createAssessmentsDetail(organizationId, assessmentId,
            createAssessmentsDetail, accessToken);

    assertTrue(result.isEmpty());
  }

  @Test
  void givenAssessmentsWithWrongOrganizationIdWhenCreateAssessmentsDetailThenResourceNotFoundException(){
    long organizationId = 1L;
    long assessmentsRegistryId = 2L;
    String accessToken = "accessToken";
    Assessments assessments = podamFactory.manufacturePojo(Assessments.class);
    assessments.setOrganizationId(organizationId+1);
    CreateAssessmentsDetail createAssessmentsDetail = new CreateAssessmentsDetail(assessmentsRegistryId, Collections.emptySet());
    Long assessmentId = assessments.getAssessmentId();

    when(assessmentsRepositoryMock.findById(assessmentId))
            .thenReturn(Optional.of(assessments));

    NotFoundException resultException = assertThrows(NotFoundException.class, () -> assessmentsDetailService.createAssessmentsDetail(organizationId, assessmentId,
      createAssessmentsDetail, accessToken));

    Assertions.assertEquals("ASSESSMENT_NOT_FOUND",resultException.getCode());
    Assertions.assertEquals("Assessment with id "+assessmentId+" not found",resultException.getMessage());
  }

  @Test
  void givenNoAssessmentsWhenCreateAssessmentsDetailThenResourceNotFoundException(){
    Long organizationId = 1L;
    Long assessmentsRegistryId = 2L;
    Long assessmentId = 3L;
    String accessToken = "accessToken";
    CreateAssessmentsDetail createAssessmentsDetail = new CreateAssessmentsDetail(assessmentsRegistryId, Collections.emptySet());

    when(assessmentsRepositoryMock.findById(assessmentId))
            .thenReturn(Optional.empty());

    NotFoundException resultException = assertThrows(NotFoundException.class, () -> assessmentsDetailService.createAssessmentsDetail(organizationId, assessmentId,
      createAssessmentsDetail, accessToken));

    Assertions.assertEquals("ASSESSMENT_NOT_FOUND",resultException.getCode());
    Assertions.assertEquals("Assessment with id "+assessmentId+" not found",resultException.getMessage());
  }

  @Test
  void givenAssessmentDetailWhenDeleteThenOk() {
    Long organizationId = 1L;
    String iuv = "iuv";
    String iud = "iud";
    assessmentsDetailService.deleteAssessmentDetailsByOrgAndInstallment(organizationId, iuv, iud);
    verify(assessmentsDetailRepositoryMock).deleteAllByOrganizationIdAndIuvAndIud(organizationId, iuv, iud);
  }

  @Test
  void givenNotificationCostAndNoDebtPositionTypeOrgBalanceCostWhenCreateAssessmentsDetailThenOk() {
    Long organizationId = 1L;
    String accessToken = "accessToken";
    Assessments assessments = podamFactory.manufacturePojo(Assessments.class);
    assessments.setOrganizationId(organizationId);
    AssessmentsRegistry assessmentsRegistry = podamFactory.manufacturePojo(AssessmentsRegistry.class);
    assessmentsRegistry.setOrganizationId(organizationId);
    assessmentsRegistry.setOfficeCode("registryOfficeCode");
    InstallmentNoPII installment = podamFactory.manufacturePojo(InstallmentNoPII.class);
    ReceiptNoPII receipt = podamFactory.manufacturePojo(ReceiptNoPII.class);
    installment.setReceiptId(receipt.getReceiptId());
    List<Transfer> transfers = podamFactory.manufacturePojo(List.class, Transfer.class);
    Long transferTotalAmount = 0L;
    for(Transfer t:transfers){
      t.setOrgFiscalCode(receipt.getOrgFiscalCode());
      transferTotalAmount+=t.getAmountCents();
    }
    installment.setAmountCents(transferTotalAmount);
    installment.setNotificationFeeCents(transferTotalAmount / 2);
    CreateAssessmentsDetail createAssessmentsDetail = new CreateAssessmentsDetail(assessmentsRegistry.getAssessmentRegistryId(), Set.of(installment.getIud()));

    when(assessmentsRepositoryMock.findById(assessments.getAssessmentId()))
      .thenReturn(Optional.of(assessments));
    when(installmentServiceMock.findByOrganizationIdAndIuds(organizationId, createAssessmentsDetail.getIuds(), accessToken))
      .thenReturn(List.of(installment));
    when(receiptServiceMock.getByReceiptIdAndDebtPositionTypeOrgCode(receipt.getReceiptId(), assessments.getDebtPositionTypeOrgCode(), accessToken))
      .thenReturn(receipt);
    when(transferServiceMock.getByInstallmentId(installment.getInstallmentId(), accessToken))
      .thenReturn(transfers);
    when(assessmentsRegistryRepositoryMock.findById(assessmentsRegistry.getAssessmentRegistryId()))
      .thenReturn(Optional.of(assessmentsRegistry));
    when(debtPositionTypeOrgBalanceCostServiceMock.getDptoBalanceCostByInstallmentIdAndTypeAndOperatingYear(
      installment.getInstallmentId(),
      DebtPositionTypeOrgBalanceCostType.NOTIFICATION_COST,
      assessmentsRegistry.getOperatingYear(),
      accessToken))
      .thenReturn(null);
    ArgumentCaptor<AssessmentsDetail> assessmentsDetailArgumentCaptor = ArgumentCaptor.forClass(AssessmentsDetail.class);
    when(assessmentsDetailRepositoryMock.save(assessmentsDetailArgumentCaptor.capture()))
      .thenReturn(new AssessmentsDetail());

    List<AssessmentsDetail> result = assessmentsDetailService.createAssessmentsDetail(organizationId, assessments.getAssessmentId(),
      createAssessmentsDetail, accessToken);

    assertNotNull(result);
    assertEquals(2, result.size());
    List<AssessmentsDetail> assessmentsDetails = assessmentsDetailArgumentCaptor.getAllValues();
    for (AssessmentsDetail assessmentsDetail : assessmentsDetails) {
      assertEquals(assessments.getAssessmentId(), assessmentsDetail.getAssessmentId());
      assertEquals(assessments.getOrganizationId(), assessmentsDetail.getOrganizationId());
      assertEquals(assessments.getDebtPositionTypeOrgCode(), assessmentsDetail.getDebtPositionTypeOrgCode());
      assertEquals(installment.getIuv(), assessmentsDetail.getIuv());
      assertEquals(installment.getIud(), assessmentsDetail.getIud());
      assertEquals(installment.getIur(), assessmentsDetail.getIur());
      assertEquals(installment.getDebtorFiscalCodeHash(), assessmentsDetail.getDebtorFiscalCodeHash());
      assertEquals(receipt.getPaymentDateTime(), assessmentsDetail.getPaymentDateTime());
      assertEquals(receipt.getReceiptId(), assessmentsDetail.getReceiptId());
      if(assessmentsRegistry.getOfficeCode().equals(assessmentsDetail.getOfficeCode())){
        assertEquals(assessmentsRegistry.getOfficeCode(),assessmentsDetail.getOfficeCode());
        assertEquals(assessmentsRegistry.getSectionCode(),assessmentsDetail.getSectionCode());
        assertEquals(assessmentsRegistry.getAssessmentCode(),assessmentsDetail.getAssessmentCode());
        assertEquals(assessmentsRegistry.getOfficeDescription(), assessmentsDetail.getOfficeDescription());
        assertEquals(assessmentsRegistry.getSectionDescription(), assessmentsDetail.getSectionDescription());
        assertEquals(assessmentsRegistry.getAssessmentDescription(), assessmentsDetail.getAssessmentDescription());
        assertEquals(transferTotalAmount-installment.getNotificationFeeCents(), assessmentsDetail.getAmountCents());
      }else{
        assertEquals(Constants.DEFAULT_SEND_DPTOBC_CODE, assessmentsDetail.getOfficeCode());
        assertEquals(Constants.DEFAULT_SEND_DPTOBC_CODE, assessmentsDetail.getSectionCode());
        assertEquals(Constants.DEFAULT_SEND_DPTOBC_CODE, assessmentsDetail.getAssessmentCode());
        assertNull(assessmentsDetail.getOfficeDescription());
        assertNull(assessmentsDetail.getSectionDescription());
        assertNull(assessmentsDetail.getAssessmentDescription());
        assertEquals(installment.getNotificationFeeCents(), assessmentsDetail.getAmountCents());
      }
    }
  }

  @Test
  void givenNotificationCostAndDebtPositionTypeOrgBalanceCostWhenCreateAssessmentsDetailThenOk(){
    Long organizationId = 1L;
    String accessToken = "accessToken";
    Assessments assessments = podamFactory.manufacturePojo(Assessments.class);
    assessments.setOrganizationId(organizationId);
    AssessmentsRegistry assessmentsRegistry = podamFactory.manufacturePojo(AssessmentsRegistry.class);
    assessmentsRegistry.setOrganizationId(organizationId);
    assessmentsRegistry.setOfficeCode("registryOfficeCode");
    InstallmentNoPII installment = podamFactory.manufacturePojo(InstallmentNoPII.class);
    ReceiptNoPII receipt = podamFactory.manufacturePojo(ReceiptNoPII.class);
    installment.setReceiptId(receipt.getReceiptId());
    List<Transfer> transfers = podamFactory.manufacturePojo(List.class, Transfer.class);
    Long transferTotalAmount = 0L;
    for(Transfer t:transfers){
      t.setOrgFiscalCode(receipt.getOrgFiscalCode());
      transferTotalAmount+=t.getAmountCents();
    }
    installment.setAmountCents(transferTotalAmount);
    installment.setNotificationFeeCents(transferTotalAmount / 2);
    CreateAssessmentsDetail createAssessmentsDetail = new CreateAssessmentsDetail(assessmentsRegistry.getAssessmentRegistryId(), Set.of(installment.getIud()));
    DebtPositionTypeOrgBalanceCost debtPositionTypeOrgBalanceCost = podamFactory.manufacturePojo(DebtPositionTypeOrgBalanceCost.class);
    debtPositionTypeOrgBalanceCost.setOfficeCode("dptoBalanceCostOfficeCode");

    when(assessmentsRepositoryMock.findById(assessments.getAssessmentId()))
      .thenReturn(Optional.of(assessments));
    when(installmentServiceMock.findByOrganizationIdAndIuds(organizationId, createAssessmentsDetail.getIuds(), accessToken))
      .thenReturn(List.of(installment));
    when(receiptServiceMock.getByReceiptIdAndDebtPositionTypeOrgCode(receipt.getReceiptId(), assessments.getDebtPositionTypeOrgCode(), accessToken))
      .thenReturn(receipt);
    when(transferServiceMock.getByInstallmentId(installment.getInstallmentId(), accessToken))
      .thenReturn(transfers);
    when(assessmentsRegistryRepositoryMock.findById(assessmentsRegistry.getAssessmentRegistryId()))
      .thenReturn(Optional.of(assessmentsRegistry));
    when(debtPositionTypeOrgBalanceCostServiceMock.getDptoBalanceCostByInstallmentIdAndTypeAndOperatingYear(
      installment.getInstallmentId(),
      DebtPositionTypeOrgBalanceCostType.NOTIFICATION_COST,
      assessmentsRegistry.getOperatingYear(),
      accessToken))
      .thenReturn(debtPositionTypeOrgBalanceCost);
    ArgumentCaptor<AssessmentsDetail> assessmentsDetailArgumentCaptor = ArgumentCaptor.forClass(AssessmentsDetail.class);
    when(assessmentsDetailRepositoryMock.save(assessmentsDetailArgumentCaptor.capture()))
      .thenReturn(new AssessmentsDetail());

    List<AssessmentsDetail> result = assessmentsDetailService.createAssessmentsDetail(organizationId, assessments.getAssessmentId(),
      createAssessmentsDetail, accessToken);

    assertNotNull(result);
    assertEquals(2, result.size());
    List<AssessmentsDetail> assessmentsDetails = assessmentsDetailArgumentCaptor.getAllValues();
    for (AssessmentsDetail assessmentsDetail : assessmentsDetails) {
      assertEquals(assessments.getAssessmentId(), assessmentsDetail.getAssessmentId());
      assertEquals(assessments.getOrganizationId(), assessmentsDetail.getOrganizationId());
      assertEquals(assessments.getDebtPositionTypeOrgCode(), assessmentsDetail.getDebtPositionTypeOrgCode());
      assertEquals(installment.getIuv(), assessmentsDetail.getIuv());
      assertEquals(installment.getIud(), assessmentsDetail.getIud());
      assertEquals(installment.getIur(), assessmentsDetail.getIur());
      assertEquals(installment.getDebtorFiscalCodeHash(), assessmentsDetail.getDebtorFiscalCodeHash());
      assertEquals(receipt.getPaymentDateTime(), assessmentsDetail.getPaymentDateTime());
      assertEquals(receipt.getReceiptId(), assessmentsDetail.getReceiptId());
      if(assessmentsRegistry.getOfficeCode().equals(assessmentsDetail.getOfficeCode())){
        assertEquals(assessmentsRegistry.getOfficeCode(),assessmentsDetail.getOfficeCode());
        assertEquals(assessmentsRegistry.getSectionCode(),assessmentsDetail.getSectionCode());
        assertEquals(assessmentsRegistry.getAssessmentCode(),assessmentsDetail.getAssessmentCode());
        assertEquals(assessmentsRegistry.getOfficeDescription(), assessmentsDetail.getOfficeDescription());
        assertEquals(assessmentsRegistry.getSectionDescription(), assessmentsDetail.getSectionDescription());
        assertEquals(assessmentsRegistry.getAssessmentDescription(), assessmentsDetail.getAssessmentDescription());
        assertEquals(transferTotalAmount-installment.getNotificationFeeCents(), assessmentsDetail.getAmountCents());
      }else{
        assertEquals(debtPositionTypeOrgBalanceCost.getOfficeCode(), assessmentsDetail.getOfficeCode());
        assertEquals(debtPositionTypeOrgBalanceCost.getSectionCode(), assessmentsDetail.getSectionCode());
        assertEquals(debtPositionTypeOrgBalanceCost.getAssessmentCode(), assessmentsDetail.getAssessmentCode());
        assertEquals(debtPositionTypeOrgBalanceCost.getOfficeDescription(), assessmentsDetail.getOfficeDescription());
        assertEquals(debtPositionTypeOrgBalanceCost.getSectionDescription(), assessmentsDetail.getSectionDescription());
        assertEquals(debtPositionTypeOrgBalanceCost.getAssessmentDescription(), assessmentsDetail.getAssessmentDescription());
        assertEquals(installment.getNotificationFeeCents(), assessmentsDetail.getAmountCents());
      }
    }
  }

  @Test
  void givenNotificationCostGreaterThanTransferAmountAndDebtPositionTypeOrgBalanceCostWhenCreateAssessmentsDetailThenOk(){
    Long organizationId = 1L;
    String accessToken = "accessToken";
    Assessments assessments = podamFactory.manufacturePojo(Assessments.class);
    assessments.setOrganizationId(organizationId);
    AssessmentsRegistry assessmentsRegistry = podamFactory.manufacturePojo(AssessmentsRegistry.class);
    assessmentsRegistry.setOrganizationId(organizationId);
    assessmentsRegistry.setOfficeCode("registryOfficeCode");
    InstallmentNoPII installment = podamFactory.manufacturePojo(InstallmentNoPII.class);
    ReceiptNoPII receipt = podamFactory.manufacturePojo(ReceiptNoPII.class);
    installment.setReceiptId(receipt.getReceiptId());
    List<Transfer> transfers = podamFactory.manufacturePojo(List.class, Transfer.class);
    Long transferTotalAmount = 0L;
    for(Transfer t:transfers){
      t.setOrgFiscalCode(receipt.getOrgFiscalCode());
      transferTotalAmount+=t.getAmountCents();
    }
    installment.setAmountCents(transferTotalAmount);
    installment.setNotificationFeeCents(transferTotalAmount +1);
    CreateAssessmentsDetail createAssessmentsDetail = new CreateAssessmentsDetail(assessmentsRegistry.getAssessmentRegistryId(), Set.of(installment.getIud()));
    DebtPositionTypeOrgBalanceCost debtPositionTypeOrgBalanceCost = podamFactory.manufacturePojo(DebtPositionTypeOrgBalanceCost.class);
    debtPositionTypeOrgBalanceCost.setOfficeCode("dptoBalanceCostOfficeCode");

    when(assessmentsRepositoryMock.findById(assessments.getAssessmentId()))
      .thenReturn(Optional.of(assessments));
    when(installmentServiceMock.findByOrganizationIdAndIuds(organizationId, createAssessmentsDetail.getIuds(), accessToken))
      .thenReturn(List.of(installment));
    when(receiptServiceMock.getByReceiptIdAndDebtPositionTypeOrgCode(receipt.getReceiptId(), assessments.getDebtPositionTypeOrgCode(), accessToken))
      .thenReturn(receipt);
    when(transferServiceMock.getByInstallmentId(installment.getInstallmentId(), accessToken))
      .thenReturn(transfers);
    when(assessmentsRegistryRepositoryMock.findById(assessmentsRegistry.getAssessmentRegistryId()))
      .thenReturn(Optional.of(assessmentsRegistry));
    when(debtPositionTypeOrgBalanceCostServiceMock.getDptoBalanceCostByInstallmentIdAndTypeAndOperatingYear(
      installment.getInstallmentId(),
      DebtPositionTypeOrgBalanceCostType.NOTIFICATION_COST,
      assessmentsRegistry.getOperatingYear(),
      accessToken))
      .thenReturn(debtPositionTypeOrgBalanceCost);
    ArgumentCaptor<AssessmentsDetail> assessmentsDetailArgumentCaptor = ArgumentCaptor.forClass(AssessmentsDetail.class);
    when(assessmentsDetailRepositoryMock.save(assessmentsDetailArgumentCaptor.capture()))
      .thenReturn(new AssessmentsDetail());

    List<AssessmentsDetail> result = assessmentsDetailService.createAssessmentsDetail(organizationId, assessments.getAssessmentId(),
      createAssessmentsDetail, accessToken);

    assertNotNull(result);
    assertEquals(2, result.size());
    List<AssessmentsDetail> assessmentsDetails = assessmentsDetailArgumentCaptor.getAllValues();
    for (AssessmentsDetail assessmentsDetail : assessmentsDetails) {
      assertEquals(assessments.getAssessmentId(), assessmentsDetail.getAssessmentId());
      assertEquals(assessments.getOrganizationId(), assessmentsDetail.getOrganizationId());
      assertEquals(assessments.getDebtPositionTypeOrgCode(), assessmentsDetail.getDebtPositionTypeOrgCode());
      assertEquals(installment.getIuv(), assessmentsDetail.getIuv());
      assertEquals(installment.getIud(), assessmentsDetail.getIud());
      assertEquals(installment.getIur(), assessmentsDetail.getIur());
      assertEquals(installment.getDebtorFiscalCodeHash(), assessmentsDetail.getDebtorFiscalCodeHash());
      assertEquals(receipt.getPaymentDateTime(), assessmentsDetail.getPaymentDateTime());
      assertEquals(receipt.getReceiptId(), assessmentsDetail.getReceiptId());
      if(assessmentsRegistry.getOfficeCode().equals(assessmentsDetail.getOfficeCode())){
        assertEquals(assessmentsRegistry.getOfficeCode(),assessmentsDetail.getOfficeCode());
        assertEquals(assessmentsRegistry.getSectionCode(),assessmentsDetail.getSectionCode());
        assertEquals(assessmentsRegistry.getAssessmentCode(),assessmentsDetail.getAssessmentCode());
        assertEquals(assessmentsRegistry.getOfficeDescription(), assessmentsDetail.getOfficeDescription());
        assertEquals(assessmentsRegistry.getSectionDescription(), assessmentsDetail.getSectionDescription());
        assertEquals(assessmentsRegistry.getAssessmentDescription(), assessmentsDetail.getAssessmentDescription());
        assertEquals(0L, assessmentsDetail.getAmountCents());
      }else{
        assertEquals(debtPositionTypeOrgBalanceCost.getOfficeCode(), assessmentsDetail.getOfficeCode());
        assertEquals(debtPositionTypeOrgBalanceCost.getSectionCode(), assessmentsDetail.getSectionCode());
        assertEquals(debtPositionTypeOrgBalanceCost.getAssessmentCode(), assessmentsDetail.getAssessmentCode());
        assertEquals(debtPositionTypeOrgBalanceCost.getOfficeDescription(), assessmentsDetail.getOfficeDescription());
        assertEquals(debtPositionTypeOrgBalanceCost.getSectionDescription(), assessmentsDetail.getSectionDescription());
        assertEquals(debtPositionTypeOrgBalanceCost.getAssessmentDescription(), assessmentsDetail.getAssessmentDescription());
        assertEquals(installment.getNotificationFeeCents(), assessmentsDetail.getAmountCents());
      }
    }
  }
}
