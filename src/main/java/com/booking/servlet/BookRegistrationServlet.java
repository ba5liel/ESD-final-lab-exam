package com.booking.servlet;

import com.booking.db.DBUtil;
import lombok.NoArgsConstructor;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class BookRegistrationServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private final DBUtil dbManager;
    
    public BookRegistrationServlet(DBUtil dbManager) {
        this.dbManager = dbManager;
    }
    
    public BookRegistrationServlet() {
        this.dbManager = null;
    }
    
    @Override
    public void init() throws ServletException {
        if (this.dbManager == null) {
            org.springframework.web.context.WebApplicationContext springContext = 
                org.springframework.web.context.support.WebApplicationContextUtils
                    .getWebApplicationContext(getServletContext());
                    
            DBUtil manager = springContext.getBean(DBUtil.class);
            
            try {
                java.lang.reflect.Field field = BookRegistrationServlet.class.getDeclaredField("dbManager");
                field.setAccessible(true);
                field.set(this, manager);
            } catch (Exception e) {
                throw new ServletException("Failed to inject dependencies", e);
            }
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String title = request.getParameter("title");
        String author = request.getParameter("author");
        double price = Double.parseDouble(request.getParameter("price"));

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            dbManager.openConnection();
            Connection conn = dbManager.getConnection();

            String sql = "INSERT INTO Books (title, author, price) VALUES (?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, title);
            stmt.setString(2, author);
            stmt.setDouble(3, price);

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                out.println("<h2>✅ Book registered successfully!</h2>");
            } else {
                out.println("<h2>❌ Failed to register book.</h2>");
            }

            dbManager.closeConnection();
        } catch (Exception e) {
            e.printStackTrace(out);
        }
    }
}