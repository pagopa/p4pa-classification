import org.openapitools.generator.gradle.plugin.tasks.GenerateTask
import java.util.*

plugins {
  java
  id("org.springframework.boot") version "3.5.3"
  id("io.spring.dependency-management") version "1.1.7"
  jacoco
  id("org.sonarqube") version "6.2.0.5505"
  id("com.github.ben-manes.versions") version "0.52.0"
  id("org.openapi.generator") version "7.13.0"
  id("org.ajoberstar.grgit") version "5.3.2"
  id("com.gorylenko.gradle-git-properties") version "2.5.0"
  id("com.intershop.gradle.jaxb") version "7.0.1"
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
}

repositories {
  mavenCentral()
}

val springDocOpenApiVersion = "2.8.9"
val openApiToolsVersion = "0.2.6"
val micrometerVersion = "1.5.1"
val postgresJdbcVersion = "42.7.7"
val bouncycastleVersion = "1.81"
val httpClientVersion = "5.5"
val activationVersion = "2.1.3"
val jaxbVersion = "4.0.5"
val jaxbApiVersion = "4.0.2"
val xmlSchemaVersion = "2.3.1"
val podamVersion = "8.0.2.RELEASE"

dependencies {
  implementation("org.springframework.boot:spring-boot-starter")
  implementation("org.springframework.boot:spring-boot-starter-web")
  implementation("org.springframework.boot:spring-boot-starter-validation")
  implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
  implementation("org.springframework.boot:spring-boot-starter-actuator")
  implementation("org.springframework.boot:spring-boot-starter-data-rest")
  implementation("org.springframework.boot:spring-boot-starter-data-jpa") {
    exclude(group = "org.glassfish.jaxb", module = "jaxb-core")
  }
  implementation("io.micrometer:micrometer-tracing-bridge-otel:$micrometerVersion")
  implementation("io.micrometer:micrometer-registry-prometheus")
  implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:$springDocOpenApiVersion")
  implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")
  implementation("org.openapitools:jackson-databind-nullable:$openApiToolsVersion")
  implementation("org.bouncycastle:bcprov-jdk18on:$bouncycastleVersion")
  implementation("org.postgresql:postgresql:$postgresJdbcVersion")
  implementation("org.apache.httpcomponents.client5:httpclient5:$httpClientVersion")

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
  testImplementation("org.springframework.boot:spring-boot-starter-test")
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
  test {
    jvmArgs("-javaagent:${mockitoAgent.asPath}")
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
}


jaxb {
  javaGen {
    register("Assessment") {
      extension = true
      args = listOf("-xmlschema")
      outputDir = file("$projectDir/build/generated/jaxb/java")
      schema = file("src/main/resources/xsd/PagInf_Dovuti_Pagati_6_2_0.xsd")
    }
    register("bilancio") {
      extension = true
      args = listOf("-xmlschema")
      outputDir = file("$projectDir/build/generated/jaxb/java")
      schema = file("src/main/resources/xsd/bilancioDefault.xsd")
    }
  }
}



configurations {
  compileClasspath {
    resolutionStrategy.activateDependencyLocking()
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
  typeMappings.set(mapOf(
    "Treasury" to "it.gov.pagopa.pu.classification.model.Treasury",
    "PaymentsReporting" to "it.gov.pagopa.pu.classification.model.PaymentsReporting",
    "Classification" to "it.gov.pagopa.pu.classification.model.Classification",
    "ClassificationView" to "it.gov.pagopa.pu.classification.dto.ClassificationViewDTO",
    "Assessments" to "it.gov.pagopa.pu.classification.model.Assessments",
    "PaymentNotificationDTO" to "it.gov.pagopa.pu.classification.dto.PaymentNotificationDTO",
    "PersonDTO" to "it.gov.pagopa.pu.debtposition.dto.generated.PersonDTO",
    "FullClassificationView" to "it.gov.pagopa.pu.classification.dto.FullClassificationViewDTO",
    "ClassificationsEnum" to "it.gov.pagopa.pu.classification.enums.ClassificationsEnum",
    "TreasuredClassificationDTO" to "it.gov.pagopa.pu.classification.model.view.TreasuredClassificationView",
    "ClassificationDetailViewDTO" to "it.gov.pagopa.pu.classification.dto.ClassificationDetailViewDTO",
    "AssessmentsStatusEnum" to "it.gov.pagopa.pu.classification.enums.AssessmentStatus",
    "DebtPositionDTO" to "it.gov.pagopa.pu.debtposition.dto.generated.DebtPositionDTO",
    "AssessmentsRegistry" to "it.gov.pagopa.pu.classification.model.AssessmentsRegistry",
    "AssessmentsDetail" to "it.gov.pagopa.pu.classification.model.AssessmentsDetail"
  ))
  configOptions.set(mapOf(
    "dateLibrary" to "java8",
    "requestMappingMode" to "api_interface",
    "useSpringBoot3" to "true",
    "interfaceOnly" to "true",
    "useTags" to "true",
    "useBeanValidation" to "true",
    "generateConstructorWithAllArgs" to "true",
    "generatedConstructorWithRequiredArgs" to "true",
    "enumPropertyNaming" to "original",
    "additionalModelTypeAnnotations" to "@lombok.experimental.SuperBuilder(toBuilder = true)"
  ))
}

var targetEnv = when (Objects.requireNonNullElse(System.getProperty("targetBranch"), grgit.branch.current().name)) {
  "uat" -> "uat"
  "main" -> "main"
  else -> "develop"
}

tasks.register<GenerateTask>("openApiGenerateDEBTPOSITIONS") {
  group = "AutomaticallyGeneratedCode"
  description = "openapi"

  generatorName.set("java")
  remoteInputSpec.set("https://raw.githubusercontent.com/pagopa/p4pa-debt-positions/refs/heads/$targetEnv/openapi/generated.openapi.json")
  outputDir.set("$projectDir/build/generated")
  invokerPackage.set("it.gov.pagopa.pu.debtposition.generated")
  apiPackage.set("it.gov.pagopa.pu.debtposition.client.generated")
  modelPackage.set("it.gov.pagopa.pu.debtposition.dto.generated")
  typeMappings.set(mapOf("LocalDateTime" to "java.time.LocalDateTime"))
  configOptions.set(mapOf(
    "swaggerAnnotations" to "false",
    "openApiNullable" to "false",
    "dateLibrary" to "java8",
    "serializableModel" to "true",
    "useSpringBoot3" to "true",
    "useJakartaEe" to "true",
    "useOneOfInterfaces" to "true",
    "useBeanValidation" to "true",
    "serializationLibrary" to "jackson",
    "generateSupportingFiles" to "true",
    "generateConstructorWithAllArgs" to "true",
    "generatedConstructorWithRequiredArgs" to "true",
    "enumPropertyNaming" to "original",
    "additionalModelTypeAnnotations" to "@lombok.experimental.SuperBuilder(toBuilder = true)"
  ))
  library.set("resttemplate")
}


tasks.register<GenerateTask>("openApiGeneratePROCESSEXECUTION") {
  group = "openapi"
  description = "description"

  generatorName.set("java")
  remoteInputSpec.set("https://raw.githubusercontent.com/pagopa/p4pa-process-executions/refs/heads/$targetEnv/openapi/generated.openapi.json")
  outputDir.set("$projectDir/build/generated")
  apiPackage.set("it.gov.pagopa.pu.p4paprocessexecutions.controller.generated")
  modelPackage.set("it.gov.pagopa.pu.p4paprocessexecutions.dto.generated")
  typeMappings.set(mapOf(
    "LocalDateTime" to "java.time.LocalDateTime"
  ))
  configOptions.set(mapOf(
    "swaggerAnnotations" to "false",
    "openApiNullable" to "false",
    "dateLibrary" to "java8",
    "serializableModel" to "true",
    "useSpringBoot3" to "true",
    "useJakartaEe" to "true",
    "useOneOfInterfaces" to "true",
    "useBeanValidation" to "true",
    "serializationLibrary" to "jackson",
    "generateSupportingFiles" to "true",
    "generateConstructorWithAllArgs" to "true",
    "generatedConstructorWithRequiredArgs" to "true",
    "enumPropertyNaming" to "original",
    "additionalModelTypeAnnotations" to "@lombok.experimental.SuperBuilder(toBuilder = true)"
  ))
  library.set("resttemplate")
}
