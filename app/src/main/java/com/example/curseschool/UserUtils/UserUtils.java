package com.example.curseschool.UserUtils;

import android.util.Log;

import com.example.curseschool.Helpers.ConnectionHelper;
import com.example.curseschool.Objects.User;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class UserUtils {

    public static User getUserFromEmail(String email) {
        User user = getUserData(email);
        return user;
    }

    private static User getUserData(String email) {
        User user = null;
        try {
            ConnectionHelper connectionHelper = new ConnectionHelper();
            Connection connect = connectionHelper.getConnection();
            if (connect != null) {
                String query = "Select * from users where email = '" + email + "'";
                Statement statement = connect.createStatement();
                ResultSet resultSet = statement.executeQuery(query);
                while (resultSet.next()) {
                    user.setId(resultSet.getInt(1));
                    user.setFirstName(resultSet.getString(2));
                    user.setLastName(resultSet.getString(3));
                    user.setEmail(resultSet.getString(4));
                    user.setPhoneNumber(resultSet.getString(6));
                    user.setCity(resultSet.getString(7));
                    user.setStreet(resultSet.getString(8));
                    user.setType(resultSet.getString(9));
                    user.setArchival(resultSet.getBoolean(10));
                    user.setPostalCode(resultSet.getString(11));
                    user.setIdn(resultSet.getString(12));
                }
                connect.close();

            } else {
                String connectionResult = "Check Connection";
            }
        } catch (Exception ex) {
            Log.e("Error :", ex.getMessage());
        }

        return user;

    }

    public static User getUserById(int id) {
        User user = null;
        try {
            ConnectionHelper connectionHelper = new ConnectionHelper();
            Connection connect = connectionHelper.getConnection();
            if (connect != null) {
                String query = "Select * from users where id = '" + id + "'";
                Statement statement = connect.createStatement();
                ResultSet resultSet = statement.executeQuery(query);
                while (resultSet.next()) {
                    user.setId(resultSet.getInt(1));
                    user.setFirstName(resultSet.getString(2));
                    user.setLastName(resultSet.getString(3));
                    user.setEmail(resultSet.getString(4));
                    user.setPhoneNumber(resultSet.getString(6));
                    user.setCity(resultSet.getString(7));
                    user.setStreet(resultSet.getString(8));
                    user.setType(resultSet.getString(9));
                    user.setArchival(resultSet.getBoolean(10));
                    user.setPostalCode(resultSet.getString(11));
                    user.setIdn(resultSet.getString(12));
                }
                connect.close();

            } else {
                String connectionResult = "Check Connection";
            }
        } catch (Exception ex) {
            Log.e("Error :", ex.getMessage());
        }

        return user;

    }
}
