package com.healbe.healbe_example_andorid.pojo.acti;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.healbe.healbe_example_andorid.R;
import com.healbe.healbe_example_andorid.pojo.HealBePost;
import com.healbe.healbe_example_andorid.pojo.fortest.HealBePostResponse;

public class RegActivity extends AppCompatActivity {
    private EditText firstname;
    private EditText lastname;
    private EditText username;
    private EditText password;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg);
        firstname = findViewById(R.id.firstname);
        lastname = findViewById(R.id.lastname);
        username = findViewById(R.id.usernameReg);
        password = findViewById(R.id.passwordReg);

    }


    public void onClickHome(View view) {
        finish();
    }


    public void onClickRegReg(View view) {
        String postBody = "{" +
                "\"firstname\": \""+ firstname.getText().toString() + "\"," +
                "\"lastname\": \""+ lastname.getText().toString() + "\"," +
                "\"email\": \""+ username.getText().toString() + "\"," +
                "\"password\": \""+ password.getText().toString() + "\"" +
                "}";

        HealBePostResponse hbPost = new HealBePostResponse(HealBePost.URL_REG, postBody);
        int code = hbPost.post();

        if (code == 200) {
            Toast.makeText(RegActivity.this, "Вы успешно зарегистрировались!", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(RegActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
        else {
            Toast.makeText(RegActivity.this, hbPost.getResponseBody(), Toast.LENGTH_LONG).show();
        }


    }
}
