package com.example.dam.database;

import com.example.dam.data.model.Party;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class DbDataSource {
    Connection connection;

    public ArrayList<Party> getPartiesForUser(int userID) {
        ArrayList<Party> array = new ArrayList<>();
        try {
            SQLConnection sqlConnection = new SQLConnection();
            connection = sqlConnection.connect();
            System.out.println(connection.toString());
            if (connection != null) {
                String query = "exec dbo.GetAllPartiesForId @id = ?";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setInt(1, userID);
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String name = resultSet.getString("name");
                    String location = resultSet.getString("location");
                    String date = resultSet.getString("date");
                    int plannerId = resultSet.getInt("id_user_planner");
                    array.add(new Party(id, name, location, date, plannerId));
                }
                return array;
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return new ArrayList<>();
    }

    public boolean isAcceptedForUser(int userID, int partyID) {
        try {
            SQLConnection sqlConnection = new SQLConnection();
            connection = sqlConnection.connect();
            System.out.println(connection.toString());
            if (connection != null) {
                String query = "exec dbo.IsPartyAccepted @userid = ?, @partyid = ?";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setInt(1, userID);
                statement.setInt(2, partyID);
                ResultSet resultSet = statement.executeQuery();
                resultSet.next();
                boolean accepted = resultSet.getBoolean("is_accepted");
                return accepted;
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return false;
    }

    public ArrayList<String> getInvitedAcceptedUsernames(int partyID) {
        ArrayList<String> array = new ArrayList<>();
        try {
            SQLConnection sqlConnection = new SQLConnection();
            connection = sqlConnection.connect();
            System.out.println(connection.toString());
            if (connection != null) {
                String query = "exec dbo.GetInvitedAcceptedUsernames @partyid = ?";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setInt(1, partyID);
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    array.add(resultSet.getString("username"));
                }
                return array;
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return new ArrayList<>();
    }

    public ArrayList<String> getInvitedUsernames(int partyID) {
        ArrayList<String> array = new ArrayList<>();
        try {
            SQLConnection sqlConnection = new SQLConnection();
            connection = sqlConnection.connect();
            System.out.println(connection.toString());
            if (connection != null) {
                String query = "exec dbo.GetInvitedUsernames @partyid = ?";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setInt(1, partyID);
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    array.add(resultSet.getString("username"));
                }
                return array;
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return new ArrayList<>();
    }

    public ArrayList<String> getNotInvitedUsernames(int partyID){
        ArrayList<String> array = new ArrayList<>();
        try {
            SQLConnection sqlConnection = new SQLConnection();
            connection = sqlConnection.connect();
            System.out.println(connection.toString());
            if (connection != null) {
                String query = "exec dbo.GetAllUsers";
                PreparedStatement statement = connection.prepareStatement(query);
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    array.add(resultSet.getString("username"));
                }
                ArrayList<String> invited = getInvitedUsernames(partyID);
                for(String user : invited){
                    array.remove(user);
                }
                return array;
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return new ArrayList<>();
    }

    public void updateIsAccepted(int userID, int partyID, boolean accept){
        try {
            SQLConnection sqlConnection = new SQLConnection();
            connection = sqlConnection.connect();
            System.out.println(connection.toString());
            if (connection != null) {
                String query = "exec dbo.UpdateIsAccepted @userid = ?, @partyid = ?, @accept = ?";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setInt(1, userID);
                statement.setInt(2, partyID);
                statement.setBoolean(3, accept);
                statement.executeQuery();
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void updateParty(int partyID, String name, String location, String date){
        try {
            SQLConnection sqlConnection = new SQLConnection();
            connection = sqlConnection.connect();
            System.out.println(connection.toString());
            if (connection != null) {
                String query = "exec dbo.UpdateParty @id = ?, @name = ?, @location = ?, @date = ?";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setInt(1, partyID);
                statement.setString(2, name);
                statement.setString(3, location);
                statement.setString(4, date);
                statement.executeQuery();
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public int addParty(String name, String location, String date, int userID){
        try {
            SQLConnection sqlConnection = new SQLConnection();
            connection = sqlConnection.connect();
            System.out.println(connection.toString());
            if (connection != null) {
                String query = "exec dbo.AddParty @name = ?, @location = ?, @date = ?, @userid = ?";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, name);
                statement.setString(2, location);
                statement.setString(3, date);
                statement.setInt(4, userID);
                int changed = statement.executeUpdate();
                if(changed != 0){
                    String newQuery = "exec dbo.GetAllPlannedParties @id = ?";
                    PreparedStatement newStatement = connection.prepareStatement(newQuery);
                    newStatement.setInt(1, userID);
                    ResultSet resultSet = newStatement.executeQuery();
                    int partyID = -1;
                    while(resultSet.next()) {
                        partyID = resultSet.getInt("id");
                    }
                    return partyID;
                }
                return -1;
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return -1;
    }

    public void addInvitation(int userID, int partyID, boolean accept){
        try {
            SQLConnection sqlConnection = new SQLConnection();
            connection = sqlConnection.connect();
            System.out.println(connection.toString());
            if (connection != null) {
                String query = "exec dbo.AddInvitation @userid = ?, @partyid = ?, @accept = ?";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setInt(1, userID);
                statement.setInt(2, partyID);
                statement.setBoolean(3, accept);
                statement.executeQuery();
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public int getUserIdFromUsername(String username){
        try {
            SQLConnection sqlConnection = new SQLConnection();
            connection = sqlConnection.connect();
            System.out.println(connection.toString());
            if (connection != null) {
                String query = "exec dbo.GetAllUsers";
                PreparedStatement statement = connection.prepareStatement(query);
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    if(resultSet.getString("username").equals(username))
                        return resultSet.getInt("id");
                }
                return -1;
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return -1;
    }

    public int addUser(String username, String password){
        try {
            SQLConnection sqlConnection = new SQLConnection();
            connection = sqlConnection.connect();
            System.out.println(connection.toString());
            if (connection != null) {
                String query = "exec dbo.GetAllUsers";
                PreparedStatement statement = connection.prepareStatement(query);
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    if(resultSet.getString("username").equals(username))
                        return -1;
                }

                String addQuery = "exec dbo.AddUser @username = ?, @pass = ?";
                PreparedStatement addStatement = connection.prepareStatement(addQuery);
                addStatement.setString(1, username);
                addStatement.setString(2, password);
                int changed = addStatement.executeUpdate();
                if(changed != 0){
                    String getIdQuery = "exec dbo.GetAllUsers";
                    PreparedStatement idStatement = connection.prepareStatement(getIdQuery);
                    ResultSet idResult = idStatement.executeQuery();
                    while (idResult.next()) {
                        if(idResult.getString("username").equals(username))
                            return idResult.getInt("id");
                    }
                }
                return -1;
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return -1;
    }

    public void deleteParty(int partyID){
        try {
            SQLConnection sqlConnection = new SQLConnection();
            connection = sqlConnection.connect();
            System.out.println(connection.toString());
            if (connection != null) {
                String query = "exec dbo.DeleteParty @id = ?";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setInt(1, partyID);
                statement.executeQuery();
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void deleteUserParty(int userID, int partyID){
        try {
            SQLConnection sqlConnection = new SQLConnection();
            connection = sqlConnection.connect();
            System.out.println(connection.toString());
            if (connection != null) {
                String query = "exec dbo.DeleteUserParty @userid = ?, @partyid = ?";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setInt(1, userID);
                statement.setInt(2, partyID);
                statement.executeQuery();
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
