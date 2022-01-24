package ru.job4j.store;

import ru.job4j.model.Account;
import ru.job4j.model.Ticket;

import java.util.Collection;

public interface StoreTicket {
    void saveTicket(Ticket acc);
    void deleteTicket(Ticket acc);
    Ticket findTicketById(int id);
    Collection<Ticket> findTicketsByAccount(Account acc);
    Collection<Ticket> findAllTickets();

}
