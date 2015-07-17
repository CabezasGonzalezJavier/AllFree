package com.lumbralessoftware.reusame.adapters;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.lumbralessoftware.reusame.R;
import com.lumbralessoftware.reusame.models.Item;
import com.lumbralessoftware.reusame.views.CircleTransform;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by javiergonzalezcabezas on 28/6/15.
 */
public class WindowsAdapter implements GoogleMap.InfoWindowAdapter {

    private final View mContentsView;
    private Activity mActivity;
    private List<Item> mItems;

    public WindowsAdapter(Activity activity, List<Item> items) {
        mContentsView = activity.getLayoutInflater().inflate(R.layout.row_marker, null);
        mActivity = activity;
        mItems = items;
    }

    static class ViewHolder {
        public TextView mName;
        public TextView mCategory;
        public View mImage;
    }

    @Override
    public View getInfoContents(Marker marker) {


        // configure view holder
        TextView name = (TextView) mContentsView.findViewById(R.id.row_marker_name);
        TextView category = (TextView) mContentsView.findViewById(R.id.row_marker_category);
        ImageView image = (ImageView) mContentsView.findViewById(R.id.row_marker_icon);

        // Get the item from the list by retrieving the index from marker's snippet
        Item item = mItems.get(Integer.valueOf(marker.getSnippet()));
        name.setText(item.getName());
        category.setText(item.getCategory());
        Picasso.with(mActivity).load(item.getImage()).transform(new CircleTransform()).into((ImageView) image);

        return mContentsView;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

}

