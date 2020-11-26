package com.healbe.healbe_example_andorid.pojo.fortest;
//package com.healbe.healbe_example_andorid.pojo;
//
//import com.fasterxml.jackson.annotation.JsonIgnore;
//import com.fasterxml.jackson.annotation.JsonProperty;
//import com.healbe.healbesdk.business_api.healthdata.Container;
//import com.healbe.healbesdk.business_api.healthdata.data.energy.EnergyData;
//import com.healbe.healbesdk.business_api.healthdata.data.heart.HeartData;
//import com.healbe.healbesdk.business_api.healthdata.data.sleep.SleepEpisode;
//import com.healbe.healbesdk.business_api.healthdata.data.sleep.SleepRecommendations;
//import com.healbe.healbesdk.business_api.healthdata.data.stress.StressData;
//import com.healbe.healbesdk.business_api.healthdata.data.stress.StressState;
//import com.healbe.healbesdk.business_api.healthdata.data.water.HydrationData;
//import com.healbe.healbesdk.business_api.healthdata.data.water.HydrationState;
//
//import java.util.List;
//
//public class HealthPack {
//
//    @JsonProperty("Mac")
//    private String mac;
//
//    @JsonProperty("CurrentStressLevel")
//    private Float currentStressLevel;
//
//    @JsonProperty("CurrentStressState")
//    private StressState currentStressState;
//
//    //Дата последенего сбора данных об энергии
//    @JsonProperty("LastEnergyData")
//    private long lastEnergyData;
//
//    //Баланс воды
//    @JsonProperty("HydrationState")
//    private HydrationState hydrationState;
//
//    //Данные о последнем напряжении
//    @JsonProperty("StressData")
//    private StressData stressData;
//
//    @JsonProperty("SleepRecommendations")
//    private Container<SleepRecommendations> sleepRecommendations;
//
//    @JsonProperty("EnergyData")
//    private List<EnergyData> energyData;
//
//    @JsonProperty("HeartData")
//    private List<HeartData> heartData;
//
//    @JsonProperty("SleepEpisode")
//    private List<SleepEpisode> sleepEpisodes;
//
//    @JsonProperty("StressListData")
//    private List<StressData> stressListData;
//
//    @JsonProperty("HydrationData")
//    private List<HydrationData> hydrationData;
//
//    //Нужно пустой конструктор
//    public HealthPack() {
//    }
//
//    public HealthPack(List<HydrationData> hydrationData, List<StressData> stressListData,List<SleepEpisode> slpe, List<HeartData> heartData, List<EnergyData> energyData, String mac, Float currentStressLevel, StressState currentStressState, long lastEnergyData, HydrationState hydrationState, StressData stressData, Container<SleepRecommendations> slpr) {
//        this.mac = mac;
//        this.currentStressLevel = currentStressLevel;
//        this.currentStressState = currentStressState;
//        this.lastEnergyData = lastEnergyData;
//        this.hydrationState = hydrationState;
//        this.stressData = stressData;
//        this.sleepRecommendations = slpr;
//        this.energyData = energyData;
//        this.heartData = heartData;
//        this.sleepEpisodes = slpe;
//        this.stressListData = stressListData;
//        this.hydrationData = hydrationData;
//    }
//
//    public String getMac() {
//        return mac;
//    }
//
//    public void setMac(String mac) {
//        this.mac = mac;
//    }
//
//    public Float getCurrentStressLevel() {
//        return currentStressLevel;
//    }
//
//    public void setCurrentStressLevel(Float currentStressLevel) {
//        this.currentStressLevel = currentStressLevel;
//    }
//
//    public StressState getCurrentStressState() {
//        return currentStressState;
//    }
//
//    public void setCurrentStressState(StressState currentStressState) {
//        this.currentStressState = currentStressState;
//    }
//
//    public long getLastEnergyData() {
//        return lastEnergyData;
//    }
//
//    public void setLastEnergyData(long lastEnergyData) {
//        this.lastEnergyData = lastEnergyData;
//    }
//
//    public HydrationState getHydrationState() {
//        return hydrationState;
//    }
//
//    public void setHydrationState(HydrationState hydrationState) {
//        this.hydrationState = hydrationState;
//    }
//
//    public StressData getStressData() {
//        return stressData;
//    }
//
//    public void setStressData(StressData stressData) {
//        this.stressData = stressData;
//    }
//
//    public Container<SleepRecommendations> getSleepRecommendations() {
//        return sleepRecommendations;
//    }
//
//    public void setSleepRecommendations(Container<SleepRecommendations> sleepRecommendations) {
//        this.sleepRecommendations = sleepRecommendations;
//    }
//
//    @Override
//    public String toString() {
//        return "HealthPack[Mac:" + mac + "]";
//    }
//
//    public List<EnergyData> getEnergyData() {
//        return energyData;
//    }
//
//    public void setEnergyData(List<EnergyData> energyData) {
//        this.energyData = energyData;
//    }
//
//    public List<HeartData> getHeartData() {
//        return heartData;
//    }
//
//    public void setHeartData(List<HeartData> heartData) {
//        this.heartData = heartData;
//    }
//
//    public List<SleepEpisode> getSleepEpisodes() {
//        return sleepEpisodes;
//    }
//
//    public void setSleepEpisodes(List<SleepEpisode> sleepEpisodes) {
//        this.sleepEpisodes = sleepEpisodes;
//    }
//
//    public List<StressData> getStressListData() {
//        return stressListData;
//    }
//
//    public void setStressListData(List<StressData> stressListData) {
//        this.stressListData = stressListData;
//    }
//
//    public List<HydrationData> getHydrationData() {
//        return hydrationData;
//    }
//
//    public void setHydrationData(List<HydrationData> hydrationData) {
//        this.hydrationData = hydrationData;
//    }
//}

public class HealbePack {

}