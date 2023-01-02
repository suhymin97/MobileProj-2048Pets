package dsa.hcmiu.a2048pets.entities.model;
import android.net.Uri;
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
    private boolean logged;
    private int avatar;
    public String UserHighScore = "Bot";
    public long highScore = 0;
    public int undo = 5;
    public int hammer = 5;
    public ArrayList<Integer> purchasedIdItem;
    public Uri photoUrl;
    public String socialType = null;
    private String acessTokenFb = null;

    public Uri getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(Uri photoUrl) {
        this.photoUrl = photoUrl;
    }

    public User() {
        Log.d("Add user", String.valueOf(UID));
        name = MyApplication.getContext().getResources().getString(R.string.default_name);
        UID = ++Features.uidCount;
        logged = false;
        setAvatar(R.drawable.default_ava);
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

    public boolean isLogged() {
        return logged;
    }

    public void setLogged(boolean logged) {
        this.logged = logged;
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

    public String getSocialType() {
        return socialType;
    }

    public void setSocialType(String socialType) {
        this.socialType = socialType;
    }

    public String getAcessTokenFb() {
        return acessTokenFb;
    }

    public void setAcessTokenFb(String acessTokenFb) {
        this.acessTokenFb = acessTokenFb;
    }

    public long getUID() {
        return UID;
    }

    public void setUID(long UID) {
        this.UID = UID;
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
        logged =false;
    }

    public void reset() {
        undo = 5;
        hammer = 5;
        highScore = 0;
        totalGold = 4000;
        purchasedIdItem.clear();
    }
}
