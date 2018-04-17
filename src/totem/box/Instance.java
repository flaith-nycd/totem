package totem.box;

import java.util.Hashtable;

public class Instance {
    public String idCase;
    public String iRFID;

    public static Hashtable<String, Instance> instances = new Hashtable<>();

    // Constructor
    public Instance(String idCase, String iRFID) {
        if (!instances.containsKey(idCase)) {
            instances.put(idCase, this);
        }
        this.idCase = idCase;
        this.iRFID = iRFID;
    }

    // Test method
    public void ShowRFIDfromIdCase(String idCase) {
        Instance module = Instance.instances.get(idCase);
        //System.out.println("idCase: " + idCase + " - RFID: " + module.iRFID);
        System.out.println("RFID for case ID " + idCase + ": " + module.iRFID);
        /*
        for (String ident : Instance.instances.keySet()) {
            Instance module = Instance.instances.get(ident);
            //System.out.println(ident);
            //System.out.println(module.toString());
            System.out.println("idCase: " + idCase + " - RFID: " + module.iRFID);
        }
        */
    }
}
