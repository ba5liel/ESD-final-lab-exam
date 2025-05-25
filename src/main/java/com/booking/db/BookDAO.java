package com.booking.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class BookDAO {
    public int addBook(DBUtil dbManager, Book book) throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");

        dbManager.openConnection();
        Connection conn = dbManager.getConnection();

        String sql = "INSERT INTO Books (title, author, price) VALUES (?, ?, ?)";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, book.getTitle());
        stmt.setString(2, book.getAuthor());
        stmt.setDouble(3, book.getPrice());

        int rows = stmt.executeUpdate();

        dbManager.closeConnection();
        return rows;
    }
}
