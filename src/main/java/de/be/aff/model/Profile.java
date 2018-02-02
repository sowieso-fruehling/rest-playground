package de.be.aff.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Profile {

    private String name;
    private String surname;
    private int age;


    public Profile(String name, String surname) {
        this.name = name;
        this.surname = surname;
    }

    public Profile(String name, String surname, int age) {
        this(name, surname);
        this.age=age;
    }
}

