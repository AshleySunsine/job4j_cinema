package ru.job4j.store;

import ru.job4j.model.Account;
import ru.job4j.model.Ticket;

import java.util.Collection;
import java.util.List;

public interface StoreTicket {
    int saveTicket(Ticket ticket);
    void deleteTicket(int ticket_id);
    Ticket findTicketById(int id);
    List<Ticket> findTicketsByAccountId(int account_id);
    List<Ticket> findAllTickets();

}
