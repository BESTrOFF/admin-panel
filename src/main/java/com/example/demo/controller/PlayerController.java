package com.example.demo.controller;

import com.example.demo.controller.request.CreatePlayerRequest;
import com.example.demo.controller.request.GetPlayersCountRequest;
import com.example.demo.controller.request.GetPlayersListRequest;
import com.example.demo.controller.response.*;
import com.example.demo.entity.Profession;
import com.example.demo.entity.Race;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class PlayerController {

    @GetMapping("/rest/players")
    public List<GetPlayersListResponse> getPlayers(GetPlayersListRequest request) {
        List<GetPlayersListResponse> players = new ArrayList<>();

        GetPlayersListResponse request1 = GetPlayersListResponse.builder()
                .name("qwe")
                .title("qwe")
                .race(Race.ELF)
                .profession(Profession.CLERIC)
                .birthday(123L)
                .banned(false)
                .experience(123)
                .level(42)
                .untilNextLevel(12)
                .build();

        GetPlayersListResponse request2 = GetPlayersListResponse.builder()
                .name("asd")
                .title("asd")
                .race(Race.HUMAN)
                .profession(Profession.SORCERER)
                .birthday(123L)
                .banned(false)
                .experience(123)
                .level(42)
                .untilNextLevel(12)
                .build();


        return List.of(request1, request2);
    }

    @GetMapping("rest/players/{id}")
    public ResponseEntity<GetPlayerResponse> getPlayer(@PathVariable Long id) {
        GetPlayerResponse response = GetPlayerResponse.builder()
                .id(12L)
                .name("name")
                .title("title")
                .race(Race.ELF)
                .profession(Profession.CLERIC)
                .birthday(123L)
                .banned(false)
                .experience(123)
                .level(42)
                .untilNextLevel(12)
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("rest/players/count")
    public Integer getPlayersCount(GetPlayersCountRequest request) {
        GetPlayersCountResponse response = new GetPlayersCountResponse();

        response.setCount(4);

        return response.getCount();
    }

    @PostMapping("rest/players")
    public ResponseEntity<CreatePlayerResponse> createPlayer(CreatePlayerRequest request) {
        CreatePlayerResponse response = CreatePlayerResponse.builder()
                .id(13L)
                .name("player")
                .title("title")
                .race(Race.HUMAN)
                .profession(Profession.PALADIN)
                .birthday(4563L)
                .banned(false)
                .experience(321)
                .level(54)
                .untilNextLevel(126)
                .build();

        return ResponseEntity.ok(response);
    }

    @PostMapping("/rest/players/{id}")
    public ResponseEntity<UpdatePlayerResponse> updatePlayer(@PathVariable Long id,
                                                             CreatePlayerRequest request) {

        UpdatePlayerResponse response = UpdatePlayerResponse.builder()
                .id(13L)
                .name("player1")
                .title("title1")
                .race(Race.GIANT)
                .profession(Profession.SORCERER)
                .birthday(123L)
                .banned(false)
                .experience(123)
                .level(54)
                .untilNextLevel(126)
                .build();

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/rest/players/{id}")
    public ResponseEntity<Void> deletePlayer(@PathVariable Long id) {

        return ResponseEntity.ok().build();
    }
}
