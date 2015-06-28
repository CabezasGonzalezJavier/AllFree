package com.lumbralessoftware.freeall.fragments;

import android.app.Activity;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;

import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.lumbralessoftware.freeall.R;
import com.lumbralessoftware.freeall.adapters.WindowsAdapter;
import com.lumbralessoftware.freeall.interfaces.UpdateableFragment;
import com.lumbralessoftware.freeall.models.Item;
import com.lumbralessoftware.freeall.utils.Constants;

import java.util.Iterator;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MapTabFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MapTabFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MapTabFragment extends Fragment implements UpdateableFragment {
    /** Local variables **/
    GoogleMap mGoogleMap;
    LatLng mUserLocation;
    List<Item> mItemList;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *

     * @return A new instance of fragment MapFragment.
     */
    public static MapTabFragment newInstance() {
        MapTabFragment fragment = new MapTabFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    public MapTabFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_map, container, false);
        setUpMapIfNeeded();
        // Inflate the layout for this fragment
        return view;
    }

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

    @Override
    public void update(List<Item> items) {
        mItemList=items;
        drawnPoints();
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
    public void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }




    /**
     *
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mGoogleMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mGoogleMap = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.fragment_map_map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mGoogleMap != null) {
                mGoogleMap.setMyLocationEnabled(true);

                mGoogleMap.setOnMyLocationChangeListener(myLocationChangeListener);

                mUserLocation = new LatLng(0,0);
                if (mGoogleMap.getMyLocation()!=null){
                    mUserLocation = new LatLng(mGoogleMap.getMyLocation().getLatitude(),mGoogleMap.getMyLocation().getLongitude());
                }else{
                    lastLocation();
                }
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(mUserLocation, Constants.ZOOM_MAP);
                mGoogleMap.animateCamera(cameraUpdate);
            }
        }
    }

    private void drawnPoints(){
        for (Item item : mItemList){
            setUpMap(item);
        }
    }

    private void setUpMap(Item item) {
        mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(Float.parseFloat(item.getLocation().getLatPosition()), Float.parseFloat(item.getLocation().getLongPosition()))));
        mGoogleMap.setInfoWindowAdapter(new WindowsAdapter(getActivity(),item));
    }
    @Override
    public void onPause() {
        Fragment fragment = (getChildFragmentManager().findFragmentById(R.id.fragment_map_map));
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.remove(fragment);
        ft.commit();
        super.onPause();
    }

    public void lastLocation(){
        LocationManager service = (LocationManager) getActivity().getSystemService(getActivity().LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String provider = service.getBestProvider(criteria, false);
        Location location = service.getLastKnownLocation(provider);
        if (location!=null){
            mUserLocation = new LatLng(location.getLatitude(),location.getLongitude());
        }
    }

    private GoogleMap.OnMyLocationChangeListener myLocationChangeListener = new GoogleMap.OnMyLocationChangeListener() {
        @Override
        public void onMyLocationChange(Location location) {
            LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());
            if(mGoogleMap != null){
                mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, Constants.ZOOM_MAP));
            }
        }
    };

}
