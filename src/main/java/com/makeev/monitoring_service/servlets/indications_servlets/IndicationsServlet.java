//package com.makeev.monitoring_service.servlets.indications_servlets;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.makeev.monitoring_service.aop.annotations.Loggable;
//import com.makeev.monitoring_service.dao.CounterDAO;
//import com.makeev.monitoring_service.exceptions.DaoException;
//import com.makeev.monitoring_service.exceptions.EmptyException;
//
//import com.makeev.monitoring_service.exceptions.IncorrectValuesException;
//import com.makeev.monitoring_service.model.Counter;
//import com.makeev.monitoring_service.model.IndicationsOfUser;
//import com.makeev.monitoring_service.service.IndicationService;
//import com.makeev.monitoring_service.utils.ConnectionManagerImpl;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.annotation.WebServlet;
//import jakarta.servlet.http.HttpServlet;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//
//import java.io.IOException;
//import java.time.LocalDate;
//import java.time.format.DateTimeParseException;
//import java.util.List;
//
//@WebServlet("/indications")
//public class IndicationsServlet extends HttpServlet {
//    private ObjectMapper objectMapper;
//    private IndicationService indicationService;
//    private CounterDAO counterDAO;
//
//    @Loggable
//    @Override
//    public void init() throws ServletException {
//        super.init();
//        objectMapper = new ObjectMapper();
//        indicationService = new IndicationService(new ConnectionManagerImpl());
//        counterDAO = new CounterDAO(new ConnectionManagerImpl());
//    }
//
//    @Loggable
//    @Override
//    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
//        try {
//            List<IndicationsOfUser> indications = indicationService.getAllIndications();
//            resp.setContentType("application/json");
//            resp.setStatus(HttpServletResponse.SC_OK);
//            objectMapper.writeValue(resp.getOutputStream(), indications);
//        } catch (EmptyException e) {
//            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
//            resp.getWriter().write("No indications found");
//        } catch (DaoException e) {
//            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//            resp.getWriter().write("Error occurred: " + e.getMessage());
//        }
//    }
//    @Loggable
//    @Override
//    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
//        String counterIdStr = req.getParameter("counterId");
//        String yearStr = req.getParameter("year");
//        String monthStr = req.getParameter("month");
//
//        if (counterIdStr == null || yearStr == null || monthStr == null) {
//            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
//            resp.getWriter().write("One or more parameters are missing");
//            return;
//        }
//
//        try {
//            long counterId = Long.parseLong(counterIdStr);
//            int year = Integer.parseInt(yearStr);
//            int month = Integer.parseInt(monthStr);
//            String login = req.getParameter("login");
//            Double value = Double.parseDouble(req.getParameter("value"));
//            LocalDate date = LocalDate.of(year,month,1);
//            Counter counter = counterDAO.getCounterById(counterId).orElseThrow();
//
//            indicationService.addIndicationOfUser(login, counter, date, value);
//
//            resp.setStatus(HttpServletResponse.SC_CREATED);
//        } catch (IncorrectValuesException e) {
//            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
//            resp.getWriter().write("Incorrect values provided");
//        } catch (NumberFormatException | DateTimeParseException e) {
//            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
//            resp.getWriter().write("Invalid format for counter ID or date");
//        } catch (DaoException e) {
//            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//            resp.getWriter().write("Error occurred: " + e.getMessage());
//        }
//    }
//}