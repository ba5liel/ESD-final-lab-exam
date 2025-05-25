package com.booking;

import com.booking.db.DBUtil;

public class TestConnection {
    public static void main(String[] args) {
        DBUtil db = new DBUtil();
        try {
            db.openConnection();
            System.out.println("Connected to the database successfully!");
            db.closeConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
