package it.gov.pagopa.pu.classification.config;

import it.gov.pagopa.pu.classification.util.SecurityUtils;
import jakarta.persistence.EntityManagerFactory;
import org.hibernate.boot.model.naming.PhysicalNamingStrategySnakeCaseImpl;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.hibernate.SpringImplicitNamingStrategy;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import javax.sql.DataSource;
import java.util.Map;
import java.util.Optional;

@Configuration
@EnableTransactionManagement
@EnableJpaAuditing(auditorAwareRef="auditorProvider")
@EnableJpaRepositories(
  entityManagerFactoryRef = "emfClassification",
  transactionManagerRef = "tmClassification",
  basePackages = {"it.gov.pagopa.pu.classification.repository"}
)
public class ClassificationDataSourceConfig {
  @Primary
  @Bean(name="dsClassification")
  @ConfigurationProperties("spring.datasource.classification")
  public DataSource classificationDataSource()  {
    return DataSourceBuilder.create().build();
  }

  @Primary
  @Bean(name = "emfClassification")
  public LocalContainerEntityManagerFactoryBean classificationEntityManagerFactory(
    @Qualifier("dsClassification") DataSource dataSource,
    EntityManagerFactoryBuilder builder,
    LocalValidatorFactoryBean validatorFactoryBean) {

    return builder.dataSource(dataSource)
      .packages("it.gov.pagopa.pu.classification.model")
      .properties(Map.of(
        "hibernate.physical_naming_strategy", PhysicalNamingStrategySnakeCaseImpl.class.getName(),
        "hibernate.implicit_naming_strategy", SpringImplicitNamingStrategy.class.getName(),
        "jakarta.persistence.validation.factory", validatorFactoryBean
      ))
      .persistenceUnit("classification")
      .build();
  }

  @Primary
  @Bean(name = "tmClassification")
  public PlatformTransactionManager classificationTransactionManager(
    @Qualifier("emfClassification") EntityManagerFactory classificationEntityManagerFactory) {

    return new JpaTransactionManager(classificationEntityManagerFactory);
  }

  @Bean
  public AuditorAware<String> auditorProvider() {
    return () -> Optional.ofNullable(SecurityUtils.getCurrentUserExternalId());
  }
}
