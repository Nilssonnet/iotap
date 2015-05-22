package bluetooth.exjobb.com.findbt;

/**
 * Created by Sebastian Olsson on 15-04-12.
 */
public class Devices {
    public String macAddress;
    public String type;
    public String hashFull;
    public String hashSemi;
    public String hashNo;
    public int RSSI;
    public String BTclass;

    public Devices(String type, String macAddress, String hashFull, String hashSemi, String hashNo,
                   int RSSI, String BTclass){
        this.type = type;
        this.macAddress = macAddress;
        this.hashFull = hashFull;
        this.hashSemi = hashSemi;
        this.hashNo = hashNo;
        this.RSSI = RSSI;
        this.BTclass = BTclass;
    }
}