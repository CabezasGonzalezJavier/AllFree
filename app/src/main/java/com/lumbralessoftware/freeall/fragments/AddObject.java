package com.lumbralessoftware.freeall.fragments;

import android.app.Activity;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
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

    private static Uri mOutputFileUri;
    private EditText mNameEditText;
    private EditText mDescripitionEditText;
    private Button mAddPhotoButton;
    private ImageView mImageView;
    private static int RESULT_LOAD_IMAGE = 147;
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


    // TODO: Rename method, update argument and hook method into UI event
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

        mOutputFileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);

        // Camera.
        final List<Intent> cameraIntents = new ArrayList<Intent>();
        final Intent captureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mOutputFileUri);
        captureIntent.putExtra("return-data", true);
        final PackageManager packageManager = getActivity().getPackageManager();
        final List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);
        for (ResolveInfo res : listCam) {
            final String packageName = res.activityInfo.packageName;
            final Intent intent = new Intent(captureIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(packageName);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, mOutputFileUri);
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
                if (data == null) {
                    isCamera = true;
                } else {
                    final String action = data.getAction();

                    if (action == null) {
                        isCamera = false;
                    } else {
                        isCamera = action.equals(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    }
                }

                if (isCamera) {
                    mImagePath = mOutputFileUri.getPath();
                } else {
                    mImagePath = data == null ? null : getPath(data.getData());
                }
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
}
