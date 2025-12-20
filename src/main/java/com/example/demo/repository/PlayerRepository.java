package com.example.demo.repository;

import com.example.demo.controller.request.CreatePlayerRequest;
import com.example.demo.controller.request.GetPlayersCountRequest;
import com.example.demo.controller.request.UpdatePlayerRequest;
import com.example.demo.entity.Player;
import com.example.demo.entity.Race;
import com.example.demo.mapper.PlayerMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestBody;

import javax.annotation.PostConstruct;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Repository
public class PlayerRepository {
    private final JdbcTemplate template;
    private final PlayerMapper playerMapper;
    private final RaceRepository raceRepository;
    private final ProfessionRepository professionRepository;

    @PostConstruct
    public void init(){

    }

    public List<Player> getAll(int page, int pageSize) {
        int offset = (page) * pageSize;

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
                ORDER BY player.id
                LIMIT ? OFFSET ?
                """;

        return template.query(sql, playerMapper, pageSize, offset);
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
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM players WHERE 1=1");
        List<Object> args = new ArrayList<>();
        List<Integer> types = new ArrayList<>();

        if (request.getName() != null && !request.getName().trim().isEmpty()) {
            sql.append(" AND name ILIKE ?");
            args.add("%" + request.getName().trim() + "%");
            types.add(Types.VARCHAR);
        }

        if (request.getTitle() != null && !request.getTitle().trim().isEmpty()) {
            sql.append(" AND title ILIKE ?");
            args.add("%" + request.getTitle().trim() + "%");
            types.add(Types.VARCHAR);
        }

        if (request.getRace() != null) {
            sql.append(" AND race_id = ?");
            args.add(raceRepository.getRaceId(request.getRace()));
            types.add(Types.BIGINT);
        }

        if (request.getProfession() != null) {
            sql.append(" AND profession_id = ?");
            args.add(professionRepository.getProfessionId(request.getProfession()));
            types.add(Types.BIGINT);
        }

        if (request.getAfter() != null) {
            sql.append(" AND birthday >= ?");
            args.add(new java.sql.Timestamp(request.getAfter()));
            types.add(Types.TIMESTAMP);
        }

        if (request.getBefore() != null) {
            sql.append(" AND birthday <= ?");
            args.add(new java.sql.Timestamp(request.getBefore()));
            types.add(Types.TIMESTAMP);
        }

        if (request.getBanned() != null) {
            sql.append(" AND banned = ?");
            args.add(request.getBanned());
            types.add(Types.BOOLEAN);
        }

        if (request.getMinExperience() != null) {
            sql.append(" AND experience >= ?");
            args.add(request.getMinExperience());
            types.add(Types.INTEGER);
        }

        if (request.getMaxExperience() != null) {
            sql.append(" AND experience <= ?");
            args.add(request.getMaxExperience());
            types.add(Types.INTEGER);
        }

        if (request.getMinLevel() != null) {
            sql.append(" AND level >= ?");
            args.add(request.getMinLevel());
            types.add(Types.INTEGER);
        }

        if (request.getMaxLevel() != null) {
            sql.append(" AND level <= ?");
            args.add(request.getMaxLevel());
            types.add(Types.INTEGER);
        }

        return template.queryForObject(
                sql.toString(),
                args.toArray(),
                types.stream().mapToInt(i -> i).toArray(),
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
        List<Integer> types = new ArrayList<>();

        boolean hasUpdates = false;

        if (request.getName() != null) {
            sql.append("name = ?, ");
            args.add(request.getName());
            types.add(Types.VARCHAR);
            hasUpdates = true;
        }

        if (request.getTitle() != null) {
            sql.append("title = ?, ");
            args.add(request.getTitle());
            types.add(Types.VARCHAR);
            hasUpdates = true;
        }

        if (request.getRace() != null) {
            sql.append("race_id = ?, ");
            args.add(raceRepository.getRaceId(request.getRace()));
            types.add(Types.BIGINT);
            hasUpdates = true;
        }

        if (request.getProfession() != null) {
            sql.append("profession_id = ?, ");
            args.add(professionRepository.getProfessionId(request.getProfession()));
            types.add(Types.BIGINT);
            hasUpdates = true;
        }

        if (request.getBirthday() != null) {
            sql.append("birthday = ?, ");
            args.add(new Timestamp(request.getBirthday()));
            types.add(Types.TIMESTAMP);
            hasUpdates = true;
        }

        if (request.getBanned() != null) {
            sql.append("banned = ?, ");
            args.add(request.getBanned());
            types.add(Types.BOOLEAN);
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

            types.add(Types.INTEGER);
            types.add(Types.INTEGER);
            types.add(Types.INTEGER);

            hasUpdates = true;
        }

        sql.delete(sql.length() - 2, sql.length());

        sql.append(" WHERE id = ?");
        args.add(id);
        types.add(Types.BIGINT);

        if (!hasUpdates) {
            return getById(id);
        }

        template.update(
                sql.toString(),
                args.toArray(),
                types.stream().mapToInt(i -> i).toArray()
        );


        return getById(id);
    }

    public void deletePlayer(Long id) {
        String sql = "DELETE FROM players WHERE id = ?";

        template.update(sql, id);
    }
}
