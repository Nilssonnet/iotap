package bluetooth.exjobb.com.findwifibt;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;

/**
 * Created by Sebastian Olsson on 15-04-12.
 * Modified by Mattias Nilsson
 */
public class HashMethods {


    public static String hashMethodMD5 (String mac) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        md.update(mac.getBytes(), 0, mac.length());
        String md5 = new BigInteger(1, md.digest()).toString(16);
        while (md5.length() < 32) {
            md5 = "0" + md5;
        }
        return md5;

    }

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

    public static String hashMethodSHA_512 (String mac) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-512");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        md.update(mac.getBytes(), 0, mac.length());
        String md5 = new BigInteger(1, md.digest()).toString(16);
        while (md5.length() < 32) {
            md5 = "0" + md5;
        }
        return md5;
    }

    public static String currentHour(){
        Calendar rightNow = Calendar.getInstance();
        return  String.valueOf(rightNow.get(Calendar.YEAR)) +
                String.valueOf(rightNow.get(Calendar.MONTH)) +
                String.valueOf(rightNow.get(Calendar.DATE)) +
                String.valueOf(rightNow.get(Calendar.HOUR_OF_DAY));
    }

    public static String currentSecond(){
        Calendar rightNow = Calendar.getInstance();
        return  String.valueOf(rightNow.get(Calendar.YEAR)) +
                String.valueOf(rightNow.get(Calendar.MONTH)) +
                String.valueOf(rightNow.get(Calendar.DATE)) +
                String.valueOf(rightNow.get(Calendar.HOUR_OF_DAY)) +
                String.valueOf(rightNow.get(Calendar.MINUTE)) +
                String.valueOf(rightNow.get(Calendar.SECOND));
    }

}
