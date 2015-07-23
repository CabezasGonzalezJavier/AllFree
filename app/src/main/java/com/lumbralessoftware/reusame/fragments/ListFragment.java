package com.lumbralessoftware.reusame.fragments;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.lumbralessoftware.reusame.R;
import com.lumbralessoftware.reusame.activities.DetailActivity;
import com.lumbralessoftware.reusame.adapters.ItemsAdapter;
import com.lumbralessoftware.reusame.interfaces.RefreshListener;
import com.lumbralessoftware.reusame.interfaces.UpdateableFragment;
import com.lumbralessoftware.reusame.models.Item;
import com.lumbralessoftware.reusame.utils.Constants;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * create an instance of this fragment.
 */
public class ListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, UpdateableFragment, AdapterView.OnItemClickListener {

    private static RefreshListener sResfreshListener;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ItemsAdapter mAdapter;
    private ListView mListView;
    private OnFragmentInteractionListener mListener;
    private static List<Item> mItems;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *


     * @return A new instance of fragment SecondFragment.
     */
    public static ListFragment newInstance(RefreshListener listener) {
        ListFragment fragment = new ListFragment(listener);
        sResfreshListener = listener;
        return fragment;
    }

    public ListFragment(){}

    public ListFragment(RefreshListener listener) {
        // Required empty public constructor
        sResfreshListener = listener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setRetainInstance(true);
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list, container, false);

        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.activity_feed_swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(this);


        mListView = (ListView) view.findViewById(R.id.fragment_list_listView);
        mListView.setOnItemClickListener(this);

        if (isAdded() && mItems != null) {
            mListView.setAdapter(new ItemsAdapter(getActivity(), mItems));
        }
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

    public void showList(){
        if (mListView.getAdapter() != null) {
            ((ItemsAdapter)mListView.getAdapter()).notifyDataSetChanged();
            mListView.setAdapter(new ItemsAdapter(getActivity(), mItems));
        } else {
            mListView.setAdapter(new ItemsAdapter(getActivity(), mItems));
//        mListView.setOnItemClickListener(this);
        }
    }

    @Override
    public void update(List<Item> items) {
        mItems = items;
        showList();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

        Intent intent = new Intent(getActivity(), DetailActivity.class);
        intent.putExtra(Constants.DETAIL, mItems.get(position));
        startActivity(intent);

    }

    @Override
    public void onRefresh() {
        sResfreshListener.refreshList();
        mSwipeRefreshLayout.setRefreshing(false);
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
