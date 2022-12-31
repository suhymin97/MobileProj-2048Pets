package dsa.hcmiu.a2048pets;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import dsa.hcmiu.a2048pets.entities.handle.HandleFile;
import dsa.hcmiu.a2048pets.entities.model.Features;
import dsa.hcmiu.a2048pets.entities.model.User;

public class WelcomeActivity extends Activity {

    private  int timeDelay = 1000, status = 0, stepDelay = 10;
    private ProgressBar progressBar;
    Handler setDelay;
    Runnable startDelay;
    TextView tvTouchMess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        tvTouchMess = (TextView) findViewById(R.id.tvTouchMess);
        setDelay = new Handler();
        status+= initData();
        tvTouchMess.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        progressBar.setProgress(status);
        init();
    }

    public void init() {
        startDelay = new Runnable() {
            @Override
            public void run() {
                status+=stepDelay;
                progressBar.setProgress(status);
                if (status!=100) setDelay.postDelayed(this,timeDelay);
                else interactive();
            }
        };
        startDelay.run();
    }

    private void interactive() {
        setDelay.removeCallbacks(startDelay);
        progressBar.setVisibility(View.GONE);
        tvTouchMess.setVisibility(View.VISIBLE);
        RelativeLayout main = (RelativeLayout) findViewById(R.id.mainWelcome);
        main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iMenu = new Intent(WelcomeActivity.this, MenuActivity.class);
                startActivity(iMenu);
            }
        });
    }

    public int initData() {
        Features.user = new User();
        Features.usersList =  new HashMap<>();
        try {
            HandleFile.get().readFeaturesJSONFile();
            Log.d("Welcome", "onCreate: ReadJson");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return 50;
    }
}
