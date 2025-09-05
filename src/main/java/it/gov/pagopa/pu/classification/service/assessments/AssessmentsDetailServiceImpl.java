package it.gov.pagopa.pu.classification.service.assessments;

import it.gov.pagopa.pu.classification.connector.debtposition.InstallmentService;
import it.gov.pagopa.pu.classification.connector.debtposition.ReceiptService;
import it.gov.pagopa.pu.classification.connector.debtposition.TransferService;
import it.gov.pagopa.pu.classification.dto.generated.CreateAssessmentsDetail;
import it.gov.pagopa.pu.classification.enums.DataEventType;
import it.gov.pagopa.pu.classification.event.dto.DataEventRequestDTO;
import it.gov.pagopa.pu.classification.event.producer.DataEventsProducerService;
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

import java.util.ArrayList;
import java.util.List;

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


  public AssessmentsDetailServiceImpl(AssessmentsDetailRepository assessmentsDetailRepository, BalanceUnmarshallerService balanceUnmashallerService, AssessmentsRepository assessmentsRepository, AssessmentsRegistryRepository assessmentsRegistryRepository, InstallmentService installmentService, ReceiptService receiptService, TransferService transferService,
    DataEventsProducerService dataEventsProducerService) {
    this.assessmentsDetailRepository = assessmentsDetailRepository;
    this.balanceUnmashallerService = balanceUnmashallerService;
    this.assessmentsRepository = assessmentsRepository;
    this.assessmentsRegistryRepository = assessmentsRegistryRepository;
    this.installmentService = installmentService;
    this.receiptService = receiptService;
    this.transferService = transferService;
    this.dataEventsProducerService = dataEventsProducerService;
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
      dataEventsProducerService.notifyAssessmentsEvent(assessmentDetail, new DataEventRequestDTO(
        DataEventType.ASSESSMENTS_CREATED, buildDataEventDescription(assessmentDetail)));
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
    List<InstallmentNoPII> installments = getInstallmentsByOrganizationIdAndIuds(organizationId, createAssessmentsDetail, accessToken);
    List<AssessmentsDetail> assessmentsDetails = new ArrayList<>();
    for (InstallmentNoPII installment : installments) {
      ReceiptNoPII receipt = getReceiptByReceiptIdAndDebtPositionTypeOrgCode(installment, assessments, accessToken);
      assessmentsDetails.add(saveAssessmentsDetail(organizationId, createAssessmentsDetail.getAssessmentRegistryId(),
                installment, receipt, assessments, accessToken));
    }
    return assessmentsDetails;
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

  private AssessmentsDetail saveAssessmentsDetail(Long organizationId, Long assessmentRegistryId, InstallmentNoPII installment, ReceiptNoPII receipt, Assessments assessments, String accessToken) {
    AssessmentsRegistry assessmentsRegistry = assessmentsRegistryRepository.findById(assessmentRegistryId)
            .filter(ar-> organizationId.equals(ar.getOrganizationId()))
            .orElseThrow(
            ()->new ResourceNotFoundException("AssessmentsRegistry having id "+assessmentRegistryId+" not found")
    );
    AssessmentsDetail assessmentsDetail = assessmentsDetailRepository.save(
            buildAssessmentsDetail(receipt, installment, assessments,assessmentsRegistry.getOfficeCode(),
                    assessmentsRegistry.getSectionCode(),assessmentsRegistry.getAssessmentCode(),
                    getAssessmentDetailAmount(installment.getInstallmentId(), receipt.getOrgFiscalCode(),accessToken)
            )
    );
    dataEventsProducerService.notifyAssessmentsEvent(assessmentsDetail, new DataEventRequestDTO(
      DataEventType.ASSESSMENTS_CREATED, buildDataEventDescription(assessmentsDetail)));
    return assessmentsDetail;
  }

  private Long getAssessmentDetailAmount(Long installmentId, String orgFiscalCode, String accessToken){
    List<Transfer> transfers = transferService.getByInstallmentId(installmentId, accessToken);
    return transfers.stream().filter(t->orgFiscalCode.equals(t.getOrgFiscalCode())).map(Transfer::getAmountCents).reduce(0L,Long::sum);
  }

  private String buildDataEventDescription(AssessmentsDetail assessmentsDetail) {
    return "assessmentId:" + assessmentsDetail.getAssessmentId();
  }
}
