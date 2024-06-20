package com.example.demo.controller;

import com.example.demo.dto.CarDTO;
import com.example.demo.exception.ResourceInvalidException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.CarEntity;
import com.example.demo.service.CarService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

/**
 * Rest API controller to consume requests to manage CRUD operations on the car entity
 */

@RestController
@RequestMapping("/cars")
@CrossOrigin
public class CarController {

    private final CarService carService;

    public CarController(CarService carService) {
        this.carService = carService;
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<CarEntity>> getAllCars() {
        try {
            List<CarEntity> carsList = carService.listAll();

            if (carsList.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(carsList, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CarEntity> getCarById(@PathVariable Long id) throws ResourceNotFoundException {
        CarEntity carEntity = carService.getById(id);
        return ResponseEntity.ok(carEntity);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<CarEntity> addCar(@Valid @RequestBody CarDTO carDTO) throws ResourceInvalidException {
        CarEntity carEntity = carService.create(carDTO);
        return ResponseEntity.ok(carEntity);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','EDITOR')")
    public ResponseEntity<CarEntity> updateCar(@PathVariable Long id, @Valid @RequestBody CarDTO carDTO) throws ResourceNotFoundException {
        CarEntity carEntity = carService.update(id, carDTO);
        return ResponseEntity.ok(carEntity);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<Void> deleteCar(@PathVariable Long id) throws ResourceNotFoundException {
        carService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
