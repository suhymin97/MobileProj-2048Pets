package dsa.hcmiu.a2048pets.profile_shop;

import static com.facebook.FacebookSdk.getApplicationContext;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookAuthorizationException;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphResponse;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.PicassoProvider;

import org.json.JSONException;
import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;
import dsa.hcmiu.a2048pets.R;
import dsa.hcmiu.a2048pets.entities.handle.FbConnectHelper;
import dsa.hcmiu.a2048pets.entities.handle.HandleImage;
import dsa.hcmiu.a2048pets.entities.model.User;

import static dsa.hcmiu.a2048pets.entities.model.Features.user;

import java.util.Arrays;

public class FragmentProfile extends Fragment implements GoogleApiClient.OnConnectionFailedListener{

    TextView tvHighscore, tvUndo, tvHammer;
    CircleImageView ivAva;
    private ImageButton btnFb,btnTwt;
    ImageView btnGoogle;
    private Button btnLogout;
    private TextView tvNick;
    GoogleSignInOptions gso;
    GoogleSignInClient gsc;


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
        tvHighscore = view.findViewById(R.id.tvAchiveHighscore);
        tvHammer = view.findViewById(R.id.tvAchiveHammer);
        tvUndo = view.findViewById(R.id.tvAchiveUndo);
        tvNick = view.findViewById(R.id.tvNick);
        btnLogout = view.findViewById(R.id.btnLogout);

        ivAva = view.findViewById(R.id.ivAvaFb);

        btnGoogle = view.findViewById(R.id.btnGoogle);
        btnFb = view.findViewById(R.id.btnFb);
        btnTwt = view.findViewById(R.id.btnTwt);

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestProfile()
                .requestId()
                .build();
        gsc = GoogleSignIn.getClient(this.getActivity(),gso);

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this.getActivity());

        btnGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signOut();
            }
        });

        if(account != null){
            user.setLoggedFb(true);
            user.setName(account.getGivenName());
            user.setIDFacebook(account.getId());
            user.setProfilePic(String.valueOf(account.getPhotoUrl()));
            user.setPhotoUrl(account.getPhotoUrl());
            Picasso.get().load(account.getPhotoUrl()).into(ivAva);
        }
        else {
            user.returnDef();
        }
        updateDataUser();
        return view;
    }

    void signIn(){
        Intent signInIntent = gsc.getSignInIntent();
        startActivityForResult(signInIntent, Activity.RESULT_OK);
//        startActivityForResult(Activity.RESULT_OK,signInIntent);
    }

    public void update() {
        tvHighscore.setText(String.valueOf(user.highScore));
        tvUndo.setText(String.valueOf(user.undo));
        tvHammer.setText(String.valueOf(user.hammer));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1000){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            Toast.makeText(this.getContext(), "Success", Toast.LENGTH_SHORT).show();
            try {
                task.getResult(ApiException.class);
            } catch (ApiException e) {
                Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        }

    }

    public void updateDataUser() {
        if (user.isLoggedFb()) {
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
            ivAva.setImageResource(R.drawable.default_ava);
        }
        tvNick.setText(user.getName());
        update();
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
    public void signOut(){
        gsc.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(Task<Void> task) {
                GoogleSignIn.getClient(getApplicationContext(),GoogleSignInOptions.DEFAULT_SIGN_IN).signOut();
                user.returnDef();
                updateDataUser();
                DialogNoti dialogNoti = DialogNoti.newInstance();
                dialogNoti.show(getChildFragmentManager(),"Success");
            }
        });
    }
}
