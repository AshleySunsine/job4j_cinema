package ru.job4j.servlets;

import ru.job4j.model.Account;
import ru.job4j.model.Point;
import ru.job4j.model.Ticket;
import ru.job4j.store.DbStore;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;

public class SaveServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("UTF-8");
        String user = req.getParameter("username");
        String phone = req.getParameter("phone");
        String email = req.getParameter("email");
        String row = req.getParameter("row");
        String colom = req.getParameter("colom");
        String sessionId = req.getParameter("sessionid");
        System.out.println(sessionId);
        Account account = new Account(0, user, email, phone);
        int accountId = DbStore.instOf().saveAccount(account);
        Ticket ticket = new Ticket(0,
                Integer.parseInt(sessionId),
                accountId,
                new Point(Integer.parseInt(row),
                        Integer.parseInt(colom))
        );
        DbStore.instOf().saveTicket(ticket);
        resp.sendRedirect(req.getContextPath() + "/index.html");
    }
}
