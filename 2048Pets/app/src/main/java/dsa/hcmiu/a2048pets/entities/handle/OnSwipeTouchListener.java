package dsa.hcmiu.a2048pets.entities.handle;

import android.content.Context;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import dsa.hcmiu.a2048pets.PlayActivity;

/**
 * Created by Admin on 3/31/2018.
 */

public class OnSwipeTouchListener implements View.OnTouchListener {
    private float x1,x2,y1,y2;
    private Context context;

    public OnSwipeTouchListener(Context context) {
        this.context = context;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: //nhấn vô
               x1 = event.getX();
               y1 = event.getY();
               break;
            case MotionEvent.ACTION_UP: //thả ra
                float x2 = event.getX();
                float y2 = event.getY();
                float diffX = Math.abs(x2-x1);
                float diffY = Math.abs(y2-y1);
                Log.d("Swipe","diff X / Y " + String.valueOf(diffX) +" / " + diffY);
                if (diffX < 20 && diffY < 20) return true;
                if (diffX > diffY) { //horizontal
                    if (x2 > x1) onSwipeRight();
                    else onSwipeLeft();
                }
                else {
                    if (y2 > y1) onSwipeDown();
                    else onSwipeUp();
                }
                break;
        }
        return true;
    }

    public void onSwipeRight() {
        //make text qua phải
    }

    public void onSwipeLeft() {

    }

    public void onSwipeUp() {
    }

    public void onSwipeDown() {
    }
}

