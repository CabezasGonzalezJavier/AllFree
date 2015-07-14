package com.lumbralessoftware.freeall.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.content.Intent;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphRequestAsyncTask;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.lumbralessoftware.freeall.controller.ControllersFactory;
import com.lumbralessoftware.freeall.controller.RegistrationController;
import com.lumbralessoftware.freeall.controller.SharedPreferenceController;
import com.lumbralessoftware.freeall.interfaces.RegistrationResponseListener;
import com.lumbralessoftware.freeall.models.Registration;
import com.lumbralessoftware.freeall.utils.Constants;
import com.lumbralessoftware.freeall.utils.Utils;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterApiException;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import com.lumbralessoftware.freeall.R;
import com.twitter.sdk.android.core.internal.TwitterApiConstants;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LoginTabFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LoginTabFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginTabFragment extends Fragment implements RegistrationResponseListener {
    private TwitterLoginButton mTwitterButton;
    private RegistrationController mRegistrationController;

    private CallbackManager callbackManager;



    private OnFragmentInteractionListener mListener;


    public static LoginTabFragment newInstance() {
        LoginTabFragment fragment = new LoginTabFragment();
        return fragment;
    }

    public LoginTabFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setRetainInstance(true);
        super.onCreate(savedInstanceState);
        ControllersFactory.setRegistrationResponseListener(this);
        SharedPreferenceController.initializeInstance(getActivity());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        mTwitterButton = (TwitterLoginButton) view.findViewById(R.id.activity_login_twitter_button);
        setupTwitterLogin();

        LoginButton facebookLoginButton = (LoginButton) view.findViewById(R.id.login_button);
        setupFacebookLogin(facebookLoginButton);

        return view;
    }

    private void setupTwitterLogin() {
        mTwitterButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                // Do something with result, which provides a TwitterSession for making API calls
                final TwitterAuthToken authToken = result.data.getAuthToken();

                if (SharedPreferenceController.getInstance().getTwitterEmail().equals("")) {
                    final EditText input = new EditText(getActivity());
                    new AlertDialog.Builder(getActivity())
                            .setTitle("Enter your email")
                            .setMessage("Please, enter your email so that we can put you in touch with the people you deal with.")
                            .setView(input)
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    String email = input.getText().toString();
                                    String atoken = authToken.token;
                                    Log.d("tokenTwitter", atoken);
                                    String secret = authToken.secret;
                                    Log.d("secrettokenTwitter", secret);
                                    SharedPreferenceController.getInstance().setTwitterAccess(atoken);
                                    SharedPreferenceController.getInstance().setTwitterSecret(secret);
                                    SharedPreferenceController.getInstance().setTwitterEmail(email);
                                    registerUser(Constants.BACKEND_TWITTER, email, atoken, secret);
                                }
                            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            // Do nothing.
                        }
                    }).show();
                } else {
                    String atoken = authToken.token;
                    Log.d("tokenTwitter", atoken);
                    String secret = authToken.secret;
                    Log.d("secrettokenTwitter", secret);
                    SharedPreferenceController.getInstance().setTwitterAccess(atoken);
                    SharedPreferenceController.getInstance().setTwitterSecret(secret);
                    registerUser(Constants.BACKEND_TWITTER, SharedPreferenceController.getInstance().getTwitterEmail(), atoken, secret);
                }
            }

            @Override
            public void failure(TwitterException exception) {
                Toast.makeText(getActivity(), getString(R.string.login_error), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void registerUser(String backend, String email, String atoken, String secret) {
        if (Utils.isOnline(getActivity())) {
            mRegistrationController = ControllersFactory.getRegistrationController();
            mRegistrationController.request(backend, atoken, secret, email);

        }else{
            Toast.makeText(getActivity(), getString(R.string.no_connection), Toast.LENGTH_LONG).show();
        }
    }

    private void setupFacebookLogin(LoginButton facebookLoginButton) {
        facebookLoginButton.setReadPermissions("email");
        // If using in a fragment
        facebookLoginButton.setFragment(this);
        // Other app specific specialization

        callbackManager = CallbackManager.Factory.create();
        // Callback registration
        facebookLoginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                final AccessToken accessToken = loginResult.getAccessToken();
                GraphRequestAsyncTask request = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject user, GraphResponse graphResponse) {

                        try {
                            String email = user.getString("email");
                            //Need User email address after login success.
                            SharedPreferenceController.getInstance().setFacebookAccess(accessToken.getToken());
                            registerUser(Constants.BACKEND_FACEBOOK, email, accessToken.getToken(), null);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }).executeAsync();
            }

            @Override
            public void onCancel() {
                // App code
                Log.d("FACEBOOK", "user cancelled");

            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                Log.d("FACEBOOK", "error");
                Toast.makeText(getActivity(), getString(R.string.login_error), Toast.LENGTH_LONG).show();
            }
        });
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mTwitterButton.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onSuccess(Registration successResponse) {

        SharedPreferenceController.getInstance().setAccessToken(successResponse.getAccessToken());
    }

    @Override
    public void onError(String errorResponse) {

    }
}
