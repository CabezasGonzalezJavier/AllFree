package com.lumbralessoftware.freeall;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.lumbralessoftware.freeall.adapters.SectionPagerAdapter;
import com.lumbralessoftware.freeall.controller.ItemsController;
import com.lumbralessoftware.freeall.controller.ItemsControllersFactory;
import com.lumbralessoftware.freeall.fragments.MapTabFragment;
import com.lumbralessoftware.freeall.fragments.SecondTabFragment;
import com.lumbralessoftware.freeall.fragments.ThirdTabFragment;
import com.lumbralessoftware.freeall.interfaces.UpdateableFragment;
import com.lumbralessoftware.freeall.models.Item;
import com.lumbralessoftware.freeall.utils.Constants;
import com.lumbralessoftware.freeall.utils.Utils;
import com.lumbralessoftware.freeall.webservice.ResponseListener;

import java.util.List;


public class MainActivity extends AppCompatActivity implements ResponseListener {

    private ItemsController mItemsController;
    private SectionPagerAdapter mAdapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_main_toolbar);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.activity_main_tab_layout);
        mViewPager = (ViewPager) findViewById(R.id.activity_main_pager);

        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
        getData();
        mAdapter = new SectionPagerAdapter(getSupportFragmentManager(),this);
        mViewPager.setAdapter(mAdapter);
        tabLayout.setupWithViewPager(mViewPager);
    }



    public void getData(){
        if (Utils.isOnline(this)) {

            ItemsControllersFactory.setsResponseListener(this);

            mItemsController = ItemsControllersFactory.getsItemsController();
            mItemsController.request();

        }else{
            Toast.makeText(this, getString(R.string.no_connection), Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onSuccess(List<Item> successResponse) {

        mAdapter.update(successResponse);
    }

    @Override
    public void onError(String errorResponse) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result to the fragment, which will
        // then pass the result to the login button.

        Fragment fragment = findFragmentByPosition(2);

        if (fragment != null) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    public Fragment findFragmentByPosition(int position) {
        return getSupportFragmentManager().findFragmentByTag(
                "android:switcher:" + mViewPager.getId() + ":"
                        + mAdapter.getItemId(position));
    }
}
