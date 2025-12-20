package com.example.demo.controller;

import com.example.demo.controller.request.CreatePlayerRequest;
import com.example.demo.controller.request.GetPlayersCountRequest;
import com.example.demo.controller.request.GetPlayersListRequest;
import com.example.demo.controller.request.UpdatePlayerRequest;
import com.example.demo.controller.response.*;
import com.example.demo.entity.Player;
import com.example.demo.entity.Profession;
import com.example.demo.entity.Race;
import com.example.demo.mapper.PlayerMapper;
import com.example.demo.repository.PlayerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;
@RequiredArgsConstructor
@RestController
public class PlayerController {
    private final PlayerRepository playerRepository;

    @GetMapping("/rest/players")
    public List<GetPlayersListResponse> getPlayers(GetPlayersListRequest request) {
        int pageNumber = request.getPageNumber() == null ? 0 : request.getPageNumber();
        int pageSize = request.getPageSize() == null ? 10 : request.getPageSize();

        return playerRepository.getAll(pageNumber, pageSize).stream()
                .map(PlayerMapper::toGetPlayersListResponse)
                .toList();
    }

    @GetMapping("rest/players/{id}")
    public ResponseEntity<GetPlayerResponse> getPlayer(@PathVariable Long id) {
        Player player = playerRepository.getById(id);

        GetPlayerResponse response = PlayerMapper.toGetPlayerResponse(player);

        return ResponseEntity.ok(response);
    }

    @GetMapping("rest/players/count")
    public Integer getPlayersCount(GetPlayersCountRequest request) {
         return  playerRepository.getPlayersCount(request);
    }

    @PostMapping("rest/players")
    public ResponseEntity<CreatePlayerResponse> createPlayer(@RequestBody CreatePlayerRequest request) {
        Player createdPlayer = playerRepository.createPlayer(request);

        CreatePlayerResponse response = PlayerMapper.toCreatePlayerResponse(createdPlayer);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/rest/players/{id}")
    public ResponseEntity<UpdatePlayerResponse> updatePlayer(@PathVariable Long id,
                                                             @RequestBody UpdatePlayerRequest request) {

        Player updatedPlayer = playerRepository.updatePlayer(id, request);

        UpdatePlayerResponse response = PlayerMapper.toUpdatePlayerResponse(updatedPlayer);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/rest/players/{id}")
    public ResponseEntity<Void> deletePlayer(@PathVariable Long id) {
        playerRepository.deletePlayer(id);

        return ResponseEntity.ok().build();
    }
}
