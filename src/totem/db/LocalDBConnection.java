package totem.db;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import static java.lang.String.format;

import totem.Main;
import totem.settings.Config;
import totem.debug.Log;


/**
 * Local Database Connection Management
 */
public class LocalDBConnection {
    private Connection localDBCnx;
    private static SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * Constructor
     *
     * Init access and connect to HSQLDB JDBC Driver
     */
    private LocalDBConnection() {
        // In order to have HSQLDB register itself, you need to access its JDBCDriver class first
        try {
            // Class.forName("org.hsqldb.jDBCDriver");
            // or
            Class.forName("org.hsqldb.jdbc.JDBCDriver");
        } catch (Exception e) {
            Log.dd("Failed to load HSQLDB JDBC driver.", e, 2);
        }

        // After accessing the driver, we try to connect
        try {
            this.localDBCnx = DriverManager.getConnection(
                    // ADDED ";hsqldb.write_delay=false;shutdown=true"
                    // to be sure the local database will be updated and persistent
                    "jdbc:hsqldb:" + Config.dbName + ";hsqldb.write_delay=false;shutdown=true",
                    Config.dbUsername,
                    Config.dbUserPassword
            );

            this.localDBCnx.setAutoCommit(true);
        } catch (SQLException e) {
            Log.dd("Failed to connect to the database " + Config.dbName, e, 2);
        }
    }

    /**
     * Do a SQL Query accessible from another class
     *
     * @param expression String
     * @throws SQLException
     */
    public static ArrayList<HashMap> DBquery(String expression) throws SQLException {
        // Calling query in this class but got 'localDB' object from Main.java
        return Main.localDB.query(expression);
    }

    /**
     * Execute a SQL SELECT Query
     *
     * So we will use:
     *      ResultSet executeQuery(String sql)
     *
     * @param expression String
     * @throws SQLException
     */
    private synchronized ArrayList<HashMap> query(String expression) throws SQLException {
        PreparedStatement ps = localDBCnx.prepareStatement(expression);
        ResultSet rs = ps.executeQuery();
        ResultSetMetaData rsmd = rs.getMetaData();

        int totalColumn = rsmd.getColumnCount();

        // Set the collection of all rows we will collect
        ArrayList<HashMap> collection = new ArrayList<>();

        while (rs.next()) {
            HashMap<String, String> columns = new HashMap<>(totalColumn);

            for (int i = 1; i <= totalColumn; i++) {
                columns.put(rsmd.getColumnName(i), rs.getString(i));
            }
            collection.add(columns);
        }

        return collection;
    }

    /**
     * Get a value from a key in the collection we get from a SQL Query with one line of result
     *
     * @param collection ArrayList<HashMap>
     * @param keyToFind  String
     * @return Object|null
     */
    public static Object getValueFromKey(ArrayList<HashMap> collection, String keyToFind) {
        for (HashMap row : collection) {
            for (Object key : row.keySet()) {
                if (key.equals(keyToFind)) {
                    return row.get(key);
                }
            }
        }
        return null;
    }

    /**
     * Get Values from a key in the collection we get from a SQL Query with several lines of result
     *
     * @param row       HashMap
     * @param keyToFind String
     * @return Object|null
     */
    public static Object getValueFromKey(HashMap row, String keyToFind) {
        for (Object key : row.keySet()) {
            if (key.equals(keyToFind)) {
                return row.get(key);
            }
        }
        return null;
    }

    /**
     * Update a table with a sql request
     *
     * @param sql String
     * @throws SQLException
     */
    private synchronized void update(String sql) throws SQLException {
        Statement stmt = this.localDBCnx.createStatement();

        // To execute SELECT query use: ResultSet executeQuery(String sql)
        // executeUpdate(String sql): is used to execute specified query, it may be
        // create, drop, insert, update, delete etc.
        int result = stmt.executeUpdate(sql);

        stmt.close();
    }

    private static ArrayList<ArrayList<Object>> dump(ResultSet queryResults) throws SQLException {
        ResultSetMetaData meta = queryResults.getMetaData();
        int colmax = meta.getColumnCount();

        Object column;

        ArrayList<ArrayList<Object>> result = new ArrayList<>();

        while (queryResults.next()) {
            ArrayList<Object> col = new ArrayList<>();
            for (int i = 0; i < colmax; i++) {
                column = queryResults.getObject(i + 1);

                col.add(column);
            }

            result.add(col);
        }

        return result;
    }

    /**
     * Called from the Main as a static
     *
     * @return LocalDBConnection
     */
    public static LocalDBConnection createDB() {
        LocalDBConnection localDB;

        try {
            localDB = new LocalDBConnection();
        } catch (Exception e) {
            Log.dd("Cannot create Database", e, -1);
            return null;
        }

        try {
            // IDENTITY keyword to define an auto-increment column, normally, this is the primary key
            localDB.update("CREATE TABLE IF NOT EXISTS LOGS (ID INTEGER GENERATED BY DEFAULT AS IDENTITY(START WITH 0) NOT NULL PRIMARY KEY, ID_USER VARCHAR(50) NOT NULL, ID_SITE VARCHAR(50) NOT NULL, DATE DATETIME NOT NULL, MESSAGE VARCHAR(200) NOT NULL, SYNC INT NOT NULL)");

            //localDB.update("CREATE TABLE IF NOT EXISTS LITIGE (IDPLAIGNANT VARCHAR(50) NOT NULL, IDSITE VARCHAR(50) NOT NULL, DATE DATETIME NOT NULL, MESSAGE VARCHAR(200) NOT NULL, SYNC INT NOT NULL, INDEX INTEGER IDENTITY PRIMARY KEY)");

            localDB.update("CREATE TABLE IF NOT EXISTS RESERVATION (ID INTEGER IDENTITY PRIMARY KEY, ID_RFID VARCHAR(50) NOT NULL, DATE_DEBUT DATETIME NOT NULL, DATE_FIN DATETIME NOT NULL, SYNC INT NOT NULL, DONE INT NOT NULL, SOURCE VARCHAR(50) NOT NULL)");

            localDB.update("CREATE TABLE IF NOT EXISTS USERS (ID INTEGER IDENTITY PRIMARY KEY, ID_RFID VARCHAR(50) NOT NULL)");

            //localDB.update("CREATE TABLE IF NOT EXISTS STATES (IDCASE INT, BATTID VARCHAR(50), USERENCOURS VARCHAR(50), USERRESERVATION VARCHAR(50), ENDOFRESERVATION DATETIME, STATUS VARCHAR(50))");
        } catch (SQLException e) {
            Log.dd("Cannot create Tables", e, -1);
            return null;
        }
        return localDB;
    }


    /**
     * Insert log in the table LOGS
     *
     * @param user    String
     * @param message String
     */
    public static void addLog(String user, String message) {
        try {
            // Get localDB from Main who call createDB (BE CAREFUL IN message, DO NOT USE QUOTE !!!)
            Main.localDB.update(format("INSERT INTO logs (id_user, id_site, date, message, sync) VALUES ('%s','%s','%s','%s',0);"
                    , user
                    , Config.siteId
                    , formatter.format(new Date())
                    , message)
            );
        } catch (Exception e) {
            Log.dd("Cannot INSERT INTO LOGS TABLE", e, -1);
        }
    }
}
