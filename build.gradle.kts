import org.openapitools.generator.gradle.plugin.tasks.GenerateTask
import java.util.*
import com.github.jk1.license.render.*
import com.github.jk1.license.filter.*
import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent

plugins {
  java
  id("org.springframework.boot") version "4.1.0"
  id("io.spring.dependency-management") version "1.1.7"
  jacoco
  id("org.sonarqube") version "7.3.1.8318"
  id("com.github.ben-manes.versions") version "0.54.0"
  id("org.openapi.generator") version "7.23.0"
  id("org.ajoberstar.grgit") version "5.3.2"
  id("com.gorylenko.gradle-git-properties") version "4.0.1"
  id("com.intershop.gradle.jaxb") version "8.0.1"
  id("com.github.jk1.dependency-license-report") version "3.1.4"
}

group = "it.gov.pagopa.payhub"
version = "0.0.1"
description = "p4pa-classification"

java {
  toolchain {
    languageVersion = JavaLanguageVersion.of(21)
  }
}

configurations {
  compileOnly {
    extendsFrom(configurations.annotationProcessor.get())
  }
  compileClasspath {
    resolutionStrategy.activateDependencyLocking()
  }
}

licenseReport {
  renderers =
    arrayOf(XmlReportRenderer("third-party-libs.xml", "Back-End Libraries"))
  outputDir = "$projectDir/dependency-licenses"
  filters = arrayOf(SpdxLicenseBundleNormalizer())
}
tasks.classes {
  finalizedBy(tasks.generateLicenseReport)
}

repositories {
  mavenCentral()
}

val springDocOpenApiVersion = "3.0.3"
val openApiToolsVersion = "0.2.10"
val bouncycastleVersion = "1.84"
val micrometerVersion = "1.7.0"
val caffeineVersion = "3.2.4"
val httpClientVersion = "5.6.1"
val httpCoreVersion = "5.4.2"
val kafkaAppender = "0.2.0-RC2"
val lz4JavaVersion = "1.11.1"
val postgresJdbcVersion = "42.7.13"
val activationVersion = "2.1.4"
val jaxbVersion = "4.0.9"
val jaxbApiVersion = "4.0.5"
val xmlSchemaVersion = "2.3.2"
val podamVersion = "8.0.2.RELEASE"
val rhinoScriptVersion = "1.9.1"
val springWolfAsyncApiVersion = "1.21.0"
val springWolfUiAsyncApiVersion = "1.21.0"
val commonsLang3Version = "3.20.0"

// Downgrading in order to handle List of enums in SpringDataRest exposed queries (see https://github.com/spring-projects/spring-data-commons/issues/3502)
val hibernateCoreVersion = "7.1.18.Final"

val springCloudDepsVersion = "2025.1.2"

dependencyManagement {
  imports {
    mavenBom("org.springframework.cloud:spring-cloud-dependencies:$springCloudDepsVersion")
  }
}

dependencies {
  implementation("org.springframework.boot:spring-boot-starter-webmvc")
  implementation("org.springframework.boot:spring-boot-starter-opentelemetry")
  implementation("org.springframework.boot:spring-boot-starter-validation")
  implementation("org.springframework.boot:spring-boot-starter-security-oauth2-resource-server")
  implementation("org.springframework.boot:spring-boot-starter-actuator")
  implementation("org.springframework.boot:spring-boot-starter-hateoas")
  implementation("org.springframework.boot:spring-boot-starter-data-rest")
  implementation("org.hibernate.orm:hibernate-core:${hibernateCoreVersion}")
  implementation("org.springframework.boot:spring-boot-starter-cache")
  implementation("com.github.ben-manes.caffeine:caffeine:$caffeineVersion")
  implementation("org.springframework.boot:spring-boot-starter-data-jpa") {
    exclude(group = "org.glassfish.jaxb", module = "jaxb-core")
  }
  implementation("org.springframework.cloud:spring-cloud-starter-stream-kafka") {
    exclude(group = "org.lz4", module = "lz4-java")
  }
  implementation("at.yawk.lz4:lz4-java:$lz4JavaVersion")
  implementation("io.micrometer:micrometer-tracing-bridge-otel:$micrometerVersion")
  implementation("io.micrometer:micrometer-registry-prometheus")
  implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:$springDocOpenApiVersion") {
    exclude(group = "org.apache.commons", module = "commons-lang3")
  }
  implementation("org.apache.commons:commons-lang3:$commonsLang3Version")
  implementation("org.openapitools:jackson-databind-nullable:$openApiToolsVersion")
  implementation("org.bouncycastle:bcprov-jdk18on:$bouncycastleVersion")
  implementation("org.postgresql:postgresql:$postgresJdbcVersion")
  implementation("org.apache.httpcomponents.client5:httpclient5:$httpClientVersion")
  implementation("org.apache.httpcomponents.core5:httpcore5:$httpCoreVersion")
  implementation("com.github.danielwegener:logback-kafka-appender:$kafkaAppender") {
    exclude(group = "org.lz4", module = "lz4-java")
  }
  implementation("io.github.springwolf:springwolf-kafka:$springWolfAsyncApiVersion") {
    exclude(group = "org.lz4", module = "lz4-java")
  }
  implementation("io.github.springwolf:springwolf-ui:$springWolfUiAsyncApiVersion")
  implementation("io.github.springwolf:springwolf-cloud-stream:$springWolfAsyncApiVersion")

  implementation("org.mozilla:rhino-engine:$rhinoScriptVersion")

  //jaxb
  implementation("org.apache.ws.xmlschema:xmlschema-core:$xmlSchemaVersion")
  runtimeOnly("org.glassfish.jaxb:jaxb-runtime:$jaxbVersion")
  jaxb("org.glassfish.jaxb:jaxb-runtime:$jaxbVersion")
  jaxb("com.sun.xml.bind:jaxb-xjc:$jaxbVersion")
  jaxb("com.sun.xml.bind:jaxb-jxc:$jaxbVersion")
  jaxb("com.sun.xml.bind:jaxb-core:$jaxbVersion")
  jaxb("jakarta.xml.bind:jakarta.xml.bind-api:$jaxbApiVersion")
  jaxb("jakarta.activation:jakarta.activation-api:$activationVersion")
  jaxbext("org.jvnet.jaxb:jaxb-plugin-annotate:3.0.2")
  jaxbext("org.slf4j:slf4j-simple:2.0.16") // see https://github.com/IntershopCommunicationsAG/jaxb-gradle-plugin/issues/37

  compileOnly("org.projectlombok:lombok")
  annotationProcessor("org.projectlombok:lombok")
  testAnnotationProcessor("org.projectlombok:lombok")

  //	Testing
  testImplementation("org.springframework.boot:spring-boot-starter-webmvc-test")
  testImplementation("org.springframework.boot:spring-boot-starter-security-test")
  testImplementation("org.mockito:mockito-core")
  testImplementation("org.projectlombok:lombok")
  testImplementation("com.h2database:h2")
  testImplementation("uk.co.jemos.podam:podam:$podamVersion")

}

tasks.withType<Test> {
  useJUnitPlatform()
  finalizedBy(tasks.jacocoTestReport)
}

val mockitoAgent = configurations.create("mockitoAgent")
dependencies {
  mockitoAgent("org.mockito:mockito-core") { isTransitive = false }
}
tasks {
  jar {
    from("${rootProject.projectDir}") {
      include("LICENSE.md")
      into("META-INF")
    }
  }
  test {
    jvmArgs("-javaagent:${mockitoAgent.asPath}")
    testLogging.events = setOf(TestLogEvent.FAILED)
    testLogging.exceptionFormat = TestExceptionFormat.FULL
  }
}

tasks.jacocoTestReport {
  dependsOn(tasks.test)
  reports {
    xml.required = true
  }
}

val projectInfo = mapOf(
  "artifactId" to project.name,
  "version" to project.version
)

tasks {
  val processResources by getting(ProcessResources::class) {
    filesMatching("**/application.yml") {
      expand(projectInfo)
    }
  }
  processResources.dependsOn("dependenciesBuild")
}


jaxb {
  javaGen {
    register("Assessment") {
      extension = true
      args = listOf("-xmlschema")
      outputDir = file("$projectDir/build/generated/jaxb/java")
      schema = file("src/main/resources/xsd/PagInf_Dovuti_Pagati_6_2_0.xsd")
      bindings = layout.files("$rootDir/src/main/resources/xsd/bindings.xjb")
    }
    register("bilancioDefault") {
      extension = true
      args = listOf("-xmlschema")
      outputDir = file("$projectDir/build/generated/jaxb/java")
      schema = file("src/main/resources/xsd/bilancioDefault.xsd")
      bindings = layout.files("$rootDir/src/main/resources/xsd/bindings-bilancioDefault.xjb")
    }
  }
}

tasks.compileJava {
  dependsOn("dependenciesBuild")
}

tasks.register("dependenciesBuild") {
  group = "AutomaticallyGeneratedCode"
  description = "grouping all together automatically generate code tasks"

  dependsOn(
    "openApiGenerate",
    "openApiGenerateDEBTPOSITIONS",
    "openApiGeneratePROCESSEXECUTION",
    "openApiGenerateORGANIZATION",
    "openApiGenerateWORKFLOWHUB",
    "jaxbJavaGenAssessment"
  )
}

configure<SourceSetContainer> {
  named("main") {
    java.srcDir("$projectDir/build/generated/src/main/java")
  }
}

springBoot {
  buildInfo()
  mainClass.value("it.gov.pagopa.pu.classification.ClassificationApplication")
}

openApiGenerate {
  generatorName.set("spring")
  inputSpec.set("$rootDir/openapi/p4pa-classification.openapi.yaml")
  outputDir.set("$projectDir/build/generated")
  apiPackage.set("it.gov.pagopa.pu.classification.controller.generated")
  modelPackage.set("it.gov.pagopa.pu.classification.dto.generated")
  typeMappings.set(
    mapOf(
      "Treasury" to "it.gov.pagopa.pu.classification.model.Treasury",
      "PaymentsReporting" to "it.gov.pagopa.pu.classification.model.PaymentsReporting",
      "Classification" to "it.gov.pagopa.pu.classification.model.Classification",
      "ClassificationView" to "it.gov.pagopa.pu.classification.dto.ClassificationViewDTO",
      "Assessments" to "it.gov.pagopa.pu.classification.model.Assessments",
      "PaymentNotificationDTO" to "it.gov.pagopa.pu.classification.dto.PaymentNotificationDTO",
      "PersonDTO" to "it.gov.pagopa.pu.debtpositions.dto.generated.PersonDTO",
      "FullClassificationView" to "it.gov.pagopa.pu.classification.dto.FullClassificationViewDTO",
      "ClassificationsEnum" to "it.gov.pagopa.pu.classification.enums.ClassificationsEnum",
      "TreasuredClassificationDTO" to "it.gov.pagopa.pu.classification.model.view.TreasuredClassificationView",
      "ClassificationDetailViewDTO" to "it.gov.pagopa.pu.classification.dto.ClassificationDetailViewDTO",
      "AssessmentsStatusEnum" to "it.gov.pagopa.pu.classification.enums.AssessmentStatus",
      "DebtPositionDTO" to "it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionDTO",
      "AssessmentsRegistry" to "it.gov.pagopa.pu.classification.model.AssessmentsRegistry",
      "AssessmentsDetail" to "it.gov.pagopa.pu.classification.model.AssessmentsDetail",
      "ClassificationPaidInstallmentsView" to "it.gov.pagopa.pu.classification.model.view.classification.ClassificationPaidInstallmentsView"
    )
  )
  configOptions.set(
    mapOf(
      "dateLibrary" to "java8",
      "requestMappingMode" to "api_interface",
      "useSpringBoot4" to "true",
      "useJackson3" to "true",
      "interfaceOnly" to "true",
      "useTags" to "true",
      "useBeanValidation" to "true",
      "generateConstructorWithAllArgs" to "true",
      "generatedConstructorWithRequiredArgs" to "true",
      "enumPropertyNaming" to "original",
      "additionalModelTypeAnnotations" to "@lombok.experimental.SuperBuilder(toBuilder = true)"
    )
  )
}

var targetEnv = when (Objects.requireNonNullElse(
  System.getProperty("targetBranch"),
  grgit.branch.current().name
)) {
  "uat" -> "uat"
  "main" -> "main"
  else -> "develop"
}

tasks.register<GenerateTask>("openApiGenerateDEBTPOSITIONS") {
  group = "AutomaticallyGeneratedCode"
  description = "openapi"

  generatorName.set("java")
  remoteInputSpec.set("https://raw.githubusercontent.com/pagopa/p4pa-doc/refs/heads/main/openapi/$targetEnv/internal/p4pa-debt-positions.generated.openapi.json")
  outputDir.set("$projectDir/build/generated")
  invokerPackage.set("it.gov.pagopa.pu.debtpositions.generated")
  apiPackage.set("it.gov.pagopa.pu.debtpositions.client.generated")
  modelPackage.set("it.gov.pagopa.pu.debtpositions.dto.generated")
  typeMappings.set(mapOf("LocalDateTime" to "java.time.LocalDateTime"))
  configOptions.set(
    mapOf(
      "swaggerAnnotations" to "false",
      "openApiNullable" to "false",
      "dateLibrary" to "java8",
      "serializableModel" to "true",
      "useSpringBoot4" to "true",
      "useJackson3" to "true",
      "useJakartaEe" to "true",
      "useOneOfInterfaces" to "true",
      "useBeanValidation" to "true",
      "serializationLibrary" to "jackson",
      "generateSupportingFiles" to "true",
      "generateConstructorWithAllArgs" to "true",
      "generatedConstructorWithRequiredArgs" to "true",
      "enumPropertyNaming" to "original",
      "additionalModelTypeAnnotations" to "@lombok.experimental.SuperBuilder(toBuilder = true)"
    )
  )
  library.set("resttemplate")
}


tasks.register<GenerateTask>("openApiGeneratePROCESSEXECUTION") {
  group = "openapi"
  description = "description"

  generatorName.set("java")
  remoteInputSpec.set("https://raw.githubusercontent.com/pagopa/p4pa-doc/refs/heads/main/openapi/$targetEnv/internal/p4pa-process-executions.generated.openapi.json")
  outputDir.set("$projectDir/build/generated")
  invokerPackage.set("it.gov.pagopa.pu.processexecutions.generated")
  apiPackage.set("it.gov.pagopa.pu.processexecutions.client.generated")
  modelPackage.set("it.gov.pagopa.pu.processexecutions.dto.generated")
  typeMappings.set(
    mapOf(
      "LocalDateTime" to "java.time.LocalDateTime"
    )
  )
  configOptions.set(
    mapOf(
      "swaggerAnnotations" to "false",
      "openApiNullable" to "false",
      "dateLibrary" to "java8",
      "serializableModel" to "true",
      "useSpringBoot4" to "true",
      "useJackson3" to "true",
      "useJakartaEe" to "true",
      "useOneOfInterfaces" to "true",
      "useBeanValidation" to "true",
      "serializationLibrary" to "jackson",
      "generateSupportingFiles" to "true",
      "generateConstructorWithAllArgs" to "true",
      "generatedConstructorWithRequiredArgs" to "true",
      "enumPropertyNaming" to "original",
      "additionalModelTypeAnnotations" to "@lombok.experimental.SuperBuilder(toBuilder = true)"
    )
  )
  library.set("resttemplate")
}

tasks.register<GenerateTask>("openApiGenerateORGANIZATION") {
  group = "AutomaticallyGeneratedCode"
  description = "openapi"

  generatorName.set("java")
  remoteInputSpec.set("https://raw.githubusercontent.com/pagopa/p4pa-doc/refs/heads/main/openapi/$targetEnv/internal/p4pa-organization.generated.openapi.json")
  outputDir.set("$projectDir/build/generated")
  invokerPackage.set("it.gov.pagopa.pu.organization.generated")
  apiPackage.set("it.gov.pagopa.pu.organization.client.generated")
  modelPackage.set("it.gov.pagopa.pu.organization.dto.generated")
  configOptions.set(
    mapOf(
      "swaggerAnnotations" to "false",
      "openApiNullable" to "false",
      "dateLibrary" to "java8",
      "serializableModel" to "true",
      "useSpringBoot4" to "true",
      "useJackson3" to "true",
      "useJakartaEe" to "true",
      "useOneOfInterfaces" to "true",
      "useBeanValidation" to "true",
      "serializationLibrary" to "jackson",
      "generateSupportingFiles" to "true",
      "generateConstructorWithAllArgs" to "true",
      "generatedConstructorWithRequiredArgs" to "true",
      "enumPropertyNaming" to "original",
      "additionalModelTypeAnnotations" to "@lombok.experimental.SuperBuilder(toBuilder = true)"
    )
  )
  library.set("resttemplate")
}

tasks.register<GenerateTask>("openApiGenerateWORKFLOWHUB") {
  group = "AutomaticallyGeneratedCode"
  description = "openapi"

  generatorName.set("java")
  remoteInputSpec.set("https://raw.githubusercontent.com/pagopa/p4pa-doc/refs/heads/main/openapi/$targetEnv/internal/p4pa-workflow-hub.generated.openapi.json")
  outputDir.set("$projectDir/build/generated")
  invokerPackage.set("it.gov.pagopa.pu.workflowhub.generated")
  apiPackage.set("it.gov.pagopa.pu.workflowhub.client.generated")
  modelPackage.set("it.gov.pagopa.pu.workflowhub.dto.generated")
  configOptions.set(
    mapOf(
      "swaggerAnnotations" to "false",
      "openApiNullable" to "false",
      "dateLibrary" to "java8",
      "serializableModel" to "true",
      "useSpringBoot4" to "true",
      "useJackson3" to "true",
      "useJakartaEe" to "true",
      "useOneOfInterfaces" to "true",
      "useBeanValidation" to "true",
      "serializationLibrary" to "jackson",
      "generateSupportingFiles" to "true",
      "generateConstructorWithAllArgs" to "true",
      "generatedConstructorWithRequiredArgs" to "true",
      "enumPropertyNaming" to "original",
      "additionalModelTypeAnnotations" to "@lombok.experimental.SuperBuilder(toBuilder = true)"
    )
  )
  library.set("resttemplate")
}
