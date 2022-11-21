package dsa.hcmiu.a2048pets.entities.handle;

import android.content.Context;
import android.media.MediaPlayer;

import java.util.Timer;
import java.util.TimerTask;

import dsa.hcmiu.a2048pets.MenuActivity;
import dsa.hcmiu.a2048pets.R;

public class HandleSound {

    private MediaPlayer song;
    private Context context;

    private float volume_fadein = 0; //The volume will increase from 0 to 1
    private float volume_fadeout = 1;

    final int FADE_DURATION = 3000; //The duration of the fade
    //The amount of time between volume changes. The smaller this is, the smoother the fade
    final int FADE_INTERVAL = 100;
    final int MAX_VOLUME = 1;
    int numberOfSteps = FADE_DURATION/FADE_INTERVAL; //Calculate the number of fade steps
    //Calculate by how much the volume changes each step
    final float deltaVolume = MAX_VOLUME / (float)numberOfSteps;

    public HandleSound(Context context, int music) {
        song = MediaPlayer.create(context, music);
    }

    public MediaPlayer getSong() {
        return song;
    }

    public void startFadeIn(){
        start();
        //Create a new Timer and Timer task to run the fading outside the main UI thread
        final Timer timer = new Timer(true);
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                fadeInStep(deltaVolume); //Do a fade step
                //Cancel and Purge the Timer if the desired volume has been reached
                if(volume_fadein>=1f){
                    timer.cancel();
                    timer.purge();
                }
            }
        };
        timer.schedule(timerTask,FADE_INTERVAL,FADE_INTERVAL);
        volume_fadein = 0;
    }

    private void fadeInStep(float deltaVolume){
        song.setVolume(volume_fadein, volume_fadein);
        volume_fadein += deltaVolume;
    }

    public void startFadeOut(){
        //Create a new Timer and Timer task to run the fading outside the main UI thread
        final Timer timer = new Timer(true);
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                fadeOutStep(deltaVolume); //Do a fade step
                //Cancel and Purge the Timer if the desired volume has been reached
                if(volume_fadeout<=0f){
                    timer.cancel();
                    timer.purge();
                    pause();
                }
            }
        };
        timer.schedule(timerTask,FADE_INTERVAL,FADE_INTERVAL);
        volume_fadeout = 1;
    }

    private void fadeOutStep(float deltaVolume){
        song.setVolume(volume_fadeout, volume_fadeout);
        volume_fadeout -= deltaVolume;
    }

    public void start() {
        song.start();
    }

    public void seekTo(int msec) {
        song.seekTo(msec);
    }

    public void pause() {
        song. pause();
    }
}
