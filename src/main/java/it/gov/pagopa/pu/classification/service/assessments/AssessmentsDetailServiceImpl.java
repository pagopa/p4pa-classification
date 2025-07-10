package it.gov.pagopa.pu.classification.service.assessments;

import it.gov.pagopa.pu.classification.connector.debtposition.InstallmentService;
import it.gov.pagopa.pu.classification.connector.debtposition.ReceiptService;
import it.gov.pagopa.pu.classification.dto.generated.CreateAssessmentsDetail;
import it.gov.pagopa.pu.classification.model.Assessments;
import it.gov.pagopa.pu.classification.model.AssessmentsDetail;
import it.gov.pagopa.pu.classification.model.AssessmentsRegistry;
import it.gov.pagopa.pu.classification.repository.AssessmentsDetailRepository;
import it.gov.pagopa.pu.classification.repository.AssessmentsRegistryRepository;
import it.gov.pagopa.pu.classification.repository.AssessmentsRepository;
import it.gov.pagopa.pu.classification.service.BalanceUnmashallerService;
import it.gov.pagopa.pu.classification.util.Utilities;
import it.gov.pagopa.pu.debtposition.dto.generated.InstallmentNoPII;
import it.gov.pagopa.pu.debtposition.dto.generated.ReceiptNoPII;
import it.veneto.regione.schemas._2012.pagamenti.ente.CtBilancio;
import it.veneto.regione.schemas._2012.pagamenti.ente.CtCapitolo;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Lazy
@Slf4j
@Service
public class AssessmentsDetailServiceImpl implements AssessmentsDetailService {

  private final AssessmentsDetailRepository assessmentsDetailRepository;
  private final BalanceUnmashallerService balanceUnmashallerService;
  private final AssessmentsRepository assessmentsRepository;
  private final AssessmentsRegistryRepository assessmentsRegistryRepository;
  private final InstallmentService installmentService;
  private final ReceiptService receiptService;


  public AssessmentsDetailServiceImpl(AssessmentsDetailRepository assessmentsDetailRepository, BalanceUnmashallerService balanceUnmashallerService, AssessmentsRepository assessmentsRepository, AssessmentsRegistryRepository assessmentsRegistryRepository, InstallmentService installmentService, ReceiptService receiptService) {
    this.assessmentsDetailRepository = assessmentsDetailRepository;
      this.balanceUnmashallerService = balanceUnmashallerService;
      this.assessmentsRepository = assessmentsRepository;
      this.assessmentsRegistryRepository = assessmentsRegistryRepository;
      this.installmentService = installmentService;
      this.receiptService = receiptService;
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
                buildAssessmentsDetail(receipt, installment, assessment, capitolo.getCodUfficio(),
                        capitolo.getCodCapitolo(),accertamento.getCodAccertamento(),
                        Utilities.bigDecimalEuroToLongCentsAmount(accertamento.getImporto())))
      ).toList();
  }

  private static AssessmentsDetail buildAssessmentsDetail(ReceiptNoPII receipt, InstallmentNoPII installment, Assessments assessment,
                    String officeCode, String sectionCode, String assessmentCode, Long amountCents) {
    return AssessmentsDetail.builder()
            .assessmentId(assessment.getAssessmentId())
            .organizationId(assessment.getOrganizationId())
            .debtPositionTypeOrgCode(assessment.getDebtPositionTypeOrgCode())
            .iuv(installment.getIuv())
            .iud(installment.getIud())
            .iur(installment.getIur())
            .debtorFiscalCodeHash(installment.getDebtorFiscalCodeHash())
            .paymentDateTime(receipt.getPaymentDateTime())
            .officeCode(officeCode)
            .sectionCode(sectionCode)
            .assessmentCode(assessmentCode)
            .amountCents(amountCents)
            .receiptId(receipt.getReceiptId())
            .build();
  }

  @Transactional
  @Override
  public List<AssessmentsDetail> createAssessmentsDetail(Long organizationId, Long assessmentId, CreateAssessmentsDetail createAssessmentsDetail, String accessToken) {
    Assessments assessments = assessmentsRepository.findById(assessmentId)
            .filter(a-> organizationId.equals(a.getOrganizationId()))
            .orElseThrow(()->
              new ResourceNotFoundException("Assessments having id "+assessmentId+" not found"));
    List<InstallmentNoPII> installments = installmentService.findByOrganizationIdAndIuds(organizationId, createAssessmentsDetail.getIudSet(), accessToken);
    List<AssessmentsDetail> assessmentsDetails = new ArrayList<>();
    for (InstallmentNoPII installment : installments) {
      if(installment.getReceiptId()!=null){
        ReceiptNoPII receipt = receiptService.getById(installment.getReceiptId(), accessToken);
        if(receipt!=null){
          assessmentsDetails.add(saveAssessmentsDetail(organizationId, createAssessmentsDetail.getAssessmentRegistryId(),
                  installment, receipt, assessments));
        }else{
          throw new ResourceNotFoundException("Receipt having id "+installment.getReceiptId()+" not found");
        }
      }else{
        log.debug("Installment having iud {} does not have a receiptId", installment.getIud());
      }
    }
    return assessmentsDetails;
  }

  private AssessmentsDetail saveAssessmentsDetail(Long organizationId, Long assessmentRegistryId, InstallmentNoPII installment, ReceiptNoPII receipt, Assessments assessments) {
    AssessmentsRegistry assessmentsRegistry = assessmentsRegistryRepository.findById(assessmentRegistryId)
            .filter(ar-> organizationId.equals(ar.getOrganizationId()))
            .orElseThrow(
            ()->new ResourceNotFoundException("AssessmentsRegistry having id "+assessmentRegistryId+" not found")
    );
    return assessmentsDetailRepository.save(
            buildAssessmentsDetail(receipt, installment, assessments,assessmentsRegistry.getOfficeCode(),
                    assessmentsRegistry.getSectionCode(),assessmentsRegistry.getAssessmentCode(),installment.getAmountCents())
    );
  }
}
