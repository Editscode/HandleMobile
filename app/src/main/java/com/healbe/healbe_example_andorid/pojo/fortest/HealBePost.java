package com.healbe.healbe_example_andorid.pojo.fortest;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.drawable.Drawable;
import android.widget.Toast;

import com.healbe.healbe_example_andorid.R;
import com.healbe.healbe_example_andorid.dashboard.DashboardActivity;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Exchanger;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HealBePost {
    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    public static final String EXTRA_TOKEN ="com.healbe.healbe_example_android.USERID";
    public static final String EXTRA_TIME="com.healbe.healbe_example_android.TIME";
    public static final String EXTRA_USER="com.healbe.healbe_example_android.USER";


    public static final String URL_AUTHO = "https://healchpack.azurewebsites.net/users/login";
    public static final String URL_REG = "https://healchpack.azurewebsites.net/users/CreateUser";
    public static final String URL_PACK = "https://healchpack.azurewebsites.net/api";

    private String URL = null;
    private String body = null;
    private String responseString = null;
    private String token = "nan";

    //CONSTRUCTORS
    public HealBePost() {
    }
    public HealBePost(String URL, String body) {
        this.URL = URL;
        this.body = body;
    }


    //ПОСТ запрос
    public String post() {
        // Отдельный поток, для отправки POST и получения ответа
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Request request = new Request.Builder()
                        .url(URL)
                        .post(RequestBody.create(JSON,body))
                        .addHeader("Authorization", token)
                        .build();



                OkHttpClient.Builder b = new OkHttpClient.Builder();
                b.connectTimeout(60, TimeUnit.SECONDS);
                b.readTimeout(6, TimeUnit.MINUTES);
                b.writeTimeout(120, TimeUnit.SECONDS);
                OkHttpClient client = b.build();


                try {
                    Response response = client.newCall(request).execute();
                    responseString = response.body().string().replaceAll(",",",\n");
                } catch (IOException e) {
                    responseString = e.getMessage();
                }

            }
        });

        try {
            thread.start();
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return responseString;
    }


    //GETTER and SETTER
    public String getURL() {
        return URL;
    }
    public void setURL(String URL) {
        this.URL = URL;
    }
    public String getBody() {
        return body;
    }
    public void setBody(String body) {
        this.body = body;
    }
    public String getResponseString() {
        return responseString;
    }
    public void setResponseString(String responseString) {
        this.responseString = responseString;
    }
    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }

}
