package bluetooth.exjobb.com.findwifibt;

/**
 * Created by Sebastian olsson on 15-04-12.
 */
public class Devices {
    public String macAddress;
    public String type;
    public String hash;

    public Devices(String macAddress, String type, String hash){
        this.macAddress=macAddress;
        this.type=type;
        this.hash=hash;

    }

}
