package com.makeev.monitoring_service.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.makeev.monitoring_service.dao.UserDAO;
import com.makeev.monitoring_service.dto.VerificationResponseDTO;
import com.makeev.monitoring_service.exceptions.DaoException;
import com.makeev.monitoring_service.exceptions.VerificationException;
import com.makeev.monitoring_service.utils.ConnectionManagerImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/verifyUser")
public class VerifyUserCredentialsServlet extends HttpServlet {

    private ObjectMapper objectMapper;
    private UserDAO userDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        objectMapper = new ObjectMapper();
        userDAO = new UserDAO(new ConnectionManagerImpl());
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

            VerificationResponseDTO responseDTO = new VerificationResponseDTO("User credentials verified successfully");

            resp.setContentType("application/json");
            resp.setStatus(HttpServletResponse.SC_OK);
            objectMapper.writeValue(resp.getOutputStream(), responseDTO);
        } catch (VerificationException e) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.getWriter().write("Invalid credentials");
        } catch (DaoException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("Error occurred: " + e.getMessage());
        }
    }
}