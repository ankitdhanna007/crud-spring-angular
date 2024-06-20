package com.example.demo.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

import static com.example.demo.model.CarEntity.UNIQUE_CONSTRAINT;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "cars", uniqueConstraints = { @UniqueConstraint(name = UNIQUE_CONSTRAINT, columnNames = { "name", "transmission", "yearOfManufacture", "pricePerDay" })})
public class CarEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransmissionType transmission;

    @Column(nullable = false)
    private String yearOfManufacture;

    @Column(nullable = false)
    private int pricePerDay;

    public static final String UNIQUE_CONSTRAINT = "UNIQUE_CAR_IDX";

    public CarEntity(String name, TransmissionType transmission, String yearOfManufacture, int pricePerDay) {
        this.name = name;
        this.transmission = transmission;
        this.yearOfManufacture = yearOfManufacture;
        this.pricePerDay = pricePerDay;
    }

    @Override
    public String toString(){
        return "Car [id=" + id + ", name=" + name + ", transmission=" + transmission.name() + ", year=" + yearOfManufacture + ", pricePerDay=" + pricePerDay + "]";
    }

}
