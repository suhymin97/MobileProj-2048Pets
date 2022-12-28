package dsa.hcmiu.a2048pets.entities.handle;

import android.app.Activity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookAuthorizationException;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.Collection;

import dsa.hcmiu.a2048pets.entities.model.User;

public class FbConnectHelper {
    private Collection<String> permissions = Arrays.asList("public_profile", "email", "user_birthday", "user_location");
    private CallbackManager callbackManager;
    private LoginManager loginManager;
    private ShareDialog shareDialog;
    private Activity activity;
    private Fragment fragment;
    private OnFbSignInListener fbSignInListener;
    private static FbConnectHelper instance;


    /**
     * Interface to listen the Facebook connect
     */
    public interface OnFbSignInListener {
        void OnFbSuccess(GraphResponse graphResponse);
        void OnFbError(String errorMessage);
    }

    public FbConnectHelper(Activity activity, OnFbSignInListener fbSignInListener) {
        this.activity = activity;
        this.fbSignInListener = fbSignInListener;
    }

    public FbConnectHelper(Fragment fragment, OnFbSignInListener fbSignInListener) {
        this.fragment = fragment;
        this.fbSignInListener = fbSignInListener;
    }

    public FbConnectHelper(Activity activity) {
        shareDialog = new ShareDialog(activity);
    }

    public FbConnectHelper(Fragment fragment) {
        shareDialog = new ShareDialog(fragment);
    }

    public void connect() {
        callbackManager = CallbackManager.Factory.create();
        if (activity != null){
            LoginManager.getInstance().logInWithReadPermissions(activity, permissions);
        }
        LoginManager.getInstance().logInWithReadPermissions(fragment, permissions);
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        callGraphAPI();
                        Log.i("Success", "Login Success");
                    }

                    @Override
                    public void onCancel() {
                        fbSignInListener.OnFbError("User cancelled.");
                        Log.i("Error", "User canceled");
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        if (exception instanceof FacebookAuthorizationException) {
                            if (AccessToken.getCurrentAccessToken() != null) {
                                LoginManager.getInstance().logOut();
                            }
                        }
                        fbSignInListener.OnFbError(exception.getMessage());
                        Log.i("Error", "User Error");
                    }
                });

    }

    public void callGraphAPI() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        if (accessToken != null) {
            GraphRequest request = GraphRequest.newMeRequest(
                    accessToken,
                    (object, response) -> fbSignInListener.OnFbSuccess(response));
            Bundle parameters = new Bundle();
            //Explicitly we need to specify the fields to get values else some values will be null.
            parameters.putString("fields","id,birthday,email,first_name,gender,last_name,link,location,name");
            request.setParameters(parameters);
            request.executeAsync();
        }
    }

    public void shareOnFBWall(String title, String description, String url) {
        if (ShareDialog.canShow(ShareLinkContent.class)) {
            ShareLinkContent linkContent = new ShareLinkContent.Builder()
                    .setContentTitle(title)
                    .setContentDescription(description)
                    .setContentUrl(Uri.parse(url))
                    .build();
            shareDialog.show(linkContent);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (callbackManager != null)
            callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public User getUserFromGraphResponse(GraphResponse graphResponse)
    {
        User user = new User();
        JSONObject jsonObject = graphResponse.getJSONObject();
        user.setName(jsonObject.optString("name"));
        user.setEmail(jsonObject.optString("email"));
        user.setIDFacebook(jsonObject.optString("id"));
        user.setProfilePic("http://graph.facebook.com/"+ user.getIDFacebook()+ "/picture?type=large");
        Log.i("FbConnect",user.getProfilePic());
        return user;
    }
}
