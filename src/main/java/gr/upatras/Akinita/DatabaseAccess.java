package gr.upatras.Akinita;


import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.sql.*;
import java.util.Objects;


/**
 * @author issaris nikolaos
 */
public class DatabaseAccess {
    private static final String SQLiteFILENAME = "database/database.db"; // relative path from resources
    private static final Logger log = LoggerFactory.getLogger(DatabaseAccess.class);
    public static Connection connection;

    /**
     * Connects to static sqlite3 file
     *
     * @throws SQLException throws sql Exception if file not found
     */
    public static void connect(final String fileName) throws SQLException {
        File file = new File(Objects.requireNonNull(DatabaseAccess.class.getClassLoader().getResource(fileName)).getFile());
        log.info(file.getAbsolutePath());
        connection = DriverManager.getConnection("jdbc:sqlite:" + file.getAbsolutePath());
    }

    /**
     * Select * from tableName
     *
     * @param tableName name of table in database
     * @param callback  callback listener. calls run method with ResultSet parameter from the sqlite response.
     */
    public static void get(String tableName, @NotNull DatabaseAccessCallback callback) {
        get(SQLiteFILENAME, tableName, callback);
    }

    /**
     * Select * from tableName in fileName database
     *
     * @param fileName  Filename for the database (we can use custom database for unit test)
     * @param tableName name of table in database
     * @param callback  callback listener. calls run method with ResultSet parameter from the sqlite response.
     */
    public static void get(String fileName, String tableName, @NotNull DatabaseAccessCallback callback) {
        try {
            connect(fileName);

            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.
            ResultSet rs = statement.executeQuery("select * from " + tableName);
            callback.run(rs);
        } catch (SQLException e) {
            log.error(e.getMessage());
        } finally {
            try {
                if (connection != null) connection.close();
            } catch (SQLException e) {
                // connection close failed.
                log.error(e.getMessage());
            }
        }
    }

    public DatabaseAccess() {
    }

}



