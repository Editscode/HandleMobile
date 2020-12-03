package com.healbe.healbe_example_andorid.pojo.acti;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.material.tabs.TabLayout;
import com.healbe.healbe_example_andorid.R;
import com.healbe.healbe_example_andorid.pojo.ConverterJson;
import com.healbe.healbe_example_andorid.pojo.fortest.HealBePost;
import com.healbe.healbe_example_andorid.pojo.HealBePostResponse;
import com.healbe.healbe_example_andorid.pojo.frag.CalendarFragment;
import com.healbe.healbe_example_andorid.pojo.frag.StatusFragment;
import com.healbe.healbe_example_andorid.pojo.frag.SyncFragment;
import com.healbe.healbe_example_andorid.pojo.tools.AlertLoader;
import com.healbe.healbe_example_andorid.pojo.tools.AuthenticateResponse;
import com.healbe.healbe_example_andorid.pojo.tools.ExtendStateData;
import com.healbe.healbe_example_andorid.pojo.tools.HealthPack;
import com.healbe.healbesdk.business_api.HealbeSdk;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class GeneralActivity extends AppCompatActivity {

    private AuthenticateResponse user;
    private TextView userName;
    private TextView textInfo;

    private ViewPager viewPager;
    private TabLayout tabLayout;

    private StatusFragment statusFragment;
    private CalendarFragment calendarFragment;
    private SyncFragment syncFragment;

    private AlertLoader loader;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general);
        /*Забираем пользователя*/
        Intent intent = getIntent();
        try {
            user = new ObjectMapper().readValue(intent.getStringExtra(HealBePost.EXTRA_USER), AuthenticateResponse.class);
        } catch (JsonProcessingException e) { Toast.makeText(GeneralActivity.this, "Такого не должно быть -> \n" + e.getMessage(), Toast.LENGTH_LONG).show(); }
        userName = (TextView) findViewById(R.id.textUserName);
        userName.setText(user.getFirstName() + " " + user.getLastName());


        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        viewPager = (ViewPager) findViewById(R.id.view_pager);

        statusFragment = new StatusFragment();
        calendarFragment = new CalendarFragment();
        syncFragment = new SyncFragment();




        tabLayout.setupWithViewPager(viewPager);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), 0);
        viewPagerAdapter.addFragment(statusFragment, "Состояние");
        viewPagerAdapter.addFragment(calendarFragment, "Календарь");
        viewPagerAdapter.addFragment(syncFragment, "Синхронизация");
        viewPager.setAdapter(viewPagerAdapter);

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
        loader = new AlertLoader(GeneralActivity.this, "Сбор данных...");
        loader.start();

        hp = new HealthPack();
        exData = new ExtendStateData();

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

            textInfo = (TextView) findViewById(R.id.textViewINFO);
            textInfo.setText("Новые данные cобраны " + ConverterJson.getLastTimeCreateJson());
//            /*Удаляется бонусный файл*/
            ConverterJson.deleteBonus();

            loader.stop(AlertLoader.END_JSON);
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("CheckResult")
    public void onClickSUBMIT(View view) {
        textInfo = (TextView) findViewById(R.id.textViewINFO);
        if(textInfo.getText().toString().indexOf("отправлены") != -1) {
            Toast.makeText(GeneralActivity.this, "Новые данные не собраны", Toast.LENGTH_LONG).show();
            return;
        }


        loader = new AlertLoader(GeneralActivity.this, "Отправка данных...");
        loader.start();


        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    jsonString = ConverterJson.readUsingFiles(Environment.getExternalStorageDirectory() + "/" + File.separator + "healthPack.json");
                } catch (IOException e) {
                    e.printStackTrace();
                }

                HealBePostResponse healBePost = new HealBePostResponse(HealBePost.URL_PACK, jsonString);
                healBePost.setToken("Bearer " + user.getJwtToken());
                int code = healBePost.post();

                if(code == 200) {
                    loader.stop(AlertLoader.END_SUBMIT);

                    /*Создание бонусного файла после отправки*/
                    ConverterJson.createBonus();
                    textInfo.setText("Данные были cобраны " + ConverterJson.getLastTimeCreateJson() + "\n и отправлены.");
                }
                else {
                    loader.stop(AlertLoader.CRASH_SUBMIT);
                    textInfo.setText(healBePost.getResponseBody());
                }
            }
        });

        thread.start();
    }



    private class ViewPagerAdapter extends FragmentPagerAdapter {

        private List<Fragment> fragments = new ArrayList<>();
        private List<String> fragmentTitle = new ArrayList<>();

        public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
            super(fm, behavior);
        }


        public void addFragment(Fragment fragment, String title) {
            fragments.add(fragment);
            fragmentTitle.add(title);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentTitle.get(position);
        }
    }

    public AuthenticateResponse getUser() {
        return user;
    }

}
