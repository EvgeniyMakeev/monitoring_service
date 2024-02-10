package com.makeev.monitoring_service.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.makeev.monitoring_service.dao.UserDAO;
import com.makeev.monitoring_service.exceptions.DaoException;
import com.makeev.monitoring_service.model.User;
import com.makeev.monitoring_service.utils.ConnectionManagerImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/users")
public class GetAllUsersServlet extends HttpServlet {

    private ObjectMapper objectMapper;
    private UserDAO userDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        objectMapper = new ObjectMapper();
        userDAO = new UserDAO(new ConnectionManagerImpl());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            List<User> users = userDAO.getAll();
            resp.setContentType("application/json");
            resp.setStatus(HttpServletResponse.SC_OK);
            objectMapper.writeValue(resp.getOutputStream(), users);
        } catch (DaoException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("Error occurred: " + e.getMessage());
        }
    }
}