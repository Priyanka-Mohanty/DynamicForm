package com.example.priyankam.dynamicform;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.example.priyankam.dynamicform.database.MainDatabase;
import com.example.priyankam.dynamicform.retrofit.model.Result;
import com.example.priyankam.dynamicform.retrofit.rest.ApiClient;
import com.example.priyankam.dynamicform.retrofit.rest.ApiInterface;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DynFormSubmitPOST {

    static String dynFormData = "";


    public static void internetConnectionDataPost(Context context, String sUserID) {
        try {
            boolean isInternetPresent = false;
            ConnectionDetector cd;
            cd = new ConnectionDetector(context);
            isInternetPresent = cd.isConnectingToInternet();
            if (isInternetPresent) {
                ArrayList<String> listImages = new ArrayList<String>();
                try {
                    listImages = GlobalMethods.fileFilter(Configuration.IMAGE_FILE_PATH, sUserID);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                String parentUrl = Configuration.URL_PARENT_PATH;

                String parentMultiPartUrl = Configuration.URL_MEDIA_UPLOAD_PATH;

                System.out.println("MultiPartUrl=" + parentMultiPartUrl);

                //JSONObject jsonObject = new JSONObject(Utility.saveMap);
                dynFormData = getDynFormData(context, sUserID);

                String value = dynFormData;
                value = value.substring(1, value.length() - 1);           //remove curly brackets
                String[] keyValuePairs = value.split(",");              //split the string to creat key-value pairs
                Map<String, String> formData = new HashMap<>();
                for (String pair : keyValuePairs)                        //iterate over the pairs
                {
                    String[] entry = pair.split("=");                   //split the pairs to get key and value
                    formData.put(entry[0].trim(), entry[1].trim());          //add them to the hashmap and trim whitespaces
                }

                JSONObject jObjectDynForm = new JSONObject(formData);

                JSONObject jsonObject = new JSONObject();
                jsonObject.put(Configuration.JSON_USER_ID, sUserID);
                jsonObject.put(Configuration.FORM_DATA, jObjectDynForm);

                System.out.println("jsonObject=" + jsonObject);

                String url = parentUrl.concat(parentMultiPartUrl);
                String identifier = Configuration.MODE_POST_DYNAMIC_FORM;
                new JsonPost(context, url, jsonObject, listImages, String.valueOf(sUserID), identifier, Configuration.TAB_IDENTIFIER_DEFAULT).execute();
                uploadImageFor(context, String.valueOf(sUserID), listImages);
            } else {
                // Internet connection is not present
                // Ask user to connect to Internet
                String message = "" + context.getResources().getString(R.string.toast_no_internet);
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                goToNextScreen(context, Configuration.MODE_POST_DYNAMIC_FORM);
            }
        } catch (Exception e) {
            if (e != null) {
                Log.e(Configuration.TAG_LOG, "DynFormSubmitPOST/internetConnectionDataPost/internetConnectionData/Exception:" + e.getMessage());
            } else {
                Log.e(Configuration.TAG_LOG, "DynFormSubmitPOST/internetConnectionDataPost/internetConnectionData/Exception");
            }
        }
    }


    public static void goToNextScreen(Context context, String flag) {

        switch (flag) {

            case Configuration.MODE_POST_DYNAMIC_FORM:

                Intent intent = new Intent(context, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);
                ((Activity) context).finish();

                break;

            default:
                break;
        }

    }


    private static void uploadImageFor(final Context mContext, String refID, ArrayList<String> listImages) {

        try {


            String imagePath = "";

            String parentMultiPartUrl = Configuration.URL_PARENT_PATH;

            String mediaUploadUrl = Configuration.URL_MEDIA_UPLOAD_PATH_IMAGE;
            //Create Upload Server Client
            ApiInterface service = ApiClient.getApiService(parentMultiPartUrl);

            //loop through object to get the path of the images that has picked by user

            Call<Result> resultCall = null;

            if (!listImages.isEmpty()) {
                for (int i = 0; i < listImages.size(); i++) {

                    imagePath = listImages.get(i);
                    //File creating from selected URL
                    final File file = new File(imagePath);

                    // create RequestBody instance from file
                    RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);

                    // MultipartBody.Part is used to send also the actual file name
                    MultipartBody.Part body = MultipartBody.Part.createFormData(Configuration.JSON_UPLOADED_FILE, file.getName(), requestFile);

                    HashMap<String, RequestBody> map = new HashMap<>();

                    map.put(Configuration.JSON_UPLOADED_FILE, requestFile);

                    resultCall = service.uploadImage(body, mediaUploadUrl, map);

                    // finally, execute the request
                    resultCall.enqueue(new Callback<Result>() {
                        @Override
                        public void onResponse(Call<Result> call, Response<Result> response) {

                            Log.d("URL", "URL = " + call.request().url().toString()); // here

                            // Response Success or Fail
                            if (response.isSuccessful()) {
                                //sendPost("22","hello");
                                if (response.body().getStatus().equals(Configuration.SUCCESS)) {
                                    Log.i("Image Upload", "Image Upload Successfully" + response.body().getMessage());
                                    String imageName = file.getName();
                                    Log.i("imageName", "" + imageName);
                                    GlobalMethods.image_file_delete(mContext, imageName);

                                } else {
                                    Log.i("Image Upload", "Image Upload Failure" + response.body().getMessage());
                                }
                            } else {
                                Log.i("Image Upload", "Image Upload Failure");
                            }


                        }

                        @Override
                        public void onFailure(Call<Result> call, Throwable t) {

                        }
                    });
                }
            } else {
                Log.d("ImageList", "there is no image to be send");
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getDynFormData(Context context, String sUserID) {

        MainDatabase mainDatabase = new MainDatabase(context);
        mainDatabase.open();
        dynFormData = mainDatabase.getDynamicFormData(sUserID);
        mainDatabase.close();
        return dynFormData;

    }


}
