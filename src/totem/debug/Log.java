package totem.debug;

import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Log Class to show error messages
 */
public class Log {
    private static SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static String timer = "[" + formatter.format(new Date()) + "]";
    private static String prefixMessage = timer + " >>> ";
    private static String prefixErrorMessage = prefixMessage + "ERROR: ";

    // Method Overloading
    // dd: Die and Dump
    public static void dd(String message, Exception exception, int status) {
        System.out.println(prefixErrorMessage + message);
        System.out.println(prefixErrorMessage + exception.getMessage());
        System.exit(status);
    }

    public static void dd(String message, Exception exception) {
        System.out.println(prefixErrorMessage + message);
        System.out.println(prefixErrorMessage + exception.getMessage());
        System.exit(-1);
    }

    public static void dd(String message) {
        System.out.println(prefixErrorMessage + message);
        System.exit(-1);
    }

    // d: Dump
    public static void d(String message, Exception exception) {
        System.out.println(prefixMessage + message);
        System.out.println(prefixErrorMessage + exception.getMessage());
    }

    public static void d(String message) {
        System.out.println(prefixMessage + message);
    }
}
