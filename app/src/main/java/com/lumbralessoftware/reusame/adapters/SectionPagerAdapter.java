package com.lumbralessoftware.reusame.adapters;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.lumbralessoftware.reusame.R;
import com.lumbralessoftware.reusame.fragments.AddObject;
import com.lumbralessoftware.reusame.fragments.MapTabFragment;
import com.lumbralessoftware.reusame.fragments.ListFragment;
import com.lumbralessoftware.reusame.fragments.LoginTabFragment;
import com.lumbralessoftware.reusame.interfaces.UpdateableFragment;
import com.lumbralessoftware.reusame.models.Item;
import com.lumbralessoftware.reusame.utils.Constants;

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
                fragment = new LoginTabFragment();
                break;
            case 3:
            default:
                fragment = new AddObject();
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
                return mActivity.getString(R.string.fragment_list_title);
            case 2:
                return mActivity.getString(R.string.fragment_login_title);
            case 3:
            default:
                return mActivity.getString(R.string.fragment_add_object);
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