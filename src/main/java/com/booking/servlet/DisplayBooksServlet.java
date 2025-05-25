package com.booking.servlet;

import com.booking.db.DBConnectionManager;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;


public class DisplayBooksServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private DBConnectionManager dbManager = new DBConnectionManager();
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        
        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Book List</title>");
        out.println("<style>");
        out.println("table { border-collapse: collapse; width: 80%; margin: 20px auto; }");
        out.println("th, td { padding: 8px; text-align: left; border-bottom: 1px solid #ddd; }");
        out.println("th { background-color: #f2f2f2; }");
        out.println("tr:hover { background-color: #f5f5f5; }");
        out.println("h2 { text-align: center; color: #333; }");
        out.println(".delete-btn { background-color: #ff6b6b; color: white; border: none; padding: 5px 10px; cursor: pointer; }");
        out.println("</style>");
        out.println("</head>");
        out.println("<body>");
        out.println("<h2>Book Inventory</h2>");
        
        try {
            // Load the driver explicitly
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            dbManager.openConnection();
            Connection conn = dbManager.getConnection();
            
            Statement stmt = conn.createStatement();
            String sql = "SELECT * FROM Books";
            ResultSet rs = stmt.executeQuery(sql);
            
            out.println("<table>");
            out.println("<tr><th>ID</th><th>Title</th><th>Author</th><th>Price</th><th>Action</th></tr>");
            
            boolean hasRecords = false;
            
            while (rs.next()) {
                hasRecords = true;
                int id = rs.getInt("id");
                String title = rs.getString("title");
                String author = rs.getString("author");
                double price = rs.getDouble("price");
                
                out.println("<tr>");
                out.println("<td>" + id + "</td>");
                out.println("<td>" + title + "</td>");
                out.println("<td>" + author + "</td>");
                out.println("<td>$" + String.format("%.2f", price) + "</td>");
                out.println("<td>");
                out.println("<form action='deleteBook' method='post'>");
                out.println("<input type='hidden' name='bookId' value='" + id + "'>");
                out.println("<input type='submit' value='Delete' class='delete-btn'>");
                out.println("</form>");
                out.println("</td>");
                out.println("</tr>");
            }
            
            if (!hasRecords) {
                out.println("<tr><td colspan='5' style='text-align:center'>No books found in the database.</td></tr>");
            }
            
            out.println("</table>");
            
            rs.close();
            stmt.close();
            dbManager.closeConnection();
            
        } catch (Exception e) {
            out.println("<div style='color:red; text-align:center'>");
            out.println("<h3>Error occurred:</h3>");
            out.println("<p>" + e.getMessage() + "</p>");
            out.println("</div>");
            e.printStackTrace(out);
        }
        
        out.println("<div style='text-align:center; margin-top: 20px;'>");
        out.println("<a href='index.html'>Return to Home</a>");
        out.println("</div>");
        
        out.println("</body>");
        out.println("</html>");
    }
} 