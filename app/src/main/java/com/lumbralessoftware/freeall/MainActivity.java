package com.lumbralessoftware.freeall;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.lumbralessoftware.freeall.fragments.MapTabFragment;
import com.lumbralessoftware.freeall.fragments.SecondTabFragment;
import com.lumbralessoftware.freeall.fragments.ThirdTabFragment;
import com.lumbralessoftware.freeall.utils.Constants;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_main_toolbar);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.activity_main_tab_layout);
        ViewPager viewPager = (ViewPager) findViewById(R.id.activity_main_pager);

        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }

        viewPager.setAdapter(new SectionPagerAdapter(getSupportFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);
    }

    public class SectionPagerAdapter extends FragmentPagerAdapter {

        public SectionPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;

            switch (position) {
                case 0:
                    fragment = new MapTabFragment();
                    break;
                case 1:
                    fragment = new SecondTabFragment();
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
                    return getString(R.string.fragment_map_title);
                case 1:
                    return getString(R.string.fragment_second_title);
                case 2:
                default:
                    return getString(R.string.fragment_third_title);
            }
        }
    }

}
