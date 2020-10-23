package com.examine.connector.dao;

import com.examine.connector.entity.World;
import com.examine.connector.exception.DaoException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class WorldDaoImpl implements WorldDao {
    private static final String SELECT_ALL_QUERY = "SELECT * FROM WORLD;";

    private final JdbcTemplate jdbcTemplate;

    public WorldDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public World getLastWorld() {
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(SELECT_ALL_QUERY);

        if (rows.size() == 0) {
            throw new DaoException("Database is empty!");
        }

        Integer id = (Integer) rows.get(0).get("id");
        String greeting = (String) rows.get(0).get("greeting");

        return new World(id, greeting);
    }

}
