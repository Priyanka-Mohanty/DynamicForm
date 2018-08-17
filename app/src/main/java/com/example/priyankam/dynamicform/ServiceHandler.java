package com.example.priyankam.dynamicform;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
public class ServiceHandler {
    public static final int GET = 1;
    public static final int POST = 2;

    /**
     * Class constructor.
     */
    public ServiceHandler() {
        /**private constructor*/

    }

    public String makeServiceCall(String url, int method) {
        return this.makeServiceCall(url, method, null);
    }

    public String makeServiceCall(String url, int method, JSONObject jSonObject) {
        String response = "0";
        try {
            /** Checking http request method type*/
            if (method == POST) {
                try {
                    HttpClient hc = new DefaultHttpClient();
                    HttpPost po = new HttpPost(url);
                    StringEntity se = new StringEntity(jSonObject.toString());//encode data
                    po.setEntity(se);

                    try {
                        HttpResponse httpResponse = hc.execute(po);
                        HttpEntity entity = httpResponse.getEntity();
                        // print response
                        response = EntityUtils.toString(entity);

                    } catch (ClientProtocolException e) {
                        response = "Post Failure. Invalid app server";
                        //Log.e("AppUtil", "Error in sharing with App Server: " + e);
                        response = "Failure";
                    } catch (IOException e) {
                        e.printStackTrace();
                        response = "Failure";
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    response = "Failure";

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (method == GET) {
                try {
                    HttpParams httpParameters = new BasicHttpParams();
                    HttpConnectionParams.setConnectionTimeout(httpParameters, Configuration.INTERNET_TIME_OUT);
                    HttpConnectionParams.setSoTimeout(httpParameters, Configuration.INTERNET_TIME_OUT);
                    HttpClient hc = new DefaultHttpClient();
                    HttpGet get = new HttpGet(url);

                    try {
                        HttpResponse res = hc.execute(get);
                        HttpEntity ent = res.getEntity();
                        InputStream is = ent.getContent();
                        BufferedReader br = new BufferedReader(new InputStreamReader(is));
                        StringBuffer sb = new StringBuffer();
                        String s = null;
                        while ((s = br.readLine()) != null) {
                            sb.append(s);
                        }

                        response = sb.toString();


                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                } catch (Exception e) {
                    if (e != null) {
                        Log.e(Configuration.TAG_LOG, "ServiceHandler/makeServiceCall/Exception:" + e.getMessage());
                    } else {
                        Log.e(Configuration.TAG_LOG, "ServiceHandler/makeServiceCall/Exception");
                    }
                }

            }

        } catch (Exception e) {
            if (e != null) {
                Log.e(Configuration.TAG_LOG, "ServiceHandler/makeServiceCall/Exception:" + e.getMessage());
            } else {
                Log.e(Configuration.TAG_LOG, "ServiceHandler/makeServiceCall/Exception");
            }
        }
        return response;

    }

    private String convertInputStreamToString(InputStream inputStream) throws IOException {

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        try {
            while ((line = bufferedReader.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        /** Close Stream */
        if (null != inputStream) {
            inputStream.close();
        }
        return result;
    }

}
