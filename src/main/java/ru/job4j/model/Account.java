package ru.job4j.model;

import java.util.Collection;
import java.util.Objects;

public class Account {
    int id;
    String name;
    Collection<Ticket> tickets;

    public Account() {
    }

    public Account(int id, String name, Collection<Ticket> tickets) {
        this.id = id;
        this.name = name;
        this.tickets = tickets;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Collection<Ticket> getTickets() {
        return tickets;
    }

    public void setTickets(Collection<Ticket> tickets) {
        this.tickets = tickets;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return id == account.id &&
                Objects.equals(name, account.name) &&
                Objects.equals(tickets, account.tickets);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, tickets);
    }
}
