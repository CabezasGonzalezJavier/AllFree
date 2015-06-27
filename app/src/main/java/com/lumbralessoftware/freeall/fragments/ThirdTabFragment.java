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

import com.lumbralessoftware.freeall.controller.ControllersFactory;
import com.lumbralessoftware.freeall.controller.ItemsController;
import com.lumbralessoftware.freeall.controller.RegistrationController;
import com.lumbralessoftware.freeall.controller.SharedPreferenceController;
import com.lumbralessoftware.freeall.interfaces.ItemResponseHandler;
import com.lumbralessoftware.freeall.interfaces.ItemResponseListener;
import com.lumbralessoftware.freeall.interfaces.RegistrationResponseListener;
import com.lumbralessoftware.freeall.models.Item;
import com.lumbralessoftware.freeall.models.Registration;
import com.lumbralessoftware.freeall.models.Token;
import com.lumbralessoftware.freeall.utils.Constants;
import com.lumbralessoftware.freeall.utils.Utils;
import com.lumbralessoftware.freeall.webservice.Client;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterApiException;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import com.lumbralessoftware.freeall.R;
import com.twitter.sdk.android.core.internal.TwitterApiConstants;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ThirdTabFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ThirdTabFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ThirdTabFragment extends Fragment implements RegistrationResponseListener {
    private TwitterLoginButton mTwitterButton;
    private RegistrationController mRegistrationController;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ThirdFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ThirdTabFragment newInstance(String param1, String param2) {
        ThirdTabFragment fragment = new ThirdTabFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public ThirdTabFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ControllersFactory.setsRegistrationResponseListener(this);
        SharedPreferenceController.initializeInstance(getActivity());


        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_third, container, false);

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
                    mRegistrationController = ControllersFactory.getsRegistrationController();
                    mRegistrationController.request(atoken, secret);

                }else{
                    Toast.makeText(getActivity(), getString(R.string.no_connection), Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void failure(TwitterException exception) {
                final TwitterApiException apiException = (TwitterApiException) exception;
                final int errorCode = apiException.getErrorCode();
                if (errorCode == TwitterApiConstants.Errors.APP_AUTH_ERROR_CODE || errorCode == TwitterApiConstants.Errors.GUEST_AUTH_ERROR_CODE) {
                    // get new guestAppSession
                    // optionally retry
                }
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
    }

    @Override
    public void onSuccess(Registration successResponse) {

        SharedPreferenceController.getInstance().setAccessToken(successResponse.getAccessToken());
    }

    @Override
    public void onError(String errorResponse) {

    }
}
