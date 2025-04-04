package it.gov.pagopa.pu.classification.mapper;

import it.gov.pagopa.pu.classification.citizen.service.DataCipherService;
import it.gov.pagopa.pu.classification.citizen.service.PersonalDataService;
import it.gov.pagopa.pu.classification.dto.PaymentNotification;
import it.gov.pagopa.pu.classification.dto.PaymentNotificationPIIDTO;
import it.gov.pagopa.pu.classification.model.PaymentNotificationNoPII;
import org.springframework.stereotype.Service;

@Service
public class PaymentNotificationPIIMapper extends BasePIIMapper<PaymentNotification, PaymentNotificationNoPII, PaymentNotificationPIIDTO> {
  private final PersonalDataService personalDataService;
  private final DataCipherService dataCipherService;

  public PaymentNotificationPIIMapper(PersonalDataService personalDataService, DataCipherService dataCipherService) {
    this.personalDataService = personalDataService;
    this.dataCipherService = dataCipherService;
  }

  public PaymentNotification map(PaymentNotificationNoPII noPii) {
    PaymentNotificationPIIDTO pii = personalDataService.get(noPii.getPersonalDataId(), PaymentNotificationPIIDTO.class);

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
      .debtor(pii.getDebtor())
      .creationDate(noPii.getCreationDate())
      .updateDate(noPii.getUpdateDate())
      .updateOperatorExternalId(noPii.getUpdateOperatorExternalId())
      .noPII(noPii)
      .build();
  }

  @Override
  protected PaymentNotificationNoPII extractNoPiiEntity(PaymentNotification fullDTO) {
    PaymentNotificationNoPII noPii = new PaymentNotificationNoPII();

    noPii.setPaymentNotificationId(fullDTO.getPaymentNotificationId());
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


    return noPii;
  }

  @Override
  protected PaymentNotificationPIIDTO extractPiiDto(PaymentNotification fullDTO) {
    return PaymentNotificationPIIDTO.builder()
      .debtor(fullDTO.getDebtor())
      .build();
  }

}
