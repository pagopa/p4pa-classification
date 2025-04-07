package it.gov.pagopa.pu.classification.repository;

import it.gov.pagopa.pu.classification.citizen.enums.PersonalDataType;
import it.gov.pagopa.pu.classification.citizen.service.PersonalDataService;
import it.gov.pagopa.pu.classification.dto.PaymentNotification;
import it.gov.pagopa.pu.classification.dto.PaymentNotificationPIIDTO;
import it.gov.pagopa.pu.classification.mapper.PaymentNotificationPIIMapper;
import it.gov.pagopa.pu.classification.model.PaymentNotificationNoPII;
import org.springframework.stereotype.Service;

@Service
public class PaymentNotificationPIIRepositoryImpl extends BasePIIRepository<PaymentNotification, PaymentNotificationNoPII, PaymentNotificationPIIDTO, String> implements PaymentNotificationPIIRepository {

  private final PaymentNotificationNoPIIRepository paymentNotificationNoPIIRepository;
    private final PaymentNotificationPIIMapper paymentNotificationPIIMapper;

  PaymentNotificationPIIRepositoryImpl(PersonalDataService personalDataService, PaymentNotificationNoPIIRepository paymentNotificationNoPIIRepository, PaymentNotificationPIIMapper paymentNotificationPIIMapper) {
    super(paymentNotificationPIIMapper, personalDataService, paymentNotificationNoPIIRepository);
      this.paymentNotificationNoPIIRepository = paymentNotificationNoPIIRepository;
      this.paymentNotificationPIIMapper = paymentNotificationPIIMapper;
  }

  @Override
  void setId(PaymentNotification fullDTO, String id) {
    fullDTO.setPaymentNotificationId(id);
  }

  @Override
  void setId(PaymentNotificationNoPII noPii, String id) {
    noPii.setPaymentNotificationId(id);
  }

  @Override
  String getId(PaymentNotificationNoPII noPii) {
    return noPii.getPaymentNotificationId();
  }

  @Override
  Class<PaymentNotificationPIIDTO> getPIITDTOClass() {
    return PaymentNotificationPIIDTO.class;
  }

  @Override
  PersonalDataType getPIIPersonalDataType() {
    return PersonalDataType.PAYMENT_NOTIFICATION;
  }

  @Override
  public PaymentNotification findBySemanticKey(Long organizationId, String iud) {
        return paymentNotificationPIIMapper.map(paymentNotificationNoPIIRepository.findBySemanticKey(organizationId, iud));
    }

}
