package it.gov.pagopa.pu.classification.service;

import it.gov.pagopa.pu.classification.model.Assessments;
import it.gov.pagopa.pu.classification.model.AssessmentsDetail;
import it.gov.pagopa.pu.classification.repository.AssessmentsDetailRepository;
import it.gov.pagopa.pu.classification.util.Utilities;
import it.gov.pagopa.pu.debtposition.dto.generated.InstallmentNoPIIResponse;
import it.veneto.regione.schemas._2012.pagamenti.ente.CtAccertamento;
import it.veneto.regione.schemas._2012.pagamenti.ente.CtBilancio;
import it.veneto.regione.schemas._2012.pagamenti.ente.CtCapitolo;
import jakarta.transaction.Transactional;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.io.StringReader;
import java.util.Collections;
import java.util.List;

@Lazy
@Slf4j
@Service
public class AssessmentsDetailServiceImpl implements AssessmentsDetailService {

  private final AssessmentsDetailRepository assessmentsDetailRepository;


  public AssessmentsDetailServiceImpl(AssessmentsDetailRepository assessmentsDetailRepository) {
    this.assessmentsDetailRepository = assessmentsDetailRepository;
  }

  @Transactional
  @Override
  public void createAssessmentDetail(Assessments assessments, InstallmentNoPIIResponse installmentNoPIIResponse) {
    List<AssessmentsDetail> assessmentsDetailList = buildAssessmentDetail(installmentNoPIIResponse, assessments);
    assessmentsDetailList.forEach(assessmentDetail -> {
      AssessmentsDetail ad = assessmentsDetailRepository.getByAssessmentIdAndIuvAndIudAndOfficeCodeAndSectionCodeAndAssessmentCode(
              assessmentDetail.getAssessmentId(), assessmentDetail.getIuv(), assessmentDetail.getIud(),
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
    CtBilancio balance;
    try {
      balance = convertBalance(installmentNoPIIResponse);
    } catch (JAXBException e) {
      log.error("Error converting balance", e);
      return Collections.emptyList();
    }

    List<CtCapitolo> capitoloList = balance.getCapitolo();
    List<AssessmentsDetail> assessmentsDetailList = Collections.emptyList();

    capitoloList.forEach(capitolo -> {
      List<CtAccertamento> accertamentiList = capitolo.getAccertamento();
      accertamentiList.forEach(accertamento ->
        assessmentsDetailList.add(AssessmentsDetail.builder()
          .assessmentId(assessment.getAssessmentId())
          .organizationId(assessment.getOrganizationId())
          .debtPositionTypeOrgCode(assessment.getDebtPositionTypeOrgCode())
          .iuv(installmentNoPIIResponse.getIuv())
          .iud(installmentNoPIIResponse.getIud())
          .officeCode(capitolo.getCodUfficio())
          .sectionCode(capitolo.getCodCapitolo())
          .assessmentCode(accertamento.getCodAccertamento())
          .amountCents(Utilities.bigDecimalEuroToLongCentsAmount(accertamento.getImporto()))
          .build()));
    });
    return assessmentsDetailList;
  }


  CtBilancio convertBalance(InstallmentNoPIIResponse installmentNoPIIResponse) throws JAXBException {
    String balanceXml = installmentNoPIIResponse.getBalance();
    JAXBContext jaxbContext = JAXBContext.newInstance(CtBilancio.class);
    Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
    StringReader reader = new StringReader(balanceXml);
    return (CtBilancio) unmarshaller.unmarshal(reader);
  }


}
