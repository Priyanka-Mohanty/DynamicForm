package com.example.priyankam.dynamicform;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.sqlite.SQLiteException;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.priyankam.dynamicform.database.MainDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class JsonGet extends AsyncTask<Void, Void, String> {
    List<HashMap<String, String>> resultValue = new ArrayList<>();
    MainDatabase mainDatabase;
    int tabIdentifier;
    private Context mContext;
    private String mUrl;
    private String mIdentifier;
    private ProgressDialog pDialog;

    public JsonGet(Context context, String url, String identifier, int tabIdentifier) {
        super();
        this.mContext = context;
        this.mUrl = url;
        this.mIdentifier = identifier;
        this.tabIdentifier = tabIdentifier;

        try {
            mainDatabase = new MainDatabase(mContext);
            mainDatabase.open();

        } catch (SQLiteException e) {
            if (e != null) {
                Log.e(Configuration.TAG_LOG, "JsonParsingGet/JsonParsingGet/SQLiteException:" + e.getMessage());
            } else {
                Log.e(Configuration.TAG_LOG, "JsonParsingGet/JsonParsingGet/SQLiteException");
            }

        } catch (Exception e) {
            if (e != null) {
                Log.e(Configuration.TAG_LOG, "JsonParsingGet/JsonParsingGet/Exception:" + e.getMessage());
            } else {
                Log.e(Configuration.TAG_LOG, "JsonParsingGet/JsonParsingGet/Exception");
            }
        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        String message = "please wait";


        if ((!Configuration.sSwipeRefreshFlag)) {

            pDialog = new ProgressDialog(mContext);
            pDialog.show();
            pDialog.setCancelable(false);
            pDialog.setContentView(R.layout.custom_progressdialog);
            pDialog.getWindow().setBackgroundDrawableResource(R.color.transparentp);
            TextView txtView;
            txtView = (TextView) pDialog.findViewById(R.id.progressMessage);
            txtView.setText(message);

        }

    }

    @Override
    protected String doInBackground(Void... arg0) {
        resultValue.clear();
        ServiceHandler sh = new ServiceHandler();

        String jsonStr = null;
        try {
            jsonStr = sh.makeServiceCall(mUrl, ServiceHandler.GET);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d(mContext.getResources().getString(R.string.log_service_handler), "" + jsonStr);

        if ((jsonStr != null)) {
            parseJsonData(mIdentifier, jsonStr);
        } else {
            Log.e("" + mContext.getResources().getString(R.string.log_service_handler), "" + mContext.getResources().getString(R.string.log_not_get_url));
        }
        return jsonStr;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        Log.i(" ", "+ " + result);
        Log.i("" + mContext.getResources().getString(R.string.log_result), "" + mContext.getResources().getString(R.string.log_result) + resultValue);

        try {

            if ((pDialog != null) && (pDialog.isShowing())) {
                /** Dismiss the progress dialog*/
                pDialog.dismiss();
            }

            if (result != null) {

                if (!Configuration.STRING_ZERO.equals(result)) {

                    if (!Configuration.DATA_NOT_AVL.equals(result)) {
                        disPlayJsonData(mIdentifier, resultValue);
                    } else {
                        String message = "" + mContext.getResources().getString(R.string.log_data_not_available);
                        disPlayJsonErrorData(mIdentifier, message);
                    }
                } else {
                    String message = "" + mContext.getResources().getString(R.string.log_unable_to_fetch);
                    disPlayJsonErrorData(mIdentifier, message);
                }
            }
        } catch (Exception e) {
            if (e != null)
                Toast.makeText(mContext, "Error while fetching data", Toast.LENGTH_SHORT).show();

        } finally {
            mainDatabase.close();
        }

    }

    private String parseJsonData(String mode, String jsonStr) {
        switch (mode) {
            case Configuration.MODE_USER:

                modeUserData(jsonStr);

                break;

            case Configuration.MODE_DYNAMIC_FORM:

                modeDynamicForm(jsonStr);

                break;


            default:
                break;
        }
        return jsonStr;
    }


    private void disPlayJsonData(String mode, List<HashMap<String, String>> resultValue) {
        switch (mode) {

            case Configuration.MODE_USER:

                MainActivity mainActivity = new MainActivity();
                mainActivity.setValue(mContext, resultValue);

                break;


            case Configuration.MODE_DYNAMIC_FORM:

                MainActivity MainActivity1 = new MainActivity();
                MainActivity1.setValueDynForm(mContext, resultValue);


                break;


            default:
                break;
        }
    }

    private void disPlayJsonErrorData(String mode, String resultValue) {
        switch (mode) {

            case Configuration.MODE_USER:

                MainActivity mainActivity = new MainActivity();
                mainActivity.setErrorValue(resultValue);

                break;

            case Configuration.MODE_DYNAMIC_FORM:

                MainActivity MainActivity1 = new MainActivity();
                MainActivity1.setErrorValueDynForm(resultValue);

                break;


            default:
                break;
        }
    }

    private void modeUserData(String jsonStr) {
        //Delete old record
        mainDatabase.deleteUserMaster();

        JSONArray jsonArrayM;
        try {
            if ((jsonStr != null) && (!Configuration.STRING_ZERO.equals(jsonStr))
                    && (!Configuration.DATA_NOT_AVL.equals(jsonStr))) {
                JSONObject jsonObj = new JSONObject(jsonStr);
                jsonArrayM = jsonObj.getJSONArray(Configuration.JSON_USER);
                for (int i = 0; i < jsonArrayM.length(); i++) {
                    JSONObject c = jsonArrayM.getJSONObject(i);
                    String jUserID = c.getString(Configuration.JSON_USER_ID);
                    String jUserName = c.getString(Configuration.JSON_USER_NAME);
                    String jUserEmail = c.getString(Configuration.JSON_USER_EMAIL);

                    /**Storing the value in database*/
                    mainDatabase.insertUserMaster(jUserID, jUserName, jUserEmail);

                    /** Temporally hashMap*/
                    HashMap<String, String> result = new HashMap<>();
                    /** adding each child node to HashMap key => value*/
                    result.put(Configuration.JSON_USER_ID, String.valueOf(jUserID));
                    result.put(Configuration.JSON_USER_NAME, String.valueOf(jUserName));
                    result.put(Configuration.JSON_USER_EMAIL, String.valueOf(jUserEmail));

                    /** adding all details to result value array list*/
                    resultValue.add(result);

                }/**for loop close*/

            }/**close json string not null*/
            else {
                Log.e("", "" + mContext.getResources().getString(R.string.log_data_not_available));
            }
            /**Close database*/
            mainDatabase.close();

        } catch (JSONException e) {
            if (e != null) {
                Log.e(Configuration.TAG_LOG, "JsonParsing/modeUserData/JSONException:" + e.getMessage());
            } else {
                Log.e(Configuration.TAG_LOG, "JsonParsing/modeUserData/JSONException");
            }
        } catch (Exception e) {
            if (e != null) {
                Log.e(Configuration.TAG_LOG, "JsonParsing/modeUserData/Exception:" + e.getMessage());
            } else {
                Log.e(Configuration.TAG_LOG, "JsonParsing/modeUserData/Exception");
            }
        }

    }

    private void modeDynamicForm(String jsonStr) {
        //Delete old record
        String jUserId = MainActivity.userID;

        mainDatabase.deleteUserDynamicForm(jUserId);
        JSONArray jsonArrayMDynamicForm;
        try {
            if ((jsonStr != null) && (!Configuration.STRING_ZERO.equals(jsonStr))
                    && (!Configuration.DATA_NOT_AVL.equals(jsonStr))) {
                JSONObject jsonObj = new JSONObject(jsonStr);

                jsonArrayMDynamicForm = jsonObj.getJSONArray(Configuration.JSON_DYNAMIC_FORM);

                mainDatabase.insertUserDynamicForm(jUserId, String.valueOf(jsonArrayMDynamicForm), "");

                HashMap<String, String> result = new HashMap<>();
                result.put(Configuration.JSON_USER_ID, String.valueOf(jUserId));
                result.put(Configuration.JSON_DYNAMIC_FORM, String.valueOf(jsonArrayMDynamicForm));
                resultValue.add(result);


            }/**close json string not null*/
            else {
                Log.e("", "" + mContext.getResources().getString(R.string.log_data_not_available));
            }


            /**Close database*/
            mainDatabase.close();

        } catch (JSONException e) {
            if (e != null) {
                Log.e(Configuration.TAG_LOG, "JsonParsing/modeDynamicForm/JSONException:" + e.getMessage());
            } else {
                Log.e(Configuration.TAG_LOG, "JsonParsing/modeDynamicForm/JSONException");
            }
        } catch (Exception e) {
            if (e != null) {
                Log.e(Configuration.TAG_LOG, "JsonParsing/modeDynamicForm/Exception:" + e.getMessage());
            } else {
                Log.e(Configuration.TAG_LOG, "JsonParsing/modeDynamicForm/Exception");
            }
        }
    }


}

