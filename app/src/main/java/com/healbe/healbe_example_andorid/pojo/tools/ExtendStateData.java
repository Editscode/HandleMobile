package com.healbe.healbe_example_andorid.pojo.tools;

import com.healbe.healbesdk.business_api.healthdata.data.stress.StressState;
import com.healbe.healbesdk.business_api.healthdata.data.water.HydrationState;

public class ExtendStateData {
    public String mac;
    public StressState currentStressState;
    public float currentStressLevel;
    public long lastEnergyData;
    public HydrationState hydrationState;

    public ExtendStateData() {

    }

    public ExtendStateData(String mac, StressState currentStressState, float currentStressLevel, long lastEnergyData, HydrationState hydrationState) {
        this.mac = mac;
        this.currentStressState = currentStressState;
        this.currentStressLevel = currentStressLevel;
        this.lastEnergyData = lastEnergyData;
        this.hydrationState = hydrationState;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public StressState getCurrentStressState() {
        return currentStressState;
    }

    public void setCurrentStressState(StressState currentStressState) {
        this.currentStressState = currentStressState;
    }

    public float getCurrentStressLevel() {
        return currentStressLevel;
    }

    public void setCurrentStressLevel(float currentStressLevel) {
        this.currentStressLevel = currentStressLevel;
    }

    public long getLastEnergyData() {
        return lastEnergyData;
    }

    public void setLastEnergyData(long lastEnergyData) {
        this.lastEnergyData = lastEnergyData;
    }

    public HydrationState getHydrationState() {
        return hydrationState;
    }

    public void setHydrationState(HydrationState hydrationState) {
        this.hydrationState = hydrationState;
    }


}
