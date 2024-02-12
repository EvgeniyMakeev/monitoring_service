package com.makeev.monitoring_service.servlets.counters_servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.makeev.monitoring_service.aop.annotations.Loggable;
import com.makeev.monitoring_service.dao.CounterDAO;
import com.makeev.monitoring_service.exceptions.DaoException;
import com.makeev.monitoring_service.exceptions.NoCounterIdException;
import com.makeev.monitoring_service.mappers.CounterMapper;
import com.makeev.monitoring_service.model.Counter;
import com.makeev.monitoring_service.utils.ConnectionManagerImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Optional;

@Loggable
@WebServlet("/counters/getCounter")
public class GetCounterByIdServlet extends HttpServlet {

    private ObjectMapper objectMapper;
    private CounterDAO counterDAO;
    private CounterMapper counterMapper;

    @Override
    public void init() throws ServletException {
        super.init();
        objectMapper = new ObjectMapper();
        counterDAO = new CounterDAO(new ConnectionManagerImpl());
        counterMapper = CounterMapper.INSTANCE;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            String idString = req.getParameter("id");
            if (idString == null || idString.isEmpty()) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write("ID parameter is required");
                return;
            }

            long id = Long.parseLong(idString);
            Optional<Counter> counter = counterDAO.getCounterById(id);

            if (counter.isEmpty()) {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                resp.getWriter().write("Counter with this ID not found");
                return;
            }

            resp.setContentType("application/json");
            resp.setStatus(HttpServletResponse.SC_OK);
            objectMapper.writeValue(resp.getOutputStream(), counterMapper.toDTO(counter.get()));
        } catch (NumberFormatException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("Invalid ID format");
        } catch (NoCounterIdException e) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            resp.getWriter().write(e.getMessage());
        } catch (DaoException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("Error occurred: " + e.getMessage());
        }
    }
}