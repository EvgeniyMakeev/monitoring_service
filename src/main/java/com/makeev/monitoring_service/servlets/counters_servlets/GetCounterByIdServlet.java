package com.makeev.monitoring_service.servlets.counters_servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.makeev.monitoring_service.aop.annotations.Loggable;
import com.makeev.monitoring_service.dao.CounterDAO;
import com.makeev.monitoring_service.exceptions.DaoException;
import com.makeev.monitoring_service.exceptions.CounterIdException;
import com.makeev.monitoring_service.mappers.CounterMapper;
import com.makeev.monitoring_service.model.Counter;
import com.makeev.monitoring_service.utils.ConnectionManagerImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@Loggable
@WebServlet("/counters/getCounter")
public class GetCounterByIdServlet extends HttpServlet {

    public static final String CONTENT_TYPE = "application/json";
    private ObjectMapper objectMapper;
    private CounterDAO counterDAO;
    private final CounterMapper counterMapper = CounterMapper.INSTANCE;

    @Override
    public void init() throws ServletException {
        super.init();
        objectMapper = new ObjectMapper();
        counterDAO = new CounterDAO(new ConnectionManagerImpl());
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
            Counter counter = counterDAO.getCounterById(id);


            resp.setContentType(CONTENT_TYPE);
            resp.setStatus(HttpServletResponse.SC_OK);
            objectMapper.writeValue(resp.getOutputStream(), counterMapper.toDTO(counter));
        } catch (NumberFormatException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("Invalid ID format");
        } catch (CounterIdException e) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            resp.getWriter().write(e.getMessage());
        } catch (DaoException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("Error occurred: " + e.getMessage());
        }
    }
}