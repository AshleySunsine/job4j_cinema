package ru.job4j.servlets;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class PaymentServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String sit = req.getParameter("place");
        String[] place = sit.split("\\.");
        String row = place[0];
        String colom = place[1];
        req.setAttribute("colom", colom);
        req.setAttribute("row", row);
        req.setAttribute("user", "SomeUser");
        req.setAttribute("sessionid", req.getParameter("sessionid"));
        req.getRequestDispatcher("payment.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }
}
