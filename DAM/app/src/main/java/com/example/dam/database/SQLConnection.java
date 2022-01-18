package com.example.dam.database;

import android.annotation.SuppressLint;
import android.os.StrictMode;

import com.example.dam.App;
import com.example.dam.R;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class SQLConnection {

    @SuppressLint("NewApi")
    public Connection connect() {
        Connection conn = null;
        String ConnURL = null;
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();

            ConnURL = "jdbc:jtds:sqlserver://" +
                    App.getContext().getString(R.string.sql_ip) + ":" +
                    App.getContext().getString(R.string.sql_port) + ";" +
                    "databasename=" +
                    App.getContext().getString(R.string.sql_database) +
                    ";user=" +
                    App.getContext().getString(R.string.sql_user) + ";password=" +
                    App.getContext().getString(R.string.sql_pass) + ";";

            conn = DriverManager.getConnection(ConnURL);
            System.out.println("Connection made");
        } catch (SQLException se) {
            System.out.println(se);
        } catch (ClassNotFoundException e) {
            System.out.println(e);
        } catch (Exception e) {
            System.out.println(e);
        }
        return conn;
    }
}



