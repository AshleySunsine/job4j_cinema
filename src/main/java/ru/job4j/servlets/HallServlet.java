package ru.job4j.servlets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ru.job4j.model.Account;
import ru.job4j.model.Ticket;
import ru.job4j.store.DbStore;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class HallServlet extends HttpServlet {

    private static final Gson GSON = new GsonBuilder().create();

    private class JsonClass {
        List<Account> acc;
        List<Ticket> tickets;

        public JsonClass(List<Account> acc, List<Ticket> tickets) {
            this.acc = acc;
            this.tickets = tickets;
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json; charset=utf-8");
        OutputStream output = resp.getOutputStream();
        JsonClass jsonClass = new JsonClass(DbStore.instOf().findAllAccount(),
                                            DbStore.instOf().findAllTickets());
        String jsList = GSON.toJson(jsonClass);
        output.write((jsList).getBytes(StandardCharsets.UTF_8));
        output.flush();
        output.close();
    }
}
