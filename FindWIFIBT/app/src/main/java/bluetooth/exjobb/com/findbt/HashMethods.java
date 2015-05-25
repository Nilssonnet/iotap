package bluetooth.exjobb.com.findbt;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Contains methods to hash a MAC-address and returns the current time.
 * Created by Mattias Nilsson & Sebastian Olsson
 */
public class HashMethods {
    /*
     * Hashes a string to SHA-1.
     */
    public static String hashMethodSHA_1 (String mac) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        md.update(mac.getBytes(), 0, mac.length());
        String sha_1 = new BigInteger(1, md.digest()).toString(16);
        while (sha_1.length() < 32) {
            sha_1 = "0" + sha_1;
        }
        return sha_1;
    }

    /*
     * Return current time (hour of day) as well as the date.
     */
    public static String currentHour(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHH");
        Date curentTime = new Date();
        return dateFormat.format(curentTime);
    }

    /*
    * Return current time (hour and minute) as well as the date.
    */
    public static String currentMinute(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmm");
        Date curentTime = new Date();
        return dateFormat.format(curentTime);
    }
}
