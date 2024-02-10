package com.makeev.monitoring_service.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.makeev.monitoring_service.exceptions.DaoException;
import com.makeev.monitoring_service.exceptions.EmptyException;
import com.makeev.monitoring_service.model.IndicationsOfUser;
import com.makeev.monitoring_service.service.IndicationService;
import com.makeev.monitoring_service.utils.ConnectionManagerImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/currentIndicationForUser")
public class GetCurrentIndicationForUserServlet extends HttpServlet {

    private ObjectMapper objectMapper;
    private IndicationService indicationService;

    @Override
    public void init() throws ServletException {
        super.init();
        objectMapper = new ObjectMapper();
        indicationService = new IndicationService(new ConnectionManagerImpl());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            String login = req.getParameter("login");
            if (login == null || login.isEmpty()) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write("Login parameter is required");
                return;
            }

            List<IndicationsOfUser> indications = indicationService.getCurrentIndication(login);
            resp.setContentType("application/json");
            resp.setStatus(HttpServletResponse.SC_OK);
            objectMapper.writeValue(resp.getOutputStream(), indications);
        } catch (EmptyException e) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            resp.getWriter().write("No indications found for the user");
        } catch (DaoException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("Error occurred: " + e.getMessage());
        }
    }
}