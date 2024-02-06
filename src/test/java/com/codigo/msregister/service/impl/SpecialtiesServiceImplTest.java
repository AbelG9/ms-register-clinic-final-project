package com.codigo.msregister.service.impl;

import com.codigo.appointmentslibrary.config.RedisService;
import com.codigo.appointmentslibrary.response.ResponseBase;
import com.codigo.appointmentslibrary.util.Util;
import com.codigo.msregister.aggregates.request.RequestSpecialties;
import com.codigo.msregister.entity.SpecialtiesEntity;
import com.codigo.msregister.repository.SpecialtiesRepository;
import com.codigo.msregister.service.SpecialtiesService;
import com.codigo.msregister.util.SpecialtiesValidations;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.codigo.appointmentslibrary.constants.Constants;
import org.mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;

class SpecialtiesServiceImplTest {
    @Mock
    SpecialtiesRepository specialtiesRepository;
    @Mock
    SpecialtiesValidations specialtiesValidations;
    @Mock
    RedisService redisService;

    @InjectMocks
    SpecialtiesServiceImpl specialtiesService;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        specialtiesService = new SpecialtiesServiceImpl(
                specialtiesRepository,
                specialtiesValidations,
                redisService
        );
    }

    @Test
    void createSpecialtiesSucceed() {
        int id = 1;
        SpecialtiesEntity specialtiesEntity = new SpecialtiesEntity(
                id,
                "Medicina Fisica",
                "Medicina para la recuperacion de ligamentos y tendones",
                "MEDFIS",
                1
        );
        RequestSpecialties requestSpecialties = new RequestSpecialties(
                "Medicina Fisica",
                "Medicina para la recuperacion de ligamentos y tendones",
                "MEDFIS"
        );
        ResponseBase responseExpected = new ResponseBase(Constants.CODE_SUCCESS, Constants.MESSAGE_SUCCESS, Optional.of(specialtiesEntity));

        Mockito.when(specialtiesValidations.validateInput(Mockito.any(RequestSpecialties.class), Mockito.anyBoolean())).thenReturn(true);
        Mockito.when(specialtiesRepository.save(Mockito.any(SpecialtiesEntity.class))).thenReturn(specialtiesEntity);

        ResponseBase responseObtained = specialtiesService.createSpecialties(requestSpecialties);

        Optional<?> dataObtained = responseObtained.getData();
        SpecialtiesEntity specialtiesEntityObtained = (SpecialtiesEntity) dataObtained.get();

        assertEquals(responseExpected.getCode(), responseObtained.getCode());
        assertEquals(responseExpected.getMessage(), responseObtained.getMessage());
        assertEquals(specialtiesEntityObtained.getName(), specialtiesEntity.getName());
        assertEquals(specialtiesEntityObtained.getDescription(), specialtiesEntity.getDescription());
        assertEquals(specialtiesEntityObtained.getCode(), specialtiesEntity.getCode());
    }

    @Test
    void createSpecialtiesError() {
        int id = 1;
        SpecialtiesEntity specialtiesEntity = new SpecialtiesEntity(
                id,
                "Medicina Fisica",
                "Medicina para la recuperacion de ligamentos y tendones",
                "MEDFIS",
                1
        );
        RequestSpecialties requestSpecialties = new RequestSpecialties(
                "Medicina Fisica",
                "Medicina para la recuperacion de ligamentos y tendones",
                "MEDFIS"
        );

        ResponseBase responseExpected = new ResponseBase(Constants.CODE_ERROR_DATA_INPUT, Constants.MESSAGE_ERROR_DATA_NOT_VALID, Optional.empty());

        Mockito.when(specialtiesValidations.validateInput(Mockito.any(RequestSpecialties.class), Mockito.anyBoolean())).thenReturn(false);

        ResponseBase responseObtained = specialtiesService.createSpecialties(requestSpecialties);

        assertEquals(responseExpected.getCode(), responseObtained.getCode());
        assertEquals(responseExpected.getMessage(), responseObtained.getMessage());
        assertEquals(responseExpected.getData(), responseObtained.getData());
    }

    @Test
    void findOneSpecialtyByIdWithRedisSucceed() {
        int id = 1;
        SpecialtiesEntity specialtiesEntity = new SpecialtiesEntity(
                id,
                "Medicina Fisica",
                "Medicina para la recuperacion de ligamentos y tendones",
                "MEDFIS",
                1
        );

        ResponseBase responseExpected = new ResponseBase(Constants.CODE_SUCCESS,Constants.MESSAGE_SUCCESS, Optional.of(specialtiesEntity));

        String dataRedis = Util.convertToJsonEntity(specialtiesEntity);
        Mockito.when(redisService.getValueFromCache(Mockito.anyString())).thenReturn(dataRedis);

        ResponseBase responseObtained = specialtiesService.findOneSpecialtyById(id);

        Optional<?> dataObtained = responseObtained.getData();
        SpecialtiesEntity specialtiesEntityObtained = (SpecialtiesEntity) dataObtained.get();

        assertEquals(responseExpected.getCode(), responseObtained.getCode());
        assertEquals(responseExpected.getMessage(), responseObtained.getMessage());
        assertEquals(specialtiesEntityObtained.getName(), specialtiesEntity.getName());
        assertEquals(specialtiesEntityObtained.getDescription(), specialtiesEntity.getDescription());
        assertEquals(specialtiesEntityObtained.getCode(), specialtiesEntity.getCode());
    }

    @Test
    void findOneSpecialtyByIdWithoutRedisSucceed() {
        int id = 1;
        SpecialtiesEntity specialtiesEntity = new SpecialtiesEntity(
                id,
                "Medicina Fisica",
                "Medicina para la recuperacion de ligamentos y tendones",
                "MEDFIS",
                1
        );

        ResponseBase responseExpected = new ResponseBase(Constants.CODE_SUCCESS,Constants.MESSAGE_SUCCESS, Optional.of(specialtiesEntity));

        Mockito.when(redisService.getValueFromCache(Mockito.anyString())).thenReturn(null);
        Mockito.when(specialtiesRepository.findById(id)).thenReturn(Optional.of(specialtiesEntity));

        Mockito.doNothing().when(redisService).saveInCache(Mockito.anyString(), Mockito.anyString());

        ResponseBase responseObtained = specialtiesService.findOneSpecialtyById(id);

        Optional<?> dataObtained = responseObtained.getData();
        SpecialtiesEntity specialtiesEntityObtained = (SpecialtiesEntity) dataObtained.get();

        assertEquals(responseExpected.getCode(), responseObtained.getCode());
        assertEquals(responseExpected.getMessage(), responseObtained.getMessage());
        assertEquals(specialtiesEntityObtained.getName(), specialtiesEntity.getName());
        assertEquals(specialtiesEntityObtained.getDescription(), specialtiesEntity.getDescription());
        assertEquals(specialtiesEntityObtained.getCode(), specialtiesEntity.getCode());
    }

    @Test
    void findOneSpecialtyByIdError() {
        int id = 1;
        ResponseBase responseBaseExpected = new ResponseBase(Constants.CODE_ERROR_DATA_NOT,Constants.MESSAGE_ZERO_ROWS, Optional.empty());

        Mockito.when(redisService.getValueFromCache(Mockito.anyString())).thenReturn(null);
        Mockito.when(specialtiesRepository.findById(id)).thenReturn(Optional.empty());

        ResponseBase responseBaseObtained = specialtiesService.findOneSpecialtyById(id);

        assertEquals(responseBaseObtained.getCode(), responseBaseExpected.getCode());
        assertEquals(responseBaseObtained.getMessage(), responseBaseExpected.getMessage());
        assertEquals(responseBaseObtained.getData(), responseBaseExpected.getData());
    }

    @Test
    void findAllSpecialtiesSucceed() {
        SpecialtiesEntity specialtiesEntity1 = new SpecialtiesEntity(
                1,
                "Medicina Fisica",
                "Medicina para la recuperacion de ligamentos y tendones",
                "MEDFIS",
                1
        );
        SpecialtiesEntity specialtiesEntity2 = new SpecialtiesEntity(
                2,
                "Medicina Interna",
                "Medicina para vias respiratorias, endocrinológica, dermatológicas, reumáticas, nefrológicas, gastrointestinales y hematológicas",
                "MEDIN",
                1
        );
        List<SpecialtiesEntity> listEntities = List.of(specialtiesEntity1, specialtiesEntity2);
        Mockito.when(specialtiesRepository.findAll()).thenReturn(listEntities);
        ResponseBase responseBaseExpected = new ResponseBase(Constants.CODE_SUCCESS,Constants.MESSAGE_SUCCESS, Optional.of(listEntities));

        ResponseBase responseBaseObtained = specialtiesService.findAllSpecialties();

        assertEquals(responseBaseObtained.getCode(), responseBaseExpected.getCode());
        assertEquals(responseBaseObtained.getMessage(), responseBaseExpected.getMessage());
        assertEquals(responseBaseObtained.getData(), responseBaseExpected.getData());
    }

    @Test
    void findAllSpecialtiesError() {
        List<SpecialtiesEntity> listEntities = new ArrayList<>();

        Mockito.when(specialtiesRepository.findAll()).thenReturn(listEntities);
        ResponseBase responseBaseExpected = new ResponseBase(Constants.CODE_ERROR_DATA_NOT,Constants.MESSAGE_ZERO_ROWS, Optional.empty());

        ResponseBase responseBaseObtained = specialtiesService.findAllSpecialties();

        assertEquals(responseBaseObtained.getCode(), responseBaseExpected.getCode());
        assertEquals(responseBaseObtained.getMessage(), responseBaseExpected.getMessage());
        assertEquals(responseBaseObtained.getData(), responseBaseExpected.getData());
    }

    @Test
    void updateSpecialtySucceed() {
        boolean existsSpecialty = true;
        boolean validationEntity = true;
        int id = 2;
        RequestSpecialties requestSpecialties = new RequestSpecialties(
                "Medicina Interna",
                "Medicina para tratamiento de organos internos de la persona humana",
                "MEDINTER"
        );
        SpecialtiesEntity specialtiesEntity = new SpecialtiesEntity(
                id,
                "Medicina Interna",
                "Medicina para vias respiratorias, endocrinológica, dermatológicas, reumáticas, nefrológicas, gastrointestinales y hematológicas",
                "MEDIN",
                1
        );
        SpecialtiesEntity specialtiesEntityUpdate = new SpecialtiesEntity(
                id,
                "Medicina Interna",
                "Medicina para tratamiento de organos internos de la persona humana",
                "MEDINTER",
                1
        );

        ResponseBase responseBaseExpected = new ResponseBase(Constants.CODE_SUCCESS,Constants.MESSAGE_SUCCESS,Optional.of(specialtiesEntityUpdate));
        SpecialtiesServiceImpl spy = Mockito.spy(specialtiesService);

        Mockito.when(specialtiesRepository.existsById(anyInt())).thenReturn(existsSpecialty);
        Mockito.when(specialtiesRepository.findById(anyInt())).thenReturn(Optional.of(specialtiesEntity));
        Mockito.when(specialtiesValidations.validateInput(Mockito.any(RequestSpecialties.class), Mockito.anyBoolean())).thenReturn(validationEntity);
        Mockito.when(specialtiesRepository.save(Mockito.any(SpecialtiesEntity.class))).thenReturn(specialtiesEntityUpdate);

        ResponseBase responseBaseObtained = spy.updateSpecialty(id, requestSpecialties);

        Optional<?> dataObtained = responseBaseObtained.getData();
        SpecialtiesEntity specialtiesEntity1 = (SpecialtiesEntity) dataObtained.get();

        assertEquals(responseBaseObtained.getCode(), responseBaseExpected.getCode());
        assertEquals(responseBaseObtained.getMessage(), responseBaseExpected.getMessage());
        assertEquals(specialtiesEntity1.getName(), specialtiesEntityUpdate.getName());
        assertEquals(specialtiesEntity1.getDescription(), specialtiesEntityUpdate.getDescription());
        assertEquals(specialtiesEntity1.getCode(), specialtiesEntityUpdate.getCode());
    }


    @Test
    void updateSpecialtyValidationError() {
        boolean existsSpecialty = true;
        boolean validationEntity = false;
        int id = 2;

        RequestSpecialties requestSpecialties = new RequestSpecialties(
                "Medicina Interna",
                "Medicina para tratamiento de organos internos de la persona humana",
                "MEDINTER"
        );
        SpecialtiesEntity specialtiesEntity = new SpecialtiesEntity(
                id,
                "Medicina Interna",
                "Medicina para vias respiratorias, endocrinológica, dermatológicas, reumáticas, nefrológicas, gastrointestinales y hematológicas",
                "MEDIN",
                1
        );

        ResponseBase responseBaseExpected = new ResponseBase(Constants.CODE_ERROR_DATA_INPUT, Constants.MESSAGE_ERROR_DATA_NOT_VALID, Optional.empty());

        Mockito.when(specialtiesRepository.existsById(anyInt())).thenReturn(existsSpecialty);
        Mockito.when(specialtiesRepository.findById(anyInt())).thenReturn(Optional.of(specialtiesEntity));
        Mockito.when(specialtiesValidations.validateInput(Mockito.any(RequestSpecialties.class), Mockito.anyBoolean())).thenReturn(validationEntity);

        ResponseBase responseBaseObtained = specialtiesService.updateSpecialty(id, requestSpecialties);

        assertEquals(responseBaseObtained.getCode(), responseBaseExpected.getCode());
        assertEquals(responseBaseObtained.getMessage(), responseBaseExpected.getMessage());
        assertEquals(responseBaseObtained.getData(), responseBaseExpected.getData());
    }

    @Test
    void updateSpecialtyDoesNotExistEntityError() {
        boolean existsSpecialty = false;
        int id = 2;

        RequestSpecialties requestSpecialties = new RequestSpecialties(
                "Medicina Interna",
                "Medicina para tratamiento de organos internos de la persona humana",
                "MEDINTER"
        );

        ResponseBase responseBaseExpected = new ResponseBase(Constants.CODE_ERROR_DATA_NOT, Constants.MESSAGE_ERROR_NOT_UPDATE_SPECIALTIES, Optional.empty());

        Mockito.when(specialtiesRepository.existsById(anyInt())).thenReturn(existsSpecialty);

        ResponseBase responseBaseObtained = specialtiesService.updateSpecialty(id, requestSpecialties);

        assertEquals(responseBaseObtained.getCode(), responseBaseExpected.getCode());
        assertEquals(responseBaseObtained.getMessage(), responseBaseExpected.getMessage());
        assertEquals(responseBaseObtained.getData(), responseBaseExpected.getData());
    }

    @Test
    void deleteSpecialtySucceed() {
        boolean existsSpecialty = true;
        int id = 2;
        SpecialtiesEntity specialtiesEntity = new SpecialtiesEntity(
                id,
                "Medicina Interna",
                "Medicina para vias respiratorias, endocrinológica, dermatológicas, reumáticas, nefrológicas, gastrointestinales y hematológicas",
                "MEDIN",
                1
        );
        SpecialtiesEntity specialtiesEntityDeleted = new SpecialtiesEntity(
                id,
                "Medicina Interna",
                "Medicina para vias respiratorias, endocrinológica, dermatológicas, reumáticas, nefrológicas, gastrointestinales y hematológicas",
                "MEDIN",
                0
        );

        ResponseBase responseBaseExpected = new ResponseBase(Constants.CODE_SUCCESS, Constants.MESSAGE_SUCCESS, Optional.of(specialtiesEntityDeleted));

        Mockito.when(specialtiesRepository.existsById(anyInt())).thenReturn(existsSpecialty);

        Mockito.when(specialtiesRepository.findById(anyInt())).thenReturn(Optional.of(specialtiesEntity));
        Mockito.when(specialtiesRepository.save(Mockito.any(SpecialtiesEntity.class))).thenReturn(specialtiesEntityDeleted);

        ResponseBase responseBaseObtained = specialtiesService.deleteSpecialty(id);

        Optional<?> dataObtained = responseBaseObtained.getData();
        SpecialtiesEntity specialtiesEntity1 = (SpecialtiesEntity) dataObtained.get();

        assertEquals(responseBaseObtained.getCode(), responseBaseExpected.getCode());
        assertEquals(responseBaseObtained.getMessage(), responseBaseExpected.getMessage());
        assertEquals(specialtiesEntity1.getName(), specialtiesEntityDeleted.getName());
        assertEquals(specialtiesEntity1.getDescription(), specialtiesEntityDeleted.getDescription());
        assertEquals(specialtiesEntity1.getCode(), specialtiesEntityDeleted.getCode());
        assertEquals(specialtiesEntity1.getStatus(), specialtiesEntityDeleted.getStatus());
    }

    @Test
    void deleteSpecialtyError() {
        boolean existsSpecialty = false;
        int id = 2;

        ResponseBase responseBaseExpected = new ResponseBase(Constants.CODE_ERROR_EXIST, Constants.MESSAGE_ERROR_NOT_DELETE_SPECIALTIES, Optional.empty());

        Mockito.when(specialtiesRepository.existsById(anyInt())).thenReturn(existsSpecialty);

        ResponseBase responseBaseObtained = specialtiesService.deleteSpecialty(id);

        assertEquals(responseBaseObtained.getCode(), responseBaseExpected.getCode());
        assertEquals(responseBaseObtained.getMessage(), responseBaseExpected.getMessage());
        assertEquals(responseBaseObtained.getData(), responseBaseExpected.getData());
    }
}