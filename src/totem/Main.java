package totem;

import totem.box.BoxSettings;
import totem.db.DistantDBConnection;
import totem.db.LocalDBConnection;
import totem.debug.Log;
import totem.settings.Config;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;


/**
 * MAIN PROGRAM
 */
public class Main {
    public static LocalDBConnection localDB;
    private static DistantDBConnection remoteDB;

    // Java static block:
    // Is used to initialize the static data member.
    // It is executed before main method at the time of classloading.
    //
    // The static keyword in java is used for memory management mainly.
    // We can apply java static keyword with variables, methods, blocks and nested class.
    // The static keyword belongs to the class than instance of the class.
    //
    // The core advantage of static method is that there is no need to create object to invoke the static method.
    // The main method is executed by the JVM, so it doesn't require to create object to invoke the main method.
    // So it saves memory.
    static {
        System.out.println("Launching " + Config.appName + " version " + Config.version + "\n");

        // Exception ignored because we already check in the createDB Method
        try { localDB = LocalDBConnection.createDB(); } catch (Exception ignored) { }

        try { remoteDB = DistantDBConnection.init(); } catch (Exception ignored) { }
    }

    /**
     * Main
     *
     * @param args String[]
     */
    public static void main(String[] args) {
        // A little log in the database...
        LocalDBConnection.addLog("admin","Starting " + Config.appName + " version " + Config.version + " ...");

        // ********************************************
        // User-defined class objects in Java ArrayList
        // ********************************************

        // Creating user-defined class objects ********
        // For the test, add 4 instances of BoxSettings
        BoxSettings box1 = new BoxSettings("0", "9000");
        BoxSettings box2 = new BoxSettings("1", "9001");
        BoxSettings box3 = new BoxSettings("2", "9002");
        BoxSettings box4 = new BoxSettings("3", "9003");

        // Create an array of type "BoxSettings" ******
        ArrayList<BoxSettings> modules = new ArrayList<>();

        // For the test, add boxes in the array
        // Another way:
        //      modules.add(new BoxSettings("0", "9000"));
        modules.add(box1);
        modules.add(box2);
        modules.add(box3);
        modules.add(box4);

        // Create an object to be used in the loop below
        BoxSettings module;

        // LOOP to check and get the iRFID for the idCase 2
        //      PHP way: foreach(ClassInstance.instances.keySet() as ident)
        for (String ident : BoxSettings.instances.keySet()) {
            // Get the keys
            module = BoxSettings.instances.get(ident);
            if (module.idCase != null) {
                // Just show when idCase = 2
                if (module.idCase.equals("2")) {
                    System.out.println("idCase: " + module.idCase + " iRFID: " + module.iRFID);
                    System.out.println();
                    break;
                }
            }
        }

        /*
        Iterator<BoxSettings> it = modules.iterator();
        while (it.hasNext()) {
            try {
                BoxSettings m = it.next();
                System.out.println("module: " + m.idCase);
            } catch (Exception localException) {
            }
        }
        */

        // Smaller than using iterator, the FOREACH, again !!!
        for (BoxSettings oneModule : modules) {
            try {
                String rfid = oneModule.GetRFIDfromIdCase(oneModule.idCase);
                System.out.println("RFID for case ID " + oneModule.idCase + ": " + rfid);
            } catch (Exception ignored) { }
        }

        // Show path separator and current working directory
        System.out.println("File separator: " + File.separator);
        System.out.println("Working Directory = " + System.getProperty("user.dir"));

        System.out.println("--- WHERE IS THE CONFIG FILE ---");
        if (Config.configFileExist) {
            System.out.println("dbPath: " + Config.configFilename);
        } else {
            Log.dd("Config file \"" + Config.configFilename + "\" not found !!!");
        }

        System.out.println("--- DATABASE FROM CONFIG FILE ---");
        System.out.println("dbPath: " + Config.dbPath);
        System.out.println("dbName: " + Config.dbName);
        System.out.println("dbUsername: "+ Config.dbUsername);
        System.out.println("dbUserPassword: "+ Config.dbUserPassword);

        // Again a little log in the database...
        LocalDBConnection.addLog("admin","Ending " + Config.appName + " version " + Config.version + " ...");

        // And display our logs
        System.out.println("--- WHAT'S IN OUR LOGS TABLE ---");
        try {
            ArrayList<HashMap> logCount = LocalDBConnection.DBquery("SELECT COUNT(*) AS LOG_COUNT FROM logs");
            ArrayList<HashMap> allLogs = LocalDBConnection.DBquery("SELECT * FROM logs WHERE sync=0");

            // We gave a name for the count in the SQL Request, so now we can
            // use it to get the value returned from the SELECT
            System.out.println("TOTAL LOG_COUNT: " + LocalDBConnection.getValueFromKey(logCount, "LOG_COUNT"));

            System.out.printf("%5s %-30s %s\n", "ID", "DATE", "MESSAGES");
            for (HashMap row : allLogs) {
                // The keyToFind SHOULD BE in UPPERCASE
                String ID = (String) LocalDBConnection.getValueFromKey(row, "ID");
                String id_user = (String) LocalDBConnection.getValueFromKey(row, "ID_USER");
                // Let's try with an Object to test
                Object id_site = LocalDBConnection.getValueFromKey(row, "ID_SITE");
                String date = (String) LocalDBConnection.getValueFromKey(row, "DATE");
                // Let's try with an Object to test again
                Object message = LocalDBConnection.getValueFromKey(row, "MESSAGE");
                String sync = (String) LocalDBConnection.getValueFromKey(row, "SYNC");

                System.out.printf("%5s %-30s %s\n", ID, date, message);
            }
        } catch (SQLException e) {
            Log.dd("Connection error to table 'logs'", e);
        }

        // Test for Distant Database
        remoteDB.setLogs();
        remoteDB.getLogs();
    }
}
