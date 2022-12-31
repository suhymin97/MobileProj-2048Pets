package dsa.hcmiu.a2048pets.entities.model;

import android.media.MediaPlayer;
import android.widget.ImageView;

import com.facebook.CallbackManager;

public class Features { //store biến static
    public static long uidCount = 0;
    public static MediaPlayer mySong;
    public static boolean sound = true;
    public static User user;
    public static ShopItem shopItem;
    public static final String GAME_SERVER_URL = "https://competitive-2048.suhymin97.repl.co/";
    public static final String GAME_SERVER_IP = "http://192.168.18.20:3000";
}
