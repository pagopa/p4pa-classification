package it.gov.pagopa.pu.classification.util.faker;

import it.gov.pagopa.pu.classification.dto.PaymentNotificationDTO;
import it.gov.pagopa.pu.classification.dto.PaymentNotificationPIIDTO;
import it.gov.pagopa.pu.classification.model.PaymentNotificationNoPII;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

public class PaymentNotificationFaker {
    private static final LocalDate DATE = LocalDate.of(2099, 1, 1);
    private static final OffsetDateTime DATETIME = OffsetDateTime.of(DATE, LocalTime.MIDNIGHT, ZoneOffset.UTC);

    public static PaymentNotificationDTO buildPaymentNotification() {
        return PaymentNotificationDTO.builder()
                .paymentNotificationId("iud_1")
                .organizationId(1L)
                .ingestionFlowFileId(2L)
                .iud("iud")
                .iuv("iuv")
                .paymentExecutionDate(DATE)
                .paymentType("paymentType")
                .amountPaidCents(100L)
                .paCommission(10L)
                .remittanceInformation("remittanceInformation")
                .transferCategory("transferCategory")
                .debtPositionTypeOrgCode("debtPositionTypeOrgCode")
                .balance("balance")
                .debtor(PersonFaker.buildPerson())
                .creationDate(DATETIME.toLocalDateTime())
                .updateDate(DATETIME.toLocalDateTime())
                .updateOperatorExternalId("updateOperatorExternalId")
                .noPII(buildPaymentNotificationNoPII())
                .build();
    }

    public static PaymentNotificationNoPII buildPaymentNotificationNoPII() {
        return PaymentNotificationNoPII.builder()
                .paymentNotificationId("iud_1")
                .organizationId(1L)
                .ingestionFlowFileId(2L)
                .iud("iud")
                .iuv("iuv")
                .paymentExecutionDate(DATE)
                .paymentType("paymentType")
                .amountPaidCents(100L)
                .paCommission(10L)
                .remittanceInformation("remittanceInformation")
                .transferCategory("transferCategory")
                .debtPositionTypeOrgCode("debtPositionTypeOrgCode")
                .balance("balance")
                .remittanceInformationHash("remittanceInformationHash".getBytes())
                .debtorFiscalCodeHash("debtorFiscalCodeHash".getBytes())
                .personalDataId(1L)
                .creationDate(DATETIME.toLocalDateTime())
                .updateDate(DATETIME.toLocalDateTime())
                .updateOperatorExternalId("updateOperatorExternalId")
                .build();
    }

    public static PaymentNotificationPIIDTO buildPaymentNotificationPIIDTO() {
        return PaymentNotificationPIIDTO.builder()
                .debtor(PersonFaker.buildPerson())
                .build();
    }


    public static PaymentNotificationDTO buildPaymentNotificationDTO() {
        return PaymentNotificationDTO.builder()
                .paymentNotificationId("iud_1")
                .organizationId(1L)
                .ingestionFlowFileId(2L)
                .iud("iud")
                .iuv("iuv")
                .paymentExecutionDate(DATE)
                .paymentType("paymentType")
                .amountPaidCents(100L)
                .paCommission(10L)
                .remittanceInformation("remittanceInformation")
                .transferCategory("transferCategory")
                .debtPositionTypeOrgCode("debtPositionTypeOrgCode")
                .balance("balance")
                .debtor(PersonFaker.buildPerson())
                .creationDate(DATETIME.toLocalDateTime())
                .updateDate(DATETIME.toLocalDateTime())
                .build();
    }

}


