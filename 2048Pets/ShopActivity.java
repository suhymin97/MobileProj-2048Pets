package dsa.hcmiu.a2048pets.entities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import dsa.hcmiu.a2048pets.R;

public class ShopActivity extends AppCompatActivity {

    RelativeLayout shop1, shop2;

    ImageButton goHome;
    TextView tv_score;

    int shop1,shop2;
    int score,action;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);

        shop1=(RelativeLayout)findViewById(R.id.shop1);
        shop2=(RelativeLayout)findViewById(R.id.shop2);

        goHome=(ImageButton)findViewById(R.id.home);
        tv_score=(TextView)findViewById(R.id.score);

        final SharedPreferences setting = getSharedPreferences(  "PREFS", Context.MODE_PRIVATE);
        score=setting.getInt("Scores",0);
        action=setting.getInt("Action",1);
        shop1=setting.getInt("Undo",0);
        shop2=setting.getInt("Key",0);

        tv_score.setText(""+score);

        shop1.setonClickListener(new View.OnClickListener()) {
            @Override
                    public void onClick(View view){
                action=1;
                SharedPreferences.Editor editor=setting.edit();
                editor.putInt("Action",action);
                editor.commit();
                startActivity(new Intent(ShopActivity.this,StartActivity.class));
            }
            else if(score>=500){
                shop1=true;
                action=1;
                score-=500;

                tv_score.setText(""+score);

                SharedPreferences.Editor editor=settings.edit();
                editor.putInt("Action",action);
                editor.putInt("Scores",score);
                editor.putInt("Undo",shop1);
                editor.commit();
                startActivity(new Intent(ShopActivity.this,StartActivity.class));
            }
            else {
                Toast.makeText(ShopActivity.this,"not enough Scores",Toast.LENGTH_SHORT).show();
            }
        }}
        shop2.setonClickListener(new View.OnClickListener()) {
            @Override
                    public void onClick(View view){
            if (shop2 == true) {
                action=2;

                SharedPreferences.Editor editor=settings.edit();
                editor.putInt("Action",action);
                editor.commit();
                startActivity(new Intent(ShopActivity.this,StartActivity.class));
            }
            else if(score>=1000){
                shop2=true;
                action=2;
                score-=1000;

                tv_score.setText(""+score);

                SharedPreferences.Editor editor=settings.edit();
                editor.putInt("Action",action);
                editor.putInt("Scores",score);
                editor.putInt("Key",shop2);
                editor.commit();
                startActivity(new Intent(ShopActivity.this,StartActivity.class));
            }
            else {
                Toast.makeText(ShopActivity.this,"not enough Scores",Toast.LENGTH_SHORT).show();
            }
        }
        goHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ShopActivity.this,StartActivity.class));
                finish();
            }
        });
    }

}
