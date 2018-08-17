package com.example.priyankam.dynamicform.retrofit.rest;

/**
 * Created by Priyankam on 04-07-2017.
 */


import com.example.priyankam.dynamicform.retrofit.model.Result;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Url;


public interface ApiInterface {

     /*
    Retrofit get annotation with our URL
    And upload image
    */

    // @POST("upload.php")

    @Multipart
    @POST()
    Call<Result> uploadImage(@Part MultipartBody.Part file, @Url String medialUploadUrl, @PartMap() Map<String, RequestBody> partMap);

}

