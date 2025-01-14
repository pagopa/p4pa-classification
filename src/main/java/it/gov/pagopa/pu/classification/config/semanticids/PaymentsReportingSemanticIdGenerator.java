package it.gov.pagopa.pu.classification.config.semanticids;

import it.gov.pagopa.pu.classification.model.PaymentsReporting;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.IdGeneratorType;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.generator.BeforeExecutionGenerator;
import org.hibernate.generator.EventType;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.EnumSet;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@IdGeneratorType(PaymentsReportingSemanticIdGenerator.PaymentsReportingSemanticIdGeneratorStrategy.class)
@Retention(RUNTIME)
@Target({METHOD, FIELD})
public @interface PaymentsReportingSemanticIdGenerator {

  class PaymentsReportingSemanticIdGeneratorStrategy implements BeforeExecutionGenerator {

    @Override
    public EnumSet<EventType> getEventTypes() {
      return EnumSet.of(EventType.INSERT);
    }

    @Override
    public Object generate(SharedSessionContractImplementor session, Object o, Object currentValue, EventType eventType) {
      if (o instanceof PaymentsReporting paymentsReporting) {
        if (StringUtils.isEmpty(paymentsReporting.getPaymentsReportingId())) {
          paymentsReporting.setPaymentsReportingId(buildSemanticId(paymentsReporting));
        }
        return paymentsReporting.getPaymentsReportingId();
      } else {
        throw new IllegalStateException("PaymentsReportingSemanticIdGenerator used for unexpected entity! " + o.getClass());
      }
    }

    private String buildSemanticId(PaymentsReporting paymentsReporting) {
      return paymentsReporting.getIuf() + "/" +
        paymentsReporting.getIuv() + "/" +
        paymentsReporting.getTransferIndex() + "/" +
        paymentsReporting.getOrganizationId();
    }
  }
}
