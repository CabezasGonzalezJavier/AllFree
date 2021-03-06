package com.lumbralessoftware.reusame.activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.lumbralessoftware.reusame.R;
import com.lumbralessoftware.reusame.adapters.SectionPagerAdapter;
import com.lumbralessoftware.reusame.controller.ItemsController;
import com.lumbralessoftware.reusame.controller.ControllersFactory;
import com.lumbralessoftware.reusame.controller.SearchController;
import com.lumbralessoftware.reusame.controller.SharedPreferenceController;
import com.lumbralessoftware.reusame.interfaces.RefreshListener;
import com.lumbralessoftware.reusame.models.Item;
import com.lumbralessoftware.reusame.utils.AppRate;
import com.lumbralessoftware.reusame.utils.Constants;
import com.lumbralessoftware.reusame.utils.Utils;
import com.lumbralessoftware.reusame.interfaces.ItemResponseListener;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements ItemResponseListener, RefreshListener {

    public static final int LOGIN_FRAGMENT = 2;
    public static final int ADD_FRAGMENT = 3;

    private ItemsController mItemsController;
    private SearchController mSearchController;
    private SectionPagerAdapter mAdapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferenceController.initializeInstance(this.getApplicationContext());
        FacebookSdk.sdkInitialize(this.getApplicationContext());

        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_main_toolbar);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.activity_main_tab_layout);
        mViewPager = (ViewPager) findViewById(R.id.activity_main_pager);
        mViewPager.setOffscreenPageLimit(Constants.TOTAL_TABS);

        if (toolbar != null) {
            setSupportActionBar(toolbar);
            toolbar.setNavigationIcon(R.mipmap.ic_launcher);
        }
        getData();
        mAdapter = new SectionPagerAdapter(getSupportFragmentManager(),this,this);
        mViewPager.setAdapter(mAdapter);
        tabLayout.setupWithViewPager(mViewPager);
    }



    public void getData(){
        if (Utils.isOnline(this)) {
            ControllersFactory.setItemResponseListener(this);
            mItemsController = ControllersFactory.getItemsController();
            mItemsController.request(Utils.getLastLocation(getApplicationContext()));
        }else{
            Toast.makeText(this, getString(R.string.no_connection), Toast.LENGTH_LONG).show();
        }

    }

    public void search(String name){
        if (Utils.isOnline(this)) {
            ControllersFactory.setItemResponseListener(this);
            mSearchController = ControllersFactory.getsSearchController();
            mSearchController.request(name);
        }else{
            List<Item> successResponse = new ArrayList<>();
            mAdapter.update(successResponse);
            Toast.makeText(this, getString(R.string.no_connection), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onSuccess(List<Item> successResponse) {
        mAdapter.update(successResponse);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result to the fragment, which will
        // then pass the result to the login button.

        Fragment fragment = findFragmentByPosition(LOGIN_FRAGMENT);

        if (fragment != null) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }

    }

    public Fragment findFragmentByPosition(int position) {
        return getSupportFragmentManager().findFragmentByTag(
                "android:switcher:" + mViewPager.getId() + ":"
                        + mAdapter.getItemId(position));
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
//        android.support.v7.widget.SearchView searchView = (android.support.v7.widget.SearchView) menu.findItem(R.id.menu_search).getActionView();
        MenuItem searchItem = menu.findItem(R.id.menu_search);
        android.support.v7.widget.SearchView searchView = (android.support.v7.widget.SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(new android.support.v7.widget.SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String entryString) {
                search(entryString);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {


                return true;

            }

        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (item.getItemId()) {
            case R.id.action_about_us:
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Constants.ABOUT_US));
                startActivity(browserIntent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            case R.id.action_our_app:
                Intent browserIntentApps = new Intent(Intent.ACTION_VIEW, Uri.parse(Constants.OUR_APPS));
                startActivity(browserIntentApps);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            case R.id.action_app_rate:
                AppRate.showRateDialog(this);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onError(String errorResponse) {
        Toast.makeText(this, errorResponse, Toast.LENGTH_LONG).show();
    }

    @Override
    public void refreshList() {
        getData();
    }
}
