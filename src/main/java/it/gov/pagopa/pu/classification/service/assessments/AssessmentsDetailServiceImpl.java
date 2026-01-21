package it.gov.pagopa.pu.classification.service.assessments;

import it.gov.pagopa.pu.classification.connector.debtposition.InstallmentService;
import it.gov.pagopa.pu.classification.connector.debtposition.ReceiptService;
import it.gov.pagopa.pu.classification.connector.debtposition.TransferService;
import it.gov.pagopa.pu.classification.dto.BuildAssessmentsDetailParamsDTO;
import it.gov.pagopa.pu.classification.dto.generated.CreateAssessmentsDetail;
import it.gov.pagopa.pu.classification.exception.custom.InvalidRequestBodyException;
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


  public AssessmentsDetailServiceImpl(AssessmentsDetailRepository assessmentsDetailRepository, BalanceUnmarshallerService balanceUnmashallerService, AssessmentsRepository assessmentsRepository, AssessmentsRegistryRepository assessmentsRegistryRepository, InstallmentService installmentService, ReceiptService receiptService, TransferService transferService) {
    this.assessmentsDetailRepository = assessmentsDetailRepository;
    this.balanceUnmashallerService = balanceUnmashallerService;
    this.assessmentsRepository = assessmentsRepository;
    this.assessmentsRegistryRepository = assessmentsRegistryRepository;
    this.installmentService = installmentService;
    this.receiptService = receiptService;
    this.transferService = transferService;
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
              new ResourceNotFoundException("[ASSESSMENT_NOT_FOUND] Assessment with id "+assessmentId+" not found"));
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
    return assessmentsDetails;
  }

  @Override
  public void deleteAssessmentDetailsByOrgAndInstallment(Long organizationId, String iuv, String iud) {
    assessmentsDetailRepository.deleteAllByOrganizationIdAndIuvAndIud(organizationId, iuv, iud);
  }

  private ReceiptNoPII getReceiptByReceiptIdAndDebtPositionTypeOrgCode(InstallmentNoPII installment, Assessments assessments, String accessToken) {
    if(installment.getReceiptId()==null){
      throw new InvalidRequestBodyException("[INVALID_INSTALLMENT] Installment having iud "+ installment.getIud()+" does not have a receiptId");
    }
    ReceiptNoPII receipt = receiptService.getByReceiptIdAndDebtPositionTypeOrgCode(installment.getReceiptId(), assessments.getDebtPositionTypeOrgCode(), accessToken);
    if(receipt==null) {
      throw new ResourceNotFoundException("[RECEIPT_NOT_FOUND] Receipt with id " + installment.getReceiptId() + " not found");
    }
    return receipt;
  }

  private List<InstallmentNoPII> getInstallmentsByOrganizationIdAndIuds(Long organizationId, CreateAssessmentsDetail createAssessmentsDetail, String accessToken) {
    List<InstallmentNoPII> installments = installmentService.findByOrganizationIdAndIuds(organizationId, createAssessmentsDetail.getIuds(), accessToken);
    if(!CollectionUtils.isEmpty(createAssessmentsDetail.getIuds())
            && (CollectionUtils.isEmpty(installments) || createAssessmentsDetail.getIuds().size()!=installments.size())){
      throw new InvalidRequestBodyException("[INVALID_IUD] One or more iud is invalid. [organizationId: "+ organizationId +" iuds:"+ createAssessmentsDetail.getIuds()+"]");
    }
    return installments;
  }

  private AssessmentsDetail saveAssessmentsDetail(Long organizationId, CreateAssessmentsDetail createAssessmentsDetail, InstallmentNoPII installment, ReceiptNoPII receipt, Assessments assessments, String accessToken) {
    AssessmentsRegistry assessmentsRegistry = assessmentsRegistryRepository.findById(createAssessmentsDetail.getAssessmentRegistryId())
            .filter(ar-> organizationId.equals(ar.getOrganizationId()))
            .orElseThrow(
            ()->new ResourceNotFoundException("[ASSESSMENT_REGISTRY_NOT_FOUND] AssessmentRegistry with id "+createAssessmentsDetail.getAssessmentRegistryId()+" not found")
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
}
