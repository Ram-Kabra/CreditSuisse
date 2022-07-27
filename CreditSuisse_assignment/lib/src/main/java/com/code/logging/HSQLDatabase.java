package com.code.logging;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HSQLDatabase {

    static Connection connection;
    static String connectionString = "jdbc:hsqldb:file:db-data/hsqldatabase";

    public static void exportToDatabase(List <ServerLog> list) throws Exception {

        Thread tt = new Thread(new AsyncTimer());
        tt.start();
        Logger databaseLogger = Logger.getLogger("hsqldb.db");
        databaseLogger.setUseParentHandlers(false);
        databaseLogger.setLevel(Level.WARNING);
        databaseLogger.addHandler(new FileHandler("myapp.log"));
        Statement stmt;
        try {
            Class.forName("org.hsqldb.jdbc.JDBCDriver");
        } catch (ClassNotFoundException exception) {
            throw exception;
        }
        try {
            connection = DriverManager.getConnection(connectionString, "SA", "");
            stmt = connection.createStatement();
            stmt.executeUpdate("CREATE TABLE if not exists ServerLog (EVENT_ID VARCHAR(50) NOT NULL, EVENT_DURATION INT NOT NULL, EVENT_TYPE VARCHAR(20) NOT NULL, EVENT_HOST VARCHAR(20) NOT NULL, EVENT_FLAG BIT DEFAULT FALSE NOT NULL);");
            int logCounter = 0;
            String SQLStatement = "INSERT INTO ServerLog VALUES (?, ?, ?, ?, ?)";
            PreparedStatement prepStatement = connection.prepareStatement(SQLStatement);

            for (ServerLog eventLog : list) {
                prepStatement.setString(1, eventLog.getId());
                prepStatement.setInt(2, eventLog.getProcessedTime());
                prepStatement.setString(3, eventLog.getType());
                prepStatement.setString(4, eventLog.getHost());
                prepStatement.setBoolean(5, eventLog.isAlertFlag());
                prepStatement.executeUpdate();
                logCounter++;
            }
            System.out.println(logCounter + " rows added into database.");
            tt.interrupt();

        } catch (Exception e) {
            throw e;
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }
}