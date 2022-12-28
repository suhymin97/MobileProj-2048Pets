package dsa.hcmiu.a2048pets;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import dsa.hcmiu.a2048pets.profile_shop.FragmentProfile;
import dsa.hcmiu.a2048pets.profile_shop.FragmentShopping;
import dsa.hcmiu.a2048pets.profile_shop.SendData;

import static dsa.hcmiu.a2048pets.entities.model.Features.mySong;
import static dsa.hcmiu.a2048pets.entities.model.Features.sound;

import androidx.fragment.app.FragmentActivity;

public class ProfileActivity extends FragmentActivity implements SendData{

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
    }

    @Override
    public void data(boolean update) {
        if (update) {
            fragmentProfile.update();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,@Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onResume() {
        fragmentProfile.updateDataUser();
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

}
