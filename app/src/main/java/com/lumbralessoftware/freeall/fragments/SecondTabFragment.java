package com.lumbralessoftware.freeall.fragments;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.lumbralessoftware.freeall.R;
import com.lumbralessoftware.freeall.adapters.ItemsAdapter;
import com.lumbralessoftware.freeall.interfaces.UpdateableFragment;
import com.lumbralessoftware.freeall.models.Item;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SecondTabFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SecondTabFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SecondTabFragment extends Fragment implements UpdateableFragment {

    private ItemsAdapter mAdapter;
    private ListView mListView;
    private OnFragmentInteractionListener mListener;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *


     * @return A new instance of fragment SecondFragment.
     */
    public static SecondTabFragment newInstance() {
        SecondTabFragment fragment = new SecondTabFragment();
        return fragment;
    }

    public SecondTabFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_second, container, false);

        mListView = (ListView) view.findViewById(R.id.fragment_list_listView);


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

    public void showList(List<Item> list){
        mListView.setAdapter(new ItemsAdapter(getActivity(), 0, list));
//        mListView.setOnItemClickListener(this);
    }

    @Override
    public void update(List<Item> items) {
        showList(items);
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

}
