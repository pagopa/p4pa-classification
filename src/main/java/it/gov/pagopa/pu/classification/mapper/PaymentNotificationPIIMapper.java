package it.gov.pagopa.pu.classification.mapper;

import it.gov.pagopa.pu.classification.citizen.service.DataCipherService;
import it.gov.pagopa.pu.classification.citizen.service.PersonalDataService;
import it.gov.pagopa.pu.classification.dto.PaymentNotification;
import it.gov.pagopa.pu.classification.dto.PaymentNotificationPIIDTO;
import it.gov.pagopa.pu.classification.dto.generated.PaymentNotificationDTO;
import it.gov.pagopa.pu.classification.dto.generated.PaymentNotificationNoPIIDTO;
import it.gov.pagopa.pu.classification.model.PaymentNotificationNoPII;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.time.OffsetDateTime;
import java.util.Optional;

@Service
public class PaymentNotificationPIIMapper extends BasePIIMapper<PaymentNotification, PaymentNotificationNoPII, PaymentNotificationPIIDTO> {
  private final PersonalDataService personalDataService;
  private final DataCipherService dataCipherService;
  private final PersonMapper personMapper;

  public PaymentNotificationPIIMapper(PersonalDataService personalDataService, DataCipherService dataCipherService, PersonMapper personMapper) {
    this.personalDataService = personalDataService;
    this.dataCipherService = dataCipherService;
    this.personMapper = personMapper;
  }

  @Override
  protected PaymentNotificationNoPII extractNoPiiEntity(PaymentNotification fullDTO) {
    PaymentNotificationNoPII noPii = new PaymentNotificationNoPII();

    noPii.setOrganizationId(fullDTO.getOrganizationId());
    noPii.setIngestionFlowFileId(fullDTO.getIngestionFlowFileId());
    noPii.setIud(fullDTO.getIud());
    noPii.setIuv(fullDTO.getIuv());
    noPii.setPaymentExecutionDate(fullDTO.getPaymentExecutionDate());
    noPii.setPaymentType(fullDTO.getPaymentType());
    noPii.setAmountPaidCents(fullDTO.getAmountPaidCents());
    noPii.setPaCommission(fullDTO.getPaCommission());
    noPii.setRemittanceInformation(fullDTO.getRemittanceInformation());
    noPii.setTransferCategory(fullDTO.getTransferCategory());
    noPii.setDebtPositionTypeOrgCode(fullDTO.getDebtPositionTypeOrgCode());
    noPii.setBalance(fullDTO.getBalance());
    noPii.setCreationDate(fullDTO.getCreationDate());
    noPii.setUpdateDate(fullDTO.getUpdateDate());
    noPii.setUpdateOperatorExternalId(fullDTO.getUpdateOperatorExternalId());
    noPii.setDebtorFiscalCodeHash(dataCipherService.hash(fullDTO.getDebtor().getFiscalCode()));
    noPii.setRemittanceInformationHash(dataCipherService.hash(fullDTO.getRemittanceInformation()));
    noPii.setPaymentNotificationId(fullDTO.getPaymentNotificationId());


    return noPii;
  }

  @Override
  protected PaymentNotificationPIIDTO extractPiiDto(PaymentNotification fullDTO) {
    return PaymentNotificationPIIDTO.builder()
      .debtor(fullDTO.getDebtor())
      .build();
  }

  @Override
  public PaymentNotification map(PaymentNotificationNoPII noPii) {
    PaymentNotificationPIIDTO paymentNotification = personalDataService.get(noPii.getPersonalDataId(),PaymentNotificationPIIDTO.class);
    return PaymentNotification.builder()
      .paymentNotificationId(noPii.getPaymentNotificationId())
      .organizationId(noPii.getOrganizationId())
      .ingestionFlowFileId(noPii.getIngestionFlowFileId())
      .iud(noPii.getIud())
      .iuv(noPii.getIuv())
      .paymentExecutionDate(noPii.getPaymentExecutionDate())
      .paymentType(noPii.getPaymentType())
      .amountPaidCents(noPii.getAmountPaidCents())
      .paCommission(noPii.getPaCommission())
      .remittanceInformation(noPii.getRemittanceInformation())
      .transferCategory(noPii.getTransferCategory())
      .debtPositionTypeOrgCode(noPii.getDebtPositionTypeOrgCode())
      .balance(noPii.getBalance())
      .debtor(paymentNotification.getDebtor())
      .creationDate(noPii.getCreationDate())
      .updateDate(noPii.getUpdateDate())
      .updateOperatorExternalId(noPii.getUpdateOperatorExternalId())
            .noPII(noPii)
      .build();
  }


  public PaymentNotificationNoPIIDTO mapToNoPiiDTO(PaymentNotification paymentNotification){
    PaymentNotificationNoPII entityNoPii= extractNoPiiEntity(paymentNotification);

    return PaymentNotificationNoPIIDTO.builder()
      .paymentNotificationId(paymentNotification.getPaymentNotificationId())
      .organizationId(paymentNotification.getOrganizationId())
      .ingestionFlowFileId(paymentNotification.getIngestionFlowFileId())
      .iud(paymentNotification.getIud())
      .iuv(paymentNotification.getIuv())
      .paymentExecutionDate(paymentNotification.getPaymentExecutionDate())
      .paymentType(paymentNotification.getPaymentType())
      .amountPaidCents(paymentNotification.getAmountPaidCents().longValue())
      .paCommission(paymentNotification.getPaCommission().longValue())
      .remittanceInformation(paymentNotification.getRemittanceInformation())
      .transferCategory(paymentNotification.getTransferCategory())
      .debtPositionTypeOrgCode(paymentNotification.getDebtPositionTypeOrgCode())
      .balance(paymentNotification.getBalance())
      .personalDataId(entityNoPii.getPersonalDataId())
      .remittanceInformationHash(entityNoPii.getRemittanceInformationHash())
      .debtorFiscalCodeHash(entityNoPii.getDebtorFiscalCodeHash())

      .build();
  }

  public PaymentNotification mapToModel(PaymentNotificationDTO paymentNotificationDTO) {
    return PaymentNotification.builder()
      .paymentNotificationId(paymentNotificationDTO.getPaymentNotificationId())
      .organizationId(paymentNotificationDTO.getOrganizationId())
      .ingestionFlowFileId(paymentNotificationDTO.getIngestionFlowFileId())
      .iud(paymentNotificationDTO.getIud())
      .iuv(paymentNotificationDTO.getIuv())
      .paymentExecutionDate(paymentNotificationDTO.getPaymentExecutionDate())
      .paymentType(paymentNotificationDTO.getPaymentType())
      .amountPaidCents(BigInteger.valueOf(paymentNotificationDTO.getAmountPaidCents()))
      .paCommission(BigInteger.valueOf(paymentNotificationDTO.getPaCommission()))
      .remittanceInformation(paymentNotificationDTO.getRemittanceInformation())
      .transferCategory(paymentNotificationDTO.getTransferCategory())
      .debtPositionTypeOrgCode(paymentNotificationDTO.getDebtPositionTypeOrgCode())
      .balance(paymentNotificationDTO.getBalance())
      .debtor(personMapper.mapToModel(paymentNotificationDTO.getDebtor()))
      .creationDate(Optional.ofNullable(paymentNotificationDTO.getCreationDate()).map(OffsetDateTime::toLocalDateTime).orElse(null))
      .updateDate(Optional.ofNullable(paymentNotificationDTO.getUpdateDate()).map(OffsetDateTime::toLocalDateTime).orElse(null))
      .build();
  }

}
