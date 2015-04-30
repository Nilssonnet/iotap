package bluetooth.exjobb.com.findwifibt;

/**
 * Created by Mattias on 2015-04-30.
 */
public class Display {
    public String timeStamp;
    public String hashedMAC;
    public String deviceClass;

    public Display(String timeStamp, String hashedMAC, String deviceClass){
        this.timeStamp=timeStamp;
        this.hashedMAC=hashedMAC;
        this.deviceClass=deviceClass;
    }

}
