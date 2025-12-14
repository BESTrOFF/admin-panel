package com.example.demo.controller.request;

import com.example.demo.entity.Profession;
import com.example.demo.entity.Race;
import lombok.Value;

@Value
public class GetPlayersCountRequest {
    String name;
    String title;
    Race race;
    Profession profession;
    Long after;
    Long before;
    Boolean banned;
    Integer minExperience;
    Integer maxExperience;
    Integer minLevel;
    Integer maxLevel;
}
