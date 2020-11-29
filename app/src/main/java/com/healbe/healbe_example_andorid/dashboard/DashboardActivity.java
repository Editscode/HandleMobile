package com.healbe.healbe_example_andorid.dashboard;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.healbe.healbe_example_andorid.R;
import com.healbe.healbe_example_andorid.connect.ConnectActivity;
import com.healbe.healbe_example_andorid.enter.EnterActivity;
import com.healbe.healbe_example_andorid.pojo.ConverterJson;
import com.healbe.healbe_example_andorid.pojo.HealBePost;
import com.healbe.healbe_example_andorid.pojo.tools.HealthPack;
import com.healbe.healbe_example_andorid.pojo.tools.ExtendStateData;
import com.healbe.healbe_example_andorid.tools.BaseActivity;
import com.healbe.healbe_example_andorid.tools.SystemBarManager;
import com.healbe.healbesdk.business_api.HealbeSdk;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.core.app.ActivityOptionsCompat;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import timber.log.Timber;

public class DashboardActivity extends BaseActivity {
    private String TOKEN;

    CompositeDisposable destroy = new CompositeDisposable();
    Menu mainMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        initPadding();
        ActionBar appBar = getSupportActionBar();


        /*Забираем токен*/
        Intent intent = getIntent();
        TOKEN = intent.getStringExtra(HealBePost.EXTRA_TOKEN);

        if (appBar != null)
            appBar.setTitle(R.string.dashboard);

        // only dashboard for now
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, DashboardFragment.newInstance())
                .commit();

    }

    private void initPadding() {
        SystemBarManager tintManager = new SystemBarManager(this);
        SystemBarManager.SystemBarConfig config = tintManager.getConfig();
        findViewById(R.id.placeholder).setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, config.getPixelInsetTop(true)));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (menu != null && menu.size() != 0)
            menu.clear();
        getMenuInflater().inflate(R.menu.main_menu, menu);
        mainMenu = menu;

        updateMenu();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.logout:
                logout();
                return true;
            case R.id.connect:
                connectOrDisconnect();
                return true;
            case R.id.find:
                findAnother();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void logout() {
        destroy.add(HealbeSdk.get().USER.logout() // logout disconnecting wb itself
                .subscribe(this::goEnter, Timber::e));
    }
    void updateMenu() {
        if (mainMenu != null) {
            boolean connectionActive = HealbeSdk.get().GOBE.isConnectionStarted();
            mainMenu.findItem(R.id.connect).setTitle(connectionActive ? R.string.disconnect : R.string.connect);
            mainMenu.findItem(R.id.find).setVisible(!connectionActive);
        }
    }
    private void connectOrDisconnect() {
        if (HealbeSdk.get().GOBE.isConnectionStarted())
            destroy.add(HealbeSdk.get().GOBE.disconnect()
                    .andThen(HealbeSdk.get().GOBE.setActive(false)) // for next time connect will start from search
                    .subscribe(this::updateMenu, Timber::e)); // stay here, just update menu
        else
            destroy.add(HealbeSdk.get().GOBE.connect()
                    .subscribe(this::updateMenu, Timber::e));
    }
    private void findAnother() {
        destroy.add(HealbeSdk.get().GOBE.disconnect()
                .andThen(HealbeSdk.get().GOBE.setActive(false)) // for next time connect will start from search
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::goConnect, Timber::e)); // go search
    }
    private void goEnter() {
        Bundle bundle = ActivityOptionsCompat.makeCustomAnimation(this,
                android.R.anim.fade_in, android.R.anim.fade_out).toBundle();
        startActivity(new Intent(this, EnterActivity.class), bundle);
        finish();
    }
    private void goConnect() {
        Bundle bundle = ActivityOptionsCompat.makeCustomAnimation(this,
                android.R.anim.fade_in, android.R.anim.fade_out).toBundle();
        startActivity(new Intent(this, ConnectActivity.class), bundle);
        finish();
    }



    private static HealthPack hp = null;
    private static ExtendStateData exData = null;
    private static String jsonString = "";
    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("CheckResult")
    public void onBasicParam(View view) {
//        Date date =new Date();
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//        String str = sdf.format(date);
//
//        if(true) {
//            if(str.equals(ConverterJson.getLastTimeCreateJson()))
//                Toast.makeText(DashboardActivity.this, str + " " + ConverterJson.getLastTimeCreateJson(), Toast.LENGTH_LONG).show();
//
//            return;
//        }

        //hp = new HealthPack();
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

            AlertDialog.Builder builder = new AlertDialog.Builder(DashboardActivity.this);
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

            TextView textView = (TextView) findViewById(R.id.textResponse);
            textView.setText("Новые данные cобраны " + ConverterJson.getLastTimeCreateJson());
//            /*Удаляется бонусный файл*/
            ConverterJson.deleteBonus();
        });

    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("CheckResult")
    public void submitClick(View view) {
        TextView textView = (TextView) findViewById(R.id.textResponse);
        if(textView.getText().toString().indexOf("отправлены") != -1) {
            Toast.makeText(DashboardActivity.this, "Новые данные не собраны", Toast.LENGTH_LONG).show();
            return;
        }

        try {
            jsonString = ConverterJson.readUsingFiles(Environment.getExternalStorageDirectory() + "/" + File.separator + "healthPack.json");
        } catch (IOException e) {
            e.printStackTrace();
        }

        HealBePost healBePost = new HealBePost(HealBePost.URL_PACK, jsonString);
        healBePost.setToken("Bearer " + TOKEN);
        String str = healBePost.post();

        AlertDialog.Builder builder = new AlertDialog.Builder(DashboardActivity.this);
        builder
                .setTitle("Отправлено:")
                .setMessage(str)
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



    @SuppressLint("CheckResult")
    public void clickTestButton(View view)
    {
        TextView  textView = (TextView) findViewById(R.id.textResponse);
        textView.setText(TOKEN);
    }

}
