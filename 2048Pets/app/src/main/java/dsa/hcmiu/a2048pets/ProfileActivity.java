package dsa.hcmiu.a2048pets;

import android.os.Bundle;
import android.support.annotation.Nullable;

import dsa.hcmiu.a2048pets.profile_shop.FragmentProfile;
import dsa.hcmiu.a2048pets.profile_shop.FragmentShopping;
import dsa.hcmiu.a2048pets.profile_shop.SendData;

import static dsa.hcmiu.a2048pets.entities.model.Features.mySong;
import static dsa.hcmiu.a2048pets.entities.model.Features.sound;

import androidx.fragment.app.FragmentActivity;

public class ProfileActivity extends FragmentActivity implements SendData {

    FragmentProfile fragmentProfile = new FragmentProfile();
    FragmentShopping fragmentShopping = new FragmentShopping();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (sound) mySong.start();
        setContentView(R.layout.activity_profile);
        getSupportFragmentManager().beginTransaction().
                replace(R.id.fragmentProfileLand,fragmentProfile).
                replace(R.id.fragmentShop,fragmentShopping).
                commit();
//        fragmentProfile = (FragmentProfile) getSupportFragmentManager().
//                findFragmentById(R.id.fragmentProfile);
    }

    @Override
    public void data(boolean update) {
        if (update) {
            fragmentProfile.update();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        fragmentProfile.updateDataUser();
    }
}
