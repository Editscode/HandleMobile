package com.healbe.healbe_example_andorid.dashboard;

import android.os.Environment;
import android.util.JsonWriter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.healbe.healbesdk.business_api.healthdata.data.DaySummary;
import com.healbe.healbesdk.business_api.healthdata.data.stress.StressData;
import com.healbe.healbesdk.business_api.healthdata.data.stress.StressState;
import com.healbe.healbesdk.business_api.healthdata.data.water.HydrationState;
import com.healbe.healbesdk.business_api.tasks.entity.HeartRate;
import com.healbe.healbesdk.business_api.user.data.WeightUnits;
import com.healbe.healbesdk.business_api.healthdata.data.heart.BloodPressure;
import com.healbe.healbesdk.business_api.healthdata.data.heart.HeartData;
import com.healbe.healbesdk.business_api.healthdata.data.heart.HeartSummary;
import com.healbe.healbesdk.device_api.ClientState;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.reactivestreams.Subscriber;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;

import javax.xml.transform.stream.StreamResult;

import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;

@SuppressWarnings({"WeakerAccess", "unused"})
class SummaryInfo {
    private  Flowable<List<HeartData>> healthData ;
    private ClientState clientState;
    private boolean onHand;
    private DaySummary daySummary;
    private HeartRate heartRate;
    private HydrationState hydrationState;
    private StressState stressState;
    private float stressLevel;
    private WeightUnits weightUnits;

    public SummaryInfo() {
        this.clientState = ClientState.DISCONNECTED;
        this.onHand = false;
        this.daySummary = new DaySummary();
        this.heartRate = new HeartRate();
        this.hydrationState = HydrationState.NO_DATA;
        this.stressState = StressState.NO_DATA;
        this.healthData = new Flowable<List<HeartData>>() {
            @Override
            protected void subscribeActual(Subscriber<? super List<HeartData>> s) {

            }
        };
    }

    public SummaryInfo(HeartData data,ClientState clientState, boolean onHand, DaySummary daySummary, HeartRate heartRate, HydrationState hydrationState, StressState stressState, float stressLevel,Flowable<List<HeartData>> healthData) {
        this.clientState = clientState;
        this.onHand = onHand;
        this.daySummary = daySummary;
        this.heartRate = heartRate;
        this.hydrationState = hydrationState;
        this.stressState = stressState;
        this.stressLevel = stressLevel;
        this.healthData = healthData;
    }


    public SummaryInfo(ClientState clientState, boolean onHand, DaySummary daySummary, HeartRate heartRate) {
        this.clientState = clientState;
        this.onHand = onHand;
        this.daySummary = daySummary;
        this.heartRate = heartRate;
    }

    public boolean isConnected() {
        return clientState == ClientState.READY;
    }

    public ClientState getClientState() {
        return clientState;
    }

    public void setClientState(ClientState clientState) {
        this.clientState = clientState;
    }

    public boolean isOnHand() {
        return onHand;
    }

    public void setOnHand(boolean onHand) {
        this.onHand = onHand;
    }

    public DaySummary getDaySummary() {
        return daySummary;
    }

    public void setDaySummary(DaySummary daySummary) {
        this.daySummary = daySummary;
    }

    public HeartRate getHeartRate() {
        return heartRate;
    }

    public void setHeartRate(HeartRate heartRate) {
        this.heartRate = heartRate;
    }
public  class HeartDataObject{
        public String mac;
        public List<HeartData> heartData;
        public HeartDataObject(String mac,List<HeartData> data){
            this.mac = mac;
            this.heartData = data;
        }

}
    public void setHeartData(Flowable<List<HeartData>> healthDat){
        healthDat.subscribe(new Consumer<List<HeartData>>() {
            @Override
            public void accept(List<HeartData> heartData) throws Exception {
                File file = new File(Environment.getExternalStorageDirectory() + "/" + File.separator + "HeartData.json");
                file.createNewFile();
                if(file.exists()) {
                    OutputStream fo = new FileOutputStream(file);
                    //StreamResult result = new StreamResult(new File(android.os.Environment.getExternalStorageDirectory(), "upload_data.xml"));

                    String json = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().
                            create().toJson(heartData);
                        fo.write(json.getBytes());

                }
            }
        });
        this.healthData = healthDat;
    }

    public Flowable<List<HeartData>> getHearthData(){
        return  healthData;
    }
    public HydrationState getHydrationState() {
        return hydrationState;
    }

    public void setHydrationState(HydrationState hydrationState) {
        this.hydrationState = hydrationState;
    }

    public StressState getStressState() {
        return stressState;
    }

    public void setStressState(StressState stressState) {
        this.stressState = stressState;
    }

    public float getStressLevel() {
        return stressLevel;
    }

    public void setStressLevel(float stressLevel) {
        this.stressLevel = stressLevel;
    }

    public WeightUnits getWeightUnits() {
        return weightUnits;
    }

    public void setWeightUnits(WeightUnits weightUnits) {
        this.weightUnits = weightUnits;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SummaryInfo that = (SummaryInfo) o;
        return onHand == that.onHand &&
                Float.compare(that.stressLevel, stressLevel) == 0 &&
                clientState == that.clientState &&
                Objects.equals(daySummary, that.daySummary) &&
                Objects.equals(heartRate, that.heartRate) &&
                hydrationState == that.hydrationState &&
                stressState == that.stressState &&
                weightUnits == that.weightUnits &&
                Objects.equals(healthData, that.healthData);
    }

    @Override
    public int hashCode() {

        return Objects.hash(clientState, onHand, daySummary, heartRate, hydrationState, stressState, stressLevel, weightUnits);
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public String toString() {
        return "SummaryInfo{" +
                "clientState=" + clientState +
                ", onHand=" + onHand +
                ", daySummary=" + daySummary +
                ", heartRate=" + heartRate +
                ", hydrationState=" + hydrationState +
                ", stressState=" + stressState +
                ", stressLevel=" + stressLevel +
                ", weightUnits=" + weightUnits +
                '}';
    }
}
