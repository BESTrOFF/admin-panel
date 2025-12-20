package com.example.demo.repository;

import com.example.demo.entity.Profession;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class ProfessionRepository {
    private final JdbcTemplate template;

    public Long getProfessionId(Profession profession){
        String sql = """
               SELECT id
               FROM professions
               where name = ?
               """;

        return template.queryForObject(sql, Long.class, profession.name());
    }
}
