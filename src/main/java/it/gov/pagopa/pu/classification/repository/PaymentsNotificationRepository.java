package it.gov.pagopa.pu.classification.repository;

import it.gov.pagopa.pu.classification.model.PaymentsNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;


@RepositoryRestResource(path = "payments-notification")
public interface PaymentsNotificationRepository extends JpaRepository<PaymentsNotification,String> {

  @Query("SELECT p FROM PaymentsNotification p WHERE " +
    "p.organizationId=:organizationId AND " +
    "p.iud=:iud ")
  PaymentsNotification findBySemanticKey(Long organizationId, String iud);

}
