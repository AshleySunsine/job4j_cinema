package ru.job4j.model;

import java.util.Objects;

public class Ticket {
    int id;
    String session;
    Point point;

    public Ticket(int id, String session, Point point) {
        this.id = id;
        this.session = session;
        this.point = point;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ticket ticket = (Ticket) o;
        return id == ticket.id &&
                Objects.equals(session, ticket.session) &&
                Objects.equals(point, ticket.point);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, session, point);
    }
}


