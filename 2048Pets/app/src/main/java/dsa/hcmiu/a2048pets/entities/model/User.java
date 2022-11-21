package dsa.hcmiu.a2048pets.entities.model;
import android.util.Log;

import java.util.ArrayList;

import dsa.hcmiu.a2048pets.R;
import dsa.hcmiu.a2048pets.MyApplication;


public class User {
    public long totalGold = 4000;
    private long UID = 0;
    private String name = null;
    private String email = null;
    private String IDFacebook = null;
    private String profilePic;
    private boolean loggedFb;
    private int avatar;
    public String UserHighScore = "Bot";
    public long highScore = 0;
    public int undo = 5;
    public int hammer = 5;
    public ArrayList<Integer> purchasedIdItem;

    public User() {
        Log.d("Add user", String.valueOf(UID));
        name = MyApplication.getContext().getResources().getString(R.string.default_name);
        UID = ++Features.uidCount;
        loggedFb = false;
        setAvatar(R.drawable.default_ava);
        avatar = R.drawable.default_ava;
        purchasedIdItem = new ArrayList<>();
        purchasedIdItem.add(200);
        purchasedIdItem.add(300);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIDFacebook() {
        return IDFacebook;
    }

    public void setIDFacebook(String IDFacebook) {
        this.IDFacebook = IDFacebook;
    }

    public boolean isLoggedFb() {
        return loggedFb;
    }

    public void setLoggedFb(boolean loggedFb) {
        this.loggedFb = loggedFb;
    }

    public int getAvatar() {
        return avatar;
    }

    public void setAvatar(int resid) {
        this.avatar = resid;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public void unPurcahsedIdItem(int id) {
        for (int i=0; i< purchasedIdItem.size();i++) {
            if (purchasedIdItem.get(i) != id ) continue;
            purchasedIdItem.remove(i);
            break;
        }
    }
    public void returnDef() {
        name = MyApplication.getContext().getResources().getString(R.string.default_name);
        IDFacebook = null;
        avatar = R.drawable.default_ava;
        loggedFb =false;
    }
}
