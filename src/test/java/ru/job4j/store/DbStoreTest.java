package ru.job4j.store;

import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.job4j.model.Account;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
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

    @AfterClass
    public static void closePool() throws SQLException {
        POOL.close();
    }

    @After
    public void wipeTable() throws SQLException {
        try (Connection cn = POOL.getConnection(); PreparedStatement statement = cn.prepareStatement(
                "drop table if exists account cascade; "
                + "create table if not exists account(id SERIAL PRIMARY KEY,\n"
                + "  username VARCHAR NOT NULL,\n"
                + "  email VARCHAR NOT NULL UNIQUE,\n"
                + "  phone VARCHAR NOT NULL UNIQUE); "

                        + "drop table if exists ticket;"
                        + "CREATE TABLE IF NOT EXISTS ticket (\n"
                        + "id SERIAL PRIMARY KEY,\n"
                        + "session_id INT NOT NULL,\n"
                        + "rov INT NOT NULL,\n"
                        + "cell INT NOT NULL,\n"
                        + "account_id INT NOT NULL REFERENCES account(id));"
                        )) {
            statement.execute();
        }
    }

    @Test
    public void whenCreateAccount() {
        StoreAccount store = DbStore.instOf(POOL);
        Account acc = new Account(0, "Name1", "email1", "pone_number");
        int idAcc = store.saveAccount(acc);
        Account accFromDb = store.findAccountById(idAcc);
        assertEquals(acc, accFromDb);
        store.deleteAccount(idAcc);
    }

    @Test
    public void whenDeleteAccount() {
        StoreAccount store = DbStore.instOf(POOL);
        Account acc = new Account(0, "Name1", "email1", "pone_number");
        int idAcc = store.saveAccount(acc);
        store.deleteAccount(idAcc);
        Account accFromDb = store.findAccountById(idAcc);
        assertNull(accFromDb);
    }

    @Test
    public void whenFindAccountDyId() {
        StoreAccount store = DbStore.instOf(POOL);
        Account acc = new Account(0, "Name1", "email1", "pone_number");
        int idAcc = store.saveAccount(acc);
        Account accFromDb = store.findAccountById(idAcc);
        assertEquals(acc, accFromDb);
    }

    @Test
    public void whenFindAccountDyName() {
        StoreAccount store = DbStore.instOf(POOL);
        Account acc = new Account(0, "Name1", "email1", "pone_number");
        int idAcc = store.saveAccount(acc);
        Account accFromDb = store.findAccountByName("Name1");
        assertEquals(acc, accFromDb);
    }

    @Test
    public void whenFindAllAccount() {
        StoreAccount store = DbStore.instOf(POOL);
        Account acc1 = new Account(0, "Name1", "email1", "pone_number1");
        Account acc2 = new Account(0, "Name2", "email2", "pone_number2");
        Account acc3 = new Account(0, "Name3", "email3", "pone_number3");
        store.saveAccount(acc1);
        store.saveAccount(acc2);
        store.saveAccount(acc3);
        List<Account> listIs = new ArrayList<Account>(List.of(acc1, acc2, acc3));
        List<Account> listOut = store.findAllAccount();
        assertEquals(listIs, listOut);
    }

    @Test
    public void whenConstrainsViolationException() {
        StoreAccount store = DbStore.instOf(POOL);
        Account acc1 = new Account(0, "Name1", "email1", "pone_number1");
        Account accExeption = new Account(0, "Name2", "email1", "pone_number2");
        store.saveAccount(acc1);
        int exep = store.saveAccount(accExeption);
        assertThat(exep, is(-1));
    }
}
