package com.lumbralessoftware.reusame.fragments;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.lumbralessoftware.reusame.R;
import com.lumbralessoftware.reusame.controller.AddItemController;
import com.lumbralessoftware.reusame.controller.ControllersFactory;
import com.lumbralessoftware.reusame.controller.SharedPreferenceController;
import com.lumbralessoftware.reusame.interfaces.AddItemResponseHandler;
import com.lumbralessoftware.reusame.interfaces.AddItemResponseListener;
import com.lumbralessoftware.reusame.interfaces.DateListener;
import com.lumbralessoftware.reusame.interfaces.ItemResponseListener;
import com.lumbralessoftware.reusame.models.Item;
import com.lumbralessoftware.reusame.models.Location;
import com.lumbralessoftware.reusame.utils.Utils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AddObjectFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AddObjectFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddObjectFragment extends Fragment implements View.OnClickListener, View.OnFocusChangeListener, AddItemResponseListener {

    public static final String SAVED_PHOTO_PATH = "mCapturedPhotoPath";
    public static final String SAVED_IMG_PATH = "mImagePath";
    private EditText mNameEditText;
    private EditText mDescripitionEditText;
    private EditText mDealEditText;
    private Button mAddCategoryButton;
    private Button mAddDate;
    private String[] mCategoriesList;
    private ImageView mImageView;
    private static int RESULT_LOAD_IMAGE = 147;
    private String mCapturedPhotoPath;
    String mImagePath;
    private static final String IMAGE_DIRECTORY_NAME = "reusame";
    public static final int MEDIA_TYPE_IMAGE = 1;

    public ProgressDialog mDialogLoading;

    public AddItemController mAddItemController;
    private DateListener mListener;

    public static AddObjectFragment newInstance() {
        AddObjectFragment fragment = new AddObjectFragment();
        Bundle args = new Bundle();
        return fragment;
    }

    public AddObjectFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            mCapturedPhotoPath = savedInstanceState.getString(SAVED_PHOTO_PATH);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_object, container, false);

        mDialogLoading = new ProgressDialog(getActivity());

        mListener = new DateListener() {
            @Override
            public void getDate(int year, int month, int day) {
                String currentDate = new StringBuilder()
                        .append(year)
                        .append("-")
                        .append(month + 1)
                        .append("-")
                        .append(day)
                        .append(" 00:00").toString();
                mAddDate.setText(currentDate);
                mAddDate.setBackgroundColor(getResources().getColor(android.R.color.white));
                mAddDate.setTextColor(getResources().getColor(android.R.color.black));
            }
        };

        mNameEditText = (EditText) view.findViewById(R.id.fragment_add_object_name_edittext);
        mNameEditText.setOnFocusChangeListener(this);
        mDescripitionEditText = (EditText) view.findViewById(R.id.fragment_add_object_description_edittext);
        mDescripitionEditText.setOnFocusChangeListener(this);

        mDealEditText = (EditText) view.findViewById(R.id.fragment_add_object_conditions);

        mImageView = (ImageView) view.findViewById(R.id.fragment_add_object_add_photo_imgView);
        mImageView.setOnClickListener(this);

        mAddCategoryButton = (Button) view.findViewById(R.id.fragment_add_object_add_item_category);
        mAddCategoryButton.setOnClickListener(this);

        mAddDate = (Button) view.findViewById(R.id.fragment_add_object_add_item_experies_on);
        mAddDate.setOnClickListener(this);

        mCategoriesList = getResources().getStringArray(R.array.fragment_add_object_category);

        if (savedInstanceState != null) {
            mImagePath = savedInstanceState.getString(SAVED_IMG_PATH);
            if (mImagePath != null) {
                mImageView.setImageBitmap(BitmapFactory.decodeFile(mImagePath));
            }
        }

        Button newItem = (Button) view.findViewById(R.id.fragment_add_object_add_item_button);
        newItem.setOnClickListener(this);

        return view;
    }

    public void sendArticle() {

        if (SharedPreferenceController.getInstance().getAccessToken().matches("")) {
            Toast.makeText(
                    getActivity(),
                    getString(R.string.not_logged_in),
                    Toast.LENGTH_SHORT
            ).show();
            return;
        }

        Item item = new Item();
        if (mNameEditText.getText().toString().equals("")) {
            mNameEditText.setError(getString(R.string.add_item_empty_field));
        } else if (mDescripitionEditText.getText().toString().equals("")) {
            mDescripitionEditText.setError(getString(R.string.add_item_empty_field));
        } else if (mAddDate.getText().equals(getResources().getString(R.string.fragment_add_object_expires_on))) {
            Toast.makeText(
                    getActivity(),
                    getString(R.string.add_item_no_date),
                    Toast.LENGTH_SHORT
            ).show();
        } else if (mAddCategoryButton.getText().equals(getResources().getString(R.string.fragment_add_object_category_chose))) {
            Toast.makeText(
                    getActivity(),
                    getString(R.string.add_item_no_category),
                    Toast.LENGTH_SHORT
            ).show();
        } else if (mImagePath == null) {
            Toast.makeText(
                    getActivity(),
                    getString(R.string.add_item_no_image),
                    Toast.LENGTH_SHORT
            ).show();
        } else {
            item.setName(mNameEditText.getText().toString());

            item.setDescription(mDescripitionEditText.getText().toString());
            item.setCategory(mAddCategoryButton.getText().toString());
            item.setExpiresOn(mAddDate.getText().toString());
            item.setImage(mImagePath);

            if (mDealEditText.getText().toString().matches("")) {
                item.setDeal(mDealEditText.getHint().toString());
            } else {
                item.setDeal(mDealEditText.getText().toString());
            }
            LatLng latLng = Utils.getLastLocation(getActivity());
            Location location = new Location();
            location.setLatPosition(String.valueOf(latLng.latitude));
            location.setLongPosition(String.valueOf(latLng.longitude));
            location.setLocation("");
            item.setLocation(location);
            showProgress();
            sendItemtoWebService(item);
        }


    }

    public void showAlertView() {

        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        alert.setTitle(getString(R.string.add_item_succesful_title));
        alert.setMessage("");
        alert.setCancelable(false);
        alert.setPositiveButton(getString(android.R.string.ok),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mNameEditText.setText("");
                        mDescripitionEditText.setText("");
                        mDealEditText.setText("");
                        mAddDate.setText(getString(R.string.fragment_add_object_expires_on));
                        mAddDate.setTextColor(getResources().getColor(android.R.color.white));
                        mAddDate.setBackgroundColor(getResources().getColor(android.R.color.black));
                        mAddCategoryButton.setText(getString(R.string.fragment_add_object_category_chose));
                        mAddCategoryButton.setTextColor(getResources().getColor(android.R.color.white));
                        mAddCategoryButton.setBackgroundColor(getResources().getColor(android.R.color.black));
                        mImageView.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.no_image));

                        dialog.cancel();
                    }
                }
        );
        AlertDialog alert11 = alert.create();
        alert11.show();
        TextView messageText = (TextView) alert11.findViewById(android.R.id.message);
        messageText.setGravity(Gravity.CENTER);
        messageText.setText(R.string.add_item_succesful);

    }

    /**
     * Get the real path when user selected a picture from the gallery
     *
     * @param uri
     * @return
     */
    public String getPath(Uri uri) {
        String path = "";
        Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            String document_id = cursor.getString(0);
            document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
            cursor.close();

            cursor = getActivity().getContentResolver().query(
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
            cursor.moveToFirst();
            path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            cursor.close();
        }

        return path;
    }


    public void onButtonPressed(Uri uri) {

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fragment_add_object_add_photo_imgView:
                openImageIntent();
                break;
            case R.id.fragment_add_object_add_item_category:
                setCategory();
                break;
            case R.id.fragment_add_object_add_item_button:
                sendArticle();
                break;
            case R.id.fragment_add_object_add_item_experies_on:
                showDatePickerDialog(v);
                break;
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        switch (v.getId()) {
            case R.id.fragment_add_object_description_edittext:
                mDescripitionEditText.setError(null);
                break;
            case R.id.fragment_add_object_name_edittext:
                mNameEditText.setError(null);
                break;
        }
    }

    @Override
    public void onSuccess(Item successResponse) {
        hideProgress();
        showAlertView();
    }

    @Override
    public void onError(String errorResponse) {
        hideProgress();
        Toast.makeText(getActivity(), R.string.add_item_error, Toast.LENGTH_LONG).show();
    }

    public interface OnFragmentInteractionListener {
    }

    public void setCategory() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.fragment_add_object_category_title));
        builder.setItems(mCategoriesList, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                mAddCategoryButton.setText(mCategoriesList[item]);
                mAddCategoryButton.setBackgroundColor(getResources().getColor(android.R.color.white));
                mAddCategoryButton.setTextColor(getResources().getColor(android.R.color.black));
                dialog.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void openImageIntent() {

        Uri outputFileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
        mCapturedPhotoPath = outputFileUri.getPath();

        // Camera.
        final List<Intent> cameraIntents = new ArrayList<Intent>();
        final Intent captureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
        captureIntent.putExtra("return-data", true);
        final PackageManager packageManager = getActivity().getPackageManager();
        final List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);
        for (ResolveInfo res : listCam) {
            final String packageName = res.activityInfo.packageName;
            final Intent intent = new Intent(captureIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(packageName);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            intent.putExtra("return-data", true);

            cameraIntents.add(intent);
        }

        // Filesystem.
        final Intent galleryIntent = new Intent();
        galleryIntent.setType("image/*");
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);

        // Chooser of filesystem options.
        final Intent chooserIntent = Intent.createChooser(galleryIntent, getString(R.string.add_item_img_source));

        // Add the camera options.
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, cameraIntents.toArray(new Parcelable[cameraIntents.size()]));

        startActivityForResult(chooserIntent, RESULT_LOAD_IMAGE);
    }

    /**
     * Creating file uri to store image/video
     */
    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    /*
     * returning image / video
     */
    private static File getOutputMediaFile(int type) {

        // External sdcard location
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                IMAGE_DIRECTORY_NAME);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(IMAGE_DIRECTORY_NAME, "Oops! Failed create "
                        + IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".jpg");
        } else {
            return null;
        }

        return mediaFile;
    }

    public void sendItemtoWebService(Item item) {
        if (Utils.isOnline(getActivity())) {
            ControllersFactory.setAddItemResponseListener(this);
            mAddItemController = ControllersFactory.getAddItemController();
            mAddItemController.request(item);
        } else {
            Toast.makeText(getActivity(), getString(R.string.no_connection), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE) {

            if (resultCode == getActivity().RESULT_OK) {
                final boolean isCamera;

                // By default assume user took a picture
                mImagePath = mCapturedPhotoPath;

                if (data != null) {
                    if (data.getData() != null) {
                        // If there is a uri in intent's data, use it (image gallery)
                        mImagePath = getPath(data.getData());
                    }
                }

                mImageView.setImageBitmap(BitmapFactory.decodeFile(mImagePath));
            } else if (resultCode == getActivity().RESULT_CANCELED) {
                // user cancelled Image capture
                Toast.makeText(
                        getActivity(),
                        getString(R.string.add_item_no_image),
                        Toast.LENGTH_SHORT
                ).show();
            } else {
                // failed to capture image
                Toast.makeText(
                        getActivity(),
                        getString(R.string.add_item_error_image),
                        Toast.LENGTH_SHORT
                ).show();
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString(SAVED_PHOTO_PATH, mCapturedPhotoPath);
        savedInstanceState.putString(SAVED_IMG_PATH, mImagePath);
    }

    public void showProgress() {
        mDialogLoading.show();
        mDialogLoading.setMessage(getString(R.string.add_item_loading));
        mDialogLoading.setCancelable(false);
    }

    public void hideProgress() {
        mDialogLoading.dismiss();
    }

    public void showDatePickerDialog(View v) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            DatePickerFragment newFragment = new DatePickerFragment();
            newFragment.setListener(mListener);
            newFragment.show(getActivity().getFragmentManager(), "datePicker");
        } else{
            apiLevelTen();
        }
    }

    public void apiLevelTen(){
        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.dialog_custom_ten);
        dialog.setTitle(R.string.fragment_add_object_expires_on_title);

        // set the custom dialog components - text, image and button
        final EditText year = (EditText) dialog.findViewById(R.id.dialog_custom_ten_edittext_year);
        final EditText month = (EditText) dialog.findViewById(R.id.dialog_custom_ten_edittext_month);
        final EditText day = (EditText) dialog.findViewById(R.id.dialog_custom_ten_edittext_day);


        Button dialogButton = (Button) dialog.findViewById(R.id.dialog_custom_ten_button);
        // if button is clicked, close the custom dialog
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                checkDate(dialog, year, month, day);
            }
        });

        dialog.show();
    }

    public void checkDate(Dialog dialog, EditText year, EditText month, EditText day) {
        if (year.getText().toString().equals("")) {
            year.setError(getString(R.string.add_item_empty_field));
        } else if (month.getText().toString().equals("")) {
            month.setError(getString(R.string.add_item_empty_field));
        } else if (day.getText().toString().equals("")) {
            day.setError(getString(R.string.add_item_empty_field));
        } else {
            String currentDate = new StringBuilder()
                    .append(year.getText().toString())
                    .append("-")
                    .append(month.getText().toString())
                    .append("-")
                    .append(day.getText().toString())
                    .append(" 00:00").toString();
            mAddDate.setText(year.getText());
            dialog.dismiss();
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        DateListener mListener;

        public DatePickerFragment() {

        }

        public void setListener(DateListener mListener) {
            this.mListener = mListener;
        }


        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);


            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
            mListener.getDate(year, month, day);

        }
    }

}
