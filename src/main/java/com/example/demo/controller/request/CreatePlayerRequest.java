package com.example.demo.controller.request;

import com.example.demo.entity.Profession;
import com.example.demo.entity.Race;
import lombok.Value;

@Value
public class CreatePlayerRequest implements Request {
    String name;
    String title;
    Race race;
    Profession profession;
    Long birthday;
    Boolean banned;
    Integer experience;
}