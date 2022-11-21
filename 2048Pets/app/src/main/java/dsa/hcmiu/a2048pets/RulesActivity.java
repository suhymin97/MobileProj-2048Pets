package dsa.hcmiu.a2048pets;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;

public class RulesActivity extends Activity {

    private ImageView im;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rules);

        im = (ImageView) findViewById(R.id.imRule);

        im.setImageResource(R.drawable.rules);
    }
}
