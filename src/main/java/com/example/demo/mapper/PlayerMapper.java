package com.example.demo.mapper;

import com.example.demo.controller.response.CreatePlayerResponse;
import com.example.demo.controller.response.GetPlayerResponse;
import com.example.demo.controller.response.GetPlayersListResponse;
import com.example.demo.controller.response.UpdatePlayerResponse;
import com.example.demo.entity.Player;
import com.example.demo.entity.Profession;
import com.example.demo.entity.Race;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class PlayerMapper implements RowMapper<Player> {
    @Override
    public Player mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        return Player.builder()
                .id(resultSet.getLong("id"))
                .name(resultSet.getString("name"))
                .title(resultSet.getString("title"))
                .race(Race.valueOf(resultSet.getString("race")))
                .profession(Profession.valueOf(resultSet.getString("profession")))
                .experience(resultSet.getInt("experience"))
                .level(resultSet.getInt("level"))
                .untilNextLevel(resultSet.getInt("until_next_level"))
                .birthday(resultSet.getDate("birthday"))
                .banned(resultSet.getBoolean("banned"))
                .build();
    }

    public static GetPlayersListResponse toGetPlayersListResponse(Player player) {
        return GetPlayersListResponse.builder()
                .id(player.getId())
                .name(player.getName())
                .title(player.getTitle())
                .race(player.getRace())
                .profession(player.getProfession())
                .birthday(player.getBirthday().getTime())
                .banned(player.getBanned())
                .experience(player.getExperience())
                .level(player.getLevel())
                .untilNextLevel(player.getUntilNextLevel())
                .build();
    }

    public static GetPlayerResponse toGetPlayerResponse(Player player) {
        return GetPlayerResponse.builder()
                .id(player.getId())
                .name(player.getName())
                .title(player.getTitle())
                .race(player.getRace())
                .profession(player.getProfession())
                .birthday(player.getBirthday().getTime())
                .banned(player.getBanned())
                .experience(player.getExperience())
                .level(player.getLevel())
                .untilNextLevel(player.getUntilNextLevel())
                .build();
    }

    public static CreatePlayerResponse toCreatePlayerResponse(Player player) {
        return CreatePlayerResponse.builder()
                .id(player.getId())
                .name(player.getName())
                .title(player.getTitle())
                .race(player.getRace())
                .profession(player.getProfession())
                .birthday(player.getBirthday().getTime())
                .banned(player.getBanned())
                .experience(player.getExperience())
                .level(player.getLevel())
                .untilNextLevel(player.getUntilNextLevel())
                .build();
    }
    public static UpdatePlayerResponse toUpdatePlayerResponse(Player player) {
        return UpdatePlayerResponse.builder()
                .id(player.getId())
                .name(player.getName())
                .title(player.getTitle())
                .race(player.getRace())
                .profession(player.getProfession())
                .birthday(player.getBirthday().getTime())
                .banned(player.getBanned())
                .experience(player.getExperience())
                .level(player.getLevel())
                .untilNextLevel(player.getUntilNextLevel())
                .build();
    }
}
