package com.makeev.monitoring_service.servlets.users_servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.makeev.monitoring_service.aop.annotations.Loggable;
import com.makeev.monitoring_service.dao.UserDAO;
import com.makeev.monitoring_service.dto.UserDTO;
import com.makeev.monitoring_service.exceptions.DaoException;
import com.makeev.monitoring_service.exceptions.LoginAlreadyExistsException;
import com.makeev.monitoring_service.exceptions.VerificationException;
import com.makeev.monitoring_service.mappers.UserMapper;
import com.makeev.monitoring_service.model.User;
import com.makeev.monitoring_service.utils.ConnectionManagerImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Loggable
@WebServlet("/users")
public class UsersServlet extends HttpServlet {

    private ObjectMapper objectMapper;
    private UserDAO userDAO;
    private UserMapper userMapper;



    @Override
    public void init() throws ServletException {
        super.init();
        objectMapper = new ObjectMapper();
        userDAO = new UserDAO(new ConnectionManagerImpl());
        userMapper = userMapper.INSTANCE;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            List<User> users = userDAO.getAllUsers();

            List<UserDTO> userDTOs = users.stream()
                    .map(userMapper::toDTO)
                    .collect(Collectors.toList());

            resp.setContentType("application/json");
            resp.setStatus(HttpServletResponse.SC_OK);
            objectMapper.writeValue(resp.getOutputStream(), userDTOs);
        } catch (DaoException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("Error occurred: " + e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            String login = req.getParameter("login");
            String password = req.getParameter("password");
            resp.setContentType("application/json");
            if (login == null || login.isEmpty() || password == null || password.isEmpty()) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write("Both login and password parameters are required");
                return;
            }
            userDAO.existByLogin(login);
            userDAO.addUser(login,password);
            resp.setStatus(HttpServletResponse.SC_CREATED);
            resp.getWriter().write("User added successfully");
        } catch (LoginAlreadyExistsException e) {
            resp.setStatus(HttpServletResponse.SC_CONFLICT);
            resp.getWriter().write(e.getMessage());
        } catch (DaoException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("Error occurred: " + e.getMessage());
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            String login = req.getParameter("login");
            String password = req.getParameter("password");
            if (login == null || login.isEmpty() || password == null || password.isEmpty()) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write("Both login and password parameters are required");
                return;
            }
            userDAO.checkCredentials(login, password);

            resp.setContentType("application/json");
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write("User credentials verified successfully");
        } catch (VerificationException e) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.getWriter().write(e.getMessage());
        } catch (DaoException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("Error occurred: " + e.getMessage());
        }
    }
}