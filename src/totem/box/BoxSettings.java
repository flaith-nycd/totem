package totem.box;

import java.util.Hashtable;


/**
 * BoxSettings Class
 * Handle the Case ID and the RFID for each case
 */
public class BoxSettings {
    public String idCase;
    public String iRFID;

    public static Hashtable<String, BoxSettings> instances = new Hashtable<>();

    /**
     * Constructor got the same class name
     *
     * @param idCase String
     * @param iRFID  String
     */
    public BoxSettings(String idCase, String iRFID) {
        // The current idCase is not saved yet
        if (!instances.containsKey(idCase)) {
            instances.put(idCase, this);
        }

        // Save the values in the current instance
        this.idCase = idCase;
        this.iRFID = iRFID;
    }

    /**
     * Method to get the RFID from the idCase
     *
     * @param idCase String
     * @return String
     */
    public String GetRFIDfromIdCase(String idCase) {
        BoxSettings module = BoxSettings.instances.get(idCase);
        if (instances.containsKey(idCase)) {
            return module.iRFID;
        }
        // System.out.println("RFID for case ID " + idCase + ": " + module.iRFID);
        return null;
    }
}
