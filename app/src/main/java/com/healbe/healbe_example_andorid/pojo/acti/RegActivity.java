package com.healbe.healbe_example_andorid.pojo.acti;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.healbe.healbe_example_andorid.R;
import com.healbe.healbe_example_andorid.pojo.fortest.HealBePost;
import com.healbe.healbe_example_andorid.pojo.HealBePostResponse;
import com.healbe.healbe_example_andorid.pojo.tools.AlertLoader;

import java.util.regex.Matcher;

public class RegActivity extends AppCompatActivity {
    private EditText firstname;
    private EditText lastname;
    private EditText username;
    private EditText password;

    private AlertLoader loader;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg);
        firstname = findViewById(R.id.firstname);
        lastname = findViewById(R.id.lastname);
        username = findViewById(R.id.usernameReg);
        password = findViewById(R.id.passwordReg);

        loader = new AlertLoader(RegActivity.this, "Регистрация пользователя...");
    }


    public void onClickHome(View view) {
        finish();
    }


    public void onClickRegReg(View view) {
        //Проверка введенных данных
        if(!validate(username.getText().toString())) {
            Toast.makeText(RegActivity.this, "Некорректный логин", Toast.LENGTH_LONG).show();
            return;
        }
        if(password.getText().toString().length() > 8 || password.getText().toString().length() < 1) {
            Toast.makeText(RegActivity.this, "Некорректный пароль", Toast.LENGTH_LONG).show();
            return;
        }
        if(!validateLat(firstname.getText().toString())) {
            Toast.makeText(RegActivity.this, "Имя должно быть на латинице", Toast.LENGTH_LONG).show();
            return;
        }
        if(!validateLat(lastname.getText().toString())) {
            Toast.makeText(RegActivity.this, "Фамилия должна быть на латинице", Toast.LENGTH_LONG).show();
            return;
        }



        loader.start();

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                String postBody = "{" +
                        "\"firstname\": \""+ firstname.getText().toString() + "\"," +
                        "\"lastname\": \""+ lastname.getText().toString() + "\"," +
                        "\"email\": \""+ username.getText().toString() + "\"," +
                        "\"password\": \""+ password.getText().toString() + "\"" +
                        "}";

                HealBePostResponse hbPost = new HealBePostResponse(HealBePost.URL_REG, postBody);
                int code = hbPost.post();

                if (code == 200) {
                    Intent intent = new Intent(RegActivity.this, LoginActivity.class);
                    loader.stop(AlertLoader.STOP_REG);
                    startActivity(intent);
                    finish();
                }
                else {
                    loader.stop(AlertLoader.CRASH_REG);
                }
            }
        });

        thread.start();
    }


    // Проверка email
    public boolean validate(String emailStr) {
        Matcher matcher = LoginActivity.VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.find();
    }

    // проверка, только латинские символы
    public boolean validateLat(String str) {
        Matcher matcher = LoginActivity.VALID_LAT_REGEX.matcher(str);
        return matcher.find();
    }
}
