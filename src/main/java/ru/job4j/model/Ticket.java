package ru.job4j.model;

import java.util.Objects;

public class Ticket {
    int id;
    int sessionId;
    int accountId;
    Point point;

    public Ticket(int id, int sessionId, int accountId, Point point) {
        this.id = id;
        this.sessionId = sessionId;
        this.accountId = accountId;
        this.point = point;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSessionId() {
        return sessionId;
    }

    public void setSessionId(int sessionId) {
        this.sessionId = sessionId;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public Point getPoint() {
        return point;
    }

    public void setPoint(Point point) {
        this.point = point;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Ticket ticket = (Ticket) o;
        return id == ticket.id && sessionId == ticket.sessionId && accountId == ticket.accountId && Objects.equals(point, ticket.point);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, sessionId, accountId, point);
    }
}