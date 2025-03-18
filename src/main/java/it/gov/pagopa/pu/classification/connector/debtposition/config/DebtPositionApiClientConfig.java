package it.gov.pagopa.pu.classification.connector.debtposition.config;

import it.gov.pagopa.pu.classification.config.ApiClientConfig;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "rest.debt-position")
@SuperBuilder
@NoArgsConstructor
public class DebtPositionApiClientConfig extends ApiClientConfig {
}
