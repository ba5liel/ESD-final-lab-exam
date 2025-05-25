package com.booking;

import com.booking.db.DBConnectionManager;

public class TestConnection {
    public static void main(String[] args) {
        DBConnectionManager db = new DBConnectionManager();
        try {
            db.openConnection();
            System.out.println("✅ Connected to the database successfully!");
            db.closeConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
