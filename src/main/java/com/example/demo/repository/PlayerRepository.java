package com.example.demo.repository;

import com.example.demo.controller.request.*;
import com.example.demo.entity.Player;
import com.example.demo.mapper.PlayerMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Repository
public class PlayerRepository {
    private final JdbcTemplate template;
    private final PlayerMapper playerMapper;
    private final RaceRepository raceRepository;
    private final ProfessionRepository professionRepository;

    public List<Player> getAll(GetPlayersListRequest request, int page, int pageSize) {
        StringBuilder sql = new StringBuilder("""
                                SELECT
                                    player.id,
                                    player.name,
                                    player.title,
                                    race.name AS race,
                                    profession.name AS profession,
                                    player.experience,
                                    player.level,
                                    player.until_next_level,
                                    player.birthday,
                                    player.banned
                                FROM players player
                                JOIN races race ON player.race_id = race.id
                                JOIN professions profession ON player.profession_id = profession.id
                                WHERE 1=1
                                
                                """);

        List<Object> args = new ArrayList<>();

        if (request.getName() != null && !request.getName().trim().isEmpty()) {
            sql.append(" AND player.name LIKE ?");
            args.add("%" + request.getName().trim() + "%");
        }

        if (request.getTitle() != null && !request.getTitle().trim().isEmpty()) {
            sql.append(" AND player.title LIKE ?");
            args.add("%" + request.getTitle().trim() + "%");
        }

        if (request.getRace() != null) {
            sql.append(" AND player.race_id = ?");
            args.add(raceRepository.getRaceId(request.getRace()));
        }

        if (request.getProfession() != null) {
            sql.append(" AND player.profession_id = ?");
            args.add(professionRepository.getProfessionId(request.getProfession()));
        }

        if (request.getAfter() != null) {
            sql.append(" AND player.birthday >= ?");
            args.add(new java.sql.Timestamp(request.getAfter()));
        }

        if (request.getBefore() != null) {
            sql.append(" AND player.birthday <= ?");
            args.add(new java.sql.Timestamp(request.getBefore()));
        }

        if (request.getBanned() != null) {
            sql.append(" AND player.banned = ?");
            args.add(request.getBanned());
        }

        if (request.getMinExperience() != null) {
            sql.append(" AND player.experience >= ?");
            args.add(request.getMinExperience());
        }

        if (request.getMaxExperience() != null) {
            sql.append(" AND player.experience <= ?");
            args.add(request.getMaxExperience());
        }

        if (request.getMinLevel() != null) {
            sql.append(" AND player.level >= ?");
            args.add(request.getMinLevel());
        }

        if (request.getMaxLevel() != null) {
            sql.append(" AND player.level <= ?");
            args.add(request.getMaxLevel());
        }

        sql.append(" ORDER BY player.id LIMIT ? OFFSET ?");

        int offset = page * pageSize;
        args.add(pageSize);
        args.add(offset);

        return template.query(sql.toString(), args.toArray(), playerMapper);
    }

    public Player getById(Long id) {
        String sql = """
                SELECT
                    player.id,
                    player.name,
                    player.title,
                    race.name AS race,
                    profession.name AS profession,
                    player.experience,
                    player.level,
                    player.until_next_level,
                    player.birthday,
                    player.banned
                FROM players player
                JOIN races race ON player.race_id = race.id
                JOIN professions profession ON player.profession_id = profession.id
                WHERE player.id = ?;
        """;

        return template.queryForObject(sql, playerMapper, id);
    }

    public Integer getPlayersCount(GetPlayersCountRequest request) {
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM players player WHERE 1=1");
        List<Object> args = new ArrayList<>();

        if (request.getName() != null && !request.getName().trim().isEmpty()) {
            sql.append(" AND player.name LIKE ?");
            args.add("%" + request.getName().trim() + "%");
        }

        if (request.getTitle() != null && !request.getTitle().trim().isEmpty()) {
            sql.append(" AND player.title LIKE ?");
            args.add("%" + request.getTitle().trim() + "%");
        }

        if (request.getRace() != null) {
            sql.append(" AND player.race_id = ?");
            args.add(raceRepository.getRaceId(request.getRace()));
        }

        if (request.getProfession() != null) {
            sql.append(" AND player.profession_id = ?");
            args.add(professionRepository.getProfessionId(request.getProfession()));
        }

        if (request.getAfter() != null) {
            sql.append(" AND player.birthday >= ?");
            args.add(new java.sql.Timestamp(request.getAfter()));
        }

        if (request.getBefore() != null) {
            sql.append(" AND player.birthday <= ?");
            args.add(new java.sql.Timestamp(request.getBefore()));
        }

        if (request.getBanned() != null) {
            sql.append(" AND player.banned = ?");
            args.add(request.getBanned());
        }

        if (request.getMinExperience() != null) {
            sql.append(" AND player.experience >= ?");
            args.add(request.getMinExperience());
        }

        if (request.getMaxExperience() != null) {
            sql.append(" AND player.experience <= ?");
            args.add(request.getMaxExperience());
        }

        if (request.getMinLevel() != null) {
            sql.append(" AND player.level >= ?");
            args.add(request.getMinLevel());
        }

        if (request.getMaxLevel() != null) {
            sql.append(" AND player.level <= ?");
            args.add(request.getMaxLevel());
        }

        return template.queryForObject(
                sql.toString(),
                args.toArray(),
                Integer.class
        );
    }

    public Player createPlayer(CreatePlayerRequest request) {
        String sql = """
            INSERT INTO players
            (name, title, race_id, profession_id, experience, level, until_next_level, birthday, banned)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
            RETURNING id
            """;

        Integer experience = request.getExperience();
        Integer level = (int) (Math.sqrt(2500 + 200 * experience) / 100);
        Integer untilNextLevel = 50 * (level + 1) * (level + 2) - experience;
        Timestamp birthday = new Timestamp(request.getBirthday());

        Long newPlayerId = template.queryForObject(sql,
                Long.class,
                request.getName(),
                request.getTitle(),
                raceRepository.getRaceId(request.getRace()),
                professionRepository.getProfessionId(request.getProfession()),
                experience,
                level,
                untilNextLevel,
                birthday,
                request.getBanned() != null && request.getBanned());

        return getById(newPlayerId);
    }

    public Player updatePlayer(Long id, UpdatePlayerRequest request) {
        StringBuilder sql = new StringBuilder("UPDATE players SET ");
        List<Object> args = new ArrayList<>();

        boolean hasUpdates = false;

        if (request.getName() != null) {
            sql.append("name = ?, ");
            args.add(request.getName());
            hasUpdates = true;
        }

        if (request.getTitle() != null) {
            sql.append("title = ?, ");
            args.add(request.getTitle());
            hasUpdates = true;
        }

        if (request.getRace() != null) {
            sql.append("race_id = ?, ");
            args.add(raceRepository.getRaceId(request.getRace()));
            hasUpdates = true;
        }

        if (request.getProfession() != null) {
            sql.append("profession_id = ?, ");
            args.add(professionRepository.getProfessionId(request.getProfession()));
            hasUpdates = true;
        }

        if (request.getBirthday() != null) {
            sql.append("birthday = ?, ");
            args.add(new Timestamp(request.getBirthday()));
            hasUpdates = true;
        }

        if (request.getBanned() != null) {
            sql.append("banned = ?, ");
            args.add(request.getBanned());
            hasUpdates = true;
        }

        if (request.getExperience() != null) {
            sql.append("experience = ?, level = ?, until_next_level = ?, ");

            Integer experience = request.getExperience();
            Integer level = (int) (Math.sqrt(2500 + 200 * experience) / 100);
            Integer untilNextLevel = 50 * (level + 1) * (level + 2) - experience;

            args.add(experience);
            args.add(level);
            args.add(untilNextLevel);

            hasUpdates = true;
        }

        sql.delete(sql.length() - 2, sql.length());

        sql.append(" WHERE id = ?");
        args.add(id);

        if (!hasUpdates) {
            return getById(id);
        }

        template.update(sql.toString(), args.toArray());

        return getById(id);
    }

    public void deletePlayer(Long id) {
        String sql = "DELETE FROM players WHERE id = ?";

        template.update(sql, id);
    }
}