package com.healbe.healbe_example_andorid.pojo.tools;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.healbe.healbe_example_andorid.pojo.tools.ExtendStateData;
import com.healbe.healbesdk.business_api.healthdata.data.energy.EnergyData;
import com.healbe.healbesdk.business_api.healthdata.data.heart.HeartData;
import com.healbe.healbesdk.business_api.healthdata.data.stress.StressData;
import com.healbe.healbesdk.business_api.healthdata.data.water.HydrationData;

import java.util.List;

public class HealthPack {

    @JsonProperty("stateData")
    private ExtendStateData extendStateData;

    @JsonProperty("energyData")
    private List<EnergyData> energyData;

    @JsonProperty("heartData")
    private List<HeartData> heartData;

    @JsonProperty("hydrationData")
    private List<HydrationData> hydrationData;

    @JsonProperty("stressData")
    private List<StressData> stressListData;


    //Нужно пустой конструктор
    public HealthPack() {

    }


    @Override
    public String toString() {
        return "HP";
    }

    public void setExtendStateData(ExtendStateData extendStateData) {
        this.extendStateData = extendStateData;
    }
    public void setEnergyData(List<EnergyData> energyData) {
        this.energyData = energyData;
    }
    public void setHeartData(List<HeartData> heartData) {
        this.heartData = heartData;
    }
    public void setHydrationData(List<HydrationData> hydrationData) {
        this.hydrationData = hydrationData;
    }
    public void setStressListData(List<StressData> stressListData) {
        this.stressListData = stressListData;
    }

}
