# p4pa-classification

This application belong to the **entity** tier of the **Piattaforma Unitaria** product.

See [PU Microservice Architecture](https://pagopa.atlassian.net/wiki/spaces/SPAC/pages/1405845916/Architettura+microservizi) for more details.

## 🧱 Role

* To handle the payments reporting; 
* To handle the treasury;
* To handle the payment notifications; 
* To handle the classifications;
* To handle the assessments. 

## 🌐 APIs
See [OpenAPI](openapi/generated.openapi.json), exposed through the following path:
* `/swagger-ui/index.html`

### 📌 Relevant APIs
* `GET /export/organization/{organizationId}/full-classifications`: To export classifications;
* `GET /export/organization/{organizationId}/classifications`: To export classifications (no payment notifications).

### 📌 Common HTTP status returned:
* `401`: Invalid access token provided, thus a new login is required;
* `403`: Trying to access a not authorized resource.

## 🔎 Monitoring
See available actuator endpoints through the following path:
* `/actuator`

### 📌 Relevant endpoints
* Health (provide an accessToken to see details): `/actuator/health`
  * Liveness: `/actuator/health/liveness`
  * Readiness: `/actuator/health/readiness`
* Metrics: `/actuator/metrics`
  * Prometheus: `/actuator/prometheus`

Further endpoints are exposed through the JMX console.

## ✏️ Logging
See [log configured pattern](/src/main/resources/logback-spring.xml).

## 🔗 Dependencies

### 🗄️ Resources
* PostgreSQL
* PostgreSQL (citizen)

### 🧩 Microservices
* [p4pa-debt-positions](https://github.com/pagopa/p4pa-debt-positions):
  * To retrieve DebtPositionTypeOrg entities when creating `assessment_registry` and `assessments`;
  * To apply the right visibility during classifications' export;

## 🗃️ Entities handled
* `assessments_registry`
* `assessments`
* `assessments_detail` 
* `classification`
* `payment_notification` 
* `payments_reporting`
* `treasury`

## 🔧 Configuration

See [application.yml](src/main/resources/application.yml) for each configurable property.

### 📌 Relevant configurations

#### 🌐 Application Server
| ENV         | DESCRIPTION                       | DEFAULT |
|-------------|-----------------------------------|---------|
| SERVER_PORT | Application server listening port | 8080    |

#### ✏️ Logging
| ENV                                   | DESCRIPTION                                                                                                                                                                     | DEFAULT |
|---------------------------------------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|---------|
| LOG_LEVEL_ROOT                        | Base level                                                                                                                                                                      | INFO    |
| LOG_LEVEL_PAGOPA                      | Base level of custom classes                                                                                                                                                    | INFO    |
| LOG_LEVEL_SPRING                      | Level applied to Spring framework                                                                                                                                               | INFO    |
| LOG_LEVEL_SPRING_BOOT_AVAILABILITY    | To print availability events                                                                                                                                                    | DEBUG   |
| LOGGING_LEVEL_API_REQUEST_EXCEPTION   | Level applied to APIs exception                                                                                                                                                 | INFO    |
| LOG_LEVEL_PERFORMANCE_LOG             | Level applied to [PerformanceLog](https://pagopa.atlassian.net/wiki/spaces/SPAC/pages/1540096383/Logging#2.2.-Log-di-performance)                                               | INFO    |
| LOG_LEVEL_PERFORMANCE_LOG_API_REQUEST | Level applied to [API Performance Log](https://pagopa.atlassian.net/wiki/spaces/SPAC/pages/1540096383/Logging#2.2.2.1.-Log-di-perfomance-per-le-API)                            | INFO    |
| LOG_LEVEL_PERFORMANCE_LOG_REST_INVOKE | Level applied to [REST invoke Performance Log](https://pagopa.atlassian.net/wiki/spaces/SPAC/pages/1540096383/Logging#2.2.2.2.-Log-di-performance-per-i-servizi-REST-integrati) | INFO    |

#### 🔁 Integrations

##### 🗄️ Resources
| ENV                        | DESCRIPTION                                                                           | DEFAULT                                                                                                                      |
|----------------------------|---------------------------------------------------------------------------------------|------------------------------------------------------------------------------------------------------------------------------|
| SHOW_SQL                   | To print SQL statements                                                               | false                                                                                                                        |
| CLASSIFICATION_DB_URL      | PostgreSQL connection string (to use in order to customize the entire string)         | jdbc:postgresql://${CLASSIFICATION_DB_HOST}:${CLASSIFICATION_DB_PORT}/${CLASSIFICATION_DB_NAME}?currentSchema=classification |
| CLASSIFICATION_DB_HOST     | PostgreSQL Host                                                                       | localhost                                                                                                                    |
| CLASSIFICATION_DB_PORT     | PostgreSQL port                                                                       | 5432                                                                                                                         |
| CLASSIFICATION_DB_NAME     | PostgreSQL Database name                                                              | payhub                                                                                                                       |
| CLASSIFICATION_DB_USER     | PostgreSQL username                                                                   |                                                                                                                              |
| CLASSIFICATION_DB_PASSWORD | PostgreSQL password                                                                   |                                                                                                                              |
| CITIZENDB_URL              | Citizen PostgreSQL connection string (to use in order to customize the entire string) | jdbc:postgresql://${CITIZENDB_HOST}:${CITIZENDB_PORT}/citizen                                                                |
| CITIZENDB_HOST             | Citizen PostgreSQL Host                                                               | localhost                                                                                                                    |
| CITIZENDB_PORT             | Citizen PostgreSQL port                                                               | 5432                                                                                                                         |
| CITIZENDB_NAME             | Citizen PostgreSQL Database name                                                      | payhub                                                                                                                       |
| CITIZENDB_USER             | Citizen PostgreSQL username                                                           |                                                                                                                              |
| CITIZENDB_PASSWORD         | Citizen PostgreSQL password                                                           |                                                                                                                              |

##### 🔗 REST
| ENV                                               | DESCRIPTION                               | DEFAULT |
|---------------------------------------------------|-------------------------------------------|---------|
| DEFAULT_REST_CONNECTION_POOL_SIZE                 | Default connection pool size              | 10      |
| DEFAULT_REST_CONNECTION_POOL_SIZE_PER_ROUTE       | Default connection pool size per route    | 5       |
| DEFAULT_REST_CONNECTION_POOL_TIME_TO_LIVE_MINUTES | Default connection pool TTL (minutes)     | 10      |
| DEFAULT_REST_TIMEOUT_CONNECT_MILLIS               | Default connection timeout (milliseconds) | 120000  |
| DEFAULT_REST_TIMEOUT_READ_MILLIS                  | Default read timeout (milliseconds)       | 120000  |

##### 🧩 Microservices
| ENV                                      | DESCRIPTION                                         | DEFAULT |
|------------------------------------------|-----------------------------------------------------|---------|
| DEBT_POSITION_BASE_URL                   | DebtPositions microservice URL                      |         |
| DEBT_POSITION_MAX_ATTEMPTS               | DebtPositions API max attempts                      | 3       |
| DEBT_POSITION_WAIT_TIME_MILLIS           | DebtPositions retry waiting time (milliseconds)     | 500     |
| DEBT_POSITION_PRINT_BODY_WHEN_ERROR      | To print body when an error occurs                  | true    |

#### 💼 Business logic
| ENV                                     | DESCRIPTION                                                             | DEFAULT                           |
|-----------------------------------------|-------------------------------------------------------------------------|-----------------------------------|
| DATA_EXPORT_MAX_TOTAL_ELEMENTS          | Maximum number of elements that could be exported                       | 100000                            |
| CLASSIFICATION_VIEW_MAX_MONTHS_INTERVAL | Classifications data: Maximum number of months that could be exported   | 6                                 |                                          
| CLASSIFICATION_VIEW_MAX_TOTAL_ELEMENTS  | Classifications data: Maximum number of elements that could be exported | ${DATA_EXPORT_MAX_TOTAL_ELEMENTS} | 

#### 🔑 keys
| ENV                          | DESCRIPTION                                         | DEFAULT |
|------------------------------|-----------------------------------------------------|---------|
| JWT_TOKEN_PUBLIC_KEY         | p4pa-auth JWT public key                            |         |
| DATA_CIPHER_HASH_PEPPER      | Base64 encoded key (256 bit) used to calculate hash |         |
| DATA_CIPHER_ENCRYPT_PASSWORD | Base64 encoded key (256 bit) used to encrypt data   |         |

## 🛠️ Getting Started

### 📝 Prerequisites

Ensure the following tools are installed on your machine:

1. **Java 21+**
2. **Gradle** (or use the Gradle wrapper included in the repository)
3. **Docker** (to build and run on an isolated environment, optional)

### 🔐 Write Locks

```sh
./gradlew dependencies --write-locks
```

### ⚙️ Build

```sh
./gradlew clean build
```

### 🧪 Test

#### 📌 JUnit
```sh
./gradlew test
```

### 🚀 Run local

```sh
./gradlew bootRun
```

### 🐳 Build & run through Docker
```sh
docker build -t <APP_NAME> .
docker run --env-file <ENV_FILE> <APP_NAME>
```
