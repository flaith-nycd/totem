package totem.settings;

import java.io.*;
import java.util.Properties;

import totem.debug.Log;


/**
 * Handle the Config file
 */
public class Config {
    public static Boolean configFileExist = false;

    // We need to separate the directory with the correct separator char
    private static String separator = File.separator;

    // Default values ...
    // ------------------
    public static String appName = "Totem";
    public static String version = "0.0.1";
    public static String siteId = "site #000";

    // Local Database
    public static String dbPath = "db";
    private static String dbFilename = "totem_db";

    public static String dbName = dbPath + separator + dbFilename;
    public static String dbUsername = "totem";
    public static String dbUserPassword = "totem";

    // Remote Database
    public static String rDbURL = "localhost";
    public static String rDbPHPFile = "handleSQL.php";
    public static String rDbHostname="localhost"; // server host name
    public static String rDbUsername="totem";     // username
    public static String rDbUserPassword="totem"; // password
    public static String rDbName="totem";         // database name

    // Much better to use the working directory,
    // it's the complete directory we are launching the application
    private static String workingDirectory = System.getProperty("user.dir");
    //private static String currentUserHomeDir = System.getProperty("user.home");

    // Where is the config file now?
    public static String configFilename = workingDirectory + separator + "conf.properties";

    // Get the values from our config file (configFilename)
    //
    // Java static block:
    // Is used to initialize the static data member.
    // It is executed before main method at the time of classloading.
    static {
        File configFile = new File(configFilename);
        if (configFile.exists()) {
            configFileExist = true;

            Properties prop = new Properties();

            try {
                InputStream inputFile = new FileInputStream(configFile);
                prop.load(inputFile);

                appName = prop.getProperty("APP_NAME");
                version = prop.getProperty("VERSION");

                // Which site ?
                siteId = prop.getProperty("SITE_ID");

                // Local Database
                dbPath = prop.getProperty("dbPath");
                dbName = dbPath + separator + prop.getProperty("dbName");
                dbUsername = prop.getProperty("dbUsername");
                dbUserPassword = prop.getProperty("dbUserPassword");

                // Remote Database
                rDbURL = prop.getProperty("rDbURL");
                rDbPHPFile = prop.getProperty("rDbPHPFile");
                rDbHostname = prop.getProperty("rDbHostname");
                rDbUsername = prop.getProperty("rDbUsername");
                rDbUserPassword = prop.getProperty("rDbUserPassword");
                rDbName = prop.getProperty("rDbName");
            } catch (IOException e) {
                Log.dd("Cannot open the config file \""+ configFilename + "\"", e);
            }
        }
    }
}

