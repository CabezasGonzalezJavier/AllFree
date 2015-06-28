package com.lumbralessoftware.freeall.adapters;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.lumbralessoftware.freeall.R;
import com.lumbralessoftware.freeall.fragments.MapTabFragment;
import com.lumbralessoftware.freeall.fragments.ListFragment;
import com.lumbralessoftware.freeall.fragments.ThirdTabFragment;
import com.lumbralessoftware.freeall.interfaces.UpdateableFragment;
import com.lumbralessoftware.freeall.models.Item;
import com.lumbralessoftware.freeall.utils.Constants;

import java.util.List;

/**
 * Created by javiergonzalezcabezas on 27/6/15.
 */
public class SectionPagerAdapter extends FragmentPagerAdapter {

    private List<Item> mItems;
    private Activity mActivity;

    public SectionPagerAdapter(FragmentManager fm, Activity activity) {
        super(fm);
        mActivity = activity;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;

        switch (position) {
            case 0:
                fragment = new MapTabFragment();
                break;
            case 1:
                fragment = new ListFragment();
                break;
            case 2:
            default:
                fragment = new ThirdTabFragment();
                break;

        }
        return fragment;
    }

    @Override
    public int getCount() {
        return Constants.TOTAL_TABS;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return mActivity.getString(R.string.fragment_map_title);
            case 1:
                return mActivity.getString(R.string.fragment_second_title);
            case 2:
            default:
                return mActivity.getString(R.string.fragment_third_title);
        }
    }

    /**
     * call this method to update fragments in ViewPager dynamically
     */
    public void update(List<Item> items) {
        mItems = items;
        notifyDataSetChanged();
    }

    @Override
    public int getItemPosition(Object object) {
        if (object instanceof UpdateableFragment) {
            ((UpdateableFragment) object).update(mItems);
        }
        //don't return POSITION_NONE, avoid fragment recreation.
        return super.getItemPosition(object);
    }
}