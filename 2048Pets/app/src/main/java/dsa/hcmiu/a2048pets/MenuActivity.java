package dsa.hcmiu.a2048pets;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;
import dsa.hcmiu.a2048pets.entities.handle.HandleFile;
import dsa.hcmiu.a2048pets.entities.handle.HandleImage;
import dsa.hcmiu.a2048pets.entities.handle.HandleSound;
import dsa.hcmiu.a2048pets.entities.model.Features;
import dsa.hcmiu.a2048pets.profile_shop.FragmentProfile;

import static dsa.hcmiu.a2048pets.entities.model.Features.mySong;
import static dsa.hcmiu.a2048pets.entities.model.Features.sound;
import static dsa.hcmiu.a2048pets.entities.model.Features.user;

import com.squareup.picasso.Picasso;

public class MenuActivity extends Activity implements View.OnClickListener {

    MediaPlayer myClick;
    private ImageButton btnSound,btnQuit,btnPlay, btnStore, btnRule,btnAbout;
    Animation upToDown, downToUp;
    ImageView imgFb,ivCupCat,ivShadow;
    LinearLayout layMenu;
    private TextView tvTotalScore;
    MediaPlayer snd_singleKitty;
    private Button btnProfile;
    FragmentProfile fragmentProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        btnPlay = (ImageButton) findViewById(R.id.bMenuPlay);
        btnRule = (ImageButton) findViewById(R.id.bRule);
        btnStore = (ImageButton) findViewById(R.id.bStore);
        btnQuit = (ImageButton) findViewById(R.id.bQuit);
        btnSound = (ImageButton) findViewById(R.id.btnSound);
        btnProfile = (Button) findViewById(R.id.btnProfile);
        btnAbout = (ImageButton) findViewById(R.id.btnAbout);
        imgFb = (ImageView) findViewById(R.id.ivAvaFb);
        ivCupCat = (ImageView) findViewById(R.id.ivCupCat);
        ivShadow = (ImageView) findViewById(R.id.ivCupCatShadow);
        tvTotalScore = (TextView) findViewById(R.id.tvTotalScore);

        update();
        btnRule.setOnClickListener(this);
        btnPlay.setOnClickListener(this);
        btnStore.setOnClickListener(this);
        btnQuit.setOnClickListener(this);
        btnSound.setOnClickListener(this);
        btnProfile.setOnClickListener(this);
        btnAbout.setOnClickListener(this);
        cupCatSetup();
        soundSetup();
        }

    private void cupCatSetup() {
        Animation wobbleCup = AnimationUtils.loadAnimation(this, R.anim.wobble_cup_cats);
        Animation wobbleCatUp = AnimationUtils.loadAnimation(this, R.anim.wobble_cat_up);
        Animation linear = AnimationUtils.loadAnimation(this, R.anim.linear_move);
        ivCupCat.setAnimation(wobbleCup);
        ivShadow.setAnimation(linear);
        btnSound.setAnimation(wobbleCatUp);
        final HandleSound snd_Cats = new HandleSound(this, R.raw.bunch_cat);
        final MediaPlayer snd_singleKitty = MediaPlayer.create(this, R.raw.single_kitty);
        ivCupCat.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        snd_Cats.getSong().seekTo(0);
                        snd_Cats.startFadeIn();
                        break;
                    case MotionEvent.ACTION_UP:
                        snd_Cats.startFadeOut();
                        break;
                }
                return true;
            }
        });
    }
    public void moewIT(View view){
        snd_singleKitty.start();
    }
    public void playIT(View view){
        myClick.start();
    }
    public void storeIT(View view){
        myClick.start();
    }
    public void settingIT(View view){
        myClick.start();
    }

    @Override //Activity pause
    protected void onPause() {
        super.onPause();
        Features.mySong.pause();
    }

    @Override //Activity resume
    protected void onResume() {
        super.onResume();
        if (Features.sound) Features.mySong.start();
        update();
    }

    @Override
    public void onBackPressed() {
        try {
            quit();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void quit() throws IOException {
        HandleFile.get().writeToFile();
        final Dialog MyDialog = new Dialog(MenuActivity.this,R.style.FullHeightDialog);
        LayoutInflater inflater = MenuActivity.this.getLayoutInflater();
        MyDialog.setContentView(R.layout.dialog);
        Button btnyes = (Button) MyDialog.findViewById(R.id.btnyes);
        Button btnno = (Button) MyDialog.findViewById(R.id.btnno);
        TextView tvMess = (TextView) MyDialog.findViewById(R.id.tvMessage) ;

        tvMess.setText("Do you like this game? Let's take a visit to our open source app.");
        btnyes.setText("Github");
        btnno.setText("Not now");
        //set Anim for icon github

        Animation zoomin= AnimationUtils.loadAnimation(this,R.anim.zoom_in);
        Animation zoomout = AnimationUtils.loadAnimation(this,R.anim.zoom_out);
        ImageView imgIcon = (ImageView) MyDialog.findViewById(R.id.icon_github);
        imgIcon.setAnimation(zoomin);
        imgIcon.setAnimation(zoomout);

        btnyes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/suhymin97/2048Pets")));
                System.exit(0);
            }
        });
        btnno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.exit(0);
            }
        });
        MyDialog.show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bMenuPlay:
                Intent iPlay = new Intent(this, PlayActivity.class);
                startActivity(iPlay);
                break;
            case R.id.bStore:
                Intent iProfile = new Intent(this, ProfileActivity.class);
                startActivity(iProfile);
                break;
            case R.id.bRule:
                Intent iRule = new Intent(this, RulesActivity.class);
                startActivity(iRule);
                break;
            case R.id.bQuit:
                try {
                    quit();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.btnSound:
                if (sound) mySong.pause();
                else mySong.start();
                sound= !(sound || sound);
                break;
            case R.id.btnProfile:
                dialogPro5();
                break;
            case R.id.btnAbout:
                Intent iAbout = new Intent(this, AboutActivity.class);
                startActivity(iAbout);
                break;
        }
    }
    private void soundSetup() {
        //sound
        Features.mySong= MediaPlayer.create(MenuActivity.this,R.raw.song);
        myClick= MediaPlayer.create(MenuActivity.this,R.raw.click);

        //callAnimation
        upToDown = AnimationUtils.loadAnimation(this,R.anim.uptodown);
        downToUp = AnimationUtils.loadAnimation(this,R.anim.downtoup);

        btnPlay.setAnimation(upToDown);
        btnStore.setAnimation(upToDown);
        btnRule.setAnimation(downToUp);

        mySong.setLooping(true);
        if (sound) mySong.start();
    }

    private void update() {
        tvTotalScore.setText(String.valueOf(user.totalGold));
        if (user.getAvatar() == 0) HandleImage.get().loadImageFromDisk(imgFb);
        else imgFb.setImageResource(user.getAvatar());
    }

    private void dialogPro5() {
        final Dialog MyDialog = new Dialog(MenuActivity.this,R.style.FullHeightDialog);
        LayoutInflater inflater = MenuActivity.this.getLayoutInflater();
        MyDialog.setContentView(R.layout.fragment_profile);
        TextView tvHighscore = (TextView) MyDialog.findViewById(R.id.tvAchiveHighscore);
        TextView tvUndo = (TextView) MyDialog.findViewById(R.id.tvAchiveUndo);
        TextView tvHammer = (TextView) MyDialog.findViewById(R.id.tvAchiveHammer);
        final TextView tvNick = (TextView) MyDialog.findViewById(R.id.tvNick);
        final ImageButton btnLogin = (ImageButton) MyDialog.findViewById(R.id.btnGoogle);
        ImageButton btnTwt = (ImageButton) MyDialog.findViewById(R.id.btnTwt);
        ImageButton btnFb = (ImageButton) MyDialog.findViewById(R.id.btnFb);
        final Button btnLogout = (Button) MyDialog.findViewById(R.id.btnLogout);
        CircleImageView ivAva = (CircleImageView) MyDialog.findViewById(R.id.ivAvaFb);

        tvHighscore.setText(String.valueOf(user.highScore));
        tvUndo.setText(String.valueOf(user.undo));
        tvHammer.setText(String.valueOf(user.hammer));
        tvNick.setText(user.getName());
        if (user.getAvatar() != 0) ivAva.setImageResource(user.getAvatar());
        else HandleImage.get().loadImageFromDisk(ivAva);
        btnLogout.setVisibility(View.GONE);
        btnLogin.setVisibility(View.VISIBLE);
        if (user.isLoggedFb()) {
            btnLogout.setVisibility(View.GONE);
            btnLogin.setVisibility(View.GONE);
            btnTwt.setVisibility(View.GONE);
            btnFb.setVisibility(View.GONE);
            Picasso.get().load(user.getPhotoUrl()).into(ivAva);
//            btnLogout.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    btnLogout.setVisibility(View.GONE);
//                    btnLogin.setVisibility(View.VISIBLE);
//                    btnTwt.setVisibility(View.VISIBLE);
//                    btnFb.setVisibility(View.VISIBLE);
//                    user.returnDef();
//                    updateDataUser();
//                    ivAva.setImageResource(user.getAvatar());
//                    tvNick.setText(user.getName());
//                }
//            });
        }
            btnLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent iPro5 = new Intent(getApplicationContext(), ProfileActivity.class);
                    iPro5.putExtra("Facebook", "Log");
                    startActivity(iPro5);
                }
            });
        MyDialog.show();
    }
    void updateDataUser(){
        if(!user.isLoggedFb()){
            user.returnDef();
            user.setAvatar(R.drawable.default_ava);
        }
    }
}
