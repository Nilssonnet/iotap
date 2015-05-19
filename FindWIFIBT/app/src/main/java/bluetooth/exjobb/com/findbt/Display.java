package bluetooth.exjobb.com.findbt;

/**
 * Created by Mattias on 2015-04-30.
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
