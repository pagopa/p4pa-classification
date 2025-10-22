package it.gov.pagopa.pu.classification.service.assessments;

import it.gov.pagopa.pu.classification.connector.debtposition.InstallmentService;
import it.gov.pagopa.pu.classification.connector.debtposition.ReceiptService;
import it.gov.pagopa.pu.classification.connector.debtposition.TransferService;
import it.gov.pagopa.pu.classification.dto.BuildAssessmentsDetailParamsDTO;
import it.gov.pagopa.pu.classification.dto.generated.CreateAssessmentsDetail;
import it.gov.pagopa.pu.classification.enums.DataEventType;
import it.gov.pagopa.pu.classification.event.dto.DataEventRequestDTO;
import it.gov.pagopa.pu.classification.event.producer.DataEventsProducerService;
import it.gov.pagopa.pu.classification.exception.custom.InvalidRequestBodyException;
import it.gov.pagopa.pu.classification.mapper.Assessments2AssessmentsDetailDataMapper;
import it.gov.pagopa.pu.classification.mapper.Assessments2PaymentAssessmentsDataMapper;
import it.gov.pagopa.pu.classification.model.Assessments;
import it.gov.pagopa.pu.classification.model.AssessmentsDetail;
import it.gov.pagopa.pu.classification.model.AssessmentsRegistry;
import it.gov.pagopa.pu.classification.repository.AssessmentsDetailRepository;
import it.gov.pagopa.pu.classification.repository.AssessmentsRegistryRepository;
import it.gov.pagopa.pu.classification.repository.AssessmentsRepository;
import it.gov.pagopa.pu.classification.service.BalanceUnmarshallerService;
import it.gov.pagopa.pu.classification.util.Utilities;
import it.gov.pagopa.pu.debtposition.dto.generated.InstallmentNoPII;
import it.gov.pagopa.pu.debtposition.dto.generated.ReceiptNoPII;
import it.gov.pagopa.pu.debtposition.dto.generated.Transfer;
import it.veneto.regione.schemas._2012.pagamenti.ente.CtBilancio;
import it.veneto.regione.schemas._2012.pagamenti.ente.CtCapitolo;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Lazy
@Slf4j
@Service
public class AssessmentsDetailServiceImpl implements AssessmentsDetailService {

  private final AssessmentsDetailRepository assessmentsDetailRepository;
  private final BalanceUnmarshallerService balanceUnmashallerService;
  private final AssessmentsRepository assessmentsRepository;
  private final AssessmentsRegistryRepository assessmentsRegistryRepository;
  private final InstallmentService installmentService;
  private final ReceiptService receiptService;
  private final TransferService transferService;
  private final DataEventsProducerService dataEventsProducerService;
  private final Assessments2PaymentAssessmentsDataMapper paymentAssessmentsDataMapper;
  private final Assessments2AssessmentsDetailDataMapper assessmentsDetailDataMapper;


  public AssessmentsDetailServiceImpl(AssessmentsDetailRepository assessmentsDetailRepository, BalanceUnmarshallerService balanceUnmashallerService, AssessmentsRepository assessmentsRepository, AssessmentsRegistryRepository assessmentsRegistryRepository, InstallmentService installmentService, ReceiptService receiptService, TransferService transferService,
                                      DataEventsProducerService dataEventsProducerService,
                                      Assessments2PaymentAssessmentsDataMapper paymentAssessmentsDataMapper,
                                      Assessments2AssessmentsDetailDataMapper assessmentsDetailDataMapper) {
    this.assessmentsDetailRepository = assessmentsDetailRepository;
    this.balanceUnmashallerService = balanceUnmashallerService;
    this.assessmentsRepository = assessmentsRepository;
    this.assessmentsRegistryRepository = assessmentsRegistryRepository;
    this.installmentService = installmentService;
    this.receiptService = receiptService;
    this.transferService = transferService;
    this.dataEventsProducerService = dataEventsProducerService;
    this.paymentAssessmentsDataMapper = paymentAssessmentsDataMapper;
    this.assessmentsDetailDataMapper = assessmentsDetailDataMapper;
  }

  @Transactional
  @Override
  public void createAssessmentDetail(Assessments assessments, ReceiptNoPII receipt, InstallmentNoPII installmentNoPII) {
    List<AssessmentsDetail> assessmentsDetailList = buildAssessmentDetail(receipt, installmentNoPII, assessments);
    assessmentsDetailList.forEach(assessmentDetail -> {
      AssessmentsDetail ad = assessmentsDetailRepository.findByDebtPositionTypeOrgCodeAndIuvAndIudAndOfficeCodeAndSectionCodeAndAssessmentCode(
        assessmentDetail.getDebtPositionTypeOrgCode(), assessmentDetail.getIuv(), assessmentDetail.getIud(),
        assessmentDetail.getOfficeCode(), assessmentDetail.getSectionCode(), assessmentDetail.getAssessmentCode());
      if (ad == null) {
        assessmentsDetailRepository.save(assessmentDetail);
      } else if (!ad.getAmountCents().equals(assessmentDetail.getAmountCents())) {
        ad.setAmountCents(assessmentDetail.getAmountCents());
        assessmentsDetailRepository.save(ad);
      }
    });
    dataEventsProducerService.notifyPaymentAssessmentsEvent(paymentAssessmentsDataMapper.map(assessments, assessmentsDetailList), new DataEventRequestDTO(
      DataEventType.PAYMENT_ASSESSMENTS, buildDataEventDescription(assessments)));
  }

  List<AssessmentsDetail> buildAssessmentDetail(ReceiptNoPII receipt, InstallmentNoPII installment, Assessments assessment) {
    CtBilancio balance = balanceUnmashallerService.unmarshal(installment.getBalance());

    List<CtCapitolo> capitoloList = balance.getCapitolo();
    return capitoloList.stream()
      .flatMap(capitolo -> capitolo.getAccertamento().stream()
        .map(accertamento ->
                buildAssessmentsDetail(
                  receipt,
                  installment,
                  assessment,
                  capitolo.getCodUfficio(),
                  capitolo.getCodCapitolo(),
                  accertamento.getCodAccertamento(),
                  Utilities.bigDecimalEuroToLongCentsAmount(accertamento.getImporto())
                ))
      ).toList();
  }

  private AssessmentsDetail buildAssessmentsDetail(
    ReceiptNoPII receipt,
    InstallmentNoPII installment,
    Assessments assessment,
    String officeCode,
    String sectionCode,
    String assessmentCode,
    Long amountCents
  ) {
    AssessmentsRegistry assessmentsRegistry = assessmentsRegistryRepository.findByOrganizationIdAndCodes(
      assessment.getOrganizationId(),
      assessment.getDebtPositionTypeOrgCode(),
      sectionCode,
      officeCode,
      assessmentCode,
      String.valueOf(Optional.ofNullable(receipt.getPaymentDateTime()).orElse(OffsetDateTime.now()).getYear())
    ).orElse(null);

    return this.buildAssessmentsDetail(BuildAssessmentsDetailParamsDTO.builder()
        .receipt(receipt)
        .installment(installment)
        .assessment(assessment)
        .officeCode(officeCode)
        .sectionCode(sectionCode)
        .assessmentCode(assessmentCode)
        .amountCents(amountCents)
        .assessmentsRegistry(assessmentsRegistry)
        .build());
  }

  private AssessmentsDetail buildAssessmentsDetail(BuildAssessmentsDetailParamsDTO params) {
    String officeDescription = Optional.ofNullable(params.getAssessmentsRegistry())
      .map(AssessmentsRegistry::getOfficeDescription)
      .orElse(null);

    String sectionDescription = Optional.ofNullable(params.getAssessmentsRegistry())
      .map(AssessmentsRegistry::getSectionDescription)
      .orElse(null);

    String assessmentDescription = Optional.ofNullable(params.getAssessmentsRegistry())
      .map(AssessmentsRegistry::getAssessmentDescription)
      .orElse(null);

    return AssessmentsDetail.builder()
            .assessmentId(params.getAssessment().getAssessmentId())
            .organizationId(params.getAssessment().getOrganizationId())
            .debtPositionTypeOrgId(params.getAssessment().getDebtPositionTypeOrgId())
            .debtPositionTypeOrgCode(params.getAssessment().getDebtPositionTypeOrgCode())
            .iuv(params.getInstallment().getIuv())
            .iud(params.getInstallment().getIud())
            .iur(params.getInstallment().getIur())
            .debtorFiscalCodeHash(params.getInstallment().getDebtorFiscalCodeHash())
            .paymentDateTime(params.getReceipt().getPaymentDateTime())
            .officeCode(params.getOfficeCode())
            .officeDescription(officeDescription)
            .sectionCode(params.getSectionCode())
            .sectionDescription(sectionDescription)
            .assessmentCode(params.getAssessmentCode())
            .assessmentDescription(assessmentDescription)
            .amountCents(params.getAmountCents())
            .receiptId(params.getReceipt().getReceiptId())
            .build();
  }

  @Transactional
  @Override
  public List<AssessmentsDetail> createAssessmentsDetail(Long organizationId, Long assessmentId, CreateAssessmentsDetail createAssessmentsDetail, String accessToken) {
    Assessments assessments = assessmentsRepository.findById(assessmentId)
            .filter(a-> organizationId.equals(a.getOrganizationId()))
            .orElseThrow(()->
              new ResourceNotFoundException("Assessments having id "+assessmentId+" not found"));
    List<InstallmentNoPII> installments = getInstallmentsByOrganizationIdAndIuds(organizationId, createAssessmentsDetail, accessToken);
    List<AssessmentsDetail> assessmentsDetails = new ArrayList<>();
    for (InstallmentNoPII installment : installments) {
      ReceiptNoPII receipt = getReceiptByReceiptIdAndDebtPositionTypeOrgCode(installment, assessments, accessToken);
      assessmentsDetails.add(saveAssessmentsDetail(
        organizationId,
        createAssessmentsDetail,
        installment,
        receipt,
        assessments,
        accessToken
      ));
    }
    dataEventsProducerService.notifyAssessmentsDetailEvent(assessmentsDetailDataMapper.map(assessments, assessmentsDetails), new DataEventRequestDTO(
      DataEventType.ASSESSMENTS_DETAIL, buildDataEventDescription(assessments)));
    return assessmentsDetails;
  }

  @Override
  public void deleteAssessmentDetailsByOrgAndInstallment(Long organizationId, String iuv, String iud) {
    assessmentsDetailRepository.deleteAllByOrganizationIdAndIuvAndIud(organizationId, iuv, iud);
  }

  private ReceiptNoPII getReceiptByReceiptIdAndDebtPositionTypeOrgCode(InstallmentNoPII installment, Assessments assessments, String accessToken) {
    if(installment.getReceiptId()==null){
      throw new InvalidRequestBodyException("Installment having iud "+ installment.getIud()+" does not have a receiptId");
    }
    ReceiptNoPII receipt = receiptService.getByReceiptIdAndDebtPositionTypeOrgCode(installment.getReceiptId(), assessments.getDebtPositionTypeOrgCode(), accessToken);
    if(receipt==null) {
      throw new ResourceNotFoundException("Receipt having id " + installment.getReceiptId() + " not found");
    }
    return receipt;
  }

  private List<InstallmentNoPII> getInstallmentsByOrganizationIdAndIuds(Long organizationId, CreateAssessmentsDetail createAssessmentsDetail, String accessToken) {
    List<InstallmentNoPII> installments = installmentService.findByOrganizationIdAndIuds(organizationId, createAssessmentsDetail.getIuds(), accessToken);
    if(!CollectionUtils.isEmpty(createAssessmentsDetail.getIuds())
            && (CollectionUtils.isEmpty(installments) || createAssessmentsDetail.getIuds().size()!=installments.size())){
      throw new InvalidRequestBodyException("One or more iud is invalid. [organizationId: "+ organizationId +" iuds:"+ createAssessmentsDetail.getIuds()+"]");
    }
    return installments;
  }

  private AssessmentsDetail saveAssessmentsDetail(Long organizationId, CreateAssessmentsDetail createAssessmentsDetail, InstallmentNoPII installment, ReceiptNoPII receipt, Assessments assessments, String accessToken) {
    AssessmentsRegistry assessmentsRegistry = assessmentsRegistryRepository.findById(createAssessmentsDetail.getAssessmentRegistryId())
            .filter(ar-> organizationId.equals(ar.getOrganizationId()))
            .orElseThrow(
            ()->new ResourceNotFoundException("AssessmentsRegistry having id "+createAssessmentsDetail.getAssessmentRegistryId()+" not found")
    );
    return assessmentsDetailRepository.save(
            buildAssessmentsDetail(
              BuildAssessmentsDetailParamsDTO.builder()
                .receipt(receipt)
                .installment(installment)
                .assessment(assessments)
                .officeCode(assessmentsRegistry.getOfficeCode())
                .sectionCode(assessmentsRegistry.getSectionCode())
                .assessmentCode(assessmentsRegistry.getAssessmentCode())
                .amountCents(getAssessmentDetailAmount(installment.getInstallmentId(), receipt.getOrgFiscalCode(),accessToken))
                .assessmentsRegistry(assessmentsRegistry)
                .build()
            )
    );
  }

  private Long getAssessmentDetailAmount(Long installmentId, String orgFiscalCode, String accessToken){
    List<Transfer> transfers = transferService.getByInstallmentId(installmentId, accessToken);
    return transfers.stream().filter(t->orgFiscalCode.equals(t.getOrgFiscalCode())).map(Transfer::getAmountCents).reduce(0L,Long::sum);
  }

  private String buildDataEventDescription(Assessments assessments) {
    return "assessmentId:" + assessments.getAssessmentId();
  }
}
