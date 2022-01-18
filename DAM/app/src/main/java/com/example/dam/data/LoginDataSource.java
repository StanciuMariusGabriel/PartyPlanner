package com.example.dam.data;

import com.example.dam.data.model.LoggedInUser;

import android.content.Context;
import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import com.example.dam.database.SQLConnection;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {
    Connection connection;
    String res = "";

    public Result<LoggedInUser> login(String username, String password) {

        try {
            SQLConnection sqlConnection = new SQLConnection();
            connection = sqlConnection.connect();
            System.out.println(connection.toString());
            if (connection != null) {
                String query = "exec dbo.GetAllUsers";
                PreparedStatement statement = connection.prepareStatement(query);
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    if (resultSet.getString("username").equals(username) && resultSet.getString("password").equals(password)){
                        LoggedInUser fakeUser =
                                new LoggedInUser(
                                        resultSet.getInt("id"),
                                        username);
                        return new Result.Success<>(fakeUser);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(e);
            return new Result.Error(new IOException("Error logging in", e));
        }

        return new Result.Error(new IOException("Invalid username or password"));
    }

    public void logout() {
        // TODO: revoke authentication
    }
}