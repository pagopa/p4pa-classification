package it.gov.pagopa.pu.classification.service.assessments;

import it.gov.pagopa.pu.classification.model.Assessments;
import it.gov.pagopa.pu.classification.model.AssessmentsDetail;
import it.gov.pagopa.pu.classification.repository.AssessmentsDetailRepository;
import it.gov.pagopa.pu.classification.service.BalanceUnmashallerService;
import it.gov.pagopa.pu.debtposition.dto.generated.InstallmentNoPIIResponse;
import it.veneto.regione.schemas._2012.pagamenti.ente.CtAccertamento;
import it.veneto.regione.schemas._2012.pagamenti.ente.CtBilancio;
import it.veneto.regione.schemas._2012.pagamenti.ente.CtCapitolo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AssessmentsDetailServiceImplTest {

    @Mock
    private AssessmentsDetailRepository assessmentsDetailRepositoryMock;
    @Mock
    private BalanceUnmashallerService balanceUnmashallerServiceMock;


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
        assessmentsDetailService = new AssessmentsDetailServiceImpl(assessmentsDetailRepositoryMock, balanceUnmashallerServiceMock);
    }

    @Test
    void buildAssessmentDetail_returnsAssessmentDetails() {
        InstallmentNoPIIResponse installmentNoPIIResponse = new InstallmentNoPIIResponse();
        installmentNoPIIResponse.setBalance(BALANCE);
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

        List<AssessmentsDetail> result = assessmentsDetailService.buildAssessmentDetail(installmentNoPIIResponse, assessment);

        assertNotNull(result);
        assertEquals(1, result.size());
        AssessmentsDetail detail = result.get(0);
        assertEquals(1L, detail.getAssessmentId());
        assertEquals(1L, detail.getOrganizationId());
        assertEquals("DPTC", detail.getDebtPositionTypeOrgCode());
        assertEquals("CAP1", detail.getSectionCode());
        assertEquals("UFF1", detail.getOfficeCode());
        assertEquals("ACC1", detail.getAssessmentCode());
        assertEquals(10000L, detail.getAmountCents());
    }

    @Test
    void buildAssessmentDetail_returnsEmptyListOnEmptyCapitolo() {
        InstallmentNoPIIResponse installmentNoPIIResponse = new InstallmentNoPIIResponse();
        installmentNoPIIResponse.setBalance(BALANCE);
        Assessments assessment = new Assessments();

        CtBilancio bilancio = new CtBilancio();

        when(balanceUnmashallerServiceMock.unmarshal(BALANCE)).thenReturn(bilancio);

        List<AssessmentsDetail> result = assessmentsDetailService.buildAssessmentDetail(installmentNoPIIResponse, assessment);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void createAssessmentDetail_savesNewDetails() {
        InstallmentNoPIIResponse installmentNoPIIResponse = new InstallmentNoPIIResponse();
        installmentNoPIIResponse.setBalance(BALANCE);
        installmentNoPIIResponse.setIuv("IUV");
        installmentNoPIIResponse.setIud("IUD");
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
        doReturn(null).when(assessmentsDetailRepositoryMock).getByAssessmentIdAndIuvAndIudAndOfficeCodeAndSectionCodeAndAssessmentCode(
                1L, "IUV", "IUD", "UFF1","CAP1", "ACC1");

        assessmentsDetailService.createAssessmentDetail(assessment, installmentNoPIIResponse);

        verify(assessmentsDetailRepositoryMock, times(1)).save(any(AssessmentsDetail.class));
    }

    @Test
    void createAssessmentDetail_updatesExistingDetails() {
        InstallmentNoPIIResponse installmentNoPIIResponse = new InstallmentNoPIIResponse();
        installmentNoPIIResponse.setBalance(BALANCE);
        installmentNoPIIResponse.setIuv("IUV");
        installmentNoPIIResponse.setIud("IUD");
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
        doReturn(existingDetail).when(assessmentsDetailRepositoryMock).getByAssessmentIdAndIuvAndIudAndOfficeCodeAndSectionCodeAndAssessmentCode(
                1L,"IUV", "IUD", "UFF1", "CAP1", "ACC1");

        assessmentsDetailService.createAssessmentDetail(assessment, installmentNoPIIResponse);

        verify(assessmentsDetailRepositoryMock, times(1)).save(existingDetail);
        assertEquals(10000L, existingDetail.getAmountCents());
    }
}
