//package com.makeev.monitoring_service.servlets.admin_servlets;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.makeev.monitoring_service.aop.annotations.Loggable;
//import com.makeev.monitoring_service.dto.UserEventDTO;
//import com.makeev.monitoring_service.exceptions.DaoException;
//import com.makeev.monitoring_service.mappers.UserEventMapper;
//import com.makeev.monitoring_service.model.UserEvent;
//import com.makeev.monitoring_service.service.AdminService;
//import com.makeev.monitoring_service.utils.ConnectionManagerImpl;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.annotation.WebServlet;
//import jakarta.servlet.http.HttpServlet;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//
//import java.io.IOException;
//import java.util.List;
//
//@Loggable
//@WebServlet("/admin/logs")
//public class LoggerServlet extends HttpServlet {
//
//    public static final String CONTENT_TYPE = "application/json";
//
//    private ObjectMapper objectMapper;
//    private AdminService adminService;
//    private final UserEventMapper userEventMapper = UserEventMapper.INSTANCE;
//
//    @Override
//    public void init() throws ServletException {
//        super.init();
//        objectMapper = new ObjectMapper();
//        adminService = new AdminService(new ConnectionManagerImpl());
//    }
//
//    @Override
//    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
//        try {
//            List<UserEvent> userEvents = adminService.getAllEvents();
//
//            List<UserEventDTO> userEventDTOs = userEvents.stream()
//                    .map(userEventMapper::toDTO)
//                    .toList();
//
//            resp.setContentType(CONTENT_TYPE);
//            resp.setStatus(HttpServletResponse.SC_OK);
//            objectMapper.writeValue(resp.getOutputStream(), userEventDTOs);
//        } catch (DaoException e) {
//            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//            resp.getWriter().write("Error occurred: " + e.getMessage());
//        }
//    }
//}