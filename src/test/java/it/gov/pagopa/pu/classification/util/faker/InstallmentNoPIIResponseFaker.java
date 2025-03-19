package it.gov.pagopa.pu.classification.util.faker;

import it.gov.pagopa.pu.debtposition.dto.generated.InstallmentNoPIIResponse;
import it.gov.pagopa.pu.debtposition.dto.generated.InstallmentSyncStatus;

import java.time.LocalDate;
import java.time.OffsetDateTime;

public class InstallmentNoPIIResponseFaker {

    public static InstallmentNoPIIResponse buildInstallmentNoPIIResponse(){
        return InstallmentNoPIIResponse.builder()
                .creationDate(OffsetDateTime.now())
                .updateDate(OffsetDateTime.now())
                .updateOperatorExternalId("operator123")
                .installmentId(1L)
                .paymentOptionId(1L)
                .status(InstallmentNoPIIResponse.StatusEnum.PAID)
                .iupdPagopa("iupdPagopa")
                .iud("iud")
                .iuv("iuv")
                .iur("iur")
                .iuf("iuf")
                .nav("nav")
                .dueDate(LocalDate.now())
                .amountCents(100L)
                .remittanceInformation("remittanceInformation")
                .balance("balance")
                .legacyPaymentMetadata("legacyPaymentMetadata")
                .personalDataId(1L)
                .debtorEntityType(InstallmentNoPIIResponse.DebtorEntityTypeEnum.F)
                .debtorFiscalCodeHash("fiscalCodeHash".getBytes())
                .syncStatus(new InstallmentSyncStatus())
                .notificationDate(OffsetDateTime.now())
                .ingestionFlowFileId(1L)
                .ingestionFlowFileLineNumber(1L)
                .receiptId(1L)
                .build();
    }

}
