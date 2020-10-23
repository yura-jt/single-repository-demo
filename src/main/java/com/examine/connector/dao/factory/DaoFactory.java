package com.examine.connector.dao.factory;

import com.examine.connector.dao.WorldDao;
import com.examine.connector.dao.WorldDaoImpl;
import com.examine.connector.exception.DaoException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class DaoFactory {
    private final JdbcTemplate jdbcTemplate1;
    private final JdbcTemplate jdbcTemplate2;

    private WorldDao worldDao1;
    private WorldDao worldDao2;

    public DaoFactory(@Qualifier("first-db") JdbcTemplate jdbcTemplate1,
                      @Qualifier("second-db") JdbcTemplate jdbcTemplate2) {
        this.jdbcTemplate1 = jdbcTemplate1;
        this.jdbcTemplate2 = jdbcTemplate2;
        init();
    }

    public WorldDao getDao(DbType dbType) {
        if (dbType == DbType.FIRST) {
            return worldDao1;
        } else if (dbType == DbType.SECOND) {
            return worldDao2;
        }
        throw new DaoException("Database type is not recognised!");
    }

    private void init() {
        this.worldDao1 = new WorldDaoImpl(jdbcTemplate1);
        this.worldDao2 = new WorldDaoImpl(jdbcTemplate2);
    }

}
