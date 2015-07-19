package com.lumbralessoftware.reusame.activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lumbralessoftware.reusame.R;
import com.lumbralessoftware.reusame.controller.ControllersFactory;
import com.lumbralessoftware.reusame.controller.UserController;
import com.lumbralessoftware.reusame.interfaces.ItemRequestResponseListener;
import com.lumbralessoftware.reusame.interfaces.VoteResponseListener;
import com.lumbralessoftware.reusame.models.Item;
import com.lumbralessoftware.reusame.models.ItemRequest;
import com.lumbralessoftware.reusame.models.Location;
import com.lumbralessoftware.reusame.models.VotingResult;
import com.lumbralessoftware.reusame.utils.Constants;
import com.lumbralessoftware.reusame.utils.Utils;
import com.lumbralessoftware.reusame.views.CircleTransform;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DetailActivity extends AppCompatActivity implements RatingBar.OnRatingBarChangeListener, ItemRequestResponseListener, VoteResponseListener, View.OnClickListener, TextView.OnEditorActionListener {

    private Item mItem;
    private UserController mUserController;
    private EditText mEditText;
    private static ProgressDialog mProgressDialog;
    RatingBar mRatingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mProgressDialog = new ProgressDialog(DetailActivity.this);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setMessage(getString(R.string.activity_detail_sending));

        ControllersFactory.setItemRequestResponseListener(this);
        ControllersFactory.setsVoteResponseListener(this);
        mUserController = ControllersFactory.getUserController();

        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            return;
        }


        mItem = (Item) extras.getSerializable(Constants.DETAIL);


        TextView name = (TextView) findViewById(R.id.activity_detail_name);
        TextView category = (TextView) findViewById(R.id.activity_detail_category);
        TextView description = (TextView) findViewById(R.id.activity_detail_description);
        TextView dateTextView = (TextView) findViewById(R.id.activity_detail_date);
        TextView expireTextView = (TextView) findViewById(R.id.activity_detail_expire);
        TextView location = (TextView) findViewById(R.id.activity_detail_location);
        TextView user = (TextView) findViewById(R.id.activity_detail_user);


        mRatingBar = (RatingBar) findViewById(R.id.activity_detail_ratingBar);
        //LayerDrawable stars = (LayerDrawable) ratingBar.getProgressDrawable();
        //stars.getDrawable(2).setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP);

        mEditText = (EditText) findViewById(R.id.activity_detail_edittext);
        Button sentButton = (Button) findViewById(R.id.activity_detail_button);

        ImageView image = (ImageView) findViewById(R.id.activity_detail_icon);


        name.setText(mItem.getName());
        category.setText(mItem.getCategory());
        description.setText(mItem.getDescription());
        expireTextView.setText(stringBuilderWithDate(String.valueOf(mItem.getExpiresOn())));



        location.setText(mItem.getLocation().getLocation());

        user.setText(mItem.getOwner());

        if (mItem.getUserRating() != null) {
            mRatingBar.setRating(Float.valueOf(String.valueOf(mItem.getUserRating())));
        }
        dateTextView.setText(stringBuilderWithDate(mItem.getCreated()));

        sentButton.setOnClickListener(this);
        mRatingBar.setOnRatingBarChangeListener(this);

        Picasso.with(this).load(mItem.getImage()).transform(new CircleTransform()).into((ImageView) image);

    }

    public String lookForPoint(String stringInfo){

        String havePoint;
        if (stringInfo.contains(".")){
            havePoint = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
        } else {
            havePoint = "yyyy-MM-dd'T'HH:mm:ss'Z'";
        }
        return havePoint;
    }

    public String stringBuilderWithDate(String dateString) {

        SimpleDateFormat formatter = new SimpleDateFormat(lookForPoint(dateString));
        StringBuilder stringBuilderDate = new StringBuilder();

        try {
            Date date = formatter.parse(dateString);
            Calendar calendar = Utils.dateToCalendar(date);
            stringBuilderDate.append(String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)));
            stringBuilderDate.append("/");
            stringBuilderDate.append(String.valueOf(calendar.get(Calendar.MONTH)));
            stringBuilderDate.append("/");
            stringBuilderDate.append(String.valueOf(calendar.get(Calendar.YEAR)));
            stringBuilderDate.append(" at ");
            stringBuilderDate.append(String.valueOf(calendar.get(Calendar.HOUR_OF_DAY)));
            stringBuilderDate.append(":");
            stringBuilderDate.append(String.valueOf(calendar.get(Calendar.MINUTE)));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return stringBuilderDate.toString();
    }

    private void voteItem(Double rating) {
        mUserController.vote(mItem.getId(), rating);
    }

    private void requestItem(String message) {
        ItemRequest request = new ItemRequest();
        request.setMessage(message);
        mUserController.want(mItem.getId(), request);
    }

    @Override
    public void onSuccess(ItemRequest successResponse) {
        Toast.makeText(this, "OK", Toast.LENGTH_LONG).show();
        mProgressDialog.dismiss();
    }


    public void onSuccess(VotingResult successResponse) {
        Toast.makeText(this, successResponse.getRating().toString(), Toast.LENGTH_LONG).show();
        //mRatingBar.setRating(Float.valueOf(String.valueOf(mItem.getUserRating())));
        mProgressDialog.dismiss();
    }

    @Override
    public void onError(String errorResponse) {
        final Gson gson = new Gson();
        final ItemRequest itemRequest = gson.fromJson(errorResponse, ItemRequest.class);
        Toast.makeText(this, itemRequest.getError(), Toast.LENGTH_LONG).show();
        mProgressDialog.dismiss();
    }

    @Override
    public void onClick(View view) {

        sendMessage();

    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_SEND)) {
            sendMessage();
        }
        return false;
    }

    public void sendMessage() {
        if (mEditText.getText().equals("")) {
            requestItem(getString(R.string.activity_detail_hint_edittext));
        } else {
            requestItem(mEditText.getText().toString());
        }
        mEditText.setText("");
        mProgressDialog.show();
    }

    public void alertDialog(final float vote) {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.activity_detail_vote_title))
                .setMessage(getString(R.string.activity_detail_vote, String.valueOf(vote)))
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        mProgressDialog.show();
                        voteItem(Double.valueOf(vote));
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .show();

    }

    @Override
    public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
        alertDialog(v);
    }
}
