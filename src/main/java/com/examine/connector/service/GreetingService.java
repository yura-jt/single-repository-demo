package com.examine.connector.service;

import com.examine.connector.dao.WorldDao;
import com.examine.connector.dao.factory.DaoFactory;
import com.examine.connector.dao.factory.DbType;
import com.examine.connector.entity.World;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class GreetingService {
    private final WorldDao firstDao;
    private final WorldDao secondDao;

    public GreetingService(DaoFactory daoFactory) {
        firstDao = daoFactory.getDao(DbType.FIRST);
        secondDao = daoFactory.getDao(DbType.SECOND);
    }

    @Transactional("txManager1")
    public String getFromFirstDb() {

        World world = firstDao.getLastWorld();

        return world.getGreeting();
    }

    @Transactional("txManager2")
    public String getFromSecondDb() {

        World world = secondDao.getLastWorld();

        return world.getGreeting();
    }

}
