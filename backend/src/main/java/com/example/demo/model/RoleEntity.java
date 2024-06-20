package com.example.demo.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "roles")
public class RoleEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(unique = true, nullable = false)
    @Enumerated(EnumType.STRING)
    private RoleEnum name;

    @Column
    private String description;

    public RoleEntity(RoleEnum name, String description) {
        this.name = name;
        this.description = description;
    }

    @Override
    public String toString(){
        return "Role [id=" + id + ", name=" + name + ", description=" + description + "]";
    }

}
