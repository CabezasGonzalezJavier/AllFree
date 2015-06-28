package com.lumbralessoftware.freeall.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lumbralessoftware.freeall.R;
import com.lumbralessoftware.freeall.controller.ControllersFactory;
import com.lumbralessoftware.freeall.controller.ItemsController;
import com.lumbralessoftware.freeall.interfaces.ItemRequestResponseListener;
import com.lumbralessoftware.freeall.models.Item;
import com.lumbralessoftware.freeall.models.ItemRequest;
import com.lumbralessoftware.freeall.utils.Constants;
import com.lumbralessoftware.freeall.utils.Utils;
import com.lumbralessoftware.freeall.views.CircleTransform;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DetailActivity extends AppCompatActivity implements ItemRequestResponseListener{

    private Item mItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            return;
        }


        mItem = (Item) extras.getSerializable(Constants.DETAIL);

//        ControllersFactory.setItemResponseListener(this);
        ItemsController itemsController = ControllersFactory.getItemsController();
        itemsController.setItemRequestResponseListener(this);
        ItemRequest request = new ItemRequest();
        request.setMessage("Hi, I want this!");
        itemsController.want(mItem.getId(), request);

        TextView name = (TextView) findViewById(R.id.activity_detail_name);
        TextView category = (TextView) findViewById(R.id.activity_detail_category);
        TextView description = (TextView) findViewById(R.id.activity_detail_description);
        TextView dateTextView = (TextView) findViewById(R.id.activity_detail_date);

        ImageView image = (ImageView) findViewById(R.id.activity_detail_icon);


        name.setText(mItem.getName());
        category.setText(mItem.getCategory());
        description.setText(mItem.getDescription());

//        DateFormat parser = ISODateTimeFormat.dateTime();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        try {
            Date date = formatter.parse(mItem.getCreated());
            Calendar calendar = Utils.dateToCalendar(date);
//            calendar.get
        } catch (ParseException e) {
            e.printStackTrace();
        }



        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(getString(R.string.activity_detail_title_date));
        stringBuilder.append(": ");
        stringBuilder.append(mItem.getCreated());
        dateTextView.setText(stringBuilder);

        Picasso.with(this).load(mItem.getImage()).transform(new CircleTransform()).into((ImageView) image);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSuccess(ItemRequest successResponse) {
        Toast.makeText(this, "OK", Toast.LENGTH_LONG).show();

    }

    @Override
    public void onError(String errorResponse) {
        Toast.makeText(this, "Error", Toast.LENGTH_LONG).show();

    }
}
