package com.example.priyankam.dynamicform;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.priyankam.dynamicform.database.MainDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    static ListView sList;
    static Context context;
    static ArrayList<String> userIDList = new ArrayList<>();
    static ArrayList<String> userNameList = new ArrayList<>();
    static ArrayList<String> userEmailList = new ArrayList<>();

    static int getColumnUserID;
    static int getColumnUsername;
    static int getColumnUserEmail;

    static String userID;
    static int getColumnDynamicFormData;
    static int getColumnDynamicForm;

    static CustomAdapter adpater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = MainActivity.this;
        setContentView(R.layout.activity_main);

        Toolbar mToolbar = (Toolbar) findViewById(com.priyanka.dynamicformlibrary.R.id.toolbar);
        mToolbar.setTitle("Dynamic Form");
        setSupportActionBar(mToolbar);

        sList = (ListView) findViewById(R.id.list);
        internetConnectionData(context);

        sList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                userID = userIDList.get(position);

                internetConnectionDynamicForm(context, userID);


            }
        });
    }

    private void callDynamicForm(String userID) {
        Intent intent = new Intent(context, DynamicFormActivity.class);
        intent.putExtra(Configuration.JSON_USER_ID, userID);
        context.startActivity(intent);
    }

    private void internetConnectionDynamicForm(Context context, String refNo) {
        try {
            boolean isInternetPresent = false;
            ConnectionDetector cd;
            cd = new ConnectionDetector(context);
            isInternetPresent = cd.isConnectingToInternet();
            if (isInternetPresent) {
                String url = "http://www.mocky.io/v2/5b756f4d2e00005400536098";
                String identifier = Configuration.MODE_DYNAMIC_FORM;
                new JsonGet(context, url, identifier, Configuration.TAB_IDENTIFIER_DEFAULT).execute();

            } else {
                checkDynFormAvailable(context, refNo);
            }
        } catch (Exception e) {
            if (e != null) {
                Log.e(Configuration.TAG_LOG, "internetConnectionData/Exception:" + e.getMessage());
            } else {
                Log.e(Configuration.TAG_LOG, "internetConnectionData/Exception");
            }
        }
    }

    private void checkDynFormAvailable(Context context, String userId) {
        String mDynamicForm = getDynFormFromDatabase(context, userId);
        if (!(mDynamicForm.isEmpty()) && (mDynamicForm != null)) {
            callDynamicForm(userId);
        } else {
            String message = "" + context.getResources().getString(R.string.toast_no_internet);
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        }
    }


    private String getDynFormFromDatabase(Context context, String userID) {

        String dynFormResult = "";

        //Read Device data from database.
        try {
            //open database
            MainDatabase maindatabase = new MainDatabase(context);
            maindatabase.open();
            //read data query from database
            Cursor c = null;
            c = maindatabase.getUserDynamicForm(userID);
            if (c != null) {
                getDynFormColumnIndex(c);
                while (c.moveToNext()) {

                    dynFormResult = setDynFormArrayList(c);

                }
                c.close();
            } else {
                Log.i("" + context.getResources().getString(R.string.log_data_not_available), "" + context.getResources().getString(R.string.log_data_not_available));
            }
            //Close database
            maindatabase.close();
        } catch (SQLiteException e) {
            if (e != null) {
                Log.e(Configuration.TAG_LOG, "getDynFormFromDatabase/SQLiteException:" + e.getMessage());
            } else {
                Log.e(Configuration.TAG_LOG, "getDynFormFromDatabase/SQLiteException");
            }
        } catch (Exception e) {
            if (e != null) {
                Log.e(Configuration.TAG_LOG, "getDynFormFromDatabase/Exception:" + e.getMessage());
            } else {
                Log.e(Configuration.TAG_LOG, "getDynFormFromDatabase/Exception");
            }
        }
        return dynFormResult;
    }

    public void getDynFormColumnIndex(Cursor c) {
        getColumnDynamicForm = c.getColumnIndex(MainDatabase.DYNAMIC_FORM);
        getColumnDynamicFormData = c.getColumnIndex(MainDatabase.DYNAMIC_FORM_DATA);
    }

    public String setDynFormArrayList(Cursor c) {
        String mDynamicForm = c.getString(getColumnDynamicForm);
        String mDynamicFormData = c.getString(getColumnDynamicFormData);
        Log.i("mDynamicFormData:", "mDynamicFormData= " + mDynamicForm + "\n" + mDynamicFormData);
        return mDynamicForm;
    }

    private void internetConnectionData(Context context) {
        try {
            boolean isInternetPresent = false;
            ConnectionDetector cd;
            cd = new ConnectionDetector(context);
            isInternetPresent = cd.isConnectingToInternet();
            if (isInternetPresent) {
                String url = "http://www.mocky.io/v2/5b753c372e00006300535f64";
                String identifier = Configuration.MODE_USER;
                new JsonGet(context, url, identifier, Configuration.TAB_IDENTIFIER_DEFAULT).execute();

            } else {
                String message = "" + context.getResources().getString(R.string.toast_no_internet);
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                getDataFromDatabase();
            }
        } catch (Exception e) {
            if (e != null) {
                Log.e(Configuration.TAG_LOG, "MainActivity/internetConnectionData/Exception:" + e.getMessage());
            } else {
                Log.e(Configuration.TAG_LOG, "MainActivity/internetConnectionData/Exception");
            }
        }
    }

    private void getDataFromDatabase() {
        clearData();

        try {
            MainDatabase mainDatabase = new MainDatabase(context);
            mainDatabase.open();
            Cursor c = mainDatabase.getUserMaster();
            if (c != null) {
                getColumnIndex(c);
                while (c.moveToNext()) {
                    setArrayList(c);
                }
                c.close();
            } else {
                Log.i("" + context.getResources().getString(R.string.log_data_not_available), "" + context.getResources().getString(R.string.log_data_not_available));
            }
            mainDatabase.close();
            setListAdapter();// set list Adapter

        } catch (SQLiteException e) {
            if (e != null) {
                Log.e(Configuration.TAG_LOG, "MainActivity/getDataFromDatabase/SQLiteException:" + e.getMessage());
            } else {
                Log.e(Configuration.TAG_LOG, "MainActivity/getDataFromDatabase/SQLiteException");
            }
        } catch (Exception e) {
            if (e != null) {
                Log.e(Configuration.TAG_LOG, "MainActivity/getDataFromDatabase/Exception:" + e.getMessage());
            } else {
                Log.e(Configuration.TAG_LOG, "MainActivity/getDataFromDatabase/Exception");
            }
        }
    }

    private void clearData() {
        userIDList.clear();
        userNameList.clear();
        userEmailList.clear();
    }


    public void setValue(Context mContext, List<HashMap<String, String>> resultValue) {
        Log.i("Log", "result vale" + resultValue);
        try {
            clearData();
            for (int i = 0; i < resultValue.size(); i++) {
                String userID = resultValue.get(i).get(Configuration.JSON_USER_ID);
                String userName = resultValue.get(i).get(Configuration.JSON_USER_NAME);
                String userEmail = resultValue.get(i).get(Configuration.JSON_USER_EMAIL);

                userIDList.add(userID);
                userNameList.add(userName);
                userEmailList.add(userEmail);
            }

            setListAdapter();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    public void setErrorValue(String resultValue) {
        Log.i("Log", "result vale" + resultValue);
    }

    private void getColumnIndex(Cursor c) {

        getColumnUserID = c.getColumnIndex(MainDatabase.USER_ID);
        getColumnUsername = c.getColumnIndex(MainDatabase.USER_NAME);
        getColumnUserEmail = c.getColumnIndex(MainDatabase.USER_EMAIL);
    }

    private void setArrayList(Cursor c) {

        String userID = c.getString(getColumnUserID);
        String userName = c.getString(getColumnUsername);
        String userEmail = c.getString(getColumnUserEmail);

        userIDList.add(userID);
        userNameList.add(userName);
        userEmailList.add(userEmail);
    }

    private void setListAdapter() {

        adpater = new CustomAdapter(context, userIDList, userNameList, userEmailList);
        adpater.notifyDataSetChanged();
        sList.setAdapter(adpater);
    }

    public void setErrorValueDynForm(String resultValue) {
        Log.i("Log", "setErrorValueDynForm=result vale" + resultValue);
        Toast.makeText(context, resultValue, Toast.LENGTH_SHORT).show();
        callDynamicForm(userID);
    }

    public void setValueDynForm(Context mContext, List<HashMap<String, String>> resultValue) {
        Log.i("Log", "setValueDynForm= result vale" + resultValue);
        for (int i = 0; i < resultValue.size(); i++) {
            String userId = resultValue.get(i).get(Configuration.JSON_USER_ID);
            String DynamicForm = resultValue.get(i).get(Configuration.JSON_DYNAMIC_FORM);
            //Toast.makeText(context, srefNo +"\n" +DynamicForm, Toast.LENGTH_LONG).show();
            Log.i("Log", "setValueDynForm" + userId + " == " + " DynamicForm  " + " == " + DynamicForm);
        }
        callDynamicForm(userID);
    }

    public void setErrorValuePostDynForm(Context mContext, String resultValue) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
        Toast.makeText(context, resultValue, Toast.LENGTH_SHORT).show();
    }

    public void setValuePostDynForm(Context mContext, List<HashMap<String, String>> resultValue) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
        Toast.makeText(context, "Submitted Successfully", Toast.LENGTH_SHORT).show();
    }
}
