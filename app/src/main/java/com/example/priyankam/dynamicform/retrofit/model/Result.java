package com.example.priyankam.dynamicform.retrofit.model;

/**
 * Created by Priyankam on 25-07-2017.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Result {

  /*  @SerializedName("Result")
    @Expose
    private String result;*/


    @SerializedName("status")
    @Expose
    private String Status;

    @SerializedName("message")
    @Expose
    private String Message;

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        this.Message = message;
    }


    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        this.Status = status;
    }


    /**
     * @return The result
     *//*
    public String getResult() {
        return result;
    }

    *//**
     * @param result The result
     *//*
    public void setResult(String result) {
        this.result = result;
    }*/

}