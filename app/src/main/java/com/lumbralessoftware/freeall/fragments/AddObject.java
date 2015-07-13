package com.lumbralessoftware.freeall.fragments;

import android.app.Activity;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.lumbralessoftware.freeall.R;
import com.lumbralessoftware.freeall.models.Item;
import com.lumbralessoftware.freeall.models.Location;
import com.lumbralessoftware.freeall.utils.Utils;
import com.lumbralessoftware.freeall.webservice.Client;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AddObject.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AddObject#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddObject extends Fragment implements View.OnClickListener {

    public static final String SAVED_PHOTO_PATH = "mCapturedPhotoPath";
    public static final String SAVED_IMG_PATH = "mImagePath";
    private EditText mNameEditText;
    private EditText mDescripitionEditText;
    private Button mAddPhotoButton;
    private ImageView mImageView;
    private static int RESULT_LOAD_IMAGE = 147;
    private String mCapturedPhotoPath;
    String mImagePath;
    private static final String IMAGE_DIRECTORY_NAME = "reusame";
    public static final int MEDIA_TYPE_IMAGE = 1;

    public static AddObject newInstance() {
        AddObject fragment = new AddObject();
        Bundle args = new Bundle();
        return fragment;
    }

    public AddObject() {
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
        mNameEditText = (EditText) view.findViewById(R.id.fragment_add_object_name_edittext);
        mDescripitionEditText = (EditText) view.findViewById(R.id.fragment_add_object_description_edittext);

        mAddPhotoButton = (Button) view.findViewById(R.id.fragment_add_object_add_photo_button);
        mAddPhotoButton.setOnClickListener(this);

        mImageView = (ImageView) view.findViewById(R.id.fragment_add_object_add_photo_imgView);

        if (savedInstanceState != null) {
            mImagePath = savedInstanceState.getString(SAVED_IMG_PATH);
            if (mImagePath != null) {
                mImageView.setImageBitmap(BitmapFactory.decodeFile(mImagePath));
            }
        }

        Button newItem = (Button) view.findViewById(R.id.fragment_add_object_add_item_button);
        newItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Item item = new Item();
                item.setName("Test name");
                item.setDescription("test desc");
                item.setCategory("Other");

                if (mImagePath != null) {
                    item.setImage(mImagePath);
                    LatLng latLng = Utils.getLastLocation(getActivity());
                    Location location = new Location();
                    location.setLatPosition(String.valueOf(latLng.latitude));
                    location.setLongPosition(String.valueOf(latLng.longitude));
                    location.setLocation("");
                    item.setLocation(location);
                    Client.createItem(item);
                } else {
                    Toast.makeText(
                            getActivity(),
                            getString(R.string.add_item_no_image),
                            Toast.LENGTH_SHORT
                    ).show();
                }
            }
        });

        return view;
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
        openImageIntent();
    }


    public interface OnFragmentInteractionListener {
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
}
