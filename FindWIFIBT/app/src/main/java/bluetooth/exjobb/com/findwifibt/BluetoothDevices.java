package bluetooth.exjobb.com.findwifibt;

/**
 * Created by Mattias on 2015-04-30.
 */
public class BluetoothDevices {

    public static String DeviceClass(int input){
        switch (input){
            //Values below come from
            //developer.android.com/reference/android/bluetooth/BluetoothClass.Device.htm
            case 1076:
                return "AUDIO_VIDEO_CAMCORDER";
            case 1056:
                return "AUDIO_VIDEO_CAR_AUDIO";
            case 1032:
                return "AUDIO_VIDEO_HANDSFREE";
            case 1048:
                return "AUDIO_VIDEO_HEADPHONES";
            case 1064:
                return "AUDIO_VIDEO_HIFI_AUDIO";
            case 1044:
                return "AUDIO_VIDEO_LOUDSPEAKER";
            case 1040:
                return "AUDIO_VIDEO_MICROPHONE";
            case 1052:
                return "AUDIO_VIDEO_PORTABLE_AUDIO";
            case 1060:
                return "AUDIO_VIDEO_SET_TOP_BOX";
            case 1024:
                return "AUDIO_VIDEO_UNCATEGORIZED";
            case 1068:
                return "AUDIO_VIDEO_VCR";
            case 1072:
                return "AUDIO_VIDEO_VIDEO_CAMERA";
            case 1088:
                return "AUDIO_VIDEO_VIDEO_CONFERENCING";
            case 1084:
                return "AUDIO_VIDEO_VIDEO_DISPLAY_AND_LOUDSPEAKER";
            case 1096:
                return "AUDIO_VIDEO_VIDEO_GAMING_TOY";
            case 1080:
                return "AUDIO_VIDEO_VIDEO_MONITOR";
            case 1028:
                return "AUDIO_VIDEO_WEARABLE_HEADSET";
            case 260:
                return "COMPUTER_DESKTOP";
            case 272:
                return "COMPUTER_HANDHELD_PC_PDA";
            case 268:
                return "COMPUTER_LAPTOP";
            case 276:
                return "COMPUTER_PALM_SIZE_PC_PDA";
            case 264:
                return "COMPUTER_SERVER";
            case 256:
                return "COMPUTER_UNCATEGORIZED";
            case 280:
                return "COMPUTER_WEARABLE";
            case 2308:
                return "HEALTH_BLOOD_PRESSURE";
            case 2332:
                return "HEALTH_DATA_DISPLAY";
            case 2320:
                return "HEALTH_GLUCOSE";
            case 2324:
                return "HEALTH_PULSE_OXIMETER";
            case 2328:
                return "HEALTH_PULSE_RATE";
            case 2312:
                return "HEALTH_THERMOMETER";
            case 2304:
                return "HEALTH_UNCATEGORIZED";
            case 2316:
                return "HEALTH_WEIGHING";
            case 516:
                return "PHONE_CELLULAR";
            case 520:
                return "PHONE_CORDLESS";
            case 532:
                return "PHONE_ISDN";
            case 528:
                return "PHONE_MODEM_OR_GATEWAY";
            case 524:
                return "PHONE_SMART";
            case 512:
                return "PHONE_UNCATEGORIZED";
            case 2064:
                return "TOY_CONTROLLER";
            case 2060:
                return "TOY_DOLL_ACTION_FIGURE";
            case 2068:
                return "TOY_GAME";
            case 2052:
                return "TOY_ROBOT";
            case 2048:
                return "TOY_UNCATEGORIZED";
            case 2056:
                return "TOY_VEHICLE";
            case 1812:
                return "WEARABLE_GLASSES";
            case 1808:
                return "WEARABLE_HELMET";
            case 1804:
                return "WEARABLE_JACKET";
            case 1800:
                return "WEARABLE_PAGER";
            case 1792:
                return "WEARABLE_UNCATEGORIZED";
            case 1796:
                return "WEARABLE_WRIST_WATCH";
            //Values below comes from
            //developer.android.com/reference/android/bluetooth/BluetoothClass.Device.Major.html
            case 1536:
                return "IMAGING";
            case 0:
                return "MISC";
            case 768:
                return "NETWORKING";
            case 1280:
                return "PERIPHERAL";
            case 7936:
                return "UNCATEGORIZED";
            default:
                return null;

        }
    }

}
