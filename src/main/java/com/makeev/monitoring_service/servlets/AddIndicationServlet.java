package com.makeev.monitoring_service.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.makeev.monitoring_service.dao.CounterDAO;
import com.makeev.monitoring_service.exceptions.DaoException;
import com.makeev.monitoring_service.exceptions.IncorrectValuesException;
import com.makeev.monitoring_service.model.Counter;
import com.makeev.monitoring_service.model.Indication;
import com.makeev.monitoring_service.service.IndicationService;
import com.makeev.monitoring_service.utils.ConnectionManagerImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.format.DateTimeParseException;

@WebServlet("/addIndication")
public class AddIndicationServlet extends HttpServlet {
    private IndicationService indicationService;
    private ObjectMapper objectMapper;

    private CounterDAO counterDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        indicationService = new IndicationService(new ConnectionManagerImpl());
        objectMapper = new ObjectMapper();
        counterDAO = new CounterDAO(new ConnectionManagerImpl());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            Indication indication = objectMapper.readValue(req.getReader(), Indication.class);

            String login = req.getParameter("login");
            long counterId = Long.parseLong(req.getParameter("counterId"));
            Counter counter = counterDAO.getBy(counterId).orElseThrow();

            indicationService.addIndicationOfUser(login, counter, indication.date(), indication.value());

            resp.setStatus(HttpServletResponse.SC_CREATED);
        } catch (IncorrectValuesException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("Incorrect values provided");
        } catch (NumberFormatException | DateTimeParseException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("Invalid format for counter ID or date");
        } catch (DaoException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("Error occurred: " + e.getMessage());
        }
    }
}