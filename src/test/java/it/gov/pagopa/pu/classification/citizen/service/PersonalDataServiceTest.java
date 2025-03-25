package it.gov.pagopa.pu.classification.citizen.service;

import it.gov.pagopa.pu.classification.citizen.enums.PersonalDataType;
import it.gov.pagopa.pu.classification.citizen.model.PersonalData;
import it.gov.pagopa.pu.classification.citizen.repository.PersonalDataRepository;
import it.gov.pagopa.pu.classification.dto.ClassificationViewDTO;
import it.gov.pagopa.pu.classification.exception.custom.NotFoundException;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class PersonalDataServiceTest {

  @Mock
  private PersonalDataRepository repositoryMock;
  @Mock
  private DataCipherService cipherServiceMock;

  private PersonalDataService service;


  @BeforeEach
  void init() {
    service = new PersonalDataService(repositoryMock, cipherServiceMock);
  }

  @AfterEach
  void verifyNotMoreInteractions() {
    Mockito.verifyNoMoreInteractions(
      repositoryMock,
      cipherServiceMock);
  }

  @Test
  void testInsert() {
    // Given
    ClassificationViewDTO pii = mock(ClassificationViewDTO.class);

    byte[] cipherData = new byte[0];
    Mockito.when(cipherServiceMock.encryptObj(pii)).thenReturn(cipherData);
    PersonalData personalDataInput = PersonalData.builder()
      .type("CLASSIFICATION")
      .data(cipherData)
      .build();

    long piiId = -1L;
    PersonalData personalDataOutput = PersonalData.builder()
      .id(piiId)
      .type("CLASSIFICATION")
      .data(cipherData)
      .build();

    Mockito.when(repositoryMock.save(personalDataInput)).thenReturn(personalDataOutput);

    // When
    long insert = service.insert(pii, PersonalDataType.CLASSIFICAITON);

    // Then
    Assertions.assertEquals(piiId, insert);
  }

  //region get

  @Test
  void givenValidPersonalDataIdWhenGetThenOk(){
    //given
    ClassificationViewDTO expected = mock(ClassificationViewDTO.class);
    Mockito.when(repositoryMock.findById(1L)).thenReturn(
      Optional.of(PersonalData.builder().id(1L).data(new byte[0]).type(PersonalDataType.CLASSIFICAITON.name()).build()));
    Mockito.when(cipherServiceMock.decryptObj(new byte[0], ClassificationViewDTO.class)).thenReturn(expected);
    //when
    ClassificationViewDTO classificationViewDTO = service.get(1L, ClassificationViewDTO.class);
    //then
    Assertions.assertTrue(EqualsBuilder.reflectionEquals(expected, classificationViewDTO,true, null, true));
    Mockito.verify(repositoryMock, Mockito.times(1)).findById(1L);
    Mockito.verify(cipherServiceMock, Mockito.times(1)).decryptObj(new byte[0], ClassificationViewDTO.class);
  }

  @Test
  void givenNotFoundPersonalDataIdWhenGetThenException(){
    //given
    Mockito.when(repositoryMock.findById(1L)).thenReturn(Optional.empty());
    //when
    NotFoundException notFoundException = Assertions.assertThrows(NotFoundException.class, () -> service.get(1L, ClassificationViewDTO.class));
    //then
    Assertions.assertEquals("PII Entity not found for id 1", notFoundException.getMessage());
    Mockito.verify(repositoryMock, Mockito.times(1)).findById(1L);
    Mockito.verifyNoInteractions(cipherServiceMock);
  }
  //endregion

  @Test
  void testDelete(){
    // Given
    long id = 1L;

    // When
    service.delete(id);

    // Then
    Mockito.verify(repositoryMock).deleteById(id);
  }
}
