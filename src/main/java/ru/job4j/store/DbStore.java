package ru.job4j.store;

import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.job4j.model.Account;
import ru.job4j.model.Point;
import ru.job4j.model.Ticket;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

public class DbStore implements StoreAccount, StoreTicket {

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
    public int saveAccount(Account acc) {

        return createAccount(acc);
    }

    private int createAccount(Account acc) {
        int idAcc = -1;
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
                    idAcc = id.getInt(1);
                    acc.setId(idAcc);
                }
            }
        } catch (SQLException e) {
            LOG.error(e.getMessage(), e);
            e.printStackTrace();
            return idAcc;
        }
        return idAcc;
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
    public List<Account> findAllAccount() {
        List<Account> accounts = new ArrayList<>();
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

    /**************************************************************/

    @Override
    public int saveTicket(Ticket ticket) {
            return createTicket(ticket);
    }

    private int createTicket(Ticket ticket) {
        int idTicked = -1;
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("INSERT INTO ticket (session_id, rov, cell, account_id)"
                             + " VALUES (?, ?, ?, ?)",
                     PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, ticket.getSessionId());
            ps.setInt(2, ticket.getPoint().getRow());
            ps.setInt(3, ticket.getPoint().getColumn());
            ps.setInt(4, ticket.getAccountId());
            ps.execute();
            try (ResultSet id = ps.getGeneratedKeys()) {
                if (id.next()) {
                    idTicked = id.getInt(1);
                    ticket.setId(idTicked);
                }
            }
        } catch (SQLException e) {
            LOG.error(e.getMessage(), e);
            e.printStackTrace();
            return idTicked;
        }
        return idTicked;
    }

    @Override
    public void deleteTicket(int ticketId) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("DELETE from ticket where id = (?)")) {
            ps.setInt(1, ticketId);
            ps.execute();
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
    }

    @Override
    public Ticket findTicketById(int id) {
        Ticket ticket = null;
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("select * from ticket where id=(?)")) {
            ps.setInt(1, id);
            ps.execute();
            try (ResultSet rs = ps.getResultSet()) {
                if (rs.next()) {
                    Point point = new Point(rs.getInt("rov"), rs.getInt("cell"));
                    ticket = new Ticket(rs.getInt("id"), rs.getInt("session_id"),
                           rs.getInt("account_id"), point);
                }
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return ticket;
    }

    @Override
    public List<Ticket> findTicketsByAccountId(int accountId) {
        List<Ticket> tickets = new ArrayList<>();
        Point point = null;
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("select * from ticket where account_id=(?)")) {
            ps.setInt(1, accountId);
            ps.execute();
            try (ResultSet rs = ps.getResultSet()) {
                while (rs.next()) {
                    point = new Point(rs.getInt("rov"), rs.getInt("cell"));
                    tickets.add(new Ticket(rs.getInt("id"), rs.getInt("session_id"),
                            rs.getInt("account_id"), point));
                }
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return tickets;
    }

    @Override
    public List<Ticket> findAllTickets() {
        List<Ticket> tickets = new ArrayList<>();
        Point point = null;
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("select * from ticket")) {
            ps.execute();
            try (ResultSet rs = ps.getResultSet()) {
                while (rs.next()) {
                    point = new Point(rs.getInt("rov"), rs.getInt("cell"));
                    tickets.add(new Ticket(rs.getInt("id"), rs.getInt("session_id"),
                            rs.getInt("account_id"), point));
                }
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return tickets;
    }
}
