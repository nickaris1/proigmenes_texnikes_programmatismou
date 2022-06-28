package gr.upatras.Database;


import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sqlite.SQLiteConfig;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/**
 * @author issaris nikolaos
 *
 * Class to get access to the database
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
        SQLiteConfig config = new SQLiteConfig();
        config.enforceForeignKeys(true);
        File file = new File(Objects.requireNonNull(DatabaseAccess.class.getClassLoader().getResource(fileName)).getFile());
        log.debug(file.getAbsolutePath());
        connection = DriverManager.getConnection("jdbc:sqlite:" + file.getAbsolutePath(), config.toProperties());
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
     * Select * from tableName
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
     * Select * from tableName where &#60;primarykey&#62;=id
     *
     * @param tableName name of table in database
     * @param id        Id of primary key in table
     * @param callback  callback listener. calls run method with ResultSet parameter from the sqlite response.
     */
    public static void getById(String tableName, int id, @NotNull DatabaseAccessCallback callback) {
        getById(SQLiteFILENAME, tableName, id, callback);
    }

    /**
     * Select * from tableName where &#60;primarykey&#62;=id
     *
     * @param fileName  Filename for the database (we can use custom database for unit test)
     * @param tableName name of table in database
     * @param id        Id of primary key in table
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
     * Select * from tableName WHERE query
     *
     * @param tableName name of table in database
     * @param query     eg: column_name = value
     * @param callback  callback listener. calls run method with ResultSet parameter from the sqlite response.
     */
    public static void getCustom(String tableName, String query, @NotNull DatabaseAccessCallback callback) {
        getCustom(SQLiteFILENAME, tableName, query, callback);
    }

    /**
     * Select * from tableName WHERE query
     *
     * @param fileName  Filename for the database (we can use custom database for unit test)
     * @param tableName name of table in database
     * @param query     eg: column_name = value
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
     * INSERT INTO tableName query
     *
     * @param tableName name of table in database
     * @param query     query eg:  (column1, column2, column3, ...) VALUES (value1, value2, value3, ...)
     */
    public static Boolean addEntry(String tableName, String query) {
        return addEntry(SQLiteFILENAME, tableName, query);
    }

    /**
     * INSERT INTO tableName query
     *
     * @param fileName  Filename for the database (we can use custom database for unit test)
     * @param tableName name of table in database
     * @param query     query eg:  (column1, column2, column3, ...) VALUES (value1, value2, value3, ...)
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
     * DELETE FROM tableName WHERE &#60;primarykey&#62;=id
     *
     * @param tableName name of table in database
     * @param id        Id of primary key in table
     */
    public static Boolean deleteEntry(String tableName, int id) {
        return deleteEntry(SQLiteFILENAME, tableName, id);
    }

    /**
     * DELETE FROM tableName WHERE &#60;primarykey&#62;=id
     *
     * @param fileName  Filename for the database (we can use custom database for unit test)
     * @param tableName name of table in database
     * @param id        Id of primary key in table
     */
    public static Boolean deleteEntry(String fileName, String tableName, int id) {
        try {
            connect(fileName);
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.
            List<String> pk = getPrimaryKeys(connection.getMetaData().getPrimaryKeys(null, null, tableName));
            String q = "DELETE FROM " + tableName + " WHERE " + pk.get(0) + "=" + id;
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
     * Update tableName SET query WHERE  &#60;primarykey&#62;=id;
     *
     * @param tableName name of table in database
     * @param id        Id of primary key in table
     */
    public static Boolean updateEntry(String tableName, int id, String query) {
        return updateEntry(SQLiteFILENAME, tableName, id, query);
    }

    /**
     * Update tableName SET query WHERE  &#60;primarykey&#62;=id;
     *
     * @param fileName  Filename for the database (we can use custom database for unit test)
     * @param tableName name of table in database
     * @param id        Id of primary key in table
     */
    public static Boolean updateEntry(String fileName, String tableName, int id, String query) {
        try {
            connect(fileName);
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.
            List<String> pk = getPrimaryKeys(connection.getMetaData().getPrimaryKeys(null, null, tableName));
            String q = "UPDATE " + tableName + " SET " + query + " WHERE " + pk.get(0) + "=" + id;
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
     * @param primaryKeys Result set
     * @return returns list with primary keys
     */
    private static List<String> getPrimaryKeys(ResultSet primaryKeys) {
        List<String> primaryKeysArray = new ArrayList<>();
        try {
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


    /**
     * Run Custom Query
     *
     * @param query  Query to execute
     */
    public static Boolean runCustomQuery(String query, DatabaseAccessCallback callback) {
        return runCustomQuery(SQLiteFILENAME, query, callback);
    }

    /**
     * Run Custom Query
     *
     * @param fileName  Filename for the database (we can use custom database for unit test)
     * @param query  Query to execute
     */
    public static Boolean runCustomQuery(String fileName, String query, DatabaseAccessCallback callback) {
        try {
            connect(fileName);
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.
            List<String> pk = new ArrayList<>();

            log.info(query);
            ResultSet rs = statement.executeQuery(query);
            callback.run(rs, pk);

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
    public DatabaseAccess() {
    }
}



