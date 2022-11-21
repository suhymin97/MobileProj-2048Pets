package hohainguyen.game2048;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
private GridView gdvGamePlay;
private NumAdapter adapter;
private View.OnTouchListener listener;
private  float X,Y;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        map();//Ánh xạ
        start();
        setData();
    }
    private void map(){
        gdvGamePlay= (GridView)findViewById(R.id.gdvGamePlay);
    }

    private  void start(){
        Data.getData().intt(MainActivity.this);
        adapter = new NumAdapter(MainActivity.this,0,Data.getData().getArr());
        listener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()){
                    case  MotionEvent.ACTION_DOWN:
                        X=motionEvent.getX();
                        Y=motionEvent.getY();
                        break;
                    case MotionEvent.ACTION_UP:
                        if(Math.abs(motionEvent.getX()-X)>Math.abs(motionEvent.getY()-Y)) {
                            if(motionEvent.getX()<X) {// swipe left
                                Data.getData().onSwipeLeft();
                                adapter.notifyDataSetChanged();
                            }
                            else{
                                Data.getData().onSwipeRight();
                                adapter.notifyDataSetChanged();
                            }

                        }
                        else{
                            if(motionEvent.getY()>Y) {
                                //Toast.makeText(MainActivity.this, "Vuot xuong", Toast.LENGTH_SHORT).show();
                                Data.getData().onSwipeDown();
                                adapter.notifyDataSetChanged();
                            }
                            else {
                                //Toast.makeText(MainActivity.this, "Vuot len", Toast.LENGTH_SHORT).show();
                                Data.getData().onSwipeUp();
                                adapter.notifyDataSetChanged();
                            }
                        }
                }
                return true;
            }
        };
    }

    private  void setData(){
        gdvGamePlay.setAdapter(adapter);
        gdvGamePlay.setOnTouchListener(listener);
    }
}
