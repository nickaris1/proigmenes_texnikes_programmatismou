package gr.upatras.Akinita;


import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;


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

            callback.run(rs, getPrimaryKeys(connection.getMetaData().getPrimaryKeys(null, null, tableName)));
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


    /**
     * Select * from tableName in fileName database
     *
     * @param tableName name of table in database
     * @param id Id of primary key in table
     * @param callback  callback listener. calls run method with ResultSet parameter from the sqlite response.
     */
    public static void getById(String tableName, int id, @NotNull DatabaseAccessCallback callback) {
        getById(SQLiteFILENAME, tableName, id, callback);
    }

    /**
     * Select * from tableName in fileName database
     *
     * @param fileName  Filename for the database (we can use custom database for unit test)
     * @param tableName name of table in database
     * @param id Id of primary key in table
     * @param callback  callback listener. calls run method with ResultSet parameter from the sqlite response.
     */
    public static void getById(String fileName, String tableName, int id, @NotNull DatabaseAccessCallback callback) {
        try {
            connect(fileName);

            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.
            List<String> pk = getPrimaryKeys(connection.getMetaData().getPrimaryKeys(null, null, tableName));
            ResultSet rs = statement.executeQuery("select * from " + tableName + " where " + pk.get(0) + "=" + id);

            callback.run(rs, pk);
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


    /**
     * Select * from tableName in fileName database
     *
     * @param tableName name of table in database
     * @param query eg: column_name = value
     * @param callback  callback listener. calls run method with ResultSet parameter from the sqlite response.
     */
    public static void getCustom(String tableName, String query, @NotNull DatabaseAccessCallback callback) {
        getCustom(SQLiteFILENAME, tableName, query, callback);
    }

    /**
     * Select * from tableName in fileName database
     *
     * @param fileName  Filename for the database (we can use custom database for unit test)
     * @param tableName name of table in database
     * @param query eg: column_name = value
     * @param callback  callback listener. calls run method with ResultSet parameter from the sqlite response.
     */
    public static void getCustom(String fileName, String tableName, String query, @NotNull DatabaseAccessCallback callback) {
        try {
            connect(fileName);

            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.
            List<String> pk = getPrimaryKeys(connection.getMetaData().getPrimaryKeys(null, null, tableName));
            String q = "SELECT * FROM " + tableName + " WHERE " + query + ";";
            log.info(q);
            ResultSet rs = statement.executeQuery(q);

            callback.run(rs, pk);
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


    /**
     * Select * from tableName in fileName database
     *
     * @param tableName name of table in database
     * @param query query eg:  (column1, column2, column3, ...) VALUES (value1, value2, value3, ...)
     */
    public static Boolean addEntry(String tableName, String query) {
        return addEntry(SQLiteFILENAME, tableName, query);
    }

    /**
     * Select * from tableName in fileName database <BR>
     *
     * @param fileName  Filename for the database (we can use custom database for unit test)
     * @param tableName name of table in database
     * @param query query eg:  (column1, column2, column3, ...) VALUES (value1, value2, value3, ...)
     */
    public static Boolean addEntry(String fileName, String tableName, String query) {
        try {
            connect(fileName);
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.
            String q = "INSERT INTO " + tableName + " " + query + ";";
            log.info(q);
            statement.execute(q);

            if (connection != null) connection.close();
            return true;
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
        return false;
    }

    /**
     *
     * @param primaryKeys Result set
     * @return returns list with primary keys
     */
    private static List<String> getPrimaryKeys(ResultSet primaryKeys) {
        List<String> primaryKeysArray = new ArrayList<>();
        try{
            while (primaryKeys.next()) {
                String columnName = primaryKeys.getString("COLUMN_NAME");
                primaryKeysArray.add(columnName);
                log.debug("getPrimaryKeys(): columnName=" + columnName);
            }
        } catch (SQLException e) {
            log.error("Error at get Primary Keys");
            log.error(e.getMessage());
        }
        return primaryKeysArray;
    }

    public static <T extends Entity>String queryCreator(T obj) {
        StringJoiner query = new StringJoiner("");

        for (Field field : obj.getClass().getDeclaredFields()) {
            try {
                field.setAccessible(true); // if you want to modify private fields
                if (field.get(obj) != null && !String.valueOf(field.getType()).equals("interface java.util.Map")) {
                    query.add("\"");
                    query.add(obj.getFieldsMap().get(field.getName()));
                    query.add("\"=\"");
                    query.add(String.valueOf(field.get(obj)));
                    query.add("\" and ");
                }
            } catch (IllegalAccessException e) {
                log.error(e.getMessage());
            }
        }
        String qStr = query.toString();
        return qStr.substring(0, qStr.length() - 5);
    }

    public DatabaseAccess() {
    }

}



