package it.gov.pagopa.pu.classification.util.faker;


import it.gov.pagopa.pu.debtpositions.dto.generated.PersonDTO;
import it.gov.pagopa.pu.debtpositions.dto.generated.PersonEntityType;

public class PersonDTOFaker {

  public static PersonDTO buildPerson(){
    return PersonDTO.builder()
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

}
