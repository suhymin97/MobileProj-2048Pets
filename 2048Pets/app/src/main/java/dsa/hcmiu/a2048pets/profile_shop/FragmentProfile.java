package dsa.hcmiu.a2048pets.profile_shop;

import static com.facebook.FacebookSdk.getApplicationContext;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.GraphResponse;
import com.facebook.ProfileTracker;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.internal.OnConnectionFailedListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;

import de.hdodenhof.circleimageview.CircleImageView;
import dsa.hcmiu.a2048pets.R;
import dsa.hcmiu.a2048pets.entities.handle.FbConnectHelper;
import dsa.hcmiu.a2048pets.entities.handle.HandleFile;
import dsa.hcmiu.a2048pets.entities.handle.HandleImage;
import dsa.hcmiu.a2048pets.entities.model.User;

import static dsa.hcmiu.a2048pets.entities.model.Features.user;

import java.io.IOException;

public class FragmentProfile extends Fragment implements OnConnectionFailedListener,FbConnectHelper.OnFbSignInListener{

    TextView tvHighscore, tvUndo, tvHammer;
    CircleImageView ivAva;
    private ImageButton btnFb,btnTwt;
    ImageView btnGoogle;
    private Button btnLogout;
    private TextView tvNick;
    private ProfileTracker mProfileTracker;
    private FbConnectHelper fbConnectHelper;
    private GoogleSignInOptions gso;
    private GoogleSignInClient gsc;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public FragmentProfile() {
        super(R.layout.fragment_profile);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile,container,false);
        fbConnectHelper = new FbConnectHelper(this,this);
        tvHighscore = (TextView) view.findViewById(R.id.tvAchiveHighscore);
        tvUndo = (TextView) view.findViewById(R.id.tvAchiveUndo);
        tvHammer = (TextView) view.findViewById(R.id.tvAchiveHammer);
        ivAva = (CircleImageView) view.findViewById(R.id.ivAvaFb);
        btnLogout = (Button) view.findViewById(R.id.btnLogout);
        tvNick = (TextView) view.findViewById(R.id.tvNick);
		btnGoogle = view.findViewById(R.id.btnGoogle);
        btnFb = view.findViewById(R.id.btnFb);
        btnTwt = view.findViewById(R.id.btnTwt);
//		update(); //avu7
//        updateDataUser(); //avu7

        btnGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInGoogle();
            }
        });

        btnFb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginWithFacebook();
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signOut(); //Cong
            }
        });
        updateDataUser();
        return view;
    }

    private void signInGoogle(){
        user.setSocialType("G");
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestProfile()
                .requestId()
                .build();
        gsc = GoogleSignIn.getClient(this.getActivity(),gso);
        Intent signInIntent = gsc.getSignInIntent();
        startActivityForResult(signInIntent, Activity.RESULT_OK);
//        startActivityForResult(Activity.RESULT_OK,signInIntent);

    }

    public void loginWithFacebook() {
        user.setSocialType("F");
        fbConnectHelper.connect();
    }

    @Override
    public void OnFbSuccess(GraphResponse graphResponse) {
        try {
            user = fbConnectHelper.getUserFromGraphResponse(graphResponse);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        updateDataUser();
    }

    @Override
    public void OnFbError(String errorMessage) {
        Log.i("Login fragment","OnFbError: "+ errorMessage);
    }


	@Override
    public void onActivityResult(int requestCode, int resultCode,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (user.getSocialType().equals("F")) {
            fbConnectHelper.onActivityResult(requestCode, resultCode, data);
        } else if (user.getSocialType().equals("G")) {
                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                handleSignInGoogle(task);
                Toast.makeText(this.getContext(), "Success", Toast.LENGTH_SHORT).show();
                updateDataUser();
        }
    }

    private void handleSignInGoogle(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            if(account != null){
                user = new User();
                user.setLogged(true);
                user.setName(account.getGivenName());
                user.setIDFacebook(account.getId());
                user.setProfilePic(String.valueOf(account.getPhotoUrl()));
                user.setPhotoUrl(account.getPhotoUrl());
                user.setSocialType("G");
                HandleImage.get().downloadSaveImageFromUrl(user.getProfilePic());
//            Picasso.get().load(account.getPhotoUrl()).into(ivAva);
            }
            else {
                user.getAvatar();
            }
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("FragmentProfile", "signInResult:failed code=" + e.getStatusCode());
            Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
        }
    }

    public void updateDataUser() {
        if (user.isLogged()) {
            btnGoogle.setVisibility(View.GONE);
            btnFb.setVisibility(View.GONE);
            btnTwt.setVisibility(View.GONE);
            btnLogout.setVisibility(View.VISIBLE);
        }
        else {
            btnLogout.setVisibility(View.GONE);
            btnGoogle.setVisibility(View.VISIBLE);
            btnFb.setVisibility(View.VISIBLE);
            btnTwt.setVisibility(View.VISIBLE);
            user.returnDef();
//            ivAva.setImageResource(R.drawable.default_ava);
        }
        update(); //cong
    }
    public void update() {
        tvNick.setText(user.getName());
        tvHighscore.setText(String.valueOf(user.highScore));
        tvUndo.setText(String.valueOf(user.undo));
        tvHammer.setText(String.valueOf(user.hammer));
        setAva(); //avu7
    }
    private void setAva() { //avu7
        if (user.getAvatar() == 0) {
            HandleImage.get().downloadSaveImageFromUrl(user.getProfilePic());
            HandleImage.get().loadImageFromUrl(user.getProfilePic(), ivAva);
        }
        else  ivAva.setImageResource(user.getAvatar());
    }
    @Override
    public void onResume() {
        super.onResume();
        updateDataUser();
    }

    @Override
    public void onPause() {
        super.onPause();
        updateDataUser();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
    public void signOut() {
        if (user.getSocialType().equals("G")) {
            gsc.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(Task<Void> task) {
                    GoogleSignIn.getClient(getApplicationContext(), GoogleSignInOptions.DEFAULT_SIGN_IN).signOut();
                    DialogNoti dialogNoti = DialogNoti.newInstance();
                    dialogNoti.show(getChildFragmentManager(), "Success");
                }
            });
        }
        try {
            HandleFile.writeToFile();
            HandleFile.readFeaturesJSONFile();
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        user.returnDef();
        updateDataUser();
    }
}
