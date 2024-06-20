package com.example.demo.unit.service;

import com.example.demo.dto.CarDTO;
import com.example.demo.exception.ResourceInvalidException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.CarEntity;
import com.example.demo.model.TransmissionType;
import com.example.demo.model.UserEntity;
import com.example.demo.repository.CarRepository;
import com.example.demo.service.CarService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Optional;

import static com.example.demo.model.TransmissionType.AUTOMATIC;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CarServiceTest {

    private static final String CAR_NAME = "Tesla";
    private static final TransmissionType TRANSMISSION = AUTOMATIC;
    private static final int YEAR_OF_MANUFACTURE = 2024;
    private static final int PRICE_PER_DAY = 1200;
    @Mock
    private CarRepository carRepository;

    @InjectMocks
    private CarService carService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateSuccess() throws ResourceInvalidException {

        CarDTO carDTO = new CarDTO(null,"Toyota", AUTOMATIC, 2020, 100);
        CarEntity carEntity = createCarEntity(1L, carDTO);

        when(carRepository.save(any(CarEntity.class))).thenReturn(carEntity);

        CarEntity createdCar = carService.create(carDTO);

        assertNotNull(createdCar);
        assertEquals(createdCar, carEntity);
    }

    @Test
    void testCreateThrowsResourceInvalidException() {
        CarDTO carDTO = new CarDTO(null, "Toyota", AUTOMATIC, 2020, 100);
        DataIntegrityViolationException exception = new DataIntegrityViolationException("unique constraint violation: UNIQUE_CAR_IDX");

        when(carRepository.save(any(CarEntity.class))).thenThrow(exception);

        Exception thrown = assertThrows(ResourceInvalidException.class, () -> carService.create(carDTO));

        assertEquals("Requested entry already exists with same details", thrown.getMessage());
    }

    @Test
    void testListAll() {
        carService.listAll();
        verify(carRepository, times(1)).findAll();
    }

    @Test
    void testGetByIdSuccess() throws ResourceNotFoundException {
        CarEntity carEntity = this.createCarEntity(1L);

        when(carRepository.findById(1L)).thenReturn(Optional.of(carEntity));

        CarEntity foundCar = carService.getById(1L);

        assertNotNull(foundCar);
        assertEquals(1L, foundCar.getId());
    }

    @Test
    void testGetByIdNotFound() {
        when(carRepository.findById(1L)).thenReturn(Optional.empty());

        Exception thrown = assertThrows(ResourceNotFoundException.class, () -> {
            carService.getById(1L);
        });

        assertEquals("No car entry found with id: 1", thrown.getMessage());
    }

    @Test
    void testUpdateSuccess() throws ResourceNotFoundException {

        CarEntity carEntity = this.createCarEntity(1L);
        CarDTO carDTO = new CarDTO(null, "TOYOTA", AUTOMATIC, 2020, 100);

        when(carRepository.findById(1L)).thenReturn(Optional.of(this.createCarEntity(1L)));
        when(carRepository.save(any(CarEntity.class))).thenReturn(carEntity);

        CarEntity updatedCar = carService.update(1L, carDTO);

        assertNotNull(updatedCar);
        assertEquals(CAR_NAME, updatedCar.getName());
    }

    @Test
    void testUpdateNotFound() {
        CarDTO carDTO = new CarDTO(null, "Toyota", AUTOMATIC, 2020, 100);

        when(carRepository.findById(1L)).thenReturn(Optional.empty());

        Exception thrown = assertThrows(ResourceNotFoundException.class, () -> {
            carService.update(1L, carDTO);
        });

        assertEquals("No car entry found with id: 1", thrown.getMessage());
    }

    @Test
    void testDeleteSuccess() throws ResourceNotFoundException {
        when(carRepository.existsById(1L)).thenReturn(true);

        carService.delete(1L);

        verify(carRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteNotFound() {
        when(carRepository.existsById(1L)).thenReturn(false);

        Exception thrown = assertThrows(ResourceNotFoundException.class, () -> {
            carService.delete(1L);
        });

        assertEquals("No car entry found with id: 1", thrown.getMessage());
    }

    private CarEntity createCarEntity(Long id, CarDTO carDTO) {
        CarEntity carEntity = new CarEntity();
        carEntity.setId(id);
        carEntity.setName(carDTO.getName());
        carEntity.setTransmission(carDTO.getTransmission());
        carEntity.setYearOfManufacture(String.valueOf(carDTO.getYearOfManufacture()));
        carEntity.setPricePerDay(carDTO.getPricePerDay());
        return carEntity;
    }

    private CarEntity createCarEntity(Long id) {
        return createCarEntity(id, new CarDTO(null, CAR_NAME, AUTOMATIC, YEAR_OF_MANUFACTURE, PRICE_PER_DAY));
    }

    private CarEntity createCarEntity() {
        return createCarEntity(null, new CarDTO(null, CAR_NAME, AUTOMATIC, YEAR_OF_MANUFACTURE, PRICE_PER_DAY));
    }
}
