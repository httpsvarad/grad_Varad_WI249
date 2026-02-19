package com.example.demo.entities;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum Gender {
    MALE, FEMALE;

    @JsonCreator
    public static Gender fromString(String value) {
        if (value == null) return null;
        return Gender.valueOf(value.toUpperCase());
    }
}