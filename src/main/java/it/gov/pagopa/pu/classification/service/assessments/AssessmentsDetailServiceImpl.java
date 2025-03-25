package it.gov.pagopa.pu.classification.service.assessments;

import it.gov.pagopa.pu.classification.model.Assessments;
import it.gov.pagopa.pu.classification.model.AssessmentsDetail;
import it.gov.pagopa.pu.classification.repository.AssessmentsDetailRepository;
import it.gov.pagopa.pu.classification.service.BalanceUnmashallerService;
import it.gov.pagopa.pu.classification.util.Utilities;
import it.gov.pagopa.pu.debtposition.dto.generated.InstallmentNoPIIResponse;
import it.veneto.regione.schemas._2012.pagamenti.ente.CtBilancio;
import it.veneto.regione.schemas._2012.pagamenti.ente.CtCapitolo;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;

@Lazy
@Slf4j
@Service
public class AssessmentsDetailServiceImpl implements AssessmentsDetailService {

    private final AssessmentsDetailRepository assessmentsDetailRepository;
    private final BalanceUnmashallerService balanceUnmashallerService;


    public AssessmentsDetailServiceImpl(AssessmentsDetailRepository assessmentsDetailRepository, BalanceUnmashallerService balanceUnmashallerService) {
        this.assessmentsDetailRepository = assessmentsDetailRepository;
        this.balanceUnmashallerService = balanceUnmashallerService;
    }

    @Transactional
    @Override
    public void createAssessmentDetail(Assessments assessments, InstallmentNoPIIResponse installmentNoPIIResponse) {
        List<AssessmentsDetail> assessmentsDetailList = buildAssessmentDetail(installmentNoPIIResponse, assessments);
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


    List<AssessmentsDetail> buildAssessmentDetail(InstallmentNoPIIResponse installmentNoPIIResponse, Assessments assessment) {
        CtBilancio balance = balanceUnmashallerService.unmarshal(installmentNoPIIResponse.getBalance());

        List<CtCapitolo> capitoloList = balance.getCapitolo();
        return capitoloList.stream()
           .flatMap(capitolo -> capitolo.getAccertamento().stream()
                                                 .map(accertamento -> AssessmentsDetail.builder()
                                                    .assessmentId(assessment.getAssessmentId())
                                                    .organizationId(assessment.getOrganizationId())
                                                    .debtPositionTypeOrgCode(assessment.getDebtPositionTypeOrgCode())
                                                    .iuv(installmentNoPIIResponse.getIuv())
                                                    .iud(installmentNoPIIResponse.getIud())
                                                    .officeCode(capitolo.getCodUfficio())
                                                    .sectionCode(capitolo.getCodCapitolo())
                                                    .assessmentCode(accertamento.getCodAccertamento())
                                                    .amountCents(Utilities.bigDecimalEuroToLongCentsAmount(accertamento.getImporto()))
                                                    .build())
        ).toList();
    }
}
