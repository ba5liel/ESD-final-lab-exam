package com.booking.servlet;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class DispatcherServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        processRequest(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        processRequest(request, response);
    }
    
    private void processRequest(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Get the requested path
        String path = request.getServletPath();
        
        // Route the request based on the path
        switch (path) {
            case "/registerBook":
                // Forward to BookRegistrationServlet
                RequestDispatcher registerDispatcher = request.getRequestDispatcher("/servlet/BookRegistrationServlet");
                registerDispatcher.forward(request, response);
                break;
                
            case "/displayBooks":
                // Forward to DisplayBooksServlet
                RequestDispatcher displayDispatcher = request.getRequestDispatcher("/servlet/DisplayBooksServlet");
                displayDispatcher.forward(request, response);
                break;
                
            case "/deleteBook":
                // Forward to DeleteBookServlet
                RequestDispatcher deleteDispatcher = request.getRequestDispatcher("/servlet/DeleteBookServlet");
                deleteDispatcher.forward(request, response);
                break;
                
            case "/bookForm.html":
                // Forward to bookForm.html
                RequestDispatcher formDispatcher = request.getRequestDispatcher("/bookForm.html");
                formDispatcher.forward(request, response);
                break;
                
            default:
                // Default to index.html for root or unmatched paths
                if (path.equals("/") || path.isEmpty()) {
                    RequestDispatcher indexDispatcher = request.getRequestDispatcher("/index.html");
                    indexDispatcher.forward(request, response);
                } else {
                    // Try to forward to the requested resource
                    try {
                        RequestDispatcher resourceDispatcher = request.getRequestDispatcher(path);
                        resourceDispatcher.forward(request, response);
                    } catch (Exception e) {
                        // If resource doesn't exist, send 404
                        response.sendError(HttpServletResponse.SC_NOT_FOUND, "Resource not found: " + path);
                    }
                }
        }
    }
} 