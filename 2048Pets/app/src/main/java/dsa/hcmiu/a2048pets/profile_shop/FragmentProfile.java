package dsa.hcmiu.a2048pets.profile_shop;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.GraphResponse;
import com.facebook.ProfileTracker;

import org.json.JSONException;
import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;
import dsa.hcmiu.a2048pets.R;
import dsa.hcmiu.a2048pets.entities.handle.FbConnectHelper;
import dsa.hcmiu.a2048pets.entities.handle.HandleImage;
import dsa.hcmiu.a2048pets.entities.model.Features;
import dsa.hcmiu.a2048pets.entities.model.User;

import static dsa.hcmiu.a2048pets.entities.model.Features.user;

public class FragmentProfile extends Fragment implements FbConnectHelper.OnFbSignInListener{

    TextView tvHighscore, tvUndo, tvHammer;
    CircleImageView ivAva;
    private Button btnlogin,btnlogout;
    private TextView tvNick;
    private ProfileTracker mProfileTracker;
    private FbConnectHelper fbConnectHelper;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile,container,false);
        tvHighscore = (TextView) view.findViewById(R.id.tvAchiveHighscore);
        tvUndo = (TextView) view.findViewById(R.id.tvAchiveUndo);
        tvHammer = (TextView) view.findViewById(R.id.tvAchiveHammer);
        ivAva = (CircleImageView) view.findViewById(R.id.ivAvaFb);
        update();
        btnlogin = (Button) view.findViewById(R.id.btnLogin);
        btnlogout = (Button) view.findViewById(R.id.btnLogout);
        tvNick = (TextView) view.findViewById(R.id.tvNick);
        fbConnectHelper = new FbConnectHelper(this,this);
        updateDataUser();

        Intent intent = getActivity().getIntent();
        if (intent.getStringExtra("Facebook") == "Log") loginwithFacebook();
        updateDataUser();
        update();

        return view;
    }

    private void loggedFb() {
        btnlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user.returnDef();
                updateDataUser();

            }
        });
    }

    private void unlogin() {
        //VISIBLE = Hiện; INVISIBLE = Tàng hình; GONE = Mất tích
        //btnlogin.setReadPermissions(Arrays.asList("public_profile", "email"));
        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginwithFacebook();
            }
        });
    }

    public void update() {
        tvHighscore.setText(String.valueOf(user.highScore));
        tvUndo.setText(String.valueOf(user.undo));
        tvHammer.setText(String.valueOf(user.hammer));
        setAva();
    }

    public void loginwithFacebook() {
        fbConnectHelper.connect();
    }

    @Override
    public void OnFbSuccess(GraphResponse graphResponse) {
        try {
            JSONObject jsonObject = graphResponse.getJSONObject();
            user.setName(jsonObject.getString("name"));
            user.setEmail(jsonObject.getString("email"));
            user.setIDFacebook(jsonObject.getString("id"));
            user.setProfilePic("http://graph.facebook.com/"+ user.getIDFacebook()+ "/picture?type=large");
            HandleImage.get().downloadSaveImageFromUrl(user.getProfilePic());
            Log.d("Login fargment","OnFbSuccess: "+ user.getIDFacebook());
            Log.d("Login fargment","OnFbSuccess: "+ user.getProfilePic());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        user.setLoggedFb(true);
        updateDataUser();
    }

    @Override
    public void OnFbError(String errorMessage) {
        Log.i("Login fargment","OnFbError: "+ errorMessage);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        fbConnectHelper.onActivityResult(requestCode, resultCode, data);
    }

    public void updateDataUser() {
        if (user.isLoggedFb()) {
            btnlogin.setVisibility(View.GONE);
            btnlogout.setVisibility(View.VISIBLE);
            btnlogout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    loggedFb();
                }
            });
        }
        else {
            btnlogin.setVisibility(View.VISIBLE);
            btnlogout.setVisibility(View.GONE);
            btnlogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    unlogin();
                }
            });
        }
        setAva();
        tvNick.setText(user.getName());
    }

    private void setAva() {
        if (user.getAvatar() == 0) {
            HandleImage.get().downloadSaveImageFromUrl(user.getProfilePic());
            HandleImage.get().loadImageFromUrl(user.getProfilePic(),ivAva);
        }
        else  ivAva.setImageResource(user.getAvatar());
    }
    @Override
    public void onResume() {
        super.onResume();
        updateDataUser();
    }
}
