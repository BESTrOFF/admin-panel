package com.example.demo.repository;

import com.example.demo.entity.Race;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class RaceRepository {
    private final JdbcTemplate template;

    public Long getRaceId(Race race){
        String sql = """
                SELECT id
                FROM races
                WHERE name = ?;
                """;

        return template.queryForObject(sql, Long.class, race.name());
    }
}
