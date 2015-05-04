package bluetooth.exjobb.com.findwifibt;

/**
 * Created by Sebastian Olsson on 15-04-12.
 */
public class Devices {
    public String macAddress;
    public String type;
    public String hashFull;
    public String hashSemi;
    public String hashNo;

    public Devices(String macAddress, String type, String hashFull, String hashSemi, String hashNo){
        this.macAddress=macAddress;
        this.type=type;
        this.hashFull=hashFull;
        this.hashSemi=hashSemi;
        this.hashNo=hashNo;
    }

}