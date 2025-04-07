package it.gov.pagopa.pu.classification.util.faker;


import it.gov.pagopa.pu.classification.dto.generated.PersonDTO;
import it.gov.pagopa.pu.debtposition.dto.generated.Person;
import it.gov.pagopa.pu.debtposition.dto.generated.PersonEntityType;

public class PersonFaker {

  public static Person buildPerson(){
    return Person.builder()
      .entityType(PersonEntityType.F)
      .fiscalCode("uniqueIdentifierCode")
      .fullName("fullName")
      .address("address")
      .civic("civic")
      .postalCode("postalCode")
      .location("location")
      .province("province")
      .nation("nation")
      .email("email@test.it")
      .build();
  }

  public static PersonDTO buildPersonDTO(){
    return PersonDTO.builder()
      .entityType(PersonDTO.EntityTypeEnum.F)
      .fiscalCode("uniqueIdentifierCode")
      .fullName("fullName")
      .address("address")
      .civic("civic")
      .postalCode("postalCode")
      .location("location")
      .province("province")
      .nation("nation")
      .email("email@test.it")
      .build();
  }
}
