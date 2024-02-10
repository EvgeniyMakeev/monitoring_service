package com.makeev.monitoring_service.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.makeev.monitoring_service.dao.UserDAO;
import com.makeev.monitoring_service.exceptions.DaoException;
import com.makeev.monitoring_service.exceptions.LoginAlreadyExistsException;
import com.makeev.monitoring_service.model.User;
import com.makeev.monitoring_service.service.IndicationService;
import com.makeev.monitoring_service.utils.ConnectionManagerImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/addUser")
public class AddUserServlet extends HttpServlet {
    private ObjectMapper objectMapper;
    private UserDAO userDAO;

    private IndicationService indicationService;

    @Override
    public void init() throws ServletException {
        super.init();
        objectMapper = new ObjectMapper();
        userDAO = new UserDAO(new ConnectionManagerImpl());
        indicationService = new IndicationService(new ConnectionManagerImpl());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            User user = objectMapper.readValue(req.getReader(), User.class);

            String login = user.login();
            String password = user.password();
            if (login == null || login.isEmpty() || password == null || password.isEmpty()) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write("Both login and password parameters are required");
                return;
            }

            userDAO.existByLogin(login);
            indicationService.addUser(login,password);

            resp.setStatus(HttpServletResponse.SC_CREATED);
        } catch (LoginAlreadyExistsException e) {
            resp.setStatus(HttpServletResponse.SC_CONFLICT);
            resp.getWriter().write("User with this login already exists");
        } catch (DaoException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("Error occurred: " + e.getMessage());
        }
    }
}
