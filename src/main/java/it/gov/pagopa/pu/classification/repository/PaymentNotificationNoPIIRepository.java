package it.gov.pagopa.pu.classification.repository;

import it.gov.pagopa.pu.classification.model.PaymentNotificationNoPII;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;


@RepositoryRestResource(path = "payment-notification")
public interface PaymentNotificationNoPIIRepository extends JpaRepository<PaymentNotificationNoPII,String> {

  @RestResource(exported = false)
  @Query("SELECT p FROM PaymentNotificationNoPII p WHERE " +
    "p.organizationId=:organizationId AND " +
    "p.iud=:iud ")
  PaymentNotificationNoPII findBySemanticKey(Long organizationId, String iud);

}
