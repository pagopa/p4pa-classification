package it.gov.pagopa.pu.classification.mapper;

import it.gov.pagopa.pu.classification.dto.generated.PersonDTO;
import it.gov.pagopa.pu.debtposition.dto.generated.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static it.gov.pagopa.pu.classification.util.TestUtils.checkNotNullFields;
import static it.gov.pagopa.pu.classification.util.TestUtils.reflectionEqualsByName;
import static it.gov.pagopa.pu.classification.util.faker.PersonFaker.buildPerson;
import static it.gov.pagopa.pu.classification.util.faker.PersonFaker.buildPersonDTO;


@ExtendWith(MockitoExtension.class)
class PersonMapperTest {

  private PersonMapper personMapper;

  @BeforeEach
  void setUp() {
    personMapper = new PersonMapper();
  }

  @Test
  void givenValidPersonDTO_WhenMapToModel_ThenReturnPerson() {
    Person personExpected = buildPerson();
    PersonDTO personDTO = buildPersonDTO();

    Person result = personMapper.mapToModel(personDTO);

    reflectionEqualsByName(personExpected, result);
    checkNotNullFields(result);
  }

  @Test
  void givenValidPerson_WhenMapToDto_ThenReturnPersonDTO() {
    Person person = buildPerson();
    PersonDTO personDTOExpected = buildPersonDTO();

    PersonDTO result = personMapper.mapToDto(person);

    reflectionEqualsByName(personDTOExpected, result);
    checkNotNullFields(result);
  }
}
