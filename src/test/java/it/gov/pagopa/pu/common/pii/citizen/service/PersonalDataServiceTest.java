package it.gov.pagopa.pu.common.pii.citizen.service;

import it.gov.pagopa.pu.classification.dto.ClassificationViewDTO;
import it.gov.pagopa.pu.classification.exception.custom.NotFoundException;
import it.gov.pagopa.pu.classification.util.TestUtils;
import it.gov.pagopa.pu.common.pii.citizen.enums.PersonalDataType;
import it.gov.pagopa.pu.common.pii.citizen.model.PersonalData;
import it.gov.pagopa.pu.common.pii.citizen.repository.PersonalDataRepository;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.co.jemos.podam.api.PodamFactory;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@ExtendWith(MockitoExtension.class)
class PersonalDataServiceTest {

  @Mock
  private PersonalDataRepository repositoryMock;
  @Mock
  private DataCipherService cipherServiceMock;

  private PersonalDataService service;

  private final PodamFactory podamFactory = TestUtils.getPodamFactory();

  @BeforeEach
  void init() {
    service = new PersonalDataService(
      repositoryMock,
      cipherServiceMock
    );
  }

  @AfterEach
  void verifyNotMoreInteractions() {
    Mockito.verifyNoMoreInteractions(
      repositoryMock,
      cipherServiceMock
    );
  }

  @Test
  void testInsert() {
    // Given
    ClassificationViewDTO pii = new ClassificationViewDTO();

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
    long insert = service.insert(pii, PersonalDataType.CLASSIFICATION);

    // Then
    Assertions.assertEquals(piiId, insert);
  }

  //region get
  @Test
  void givenValidPersonalDataIdWhenGetThenOk() {
    // Given
    long personalDataId = 1L;
    ClassificationViewDTO expected = podamFactory.manufacturePojo(ClassificationViewDTO.class);
    Mockito.when(repositoryMock.findById(personalDataId)).thenReturn(
      Optional.of(PersonalData.builder().id(personalDataId).data(new byte[0]).type(PersonalDataType.CLASSIFICATION.name()).build()));
    Mockito.when(cipherServiceMock.decryptObj(new byte[0], ClassificationViewDTO.class)).thenReturn(expected);

    // When
    ClassificationViewDTO classificationViewDTO = service.get(personalDataId, ClassificationViewDTO.class);

    //Then
    Assertions.assertTrue(EqualsBuilder.reflectionEquals(expected, classificationViewDTO, true, null, true));
  }

  @Test
  void givenNotFoundPersonalDataIdWhenGetThenException() {
    // Given
    long personalDataId = 1L;
    Mockito.when(repositoryMock.findById(personalDataId)).thenReturn(Optional.empty());

    // When
    NotFoundException notFoundException = Assertions.assertThrows(NotFoundException.class, () -> service.get(personalDataId, ClassificationViewDTO.class));

    // Then
    Assertions.assertEquals("[PII_ENTITY_NOT_FOUND] PII Entity with id 1 not found", notFoundException.getMessage());
  }
//endregion

  //region getAll
  @Test
  void givenValidPersonalDataIdsWhenGetAllThenOk() {
    // Given
    long pId1 = 1L;
    long pId2 = 2L;
    Set<Long> personalDataIds = Set.of(pId1, pId2);
    ClassificationViewDTO pii1 = podamFactory.manufacturePojo(ClassificationViewDTO.class);
    ClassificationViewDTO pii2 = podamFactory.manufacturePojo(ClassificationViewDTO.class);

    Mockito.when(repositoryMock.findAllById(personalDataIds)).thenReturn(List.of(
      PersonalData.builder().id(pId1).data(new byte[0]).type(PersonalDataType.CLASSIFICATION.name()).build(),
      PersonalData.builder().id(pId2).data(new byte[0]).type(PersonalDataType.CLASSIFICATION.name()).build()
    ));
    Mockito.when(cipherServiceMock.decryptObj(new byte[0], ClassificationViewDTO.class))
      .thenReturn(pii1)
      .thenReturn(pii2);

    // When
    Map<Long, ClassificationViewDTO> results = service.getAll(personalDataIds, ClassificationViewDTO.class);

    //Then
    Assertions.assertTrue(EqualsBuilder.reflectionEquals(pii1, results.get(pId1), true, null, true));
    Assertions.assertTrue(EqualsBuilder.reflectionEquals(pii2, results.get(pId2), true, null, true));
  }

  @Test
  void givenNotFoundPersonalDataIdsWhenGetAllThenException() {
    // Given
    Set<Long> personalDataIds = Set.of(1L, 2L);
    Mockito.when(repositoryMock.findAllById(personalDataIds)).thenReturn(List.of(
      PersonalData.builder().id(1L).data(new byte[0]).type(PersonalDataType.CLASSIFICATION.name()).build()));
    Mockito.when(cipherServiceMock.decryptObj(new byte[0], ClassificationViewDTO.class)).thenReturn(podamFactory.manufacturePojo(ClassificationViewDTO.class));

    // When
    NotFoundException notFoundException = Assertions.assertThrows(NotFoundException.class, () -> service.getAll(personalDataIds, ClassificationViewDTO.class));

    // Then
    Assertions.assertEquals("[PII_ENTITY_NOT_FOUND] PII Entities with ids 2 not found", notFoundException.getMessage());
  }
//endregion

  @Test
  void testDelete() {
    // Given
    long id = 1L;

    // When
    service.delete(id);

    // Then
    Mockito.verify(repositoryMock).deleteById(id);
  }
}
