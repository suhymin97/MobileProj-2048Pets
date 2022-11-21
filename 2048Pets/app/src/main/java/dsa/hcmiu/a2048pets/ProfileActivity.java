package dsa.hcmiu.a2048pets;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import dsa.hcmiu.a2048pets.profile_shop.FragmentProfile;
import dsa.hcmiu.a2048pets.profile_shop.SendData;

import static dsa.hcmiu.a2048pets.entities.model.Features.mySong;
import static dsa.hcmiu.a2048pets.entities.model.Features.sound;
import static dsa.hcmiu.a2048pets.entities.model.Features.user;

public class ProfileActivity extends Activity implements SendData {

    FragmentProfile fragmentProfile;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (sound) mySong.start();
        setContentView(R.layout.activity_profile);
        fragmentProfile = (FragmentProfile) getFragmentManager().
                findFragmentById(R.id.fragmentProfile);
    }

    @Override
    public void data(boolean update) {
        if (update) fragmentProfile.update();
    }

    @Override
    protected void onResume() {
        super.onResume();
        fragmentProfile.updateDataUser();
    }
}
