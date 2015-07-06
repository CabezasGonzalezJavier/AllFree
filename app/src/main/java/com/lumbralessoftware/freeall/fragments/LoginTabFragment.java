package com.lumbralessoftware.freeall.fragments;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.content.Intent;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
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
        mTwitterButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                // Do something with result, which provides a TwitterSession for making API calls
                long user = result.data.getUserId();

                TwitterAuthToken authToken = result.data.getAuthToken();
                String atoken = authToken.token;
                Log.d("tokenTwitter", atoken);
                String secret = authToken.secret;
                Log.d("secrettokenTwitter", secret);
                SharedPreferenceController.getInstance().setTwitterAccess(atoken);
                SharedPreferenceController.getInstance().setTwitterSecret(secret);

                if (Utils.isOnline(getActivity())) {
                    mRegistrationController = ControllersFactory.getRegistrationController();
                    mRegistrationController.request(Constants.BACKEND_TWITTER, atoken, secret);

                }else{
                    Toast.makeText(getActivity(), getString(R.string.no_connection), Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void failure(TwitterException exception) {
                Toast.makeText(getActivity(), getString(R.string.login_error), Toast.LENGTH_LONG).show();
            }
        });

        LoginButton loginButton = (LoginButton) view.findViewById(R.id.login_button);
        loginButton.setReadPermissions("user_friends");
        // If using in a fragment
        loginButton.setFragment(this);
        // Other app specific specialization

        callbackManager = CallbackManager.Factory.create();
        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                AccessToken at = loginResult.getAccessToken();
                SharedPreferenceController.getInstance().setFacebookAccess(at.getToken());
                mRegistrationController = ControllersFactory.getRegistrationController();
                mRegistrationController.request(Constants.BACKEND_FACEBOOK, at.getToken(), null);
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

            }
        });

        return view;
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
