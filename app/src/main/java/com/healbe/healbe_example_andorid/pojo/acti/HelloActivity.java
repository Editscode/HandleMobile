package com.healbe.healbe_example_andorid.pojo.acti;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.healbe.healbe_example_andorid.R;

public class HelloActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hello);

    }


    public void onAutho(View view) {
        Intent intent = new Intent(HelloActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    public void onReg(View view) {
        Intent intent = new Intent(HelloActivity.this, RegActivity.class);
        startActivity(intent);
    }

    public void onLicense(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(HelloActivity.this);
        builder
                .setTitle("Лицензионное соглашение")
                .setMessage("Текст из файла license.txt")
                .setPositiveButton("Принять", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                })
                .setNegativeButton("Отклонить", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                        dialogInterface.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
}
