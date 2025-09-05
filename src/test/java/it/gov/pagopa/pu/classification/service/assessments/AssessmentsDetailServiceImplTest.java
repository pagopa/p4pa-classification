package it.gov.pagopa.pu.classification.service.assessments;

import it.gov.pagopa.pu.classification.connector.debtposition.InstallmentService;
import it.gov.pagopa.pu.classification.connector.debtposition.ReceiptService;
import it.gov.pagopa.pu.classification.connector.debtposition.TransferService;
import it.gov.pagopa.pu.classification.dto.generated.CreateAssessmentsDetail;
import it.gov.pagopa.pu.classification.event.producer.DataEventsProducerService;
import it.gov.pagopa.pu.classification.exception.custom.InvalidRequestBodyException;
import it.gov.pagopa.pu.classification.model.Assessments;
import it.gov.pagopa.pu.classification.model.AssessmentsDetail;
import it.gov.pagopa.pu.classification.model.AssessmentsRegistry;
import it.gov.pagopa.pu.classification.repository.AssessmentsDetailRepository;
import it.gov.pagopa.pu.classification.repository.AssessmentsRegistryRepository;
import it.gov.pagopa.pu.classification.repository.AssessmentsRepository;
import it.gov.pagopa.pu.classification.service.BalanceUnmarshallerService;
import it.gov.pagopa.pu.classification.util.TestUtils;
import it.gov.pagopa.pu.debtposition.dto.generated.InstallmentNoPII;
import it.gov.pagopa.pu.debtposition.dto.generated.ReceiptNoPII;
import it.gov.pagopa.pu.debtposition.dto.generated.Transfer;
import it.veneto.regione.schemas._2012.pagamenti.ente.CtAccertamento;
import it.veneto.regione.schemas._2012.pagamenti.ente.CtBilancio;
import it.veneto.regione.schemas._2012.pagamenti.ente.CtCapitolo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import uk.co.jemos.podam.api.PodamFactory;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
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
  private BalanceUnmarshallerService balanceUnmashallerServiceMock;
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
  private DataEventsProducerService dataEventsProducerServiceMock;

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
    assessmentsDetailService = new AssessmentsDetailServiceImpl(assessmentsDetailRepositoryMock, balanceUnmashallerServiceMock,
            assessmentsRepositoryMock, assessmentsRegistryRepositoryMock, installmentServiceMock, receiptServiceMock, transferServiceMock, dataEventsProducerServiceMock);
  }

  @AfterEach
  void verifyNoMoreInteractions(){
    Mockito.verifyNoMoreInteractions(assessmentsDetailRepositoryMock, balanceUnmashallerServiceMock,
            assessmentsRepositoryMock, assessmentsRegistryRepositoryMock, installmentServiceMock, receiptServiceMock, transferServiceMock,dataEventsProducerServiceMock);
  }

  @Test
  void buildAssessmentDetail_returnsAssessmentDetails() {
    InstallmentNoPII installment = TestUtils.getPodamFactory().manufacturePojo(InstallmentNoPII.class);
    installment.setBalance(BALANCE);
    ReceiptNoPII receipt = new ReceiptNoPII();
    receipt.setPaymentDateTime(OffsetDateTime.now());
    receipt.setReceiptId(9999L);
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

    when(balanceUnmashallerServiceMock.unmarshal(BALANCE)).thenReturn(bilancio);

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
    assertSame(installment.getIur(), detail.getIur());
    assertSame(installment.getDebtorFiscalCodeHash(), detail.getDebtorFiscalCodeHash());
    assertSame(receipt.getPaymentDateTime(), detail.getPaymentDateTime());

    TestUtils.checkNotNullFields(detail, "assessmentDetailId","creationDate","updateDate","updateOperatorExternalId","updateTraceId");
  }

  @Test
  void buildAssessmentDetail_returnsEmptyListOnEmptyCapitolo() {
    InstallmentNoPII installment = new InstallmentNoPII();
    installment.setBalance(BALANCE);
    ReceiptNoPII receipt = new ReceiptNoPII();
    Assessments assessment = new Assessments();

    CtBilancio bilancio = new CtBilancio();

    when(balanceUnmashallerServiceMock.unmarshal(BALANCE)).thenReturn(bilancio);

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

    when(balanceUnmashallerServiceMock.unmarshal(BALANCE)).thenReturn(bilancio);
    doReturn(null).when(assessmentsDetailRepositoryMock).findByDebtPositionTypeOrgCodeAndIuvAndIudAndOfficeCodeAndSectionCodeAndAssessmentCode(
      "DPTC", "IUV", "IUD", "UFF1", "CAP1", "ACC1");

    assessmentsDetailService.createAssessmentDetail(assessment, receipt, installment);

    verify(assessmentsDetailRepositoryMock, times(1)).save(any(AssessmentsDetail.class));
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
      .build();

    when(balanceUnmashallerServiceMock.unmarshal(BALANCE)).thenReturn(bilancio);
    doReturn(existingDetail).when(assessmentsDetailRepositoryMock).findByDebtPositionTypeOrgCodeAndIuvAndIudAndOfficeCodeAndSectionCodeAndAssessmentCode(
      "DPTC", "IUV", "IUD", "UFF1", "CAP1", "ACC1");

    assessmentsDetailService.createAssessmentDetail(assessment, receipt, installment);

    verify(assessmentsDetailRepositoryMock, times(1)).save(existingDetail);
    assertEquals(10000L, existingDetail.getAmountCents());
  }

  @Test
  void whenCreateAssessmentsDetailThenOk(){
    Long organizationId = 1L;
    String accessToken = "accessToken";
    Assessments assessments = podamFactory.manufacturePojo(Assessments.class);
    assessments.setOrganizationId(organizationId);
    AssessmentsRegistry assessmentsRegistry = podamFactory.manufacturePojo(AssessmentsRegistry.class);
    assessmentsRegistry.setOrganizationId(organizationId);
    List<InstallmentNoPII> installments = podamFactory.manufacturePojo(List.class,InstallmentNoPII.class);
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

    assertThrows(ResourceNotFoundException.class, ()->assessmentsDetailService.createAssessmentsDetail(organizationId, assessmentId,
            createAssessmentsDetail, accessToken));

    verifyNoInteractions(assessmentsDetailRepositoryMock,transferServiceMock);
  }

  @Test
  void givenAssessmentsRegistryWithWrongOrganizationIdWhenCreateAssessmentsDetailThenResourceNotFoundException(){
    Long organizationId = 1L;
    String accessToken = "accessToken";
    Assessments assessments = podamFactory.manufacturePojo(Assessments.class);
    assessments.setOrganizationId(organizationId);
    List<InstallmentNoPII> installments = podamFactory.manufacturePojo(List.class,InstallmentNoPII.class);
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

    assertThrows(ResourceNotFoundException.class, ()->assessmentsDetailService.createAssessmentsDetail(organizationId, assessmentId,
            createAssessmentsDetail, accessToken));

    verifyNoInteractions(assessmentsDetailRepositoryMock,transferServiceMock);
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


    assertThrows(ResourceNotFoundException.class, ()->assessmentsDetailService.createAssessmentsDetail(organizationId, assessmentId,
            createAssessmentsDetail, accessToken));

    verifyNoInteractions(assessmentsRegistryRepositoryMock,assessmentsDetailRepositoryMock,transferServiceMock);
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

    verifyNoInteractions(assessmentsRegistryRepositoryMock,assessmentsDetailRepositoryMock,receiptServiceMock,transferServiceMock);
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

    verifyNoInteractions(assessmentsRegistryRepositoryMock,assessmentsDetailRepositoryMock,receiptServiceMock,transferServiceMock);
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
    verifyNoInteractions(receiptServiceMock,assessmentsRegistryRepositoryMock,assessmentsDetailRepositoryMock,transferServiceMock);
  }

  @Test
  void givenAssessmentsWithWrongOrganizationIdWhenCreateAssessmentsDetailThenResourceNotFoundException(){
    Long organizationId = 1L;
    Long assessmentsRegistryId = 2L;
    String accessToken = "accessToken";
    Assessments assessments = podamFactory.manufacturePojo(Assessments.class);
    assessments.setOrganizationId(organizationId+1);
    CreateAssessmentsDetail createAssessmentsDetail = new CreateAssessmentsDetail(assessmentsRegistryId, Collections.emptySet());
    Long assessmentId = assessments.getAssessmentId();

    when(assessmentsRepositoryMock.findById(assessmentId))
            .thenReturn(Optional.of(assessments));

    assertThrows(ResourceNotFoundException.class,()->assessmentsDetailService.createAssessmentsDetail(organizationId, assessmentId,
            createAssessmentsDetail, accessToken));

    verifyNoInteractions(installmentServiceMock,receiptServiceMock,assessmentsRegistryRepositoryMock,assessmentsDetailRepositoryMock,transferServiceMock);
  }

  @Test
  void givenNoAssessmentsWhenCreateAssessmentsDetailThenResourceNotFoundException(){
    Long organizationId = 1L;
    Long assessmentsRegistryId = 2L;
    Long assessmentsId = 3L;
    String accessToken = "accessToken";
    CreateAssessmentsDetail createAssessmentsDetail = new CreateAssessmentsDetail(assessmentsRegistryId, Collections.emptySet());

    when(assessmentsRepositoryMock.findById(assessmentsId))
            .thenReturn(Optional.empty());

    assertThrows(ResourceNotFoundException.class,()->assessmentsDetailService.createAssessmentsDetail(organizationId,assessmentsId,
            createAssessmentsDetail, accessToken));

    verifyNoInteractions(installmentServiceMock,receiptServiceMock,assessmentsRegistryRepositoryMock,assessmentsDetailRepositoryMock,transferServiceMock);
  }
}
