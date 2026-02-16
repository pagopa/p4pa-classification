package it.gov.pagopa.pu.classification.repository;

import it.gov.pagopa.pu.common.pii.citizen.enums.PersonalDataType;
import it.gov.pagopa.pu.common.pii.citizen.service.PersonalDataService;
import it.gov.pagopa.pu.classification.dto.PaymentNotificationDTO;
import it.gov.pagopa.pu.classification.dto.pii.PaymentNotificationPIIDTO;
import it.gov.pagopa.pu.classification.mapper.PaymentNotificationPIIMapper;
import it.gov.pagopa.pu.classification.model.PaymentNotificationNoPII;
import it.gov.pagopa.pu.common.pii.repository.BasePIIRepository;
import org.springframework.stereotype.Service;

@Service
public class PaymentNotificationPIIRepositoryImpl extends BasePIIRepository<PaymentNotificationDTO, PaymentNotificationNoPII, PaymentNotificationPIIDTO, String> implements PaymentNotificationPIIRepository {

  private final PaymentNotificationNoPIIRepository paymentNotificationNoPIIRepository;
    private final PaymentNotificationPIIMapper paymentNotificationPIIMapper;

  PaymentNotificationPIIRepositoryImpl(PersonalDataService personalDataService, PaymentNotificationNoPIIRepository paymentNotificationNoPIIRepository, PaymentNotificationPIIMapper paymentNotificationPIIMapper) {
    super(paymentNotificationPIIMapper, personalDataService, paymentNotificationNoPIIRepository);
      this.paymentNotificationNoPIIRepository = paymentNotificationNoPIIRepository;
      this.paymentNotificationPIIMapper = paymentNotificationPIIMapper;
  }

  @Override
  protected void setId(PaymentNotificationDTO fullDTO, String id) {
    fullDTO.setPaymentNotificationId(id);
  }

  @Override
  protected void setId(PaymentNotificationNoPII noPii, String id) {
    noPii.setPaymentNotificationId(id);
  }

  @Override
  protected String getId(PaymentNotificationNoPII noPii) {
    return noPii.getPaymentNotificationId();
  }

  @Override
  protected Class<PaymentNotificationPIIDTO> getPIITDTOClass() {
    return PaymentNotificationPIIDTO.class;
  }

  @Override
  protected PersonalDataType getPIIPersonalDataType() {
    return PersonalDataType.PAYMENT_NOTIFICATION;
  }

  @Override
  public PaymentNotificationDTO findBySemanticKey(Long organizationId, String iud) {
        return paymentNotificationPIIMapper.map(paymentNotificationNoPIIRepository.findBySemanticKey(organizationId, iud));
    }

}
