package com.booking.servlet;

import com.booking.db.DBConnectionManager;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;

@WebServlet("/deleteBook")
public class DeleteBookServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private DBConnectionManager dbManager = new DBConnectionManager();
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Get book ID from form submission
        String bookIdStr = request.getParameter("bookId");
        int bookId;
        
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        
        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Delete Book</title>");
        out.println("<style>");
        out.println("body { font-family: Arial, sans-serif; margin: 0; padding: 20px; text-align: center; }");
        out.println("h2 { color: #333; }");
        out.println(".success { color: green; }");
        out.println(".error { color: red; }");
        out.println("</style>");
        out.println("</head>");
        out.println("<body>");
        
        try {
            // Validate book ID
            if (bookIdStr == null || bookIdStr.trim().isEmpty()) {
                throw new IllegalArgumentException("Book ID is required");
            }
            
            bookId = Integer.parseInt(bookIdStr);
            
            // Load the driver explicitly
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            // Open database connection
            dbManager.openConnection();
            Connection conn = dbManager.getConnection();
            
            // Prepare and execute delete statement
            String sql = "DELETE FROM Books WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, bookId);
            
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                out.println("<h2 class='success'>✅ Book Deleted Successfully!</h2>");
                out.println("<p>The book with ID " + bookId + " has been removed from the database.</p>");
            } else {
                out.println("<h2 class='error'>❌ Delete Operation Failed</h2>");
                out.println("<p>No book found with ID " + bookId + ".</p>");
            }
            
            stmt.close();
            dbManager.closeConnection();
            
        } catch (NumberFormatException e) {
            out.println("<h2 class='error'>❌ Invalid Book ID</h2>");
            out.println("<p>Please provide a valid numeric ID for the book.</p>");
        } catch (Exception e) {
            out.println("<h2 class='error'>❌ Error Occurred</h2>");
            out.println("<p>" + e.getMessage() + "</p>");
            e.printStackTrace(out);
        }
        
        out.println("<div style='margin-top: 20px;'>");
        out.println("<a href='displayBooks'>Back to Book List</a> | ");
        out.println("<a href='index.html'>Home</a>");
        out.println("</div>");
        
        out.println("</body>");
        out.println("</html>");
    }
} 