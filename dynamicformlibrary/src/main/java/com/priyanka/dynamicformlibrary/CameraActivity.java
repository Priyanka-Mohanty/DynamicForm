package com.priyanka.dynamicformlibrary;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.priyanka.dynamicformlibrary.controllers.FloatingEditTextCameraController;
import com.priyanka.dynamicformlibrary.controllers.FloatingEditTextDialogRadioController;
import com.priyanka.dynamicformlibrary.utils.ActionPerform;
import com.priyanka.dynamicformlibrary.utils.Utility;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Priyankam on 11-05-2017.
 */
public class CameraActivity extends AppCompatActivity {

    public static Map<String, String> mapCameraRemark = new HashMap<>();
    public static Multimap<String, String> multiMapFileName = ArrayListMultimap.create();
    static Context context;
    static LinearLayout layoutRemark;
    private static Button btnAttach;
    ImageAdapter imageAdapter;
    List<String> allImagesPath = new ArrayList<>();
    GridView gridview;
    String editTextId;
    String editTextHintName;
    String optionFlag = "";
    String remarkFlag = "";
    //declare boolean
    boolean clicked = false;
    String savedRemarkImages = "";
    String savedRemark = "";
    String savedImages = "";
    EditText editRemark;
    TextInputLayout inputLayoutRemark;
    private final TextWatcher mTextEditorWatcher = new TextWatcher() {
        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            try {
                editRemarkCharValidation(s);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            try {
                editRemarkCharValidation(s);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        public void afterTextChanged(Editable s) {
            try {
                editRemarkEditableValidation(s);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        private void editRemarkEditableValidation(Editable s) {
            try {
                if ((gridview.getChildCount() != 0) && (!gridview.getAdapter().isEmpty())) {
                    // if Gridview is not empty
                    //if gridview is not empty and editRemark is visible
                    if (editRemark.getVisibility() == View.VISIBLE) {

                        if (TextUtils.isEmpty(s) && s.length() < 3) {

                            // Boolean b= (s.toString() == null);
                            // Boolean b1= (s.toString().trim().equals(""));
                            // Log.i("tag","tag"+b + " b1=" +b1);

                            if ((s.toString() != null) && (s.toString().trim().equals(""))) {
                                inputLayoutRemark.setError(getResources().getString(R.string.remark_error_msg));
                                requestFocus(inputLayoutRemark);
                                buttonDeactivate();
                            }

                        } else {
                            inputLayoutRemark.setError(null);
                            inputLayoutRemark.setErrorEnabled(false);
                            buttonActivate();
                        }

                    } else {
                        //if gridview is not empty and editRemark is not visible
                        buttonActivate();
                    }

                } else {
                    // if Gridview is empty
                    if (allImagesPath.isEmpty()) {
                        buttonDeactivate();
                    } else {
                        buttonActivate();
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        private void editRemarkCharValidation(CharSequence s) {
            try {

                if ((gridview.getChildCount() != 0) && (!gridview.getAdapter().isEmpty())) {
                    // if Gridview is not empty
                    //if gridview is not empty and editRemark is visible
                    if (editRemark.getVisibility() == View.VISIBLE) {
                        if (TextUtils.isEmpty(s) && s.length() < 3) {
                            if ((s.toString() != null) && (s.toString().trim().equals(""))) {
                                inputLayoutRemark.setError(getResources().getString(R.string.remark_error_msg));
                                requestFocus(inputLayoutRemark);
                                buttonDeactivate();
                            }

                        } else {
                            inputLayoutRemark.setError(null);
                            inputLayoutRemark.setErrorEnabled(false);
                            buttonActivate();
                        }

                    } else {
                        //if gridview is not empty and editRemark is not visible
                        buttonActivate();
                    }

                } else {
                    // if Gridview is empty
                    if (allImagesPath.isEmpty()) {
                        buttonDeactivate();
                    } else {
                        buttonActivate();
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
    String viewId = "";
    String refNo = "";
    String imagePath;
    private String editInputType = "";
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private Button btnSelect;
    private String userChoosenTask;

    @TargetApi(Build.VERSION_CODES.M)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public static void buttonActivate() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            btnAttach.setBackground(ContextCompat.getDrawable(context.getApplicationContext(), R.drawable.btn_positive));
            btnAttach.setTextColor(ContextCompat.getColor(context.getApplicationContext(), R.color.colorWhite));
        } else {
            btnAttach.setBackground(context.getResources().getDrawable(R.drawable.btn_positive));
            btnAttach.setTextColor(context.getResources().getColor(R.color.colorWhite));
        }

        btnAttach.setClickable(true);
        btnAttach.setActivated(true);
    }

    @TargetApi(Build.VERSION_CODES.M)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public static void buttonDeactivate() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            btnAttach.setBackground(ContextCompat.getDrawable(context.getApplicationContext(), R.drawable.btn_negative));
            btnAttach.setTextColor(ContextCompat.getColor(context.getApplicationContext(), R.color.colorPrimary));
        } else {
            btnAttach.setBackground(context.getResources().getDrawable(R.drawable.btn_negative));
            btnAttach.setTextColor(context.getResources().getColor(R.color.colorPrimary));
        }

        btnAttach.setClickable(false);
        btnAttach.setActivated(false);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.form_camera_activity);
        overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
        context = CameraActivity.this;
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("Photos");
        setSupportActionBar(mToolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        Intent i = getIntent();
        editTextId = i.getStringExtra(ActionPerform.STRING_EDIT_TEXT_ID);
        editTextHintName = i.getStringExtra(ActionPerform.STRING_EDIT_TEXT_HINT_NAME);
        optionFlag = i.getStringExtra(ActionPerform.OPTION_FLAG);
        remarkFlag = i.getStringExtra(ActionPerform.REMARK_FLAG);
        viewId = i.getStringExtra(ActionPerform.VIEW_ID);
        refNo = i.getStringExtra(ActionPerform.STRING_REF_NO);
        savedRemarkImages = i.getStringExtra(ActionPerform.STRING_STORE_VALUE);
        editInputType = i.getStringExtra(ActionPerform.EDIT_INPUT_TYPE);

        Log.d("editTextId ", "editTextId = " + editTextId);

        if (!savedRemarkImages.isEmpty()) {
            allImagesPath.clear();
            String[] keyValuePairs = savedRemarkImages.split("\\|(?![^\\[]*\\])"); //split the string to creat key-value pairs,excepts value in betwwen array i.e[]

            if (optionFlag.equals(ActionPerform.OPTION_RADIO)) {
                //value=  "option|remark|images"
                savedRemark = keyValuePairs[1].trim();
                if (keyValuePairs.length > 1) {
                    savedImages = keyValuePairs[2].trim();
                    if (!savedImages.isEmpty()) {
                        callImageFilter(savedImages, refNo, viewId);
                    }
                }

            } else if (optionFlag.equals(ActionPerform.OPTION_CAMERA)) {
                //value=  "remark|images"
                savedRemark = keyValuePairs[0].trim();
                if (keyValuePairs.length > 1) {
                    savedImages = keyValuePairs[1].trim();
                    if (!savedImages.isEmpty()) {
                        callImageFilter(savedImages, refNo, viewId);
                    }
                }
            }

        } else if (!multiMapFileName.isEmpty()) {

            System.out.println("multiMapFileName = " + multiMapFileName);
            Set<String> keysFileName = multiMapFileName.keySet();
            // iterate through the key set and display key and values
            for (String key : keysFileName) {
                if (key.equals(editTextId)) {
                    allImagesPath.clear();
                    allImagesPath.addAll(multiMapFileName.get(key));
                    System.out.println("Key multiMapFileName= " + key);
                    System.out.println("Values multiMapFileName= " + multiMapFileName.get(key));
                }
            }
        }


        btnSelect = (Button) findViewById(R.id.btnSelectPhoto);
        btnAttach = (Button) findViewById(R.id.btnAttach);

        layoutRemark = (LinearLayout) findViewById(R.id.layout_remark);
        editRemark = (EditText) findViewById(R.id.edit_remark);
        inputLayoutRemark = (TextInputLayout) findViewById(R.id.input_layout_remark);
        gridview = (GridView) findViewById(R.id.gridview);

        checkEditRemarkInputType(editRemark, editInputType);

        editRemark.addTextChangedListener(mTextEditorWatcher);


        if (remarkFlag.equals(ActionPerform.REMARK_REQUIRED)) {
            editRemark.setVisibility(View.VISIBLE);
        } else if (remarkFlag.equals(ActionPerform.REMARK_NOT_REQUIRED)) {
            editRemark.setVisibility(View.GONE);
        }

        imageAdapter = new ImageAdapter(this, editTextId, allImagesPath);
        imageAdapter.notifyDataSetChanged();
        gridview.setAdapter(imageAdapter);


        if (!savedRemark.isEmpty()) {
            if (editRemark.getVisibility() == View.VISIBLE) {
                // Its visible
                if (!savedRemark.equals(ActionPerform.NOT_APPLICABLE)) {
                    editRemark.setText(savedRemark);
                    buttonActivate();
                } else {
                    editRemark.setText("");
                    buttonDeactivate();
                }
            }
        } else {
            buttonDeactivate();
        }


        btnSelect.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
        btnAttach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //change boolean value
                clicked = true;

                System.out.println("Fetching Keys and corresponding [Multiple] Values n");
                // get all the set of keys
                Set<String> keys = multiMapFileName.keySet();
                // iterate through the key set and display key and values
                for (String key : keys) {
                    System.out.println("Key = " + key);
                    System.out.println("Values = " + multiMapFileName.get(key));
                }
                String Remark = ActionPerform.NOT_APPLICABLE;
                if (editRemark.getVisibility() == View.VISIBLE) {
                    // Its visible
                    Remark = editRemark.getText().toString().trim();
                } else {
                    // Either gone or invisible
                    Remark = ActionPerform.NOT_APPLICABLE;
                }


                mapCameraRemark.put(editTextId, Remark);

                imageAdapter.notifyDataSetChanged();
                gridview.setAdapter(imageAdapter);

                onBackPressed();
                if (optionFlag.equals(ActionPerform.OPTION_CAMERA)) {
                    FloatingEditTextCameraController floatingEditTextCameraController = new FloatingEditTextCameraController(getApplicationContext(), null, null);
                    floatingEditTextCameraController.setValueCamera(editTextId, editTextHintName, viewId);
                } else {

                }
                finish();
            }
        });


    }

    private void checkEditRemarkInputType(EditText editRemark, String editInputType) {

        if (editInputType.equals(ActionPerform.EDIT_TEXT_NUMBER)) {

            editRemark.setInputType(InputType.TYPE_CLASS_NUMBER);

        } else if (editInputType.equals(ActionPerform.EDIT_TEXT_TIME)) {

            editRemark.setInputType(InputType.TYPE_CLASS_DATETIME | InputType.TYPE_DATETIME_VARIATION_TIME | InputType.TYPE_DATETIME_VARIATION_NORMAL);

        } else if (editInputType.equals(ActionPerform.EDIT_TEXT_DECIMAL)) {

            editRemark.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

        } else if (editInputType.equals(ActionPerform.EDIT_TEXT_EMAIL)) {

            editRemark.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);

        } else if (editInputType.equals(ActionPerform.EDIT_TEXT_DEFAULT)) {

            editRemark.setInputType(InputType.TYPE_CLASS_TEXT);

        } else {

            editRemark.setInputType(InputType.TYPE_CLASS_TEXT);
        }

    }

    private void callImageFilter(String savedImages, String refNo, String viewId) {
        savedImages = savedImages.substring(1, savedImages.length() - 1);  //remove curly brackets
        String[] keyValuePairs1 = savedImages.split(","); //split the string to creat key-value pairs,excepts value in betwwen array i.e[]
        fileFilter(Utility.FILE_PATH + "/", refNo + "_" + viewId + "_");
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    @SuppressLint("NewApi")
    @Override
    protected void onResume() {
        super.onResume();

        try {
            Log.d("", "" + multiMapFileName.size());
            multiMapFileName.removeAll(editTextId);
            fileFilter(Utility.FILE_PATH + "/", refNo + "_" + viewId + "_");
            if ((gridview.getChildCount() != 0) && (!gridview.getAdapter().isEmpty())) {
                // if Gridview is not empty

                //if gridview is not empty and editRemark is visible
                if (editRemark.getVisibility() == View.VISIBLE) {

                    if (editRemark.getText().toString().isEmpty()) {
                        buttonDeactivate();
                    } else {
                        if (editRemark.getText().toString().trim().length() < 3) {

                            if ((editRemark.getText().toString().trim() != null) && (editRemark.getText().toString().trim().equals(""))) {
                                buttonDeactivate();
                            }


                        } else {
                            buttonActivate();
                        }
                    }
                } else {
                    //if gridview is not empty and editRemark is not visible
                    buttonActivate();
                }
            } else {
                // if Gridview is empty
                if (allImagesPath.isEmpty()) {
                    buttonDeactivate();
                } else {
                    //if gridview is not empty and editRemark is visible
                    if (editRemark.getVisibility() == View.VISIBLE) {
                        if (editRemark.getText().toString().isEmpty()) {
                            buttonDeactivate();
                        } else {
                            if (editRemark.getText().toString().trim().length() < 3) {
                                if ((editRemark.getText().toString().trim() != null) && (editRemark.getText().toString().trim().equals(""))) {
                                    buttonDeactivate();
                                }
                            } else {
                                buttonActivate();
                            }
                        }
                    } else {
                        //if gridview is not empty and editRemark is not visible
                        buttonActivate();
                    }
                    // buttonActivate();
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (userChoosenTask.equals("Take Photo"))
                        cameraIntent();
                    else if (userChoosenTask.equals("Choose from Library"))
                        galleryIntent();
                } else {
                    //code for deny
                }
                break;
        }
    }

    private void selectImage() {
    /*    final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};*/
        //Choose from Library" option disable.
        final CharSequence[] items = {"Take Photo", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(CameraActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {


                if (items[item].equals("Take Photo")) {
                    boolean result = Utility.checkPermission(CameraActivity.this);
                    userChoosenTask = "Take Photo";
                    if (result)
                        cameraIntent();

                } else if (items[item].equals("Choose from Library")) {
                    boolean result = Utility.checkPermission(CameraActivity.this);
                    userChoosenTask = "Choose from Library";
                    if (result)
                        galleryIntent();

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");// Show only images, no videos or anything else
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true); // for selecting multiple image
        intent.setAction(Intent.ACTION_GET_CONTENT);
        // Always show the chooser (if there are multiple options available)
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
    }

    private void cameraIntent() {
        try {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, REQUEST_CAMERA);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_FILE) {
                onSelectFromGalleryResult(data);
            } else if (requestCode == REQUEST_CAMERA) {
                onCaptureImageResult(data);
            }
        }

    }

    private void onCaptureImageResult(Intent data) {

        String destination = null;
        destination = getNewImageDestination(null, refNo, viewId, data); // getting new image path after compressed the image

        multiMapFileName.put(editTextId, destination);

        setListAdapter(multiMapFileName);


    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void onSelectFromGalleryResult(Intent data) {
        String destination = null;
        Uri uri = null;
        String[] filePathColumn = {MediaStore.Images.Media.DATA};

        //When User select the image from gallery

        if (data.getData() != null) {
            //For selecting single image from gallery
            Uri mSingleUri = data.getData();
            uri = mSingleUri;

            // Get the cursor
            Cursor cursor = getContentResolver().query(mSingleUri, filePathColumn, null, null, null);
            // Move to first row
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            imagePath = cursor.getString(columnIndex); //getting image path

            destination = getNewImageDestination(mSingleUri, refNo, viewId, data); // getting new image path after compressed the image

            multiMapFileName.put(editTextId, destination);  // Storing new image in multiMapFileName

            cursor.close();

        } else {
            //For selecting multiple image from gallery
            if (data.getClipData() != null) {
                ClipData mClipData = data.getClipData();

                ArrayList<Uri> mArrayUri = new ArrayList<Uri>();
                for (int i = 0; i < mClipData.getItemCount(); i++) {

                    ClipData.Item item = mClipData.getItemAt(i);
                    Uri mMultipleUri = item.getUri();
                    uri = mMultipleUri;
                    mArrayUri.add(mMultipleUri);
                    // Get the cursor
                    Cursor cursor = getContentResolver().query(uri, filePathColumn, null, null, null);
                    // Move to first row
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);

                    imagePath = cursor.getString(columnIndex); //getting image path

                    destination = getNewImageDestination(mMultipleUri, refNo, viewId, data); // getting new image path after compressed the image

                    multiMapFileName.put(editTextId, destination); // Storing new image in multiMapFileName

                    cursor.close();

                }
                Log.v("LOG_TAG", "Selected Images" + mArrayUri.size());
            }
        }


        //=======================================

        setListAdapter(multiMapFileName);


        ///---------------------------------------------------------------------


    }

    private void setListAdapter(Multimap<String, String> multiMapFileName) {

        //getting all image from MultimapFileName map.

        Set<String> keysFileName = multiMapFileName.keySet();

        // iterate through the key set and display key and values
        for (String key : keysFileName) {
            if (key.equals(editTextId)) {

                System.out.println("Key multiMapFileName= " + key);
                System.out.println("Values multiMapFileName= " + multiMapFileName.get(key));
                if (key.equals(editTextId)) {
                    allImagesPath.clear();
                    allImagesPath.addAll(multiMapFileName.get(key));
                    System.out.println("Key multiMapFileName= " + key);
                    System.out.println("Values multiMapFileName= " + multiMapFileName.get(key));
                }
            }
        }
        System.out.println("allImages Path = " + allImagesPath);
        imageAdapter.notifyDataSetChanged();
        gridview.setAdapter(imageAdapter);

    }


    private String getNewImageDestination(Uri uri, String refNo, String viewId, Intent intentData) {

        File destination = null;
        try {

            Bitmap originalImage = null;

            if ((uri != null)) {

                //if uri is not null ,than image is coming from Gallery .
                originalImage = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);

            } else if (uri == null) {

                //if uri is null ,than image is coming from Camera.
                originalImage = (Bitmap) intentData.getExtras().get("data");

            } else {

                Log.e("error", "error");

            }


            //Compress the Original Image  -----------------------
            ByteArrayOutputStream imageCompressed = new ByteArrayOutputStream();
            originalImage.compress(Bitmap.CompressFormat.JPEG, 90, imageCompressed);
            //storing the new Compressed Image in local file-----------------------
            String path = Utility.FILE_PATH;
            File file = new File(path);
            if (!file.exists()) {
                file.mkdirs();
            }

            destination = new File(file, refNo + "_" + viewId + "_" + System.currentTimeMillis() + ".jpg");

            FileOutputStream fo;
            try {
                // destination.createNewFile();
                fo = new FileOutputStream(destination);
                fo.write(imageCompressed.toByteArray());
                fo.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return String.valueOf(destination);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    private void fileFilter(String filePath, String fileHeader) {
        try {
            allImagesPath.clear();
            File path = new File(Utility.FILE_PATH);
            if (path.exists()) {
                String[] fileNames = path.list();
                try {
                    if (fileNames != null) {
                        for (int j = 0; j < fileNames.length; j++) {
                            if (fileNames[j].startsWith(fileHeader)) {
                                File file = new File(filePath + fileNames[j]);
                                allImagesPath.add(file.getAbsolutePath());
                                multiMapFileName.put(editTextId, file.getAbsolutePath());
                            }
                        }
                    } else {
                        Log.e("fileNames is null", "fileNames is null");
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Log.i("no path exist", "no path exist");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void onSuperBackPressed() {
        super.onBackPressed();
        ((Activity) context).overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
        finish();
    }

    @Override
    public void onBackPressed() {
        if (!btnAttach.isActivated()) {
            //clear the previous activity's /form images
            clearData();
            try {
                if (FloatingEditTextDialogRadioController.selectionDialog != null && FloatingEditTextDialogRadioController.selectionDialog.isShowing()) {
                    FloatingEditTextDialogRadioController.selectionDialog.dismiss();
                }
                showDialogOption();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            //id Attach button is not clicked
            if (!clicked) {

                try {
                    if (FloatingEditTextDialogRadioController.selectionDialog != null && FloatingEditTextDialogRadioController.selectionDialog.isShowing()) {
                        FloatingEditTextDialogRadioController.selectionDialog.dismiss();
                    }
                    showDialogOption();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (savedRemarkImages.isEmpty()) {
                    clearData();
                }

            }
            // super.onBackPressed();
            //overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
            //finish();
        }
    }

    private void showDialogOption() {
        try {
            AlertDialog alertDialog = new AlertDialog.Builder(context).create();
            alertDialog.setTitle(context.getApplicationContext().getString(R.string.alert_dialog));
            alertDialog.setMessage(context.getApplicationContext().getString(R.string.alert_dialog_option));
            alertDialog.setCancelable(false);
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, context.getApplicationContext().getString(R.string.alert_dialog_ok),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            CameraActivity.this.onSuperBackPressed();
                        }
                    });
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, context.getApplicationContext().getString(R.string.alert_dialog_cancel),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void clearData() {
        function_delete();
        allImagesPath.clear();
        multiMapFileName.removeAll(editTextId);
    }

    public void function_delete() {
        if (!allImagesPath.isEmpty()) {
            for (int i = 0; i < allImagesPath.size(); i++) {
                String path = allImagesPath.get(i);
                File file = new File(path);
                if (file.exists()) {
                    boolean deleted = file.delete();
                }
            }
        }

        //Toast.makeText(context, "Image Deleted !", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}


