package com.healbe.healbe_example_andorid.pojo.acti;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.healbe.healbe_example_andorid.R;
import com.healbe.healbe_example_andorid.pojo.fortest.HealBePost;
import com.healbe.healbe_example_andorid.pojo.tools.AlertLoader;
import com.healbe.healbe_example_andorid.pojo.HealBePostResponse;
import com.healbe.healbe_example_andorid.pojo.tools.AuthenticateResponse;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {
    public static final Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    public static final Pattern VALID_LAT_REGEX = Pattern.compile("[a-zA-Z]", Pattern.CASE_INSENSITIVE);


    private EditText username;
    private EditText password;
    private AuthenticateResponse authRes;
    private AlertLoader loader;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        username = (EditText) findViewById(R.id.usernameReg);
        password = (EditText) findViewById(R.id.passwordReg);



        loader = new AlertLoader(LoginActivity.this, "Авторизация...");
    }


    public void onClickHome(View view) {
        finish();
    }

    public void onClickLogin(View view) {
        //Проверка введенных данных
        if(!validate(username.getText().toString())) {
            Toast.makeText(LoginActivity.this, "Некорректный логин", Toast.LENGTH_LONG).show();
            return;
        }
        if(password.getText().toString().length() > 8 || password.getText().toString().length() < 1) {
            Toast.makeText(LoginActivity.this, "Некорректный пароль", Toast.LENGTH_LONG).show();
            return;
        }

        //Запуск Лоадера
        loader.start();

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                String postBody = "{\n" +
                        "  \"password\": \""+ password.getText().toString() +"\",\n" +
                        "  \"email\": \""+ username.getText().toString() + "\"\n" +
                        "}";

                HealBePostResponse hbPost = new HealBePostResponse(HealBePost.URL_AUTHO, postBody);
                int code = hbPost.post();

                switch (code) {
                    case 200:
                        ObjectMapper mapper = new ObjectMapper();
                        try {
                            authRes = mapper.readValue(hbPost.getResponseBody(), AuthenticateResponse.class);
                            Intent intent = new Intent(LoginActivity.this, GeneralActivity.class);
                            intent.putExtra(HealBePost.EXTRA_USER, hbPost.getResponseBody());
                            loader.stop(AlertLoader.STOP_LOGIN);
                            startActivity(intent);
                            finish();
                        } catch (JsonProcessingException e) {
                            loader.stop(AlertLoader.WTF);
                        }
                        break;
                    case 400:
                        loader.stop(AlertLoader.UNCORRECT_LOGIN);
                        break;
                    case 500:
                        loader.stop(AlertLoader.BAD_REQUEST);
                        break;
                    default:
                        loader.stop(AlertLoader.UNKNOW);
                }
            }
        });

        thread.start();
    }

    //восстановить пароль
    public void onClickRefresh(View view) {

    }

    // Проверка email
    public static boolean validate(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.find();
    }
}

