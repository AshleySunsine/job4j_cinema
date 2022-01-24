package ru.job4j.store;

import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.job4j.model.Account;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Collection;
import java.util.Properties;

public class DbStore implements StoreAccount {

    private static final DbStore INSTANCE = new DbStore();

    private BasicDataSource pool = new BasicDataSource();

    private static final Logger LOG = LoggerFactory.getLogger(DbStore.class.getName());

    private DbStore() {
        Properties cfg = new Properties();
        try (BufferedReader io = new BufferedReader(
                new InputStreamReader(
                        DbStore.class.getClassLoader()
                                .getResourceAsStream("app.properties")
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
        pool.setDriverClassName(cfg.getProperty("jdbc.driver"));
        pool.setUrl(cfg.getProperty("jdbc.url"));
        pool.setUsername(cfg.getProperty("username"));
        pool.setPassword(cfg.getProperty("password"));
        pool.setMinIdle(5);
        pool.setMaxIdle(10);
        pool.setMaxOpenPreparedStatements(100);
    }

    private DbStore(BasicDataSource pool) {
        this.pool = pool;
    }

    private static final class Lazy {
        private static final DbStore INST = new DbStore();
    }

    public static DbStore instOf() {
        return Lazy.INST;
    }

    public static DbStore instOf(BasicDataSource pool) {
        return new DbStore(pool);
    }

    @Override
    public void saveAccount(Account acc) {
        createAccount(acc);
    }

    private void createAccount(Account acc) {
        try (Connection cn = pool.getConnection();
            PreparedStatement ps = cn.prepareStatement("INSERT INTO account (username, email, phone)"
                        + " VALUES (?, ?, ?)",
                PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, acc.getName());
            ps.setString(2, acc.getEmail());
            ps.setString(3, acc.getPhone());
            ps.execute();
            try (ResultSet id = ps.getGeneratedKeys()) {
                if (id.next()) {
                    acc.setId(id.getInt(1));
                }
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            e.printStackTrace();
        }
    }

    @Override
    public void deleteAccount(int id) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("DELETE from account where id = (?)")) {
            ps.setInt(1, id);
            ps.execute();
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
    }

    @Override
    public Account findAccountById(int id) {
        Account account = null;
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("select * from account where id=(?)")) {
            ps.setInt(1, id);
            ps.execute();
            try (ResultSet rs = ps.getResultSet()) {
                if (rs.next()) {
                    account = new Account(rs.getInt("id"), rs.getString("username"),
                            rs.getString("email"), rs.getString("phone"));
                }
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return account;
    }

    @Override
    public Account findAccountByName(String name) {
        Account account = null;
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("select * from account where username=(?)")) {
            ps.setString(1, name);
            ps.execute();
            try (ResultSet rs = ps.getResultSet()) {
                if (rs.next()) {
                    account = new Account(rs.getInt("id"), rs.getString("username"),
                            rs.getString("email"), rs.getString("phone"));
                }
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return account;
    }

    @Override
    public Collection<Account> findAllAccount() {
        Collection<Account> accounts = null;
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("select * from account")) {
            ps.execute();
            try (ResultSet rs = ps.getResultSet()) {
                while (rs.next()) {
                    accounts.add(new Account(rs.getInt("id"), rs.getString("username"),
                            rs.getString("email"), rs.getString("phone")));
                }
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return accounts;
    }
}
