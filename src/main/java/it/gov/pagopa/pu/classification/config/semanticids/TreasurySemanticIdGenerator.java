package it.gov.pagopa.pu.classification.config.semanticids;

import it.gov.pagopa.pu.classification.model.Treasury;
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

@IdGeneratorType(TreasurySemanticIdGenerator.TreasurySemanticIdGeneratorStrategy.class)
@Retention(RUNTIME)
@Target({METHOD, FIELD})
public @interface TreasurySemanticIdGenerator {

  class TreasurySemanticIdGeneratorStrategy implements BeforeExecutionGenerator {

    @Override
    public EnumSet<EventType> getEventTypes() {
      return EnumSet.of(EventType.INSERT);
    }

    @Override
    public Object generate(SharedSessionContractImplementor session, Object o, Object currentValue, EventType eventType) {
      if (o instanceof Treasury treasury) {
        if (StringUtils.isEmpty(treasury.getTreasuryId())) {
          treasury.setTreasuryId(buildSemanticId(treasury));
        }
        return treasury.getTreasuryId();
      } else {
        throw new IllegalStateException("TreasurySemanticIdGenerator used for unexpected entity! " + o.getClass());
      }
    }

    private String buildSemanticId(Treasury treasury) {
      return treasury.getBillCode() + "-" +
        treasury.getBillYear() + "-" +
        treasury.getOrganizationId() ;
    }
  }
}
