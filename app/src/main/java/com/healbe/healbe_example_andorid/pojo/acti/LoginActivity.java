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
import com.healbe.healbe_example_andorid.dashboard.DashboardActivity;
import com.healbe.healbe_example_andorid.pojo.HealBePost;
import com.healbe.healbe_example_andorid.pojo.fortest.HealBePostResponse;
import com.healbe.healbe_example_andorid.pojo.tools.AuthenticateResponse;

public class LoginActivity extends AppCompatActivity {
    private EditText username;
    private EditText password;
    private AuthenticateResponse authRes;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = (EditText) findViewById(R.id.usernameReg);
        password = (EditText) findViewById(R.id.passwordReg);

    }


    public void onClickHome(View view) {
        finish();
    }

    public void onClickLogin(View view) {
        String postBody = "{\n" +
                "  \"password\": \""+ password.getText().toString() +"\",\n" +
                "  \"email\": \""+ username.getText().toString() + "\"\n" +
                "}";

        HealBePostResponse hbPost = new HealBePostResponse(HealBePost.URL_AUTHO, postBody);
        int code = hbPost.post();
        //также нужно будет собирать остальную информацию из ответа, помимо токена
        if(code == 200) {
            ObjectMapper mapper = new ObjectMapper();
            try {
                authRes = mapper.readValue(hbPost.getResponseBody(), AuthenticateResponse.class);
                Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
                intent.putExtra(HealBePost.EXTRA_TOKEN, authRes.getJwtToken());
                startActivity(intent);
                finish();
            } catch (JsonProcessingException e) {
                Toast.makeText(LoginActivity.this, "Такого не должно быть", Toast.LENGTH_LONG).show();
            }
        }
        else if(code == 400)
            Toast.makeText(LoginActivity.this, "Неверный логин или пароль", Toast.LENGTH_LONG).show();
        else
            Toast.makeText(LoginActivity.this, hbPost.getResponseBody(), Toast.LENGTH_LONG).show();

    }

    //восстановить пароль
    public void onClickRefresh(View view) {

    }

}
