package com.lumbralessoftware.freeall.adapters;

import android.app.Activity;
import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lumbralessoftware.freeall.R;
import com.lumbralessoftware.freeall.models.Item;
import com.lumbralessoftware.freeall.views.CircleTransform;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.List;

/**
 * Created by javiergonzalezcabezas on 27/6/15.
 */
public class ItemsAdapter extends ArrayAdapter<Item> {

    private Activity mActivity;
    private List<Item> mItemList;

    static class ViewHolder {
        public TextView mName;
        public TextView mCategory;
        public View mImage;
    }

    public ItemsAdapter(Activity activity, int resource, List<Item> objects) {
        super(activity, resource, objects);
        mActivity = activity;
        mItemList = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;
        // reuse views
        if (rowView == null) {

            LayoutInflater inflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.row_list, null);

            // configure view holder
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.mName = (TextView) rowView.findViewById(R.id.row_list_name);
            viewHolder.mCategory = (TextView) rowView.findViewById(R.id.row_list_category);
            viewHolder.mImage = rowView.findViewById(R.id.row_list_icon);

            rowView.setTag(viewHolder);
        }

        // fill data
        ViewHolder holder = (ViewHolder) rowView.getTag();

        holder.mName.setText(mItemList.get(position).getName());
        holder.mCategory.setText(mItemList.get(position).getCategory());

        Picasso.with(mActivity).load(mItemList.get(position).getImage()).transform(new CircleTransform()).into((ImageView) holder.mImage);

        return rowView;
    }
}
