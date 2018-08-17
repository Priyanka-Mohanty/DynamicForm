package com.example.priyankam.dynamicform.retrofit.rest;

/**
 * Created by Priyankam on 04-07-2017.
 */


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    /**
     * Upload URL of your folder with php file name...
     * You will find this file in php_upload folder in this project
     * You can copy that folder and paste in your htdocs folder...
     */

    // private static final String BASE_URL = "http://10.1.1.59/Projects/image_upload/";


    /**
     * Get Retro Client
     *
     * @return JSON Object
     */
    static Gson gson = new GsonBuilder()
            .setLenient()
            .create();
    private static Retrofit retrofit = null;


    private static Retrofit getRetroClient(String url) {
        try {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

            if (retrofit == null) {
                retrofit = new Retrofit.Builder()
                        .baseUrl(url)
                        .addConverterFactory(GsonConverterFactory.create(gson))
                        .client(client)
                        .build();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        return retrofit;
    }

    public static ApiInterface getApiService(String url) {
        try {
            return getRetroClient(url).create(ApiInterface.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

}
