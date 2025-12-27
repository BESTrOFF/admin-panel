package com.example.demo.entity;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class Player {
    Long id;
    String name;
    String title;
    Race race;
    Profession profession;
    Integer experience;
    Integer level;
    Integer untilNextLevel;
    Date birthday;
    Boolean banned;
}
