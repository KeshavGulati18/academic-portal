/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.example;


import java.sql.Connection;
import java.sql.DriverManager;

public class connect_asap {

    public static Connection ConnectDB() {

        try {
            Class.forName("org.postgresql.Driver");
            Connection conn = DriverManager
                    .getConnection("jdbc:postgresql://localhost:5432/academic_schema",
                            "postgres", "1234");
            System.out.println("Database opened!");
            return conn;

        } catch (Exception e) {
            System.out.println("Sorry, couldn't connect!");
            System.out.println(e);
            return null;

        }

    }
}