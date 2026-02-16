package it.gov.pagopa.pu.classification.mapper.pii;

import it.gov.pagopa.pu.classification.dto.PaymentNotificationDTO;
import it.gov.pagopa.pu.classification.dto.pii.PaymentNotificationPIIDTO;
import it.gov.pagopa.pu.classification.model.PaymentNotificationNoPII;
import it.gov.pagopa.pu.common.pii.citizen.service.DataCipherService;
import it.gov.pagopa.pu.common.pii.citizen.service.PersonalDataService;
import it.gov.pagopa.pu.common.pii.mapper.BaseEntityPIIMapper;
import org.springframework.stereotype.Service;

@Service
public class PaymentNotificationPIIMapper extends BaseEntityPIIMapper<PaymentNotificationDTO, PaymentNotificationNoPII, PaymentNotificationPIIDTO> {
  private final DataCipherService dataCipherService;

  public PaymentNotificationPIIMapper(PersonalDataService personalDataService, DataCipherService dataCipherService) {
    super(PaymentNotificationPIIDTO.class, personalDataService);
    this.dataCipherService = dataCipherService;
  }

  @Override
  protected PaymentNotificationNoPII extractNoPiiEntity(PaymentNotificationDTO fullDTO) {
    PaymentNotificationNoPII noPii = new PaymentNotificationNoPII();

    noPii.setOrganizationId(fullDTO.getOrganizationId());
    noPii.setIngestionFlowFileId(fullDTO.getIngestionFlowFileId());
    noPii.setIud(fullDTO.getIud());
    noPii.setIuv(fullDTO.getIuv());
    noPii.setPaymentExecutionDate(fullDTO.getPaymentExecutionDate());
    noPii.setPaymentType(fullDTO.getPaymentType());
    noPii.setAmountPaidCents(fullDTO.getAmountPaidCents());
    noPii.setPaCommissionCents(fullDTO.getPaCommissionCents());
    noPii.setRemittanceInformation(fullDTO.getRemittanceInformation());
    noPii.setTransferCategory(fullDTO.getTransferCategory());
    noPii.setDebtPositionTypeOrgCode(fullDTO.getDebtPositionTypeOrgCode());
    noPii.setBalance(fullDTO.getBalance());
    noPii.setCreationDate(fullDTO.getCreationDate());
    noPii.setUpdateDate(fullDTO.getUpdateDate());
    noPii.setUpdateOperatorExternalId(fullDTO.getUpdateOperatorExternalId());
    noPii.setUpdateTraceId(fullDTO.getUpdateTraceId());
    noPii.setDebtorFiscalCodeHash(dataCipherService.hash(fullDTO.getDebtor().getFiscalCode()));
    noPii.setRemittanceInformationHash(dataCipherService.hash(fullDTO.getRemittanceInformation()));


    return noPii;
  }

  @Override
  protected PaymentNotificationPIIDTO extractPiiDto(PaymentNotificationDTO fullDTO) {
    return PaymentNotificationPIIDTO.builder()
      .debtor(fullDTO.getDebtor())
      .build();
  }

  @Override
  public PaymentNotificationDTO map(PaymentNotificationNoPII noPii) {
    PaymentNotificationPIIDTO pii = personalDataService.get(noPii.getPersonalDataId(),PaymentNotificationPIIDTO.class);
    return map(noPii, pii);
  }

  @Override
  protected PaymentNotificationDTO map(PaymentNotificationNoPII noPii, PaymentNotificationPIIDTO pii) {
    return PaymentNotificationDTO.builder()
      .paymentNotificationId(noPii.getPaymentNotificationId())
      .organizationId(noPii.getOrganizationId())
      .ingestionFlowFileId(noPii.getIngestionFlowFileId())
      .iud(noPii.getIud())
      .iuv(noPii.getIuv())
      .paymentExecutionDate(noPii.getPaymentExecutionDate())
      .paymentType(noPii.getPaymentType())
      .amountPaidCents(noPii.getAmountPaidCents())
      .paCommissionCents(noPii.getPaCommissionCents())
      .remittanceInformation(noPii.getRemittanceInformation())
      .transferCategory(noPii.getTransferCategory())
      .debtPositionTypeOrgCode(noPii.getDebtPositionTypeOrgCode())
      .balance(noPii.getBalance())
      .debtor(pii.getDebtor())
      .creationDate(noPii.getCreationDate())
      .updateDate(noPii.getUpdateDate())
      .updateOperatorExternalId(noPii.getUpdateOperatorExternalId())
      .updateTraceId(noPii.getUpdateTraceId())
      .noPII(noPii)
      .build();
  }
}
