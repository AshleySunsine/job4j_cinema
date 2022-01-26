package ru.job4j.model;

import java.util.Objects;

public class Ticket {
    int id;
    int session_id;
    int account_id;
    Point point;

    public Ticket(int id, int session_id, int account_id, Point point) {
        this.id = id;
        this.session_id = session_id;
        this.account_id = account_id;
        this.point = point;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSession_id() {
        return session_id;
    }

    public void setSession_id(int session_id) {
        this.session_id = session_id;
    }

    public int getAccount_id() {
        return account_id;
    }

    public void setAccount_id(int account_id) {
        this.account_id = account_id;
    }

    public Point getPoint() {
        return point;
    }

    public void setPoint(Point point) {
        this.point = point;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ticket ticket = (Ticket) o;
        return id == ticket.id && session_id == ticket.session_id && account_id == ticket.account_id && Objects.equals(point, ticket.point);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, session_id, account_id, point);
    }
}