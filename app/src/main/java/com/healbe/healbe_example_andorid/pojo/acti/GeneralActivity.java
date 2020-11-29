package com.healbe.healbe_example_andorid.pojo.acti;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.healbe.healbe_example_andorid.R;
import com.healbe.healbe_example_andorid.dashboard.DashboardActivity;
import com.healbe.healbe_example_andorid.pojo.ConverterJson;
import com.healbe.healbe_example_andorid.pojo.HealBePost;
import com.healbe.healbe_example_andorid.pojo.fortest.HealBePostResponse;
import com.healbe.healbe_example_andorid.pojo.tools.AuthenticateResponse;
import com.healbe.healbe_example_andorid.pojo.tools.ExtendStateData;
import com.healbe.healbe_example_andorid.pojo.tools.HealthPack;
import com.healbe.healbesdk.business_api.HealbeSdk;

import java.io.File;
import java.io.IOException;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;

public class GeneralActivity extends AppCompatActivity {
    private AuthenticateResponse user;
    private TextView userName;
    private TabHost tabHost;
    private TabHost.TabSpec tab1;
    private TabHost.TabSpec tab2;
    private TabHost.TabSpec tab3;

    CompositeDisposable destroy = new CompositeDisposable();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general);

        /*Забираем пользователя*/
        Intent intent = getIntent();
        try {
            user = new ObjectMapper().readValue(intent.getStringExtra(HealBePost.EXTRA_USER), AuthenticateResponse.class);
        } catch (JsonProcessingException e) {
            Toast.makeText(GeneralActivity.this, "Такого не должно быть -> \n" + e.getMessage(), Toast.LENGTH_LONG).show();
        }

        userName = (TextView) findViewById(R.id.textUserName);
        userName.setText(user.getFirstName() + " " + user.getLastName());

        TextView textView = (TextView) findViewById(R.id.textViewINFO);
        if(ConverterJson.checkToSubmit())
            textView.setText("Данные были cобраны " + ConverterJson.getLastTimeCreateJson() + " и отправлены.");
        else
            textView.setText("Новые данные cобраны " + ConverterJson.getLastTimeCreateJson());

        //TabHost
        createTab();


        Toast.makeText(GeneralActivity.this, user.toString(), Toast.LENGTH_LONG).show();
    }

    public void onClickHome(View view) {
        Intent intent = new Intent(GeneralActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private static HealthPack hp = null;
    private static ExtendStateData exData = null;
    private static String jsonString = "";
    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("CheckResult")
    public void onClickJSON(View view) {
        hp = new HealthPack();
        exData = new ExtendStateData();
        // userId


        Single.zip(
                HealbeSdk.get().GOBE.get(),                                             //MAC
                HealbeSdk.get().ARCHIVE.getCurrentStressLevel().firstOrError(),         //CurrentStressLevel
                HealbeSdk.get().ARCHIVE.getCurrentStressState().firstOrError(),         //CurrentStressState
                HealbeSdk.get().ARCHIVE.getEnergyDataMaxTimestamp().firstOrError(),     //EnergyDataMaxTimestamp
                HealbeSdk.get().ARCHIVE.getHydrationState().firstOrError(),             //HydrationState
                HealbeSdk.get().ARCHIVE.getEnergyData(1).firstOrError(),             //EnergyData
                HealbeSdk.get().ARCHIVE.getHeartData(1).firstOrError(),              //HeartData
                HealbeSdk.get().ARCHIVE.getStressData(1).firstOrError(),             //StressData
                HealbeSdk.get().ARCHIVE.observeHydrationData(1).firstOrError(),      //HydrationData
                (mac, csl, css, edmt, hs, ed, hd, sd, hydd) -> {
                    exData.setMac(mac.getMac());
                    exData.setCurrentStressLevel(csl);
                    exData.setCurrentStressState(css);
                    exData.setLastEnergyData(edmt);
                    exData.setHydrationState(hs);
                    hp.setExtendStateData(exData);
                    hp.setEnergyData(ed);
                    hp.setHeartData(hd);
                    hp.setStressListData(sd);
                    hp.setHydrationData(hydd);
                    return "sdf";
                }
        ).observeOn(AndroidSchedulers.mainThread()).subscribeOn(AndroidSchedulers.mainThread()).subscribe(s -> {
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    jsonString = ConverterJson.toJSON(hp);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            AlertDialog.Builder builder = new AlertDialog.Builder(GeneralActivity.this);
            builder
                    .setTitle("Собрано:")
                    .setMessage(ConverterJson.getLastTimeCreateJson() + "\n" + exData.getMac() +"\n" + exData.getCurrentStressLevel() + "\n" + exData.getCurrentStressState().toString())
                    .setPositiveButton("Закрыть", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
            AlertDialog alertPi = builder.create();
            alertPi.show();

            TextView textView = (TextView) findViewById(R.id.textViewINFO);
            textView.setText("Новые данные cобраны " + ConverterJson.getLastTimeCreateJson());
//            /*Удаляется бонусный файл*/
            ConverterJson.deleteBonus();
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("CheckResult")
    public void onClickSUBMIT(View view) {
        TextView textView = (TextView) findViewById(R.id.textViewINFO);
        if(textView.getText().toString().indexOf("отправлены") != -1) {
            Toast.makeText(GeneralActivity.this, "Новые данные не собраны", Toast.LENGTH_LONG).show();
            return;
        }

        try {
            jsonString = ConverterJson.readUsingFiles(Environment.getExternalStorageDirectory() + "/" + File.separator + "healthPack.json");
        } catch (IOException e) {
            e.printStackTrace();
        }

        HealBePostResponse healBePost = new HealBePostResponse(HealBePost.URL_PACK, jsonString);
        healBePost.setToken("Bearer " + user.getJwtToken());
        int code = healBePost.post();

        if(code == 200) {
            AlertDialog.Builder builder = new AlertDialog.Builder(GeneralActivity.this);
            builder
                    .setTitle("Отправлено:")
                    .setMessage(healBePost.getResponseBody())
                    .setPositiveButton("Закрыть", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
            AlertDialog alertPi = builder.create();
            alertPi.show();

            /*Создание бонусного файла после отправки*/
            ConverterJson.createBonus();

            textView.setText("Данные были cобраны " + ConverterJson.getLastTimeCreateJson() + "\n и отправлены.");
        }
        else {
            textView.setText(healBePost.getResponseBody());
        }
    }

    private void createTab() {
        tabHost = (TabHost) findViewById(android.R.id.tabhost);
        tabHost.setup();


        // создаем вкладку и указываем тег
        tab1 = tabHost.newTabSpec("tag1");

        View v = getLayoutInflater().inflate(R.layout.tab_header_one, null);
        // название вкладки
        tab1.setIndicator(v);
        // указываем id компонента из FrameLayout, он и станет содержимым
        tab1.setContent(R.id.tab1);
        // добавляем в корневой элемент
        tabHost.addTab(tab1);

        tab2 = tabHost.newTabSpec("tag2");

        v = getLayoutInflater().inflate(R.layout.tab_header_two, null);
        tab2.setIndicator(v);
        tab2.setContent(R.id.tab2);
        tabHost.addTab(tab2);

        tab3 = tabHost.newTabSpec("tag3");
        v = getLayoutInflater().inflate(R.layout.tab_header_three, null);
        tab3.setIndicator(v);
        tab3.setContent(R.id.tab3);
        tabHost.addTab(tab3);

        tabHost.setCurrentTabByTag("tag1");

        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String s) {
                Toast.makeText(getBaseContext(), "tabId = " + s, Toast.LENGTH_SHORT).show();
            }
        });
    }

}
