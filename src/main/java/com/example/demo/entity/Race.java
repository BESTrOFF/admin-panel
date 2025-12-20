package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Race {
    HUMAN,
    DWARF,
    ELF,
    GIANT,
    ORC,
    TROLL,
    HOBBIT;
    /*
    HUMAN("HUMAN"),
    DWARF("DWARF"),
    ELF("ELF"),
    GIANT("GIANT"),
    ORC("ORC"),
    TROLL("TROLL"),
    HOBBIT("HOBBIT");

    private final String value;

    Race(String value) {
        this.value = value;
    }

    // Этот метод будет использоваться при преобразовании в JSON
    @JsonValue
    public String getValue() {
        return value;
    }

    // Этот метод будет использоваться при чтении из JSON
    @JsonCreator
    public static Race fromValue(String value) {
        for (Race race : values()) {
            if (race.value.equalsIgnoreCase(value)) {
                return race;
            }
        }
        throw new IllegalArgumentException("Unknown race: " + value);
    }

     */
}