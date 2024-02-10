package com.makeev.monitoring_service.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.makeev.monitoring_service.dao.CounterDAO;
import com.makeev.monitoring_service.dto.CounterDTO;
import com.makeev.monitoring_service.exceptions.DaoException;
import com.makeev.monitoring_service.model.Counter;
import com.makeev.monitoring_service.utils.ConnectionManagerImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet("/counters")
public class GetAllCountersServlet extends HttpServlet {
    private ObjectMapper objectMapper;
    private CounterDAO counterDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        objectMapper = new ObjectMapper();
        counterDAO = new CounterDAO(new ConnectionManagerImpl());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            List<Counter> counters = counterDAO.getAll();

            List<CounterDTO> counterDTOs = counters.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());

            resp.setContentType("application/json");
            resp.setStatus(HttpServletResponse.SC_OK);
            objectMapper.writeValue(resp.getOutputStream(), counterDTOs);
        } catch (DaoException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("Error occurred: " + e.getMessage());
        }
    }

    private CounterDTO convertToDTO(Counter counter) {
        return new CounterDTO(counter.name());
    }
}