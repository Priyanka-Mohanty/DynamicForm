package com.example.priyankam.dynamicform;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.database.sqlite.SQLiteException;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.priyankam.dynamicform.database.MainDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class JsonPost extends AsyncTask<Void, Void, String> {
    public final Context mContext;
    public final String mUrl;
    public final String mIdentifier;
    public final int mTabIdentifier;
    public ProgressDialog pDialog;
    ArrayList<String> listImages = new ArrayList<String>();
    List<HashMap<String, String>> resultValue = new ArrayList<>();
    String RefID;
    /**
     * Image path to send
     */
    String imagePath = "";
    MainDatabase mainDatabase;
    //public final Map<String, String> mParams;
    private JSONObject mJsonObject;

    public JsonPost(Context context, String url, JSONObject jsonData, ArrayList<String> listImages, String refID, String identifier, int tabIdentifier) {
        // getting data to be sent
        this.mContext = context;
        this.mUrl = url;
        this.mJsonObject = jsonData;
        this.mIdentifier = identifier;
        this.mTabIdentifier = tabIdentifier;
        this.listImages = listImages;
        this.RefID = refID;
    }

    public JsonPost(Context context, String url, JSONObject jsonData, String identifier, int tabIdentifier) {
        // getting data to be sent
        this.mContext = context;
        this.mUrl = url;
        this.mJsonObject = jsonData;
        this.mIdentifier = identifier;
        this.mTabIdentifier = tabIdentifier;
        this.listImages = new ArrayList<String>();

    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        try {
            /**open the main Database */
            mainDatabase = new MainDatabase(mContext);
            mainDatabase.open();

        } catch (SQLiteException e) {
            if (e != null) {
                Log.e(Configuration.TAG_LOG, "JsonParsingPOST/JsonParsingPOST/SQLiteException:" + e.getMessage());
            } else {
                Log.e(Configuration.TAG_LOG, "JsonParsingPOST/JsonParsingPOST/SQLiteException");
            }

        } catch (Exception e) {
            if (e != null) {
                Log.e(Configuration.TAG_LOG, "JsonParsingPOST/JsonParsingPOST/Exception:" + e.getMessage());
            } else {
                Log.e(Configuration.TAG_LOG, "JsonParsingPOST/JsonParsingPOST/Exception");
            }
        }
        try {
            String message = "Please wait";
            if ((!Configuration.sSwipeRefreshFlag)) {
                pDialog = new ProgressDialog(mContext);
                pDialog.show();
                pDialog.setCancelable(false);
                pDialog.setContentView(R.layout.custom_progressdialog);
                pDialog.getWindow().setBackgroundDrawableResource(R.color.transparentp);
                TextView txtView;
                txtView = pDialog.findViewById(R.id.progressMessage);
                txtView.setText(message);
            }

        } catch (Exception e) {
            if ((pDialog != null) && (pDialog.isShowing())) {
                pDialog.dismiss();
            }
            e.printStackTrace();
        }


    }

    @SuppressLint("LongLogTag")
    @Override
    protected String doInBackground(Void... params) {

        ServiceHandler serviceClient = new ServiceHandler();

        JSONObject obj = mJsonObject;

        //Log.d("OBJ", "OBJJJJJJ  " + obj);

        String jsonStr = null;
        try {
            jsonStr = serviceClient.makeServiceCall(mUrl, ServiceHandler.POST, obj);
            Log.i("Create Prediction Request: ", "" + jsonStr);
            if ((jsonStr != null) && (!jsonStr.equals(Configuration.STRING_EMPTY))) {
                postJsonData(mIdentifier, jsonStr);
            } else {
                Log.e("" + mContext.getResources().getString(R.string.log_service_handler), "" + mContext.getResources().getString(R.string.log_not_get_url));
            }
        } catch (Exception e) {
            e.printStackTrace();
            if ((pDialog != null) && (pDialog.isShowing())) {
                /** Dismiss the progress dialog*/
                pDialog.dismiss();
            }

        }

        return jsonStr;
    }

    @Override
    protected void onPostExecute(String result) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.CUPCAKE) {
            super.onPostExecute(result);
        }
        //Log.i("" + mContext.getResources().getString(R.string.log_result), "" + mContext.getResources().getString(R.string.log_result) + resultValue);
        try {
            if ((pDialog != null) && (pDialog.isShowing())) {
                /** Dismiss the progress dialog*/
                pDialog.dismiss();
            } else {
                pDialog.dismiss();
            }
            if (result != null) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONObject joResponse = jsonObject.getJSONObject(Configuration.RESPONSE);
                    String status = joResponse.getString(Configuration.STATUS);
                    String message = joResponse.getString(Configuration.MESSAGE);

                    if (status.equals(Configuration.SUCCESS)) {
                        disPlayJsonData(mIdentifier, resultValue);
                    } else {
                        disPlayJsonErrorData(mIdentifier, message);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                String message = "" + mContext.getResources().getString(R.string.log_data_not_available);
                disPlayJsonErrorData(mIdentifier, message);
            }

        } catch (Exception e) {
            if (e != null)
                Toast.makeText(mContext, "Error while fetching data", Toast.LENGTH_SHORT).show();
            if ((pDialog != null) && (pDialog.isShowing())) {
                /** Dismiss the progress dialog*/
                pDialog.dismiss();
            }
        } finally {
            Log.e("final block", "final block excuted");
            try {
                mainDatabase.close();
            } catch (Exception ignored) {

            }
        }
    }

    private void postJsonData(String mode, String jsonStr) {
        // Log.d("jsonStr", "jsonStr:============" + jsonStr);
        switch (mode) {


            case Configuration.MODE_POST_DYNAMIC_FORM:

                resultFormData(jsonStr);

                break;


            default:
                break;

        }
    }

    private void disPlayJsonErrorData(String mode, String resultValue) {

        switch (mode) {


            case Configuration.MODE_POST_DYNAMIC_FORM:

                MainActivity mainActivity = new MainActivity();
                mainActivity.setErrorValuePostDynForm(mContext, resultValue);

                break;


            default:
                break;
        }

    }

    private void disPlayJsonData(String mode, List<HashMap<String, String>> resultValue) {
        switch (mode) {


            case Configuration.MODE_POST_DYNAMIC_FORM:

                MainActivity mainActivity = new MainActivity();
                mainActivity.setValuePostDynForm(mContext, resultValue);

                break;

            default:
                break;
        }
    }

    @SuppressLint("LongLogTag")
    private void resultFormData(String jsonStr) {

        /**Split string data and check success or failure*/
        resultValue.clear();
        if (jsonStr != null) {
            try {

                JSONObject jsonObject = new JSONObject(jsonStr);
                JSONObject joResponse = jsonObject.getJSONObject(Configuration.RESPONSE);
                String status = joResponse.getString(Configuration.STATUS);
                String message = joResponse.getString(Configuration.MESSAGE);

                if (status.equals(Configuration.SUCCESS)) {

                    Log.e(Configuration.TAG_LOG, joResponse.getString(Configuration.MESSAGE));

                } else {
                    Log.e(Configuration.TAG_LOG, joResponse.getString(Configuration.MESSAGE));
                }

                HashMap<String, String> result = new HashMap<>();
                result.put(Configuration.STATUS, status);
                result.put(Configuration.MESSAGE, message);

                resultValue.add(result);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else {
            Log.e(Configuration.TAG_LOG, mContext.getResources().getString(R.string.error_json_data));
        }
    }

}
