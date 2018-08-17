package com.example.priyankam.dynamicform;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.priyankam.dynamicform.appcrash.ExceptionHandler;
import com.example.priyankam.dynamicform.database.MainDatabase;
import com.priyanka.dynamicformlibrary.FormController;
import com.priyanka.dynamicformlibrary.FormFragment;
import com.priyanka.dynamicformlibrary.controllers.FloatingEditTextCameraController;
import com.priyanka.dynamicformlibrary.controllers.FloatingEditTextController;
import com.priyanka.dynamicformlibrary.controllers.FloatingEditTextDialogRadioController;
import com.priyanka.dynamicformlibrary.controllers.FormSectionController;
import com.priyanka.dynamicformlibrary.utils.Utility;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DynamicFormActivity extends AppCompatActivity {

    static Context context;
    static String sDynamicForm = "";
    static String sUserID = "";
    static String sDynamicFormData = "";
    static Boolean flagForm = false;
    static LayerDrawable icon;
    static int getColumnDynamicFormData;
    static int getColumnDynamicForm;
    static DynamicFormFragment formFragment;
    static LinearLayout linearFormNotAvailable;
    static TextView textNoForm;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Handle application crash
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));

        setContentView(R.layout.activity_dynamic_form);
        context = DynamicFormActivity.this;
        overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
        Toolbar mToolbar = (Toolbar) findViewById(com.priyanka.dynamicformlibrary.R.id.toolbar);
        mToolbar.setTitle("Dynamic Form");
        setSupportActionBar(mToolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_white);
        }


        try {
            Intent intent = getIntent();
            sUserID = intent.getStringExtra(Configuration.JSON_USER_ID);

            initializeView(context);

            getDynFormFromDatabase(context, sUserID);

            setSubmitAction(formFragment);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void initializeView(Context context) {
        linearFormNotAvailable = findViewById(R.id.linear_form_not_available);
        textNoForm = findViewById(R.id.text_no_form);
    }

    public void callDynamicForm(Context context) {
        try {
            initializeView(context);

            if ((sDynamicForm != null) && (sDynamicForm != "") && (!sDynamicForm.isEmpty()) && (!sDynamicForm.equals(Configuration.DATA_NOT_AVL))
                    && (!sDynamicForm.equals(Configuration.BLANK_ARRAY))) {
                invalidateOptionsMenu();
                linearFormNotAvailable.setVisibility(View.GONE);
            } else {
                invalidateOptionsMenu();
                linearFormNotAvailable.setVisibility(View.VISIBLE);
            }

            FragmentManager fm = getSupportFragmentManager();

            Fragment retainedFragment = fm.findFragmentByTag(Configuration.FORM_FRAGMENT_KEY);
            if (retainedFragment != null && retainedFragment instanceof DynamicFormFragment) {
                formFragment = (DynamicFormFragment) retainedFragment;
            } else {
                formFragment = new DynamicFormFragment();
                fm.beginTransaction()
                        .add(R.id.fragment_container, formFragment, Configuration.FORM_FRAGMENT_KEY)
                        .commit();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
        finish();
    }

    @Override
    protected void onDestroy() {
        try {
            Utility.clearAllData();// clear dynamic for data
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_submit, menu);
        if ((sDynamicForm != null) && (sDynamicForm != "") && (!sDynamicForm.isEmpty()) && (!sDynamicForm.equals(Configuration.DATA_NOT_AVL))
                && (!sDynamicForm.equals(Configuration.BLANK_ARRAY))) {
            menu.findItem(R.id.action_submit).setVisible(true);
        } else {
            menu.findItem(R.id.action_submit).setVisible(false);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int selectedItemId = item.getItemId();

        if (selectedItemId == R.id.action_submit) {
            setSubmitAction(formFragment);
            return true;

        } else {
            finish();
            return false;
        }
    }

    private void setSubmitAction(final DynamicFormFragment formFragment) {
        try {
            formFragment.validate();
            if (flagForm == true) {

                saveDataInDatabase();


                DynFormSubmitPOST.internetConnectionDataPost(context, sUserID);

            } else {
                Toast.makeText(DynamicFormActivity.this, "Please fill all the fields!", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void saveDataInDatabase() {
        MainDatabase mainDatabase = MainDatabase.getInstance(context);
        mainDatabase.open();
        mainDatabase.updateUserDynamicForm(sUserID, String.valueOf(Utility.saveMap));
        mainDatabase.close();
    }

    private String getDynFormFromDatabase(Context context, String sUserID) {
        //Read Device data from database.
        clearData();
        try {
            //open database
            MainDatabase mainDatabase = MainDatabase.getInstance(context);
            mainDatabase.open();
            //read data query from database
            Cursor c = null;

            c = mainDatabase.getUserDynamicForm(sUserID);

            if (c != null) {

                getDynFormColumnIndex(c);
                while (c.moveToNext()) {
                    setDynFormArrayList(c);
                }
                c.close();
            } else {
                Log.i("" + context.getResources().getString(R.string.log_data_not_available), "" + context.getResources().getString(R.string.log_data_not_available));
            }
            //Close database
            mainDatabase.close();
            callDynamicForm(context);// set Dynamic Form

        } catch (SQLiteException e) {
            if (e != null) {
                Log.e(Configuration.TAG_LOG, "DynamicFormActivity/getDynFormFromDatabase/SQLiteException:" + e.getMessage());
            } else {
                Log.e(Configuration.TAG_LOG, "DynamicFormActivity/getDynFormFromDatabase/SQLiteException");
            }
        } catch (Exception e) {
            if (e != null) {
                Log.e(Configuration.TAG_LOG, "DynamicFormActivity/getDynFormFromDatabase/Exception:" + e.getMessage());
            } else {
                Log.e(Configuration.TAG_LOG, "DynamicFormActivity/getDynFormFromDatabase/Exception");
            }
        }
        return sUserID;
    }

    public void getDynFormColumnIndex(Cursor c) {
        getColumnDynamicForm = c.getColumnIndex(MainDatabase.DYNAMIC_FORM);
        getColumnDynamicFormData = c.getColumnIndex(MainDatabase.DYNAMIC_FORM_DATA);

    }

    public void setDynFormArrayList(Cursor c) {

        String mDynamicForm = c.getString(getColumnDynamicForm);
        String mDynamicFormData = c.getString(getColumnDynamicFormData);
        sDynamicForm = mDynamicForm;
        sDynamicFormData = mDynamicFormData;

    }

    private void clearData() {
        sDynamicForm = "";
        sDynamicFormData = "";
    }

    @Override
    public boolean onSupportNavigateUp() {
        super.onSupportNavigateUp();
        onBackPressed();
        return true;
    }

    public static class DynamicFormFragment extends FormFragment {

        HashMap<String, List<String>> allItemsNames = new HashMap<String, List<String>>();

        FloatingEditTextDialogRadioController.DataSource dataSource = new FloatingEditTextDialogRadioController.DataSource() {


            public HashMap<String, List<String>> getItems() {
                return allItemsNames;
            }
        };

        public void initForm(FormController controller) {

            try {

                JSONArray jsonArray = new JSONArray(sDynamicForm);
                if (jsonArray != null) {

                    boolean isRequiredFlag = true;
                    String values = "";
                    JSONObject jObject = null;

                    values = sDynamicFormData;

                    Map<String, String> TempMap = new HashMap<>();
                    if ((values != null) && !(values.isEmpty())) {
                        values = values.substring(1, values.length() - 1);  //remove curly brackets
                        if (!values.isEmpty()) {
                            //  String[] keyValuePairs = savedValue.split(",");              //split the string to creat key-value pairs
                            String[] keyValuePairs = values.split(",(?![^\\[]*\\])"); //split the string to creat key-value pairs,excepts value in betwwen array i.e[]
                            for (String pair : keyValuePairs)                        //iterate over the pairs
                            {
                                String[] entry = pair.split("=");                   //split the pairs to get key and value
                                // String[] entry = pair.split(":");                   //split the pairs to get key and value
                                TempMap.put(entry[0].trim(), entry[1].trim());          //add them to the hashmap and trim whitespaces
                            }
                        }
                    }

                    FormSectionController section = new FormSectionController(context);
                    for (int i = 0; i < jsonArray.length(); i++) {

                        try {
                            JSONObject oneObject = jsonArray.getJSONObject(i);
                            // Pulling items from the array
                            String viewID = oneObject.getString(Configuration.JSON_VIEW_ID);
                            String view = oneObject.getString(Configuration.JSON_VIEW);
                            String name = oneObject.getString(Configuration.JSON_NAME);
                            String hint = oneObject.getString(Configuration.JSON_HINT);
                            String mandatoryOption = oneObject.getString(Configuration.JSON_MANDATORY_OPTION);
                            String intputType = oneObject.getString(Configuration.JSON_INPUT_TYPE);

                            JSONArray items = oneObject.getJSONArray(Configuration.JSON_ITEMS);

                            if (items != null && items.length() > 0) {
                                List<String> itemList = new ArrayList<>();
                                for (int j = 0; j < items.length(); j++) {
                                    itemList.add(items.getString(j));
                                }
                                allItemsNames.put(name, itemList);
                            }

                            switch (mandatoryOption) {
                                case "true":
                                    isRequiredFlag = true;
                                    break;
                                case "false":
                                    isRequiredFlag = false;
                                    break;
                                default:
                                    break;
                            }
                            String value = "";

                            try {
                                if (TempMap.containsKey(viewID)) {
                                    value = TempMap.get(viewID);
                                } else {
                                    value = "";
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            switch (view) {

                                case Configuration.STRING_EDIT_TEXT:
                                    section.addElement(new FloatingEditTextController(context, viewID, view + i, name, hint, value, isRequiredFlag, String.valueOf(sUserID), intputType));
                                    break;

                                case Configuration.STRING_EDIT_TEXT_RADIO:

                                    section.addElement(new FloatingEditTextDialogRadioController(context, viewID, view + i, name, hint, value, isRequiredFlag, dataSource, String.valueOf(sUserID), intputType));
                                    break;

                                case Configuration.STRING_EDIT_TEXT_CAMERA:

                                    section.addElement(new FloatingEditTextCameraController(context, viewID, view + i, name, hint, value, isRequiredFlag, String.valueOf(sUserID), intputType));
                                    break;

                                default:
                                    break;
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }//for loop close
                    controller.addSection(section);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public boolean validate() {
            getFormController().resetValidationErrors();
            if (getFormController().isValidInput()) {
                DynamicFormActivity.flagForm = true;
            } else {
                getFormController().showValidationErrors();
                DynamicFormActivity.flagForm = false;
            }
            return true;
        }

    }
}
