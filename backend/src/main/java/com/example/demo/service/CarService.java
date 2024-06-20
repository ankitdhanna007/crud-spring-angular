package com.example.demo.service;

import com.example.demo.dto.CarDTO;
import com.example.demo.exception.ResourceInvalidException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.CarEntity;
import com.example.demo.repository.CarRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.example.demo.model.CarEntity.UNIQUE_CONSTRAINT;
import static com.example.demo.service.CarServiceExceptionAnalyser.isUniqueConstraintViolated;

@Service
public class CarService {

    private final CarRepository carRepository;

    public CarService(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    public CarEntity create(CarDTO carDTO) throws ResourceInvalidException {

        CarEntity carEntity = new CarEntity();
        carEntity.setName(carDTO.getName());
        carEntity.setTransmission(carDTO.getTransmission());
        carEntity.setYearOfManufacture(String.valueOf(carDTO.getYearOfManufacture()));
        carEntity.setPricePerDay(carDTO.getPricePerDay());

        try{
            return carRepository.save(carEntity);
        } catch (DataIntegrityViolationException exception){

            if(isUniqueConstraintViolated(exception)){
                throw new ResourceInvalidException("Requested entry already exists with same details", exception);
            }
            throw new ResourceInvalidException("Requested entry in an invalid state ", exception);
        }
    }

    public List<CarEntity> listAll() {
        return carRepository.findAll();
    }

    public CarEntity getById(Long id) throws ResourceNotFoundException {
        Optional<CarEntity> optionalCarEntity = carRepository.findById(id);
        if (optionalCarEntity.isPresent()) {
            return optionalCarEntity.get();
        } else {
            throw new ResourceNotFoundException("No car entry found with id: " + id);
        }
    }

    public CarEntity update(Long id, CarDTO carDTO) throws ResourceNotFoundException {
        Optional<CarEntity> optionalCarEntity = carRepository.findById(id);
        if (optionalCarEntity.isPresent()) {
            CarEntity carEntity = optionalCarEntity.get();
            carEntity.setName(carDTO.getName());
            carEntity.setTransmission(carDTO.getTransmission());
            carEntity.setYearOfManufacture(String.valueOf(carDTO.getYearOfManufacture()));
            carEntity.setPricePerDay(carDTO.getPricePerDay());
            return carRepository.save(carEntity);
        } else {
            throw new ResourceNotFoundException("No car entry found with id: " + id);
        }
    }

    public void delete(Long id) throws ResourceNotFoundException {
        if (!carRepository.existsById(id)) {
            throw new ResourceNotFoundException("No car entry found with id: " + id);
        }
        carRepository.deleteById(id);
    }

}

/**
 * Analyse service level exceptions and extract common expected violations
 */
class CarServiceExceptionAnalyser {

    public static boolean isUniqueConstraintViolated(Exception e){
        final String rootMsg = e.getMessage();
        return rootMsg != null && rootMsg.contains(UNIQUE_CONSTRAINT);
    }

    // check further constraints ..
}
