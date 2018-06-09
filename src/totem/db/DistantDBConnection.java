package totem.db;

import armdb.*;
import totem.debug.Log;
import totem.settings.Config;

import java.io.IOException;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.codec.binary.Base64;

/**
 * Distant Database Connection Management
 *
 * armdb: https://github.com/rohit7209/AccessRemoteMySQLDB
 */
public class DistantDBConnection {
    private ConnectHost remoteDBCnx;
    private static SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * Constructor
     */
    private DistantDBConnection() {
        // To protect info, check with:
        // https://www.codeproject.com/Tips/1087423/Simplest-way-to-avoid-hardcoding-of-the-confidenti
        // --> https://github.com/rohit7209/EntityManager-API

        String fileURL = Config.rDbURL + "/" + Config.rDbPHPFile;
        String host = Config.rDbHostname;     // server host name
        String user = Config.rDbUsername;     // username
        String pass = Config.rDbUserPassword; // password
        String DBName = Config.rDbName;       // database name

        // Connect to the remote host with the info to connect to the database
        // on the server
        //if (this.isURLReachable(Config.rDbURL)) {
        if (this.isURLReachable(fileURL)) {
            try {
                this.remoteDBCnx = new ConnectHost(fileURL, host, user, pass, DBName);
            } catch (Exception e) {
                Log.dd("Cannot connect to remote server " + Config.rDbURL, e);
            }
        } else {
            if (this.isIpReachable(Config.rDbURL)) {
                Log.dd("Cannot access to \"" + Config.rDbPHPFile + "\" on the remote server " + Config.rDbURL);
            } else {
                Log.dd("Cannot access to remote server " + Config.rDbURL + ", check the config file or the network !");
            }
        }
    }

    /**
     * Check if an URL is reachable
     *
     * @param url String
     * @return boolean
     */
    private boolean isURLReachable(String url) {
        boolean result = false;

        Log.d("Checking URL " + url);

        try {
            URL siteURL = new URL(url);

            HttpURLConnection connection = (HttpURLConnection) siteURL.openConnection();

            connection.setRequestMethod("GET");
            connection.setConnectTimeout(2000);

            Log.d("Connecting...");
            connection.connect();

            if (connection.getResponseCode() == 200) {
                result = true;
            }
        } catch (Exception e) {
            result = false;
        }

        return result;
    }

    /**
     * Check if an IP is reachable
     *
     * @param target String
     * @return boolean
     */
    private boolean isIpReachable(String target) {
        boolean result;

        //String targetUrl = targetIp.replaceFirst("^(http[s]?://www\\.|http[s]?://|www\\.)", "");
//        String targetUrl = target.replaceFirst("^(http[s]?:\\/.)", "");
        String targetUrl = target.replaceFirst("^(http[s]?://)", "");

        Log.d("Checking access to " + targetUrl);

        try {
            InetAddress targetAddress = InetAddress.getByName(targetUrl);
            result = targetAddress.isReachable(3000);  //timeout 3sec
        } catch (IOException exception) {
            //Log.dd(targetIp + " is not reachable", exception);
            result = false;
        }
        return result;
    }

    /**
     * Init to access to remote server
     *
     * @return DistantDBConnection
     */
    public static DistantDBConnection init() {
        return new DistantDBConnection();
    }

    /**
     * Test to save data to the remote database
     */
    public void setLogs() {
        // Execute an update statement and print the number of rows affected
        SQLUpdate update = new SQLUpdate(this.remoteDBCnx);
        try {
            int rows = update.statement("INSERT INTO dnr_logs (id_user, id_site, date, message, id_borne, version) VALUES " +
                    "('admin', '" + Config.siteId + "','" +
                    formatter.format(new Date()) +
                    "','Test Envoi Message local vers remote', '" + Config.appName + "', '" + Config.version + "')");
            /*
            Log.d(rows + " no. of rows affected");
            */
        } catch (SQLUpdateException e) {
            Log.dd("INSERT INTO DNR_LOGS", e);
        }
    }

    /**
     * Test to get data from our previous saving
     */
    public void getLogs() {
        // Query and print the set of retrieved data
        SQLQuery query = new SQLQuery(this.remoteDBCnx);
        try {
            // Do our SELECT
            QueryResult qr = query.statement("select * from dnr_logs");

            // And display all rows
            while (qr.nextFlag()) {
                System.out.print(qr.getValue("id_user") + ", ");
                System.out.print(qr.getValue("id_site") + ", ");
                System.out.print(qr.getValue("date") + ", ");
                System.out.print(qr.getValue("message") + ", ");
                System.out.print(qr.getValue("id_borne") + ", ");
                System.out.println(qr.getValue("version"));
            }
        } catch (SQLQueryException e) {
            Log.dd("SELECT * FROM DNR_LOGS", e);
        }
    }
}
