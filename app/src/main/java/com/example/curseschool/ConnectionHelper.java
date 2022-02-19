package com.example.curseschool;

import android.annotation.SuppressLint;
import android.os.StrictMode;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionHelper {
    Connection connection;
    String ip, port, db, username, pass;

    @SuppressLint("NewApi")
    public Connection getConnection() {
        ip = "192.168.0.31";
        port = "1433";
        db = "course";
        username = "mbo";
        pass = "mbo";
        StrictMode.ThreadPolicy tpolicy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(tpolicy);
        Connection connection = null;
        String ConnectionURL = null;
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            ConnectionURL = "jdbc:jtds:sqlserver://" + ip + ":" + port + ";" + "databaseName=" + db + ";user=" + username + ";password=" + pass + ";";
            connection = DriverManager.getConnection(ConnectionURL);
        } catch (Exception ex) {
            Log.e("Error : ", ex.getMessage());
        }
        return connection;
    }
}
