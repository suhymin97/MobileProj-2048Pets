package hohainguyen.game2048;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by User on 3/26/2018.
 */

public class Square extends android.support.v7.widget.AppCompatTextView {
    public Square(Context context) {
        super(context);
    }

    public Square(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public Square(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int side = getMeasuredWidth();
        setMeasuredDimension(side,side);
    }

    public void setTett(int num){
        if(num<100) {
            setTextSize(40);
        } else if(num<1000) {
            setTextSize(35);
        } else
            setTextSize(30);

        if(num>=8) {
            setTextColor(Color.WHITE);
        } else
            setTextColor(Color.BLACK);

        GradientDrawable drawable = (GradientDrawable)this.getBackground();
        drawable.setColor(Data.getData().colorr(num));
        setBackground(drawable);
        if(num==0){
            setText(" ");
        }else{
            setText(" "+num);
        }


    }
}
