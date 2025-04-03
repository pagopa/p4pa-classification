package it.gov.pagopa.pu.classification.repository;

import it.gov.pagopa.pu.classification.citizen.enums.PersonalDataType;
import it.gov.pagopa.pu.classification.citizen.service.PersonalDataService;
import it.gov.pagopa.pu.classification.dto.PaymentNotification;
import it.gov.pagopa.pu.classification.dto.PaymentNotificationPIIDTO;
import it.gov.pagopa.pu.classification.mapper.BasePIIMapper;
import it.gov.pagopa.pu.classification.model.PaymentNotificationNoPII;
import org.springframework.data.jpa.repository.JpaRepository;

public class PaymentNotificationPIIRepositoryImpl extends BasePIIRepository<PaymentNotification, PaymentNotificationNoPII, PaymentNotificationPIIDTO, String> implements PaymentNotificationPIIRepository {


  PaymentNotificationPIIRepositoryImpl(BasePIIMapper<PaymentNotification, PaymentNotificationNoPII, PaymentNotificationPIIDTO> piiMapper, PersonalDataService personalDataService, JpaRepository<PaymentNotificationNoPII, String> noPIIRepository) {
    super(piiMapper, personalDataService, noPIIRepository);
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

}
