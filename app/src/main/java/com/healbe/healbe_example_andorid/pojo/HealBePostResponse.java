package com.healbe.healbe_example_andorid.pojo;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HealBePostResponse {
    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    public static final String EXTRA_TOKEN ="com.healbe.healbe_example_android.USERID";
    public static final String EXTRA_TIME="com.healbe.healbe_example_android.TIME";
    public static final String URL_AUTHO = "https://healchpack.azurewebsites.net/users/login";
    public static final String URL_REG = "https://healchpack.azurewebsites.net/users/CreateUser";
    public static final String URL_PACK = "https://healchpack.azurewebsites.net/api";

    private String URL = null;
    private String body = null;
    private String responseBody = null;
    private int responseHeader = 0;
    private String token = "nan";

    //CONSTRUCTORS
    public HealBePostResponse() {
    }
    public HealBePostResponse(String URL, String body) {
        this.URL = URL;
        this.body = body;
    }


    //ПОСТ запрос
    public int post() {
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
                    responseHeader = response.code();
                    responseBody = response.body().string();
                } catch (IOException e) {
                    //нет ответа
                    responseHeader = 404;
                }

            }
        });

        try {
            thread.start();
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return responseHeader;
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
    public int getResponseHeader() {
        return responseHeader;
    }
    public void setResponseHeader(int responseHeader) {
        this.responseHeader = responseHeader;
    }
    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }
    public String getResponseBody() {
        return responseBody;
    }
    public void setResponseBody(String responseBody) {
        this.responseBody = responseBody;
    }


}
