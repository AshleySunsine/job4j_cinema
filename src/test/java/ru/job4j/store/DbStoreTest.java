package ru.job4j.store;

import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.job4j.model.Account;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Properties;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class DbStoreTest {
    private static final BasicDataSource POOL = new BasicDataSource();
    private static final Logger LOG = LoggerFactory.getLogger(DbStoreTest.class.getName());

    @BeforeClass
    public static void init() {
        Properties cfg = new Properties();
        try (BufferedReader io = new BufferedReader(
                new InputStreamReader(
                        DbStoreTest.class.getClassLoader()
                                .getResourceAsStream("test.properties")
                )
        )) {
            cfg.load(io);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        try {
            Class.forName(cfg.getProperty("jdbc.driver"));
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        POOL.setDriverClassName(cfg.getProperty("jdbc.driver"));
        POOL.setUrl(cfg.getProperty("jdbc.url"));
        POOL.setUsername(cfg.getProperty("username"));
        POOL.setPassword(cfg.getProperty("password"));
        POOL.setMinIdle(5);
        POOL.setMaxIdle(10);
        POOL.setMaxOpenPreparedStatements(100);
    }

    @Test
    public void whenCreateAccount() {
        StoreAccount store = DbStore.instOf(POOL);
        Account acc = new Account(0, "Name1", "email1", "pone_number");
        store.saveAccount(acc);
        Account accFromDb = store.findAccountById(0);
        assertEquals(acc, accFromDb);
    }

}
