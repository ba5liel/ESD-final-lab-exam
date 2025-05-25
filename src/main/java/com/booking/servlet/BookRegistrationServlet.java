package com.booking.servlet;

import com.booking.db.Book;
import com.booking.db.BookDAO;
import com.booking.db.DBUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class BookRegistrationServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private DBUtil dbManager;
    private BookDAO bookDAO;

    public BookRegistrationServlet(DBUtil dbManager, BookDAO bookDAO) {
        this.dbManager = dbManager;
        this.bookDAO = bookDAO;
    }

    public BookRegistrationServlet() {
        this.dbManager = null;
        this.bookDAO = null;
    }

    @Override
    public void init() throws ServletException {

        if (this.dbManager == null || this.bookDAO == null) {
            org.springframework.web.context.WebApplicationContext springContext = org.springframework.web.context.support.WebApplicationContextUtils
                    .getWebApplicationContext(getServletContext());
            if (springContext != null) {
            if (this.dbManager == null) {

                DBUtil manager = springContext.getBean(DBUtil.class);

                try {
                    java.lang.reflect.Field field = BookRegistrationServlet.class.getDeclaredField("dbManager");
                    field.setAccessible(true);
                    field.set(this, manager);
                } catch (Exception e) {
                    throw new ServletException("Failed to inject dependencies", e);
                }
            }

            if (this.bookDAO == null) {

                BookDAO dao = springContext.getBean(BookDAO.class);

                try {
                    java.lang.reflect.Field field = BookRegistrationServlet.class.getDeclaredField("bookDAO");
                    field.setAccessible(true);
                    field.set(this, dao);
                } catch (Exception e) {
                    throw new ServletException("Failed to inject dependencies", e);
                }
            }
        } else {
            this.dbManager = new DBUtil();
            this.bookDAO = new BookDAO();
        }
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String title = request.getParameter("title");
        String author = request.getParameter("author");
        double price = Double.parseDouble(request.getParameter("price"));

        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = response.getWriter();

        try {
            Book book = new Book(title, author, price);
            BookDAO bookDAO = new BookDAO();
            bookDAO.addBook(dbManager, book);

        } catch (Exception e) {
            out.println("<h2>Error: " + e.getMessage() + "</h2>");
            e.printStackTrace(out);
        }
    }
}