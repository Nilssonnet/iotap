package bluetooth.exjobb.com.findbt;

/**
 * Stores data about a Bluetooth device (data from database).
 * Created by Mattias Nilsson & Sebastian Olsson
 */
public class Display {
    public String timeStamp;
    public String hashedMAC;
    public String deviceClass;
    public int RSSI;

    public Display(String timeStamp, String hashedMAC, String deviceClass, int RSSI){
        this.timeStamp = timeStamp;
        this.hashedMAC = hashedMAC;
        this.deviceClass = deviceClass;
        this.RSSI = RSSI;
    }

}
