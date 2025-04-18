package com.kasp.hstools.instance;

import com.kasp.hstools.CarType;
import com.kasp.hstools.instance.cache.CarCache;

import java.util.Map;

public class Car {

    private final String name;
    private final CarType carType;

    private final int earlyAcc;
    private final int midAcc;
    private final int topSpeed;
    private final int handling;

    private final int drift;
    private final int grip;
    private final int offroad;
    private final int boostDur;
    private final int boostStr;

    private final Map<String, Integer> skinBonus;

    public Car(String name, CarType carType, int EarlyAcc, int midAcc, int topSpeed, int handling, int drift, int grip, int offroad, int boostDur, int boostStr, Map<String, Integer> skinBonus) {
        this.name = name;
        this.carType = carType;
        this.earlyAcc = EarlyAcc;
        this.midAcc = midAcc;
        this.topSpeed = topSpeed;
        this.handling = handling;
        this.drift = drift;
        this.grip = grip;
        this.offroad = offroad;
        this.boostDur = boostDur;
        this.boostStr = boostStr;
        this.skinBonus = skinBonus;

        CarCache.initializeCar(this);
    }

    public String formatSkinBonus() {
        String statistic;
        switch (skinBonus.keySet().stream().findFirst().get()) {
            case "earlyacc" -> statistic = "Early Acc";
            case "midacc" -> statistic = "Mid Acc";
            case "topspeed" -> statistic = "Top Speed";
            case "handling" -> statistic = "Handling";
            default -> statistic = "unknown";
        }
        
        return skinBonus.values().stream().findFirst().get() + "% " + statistic;
    }

    public String getName() { return name; }

    public CarType getCarType() { return carType; }

    public int getEarlyAcc() { return earlyAcc; }

    public int getMidAcc() { return midAcc; }

    public int getTopSpeed() { return topSpeed; }

    public int getHandling() { return handling; }

    public int getDrift() { return drift; }

    public int getGrip() { return grip; }

    public int getOffroad() { return offroad; }

    public int getBoostDur() { return boostDur; }

    public int getBoostStr() { return boostStr; }

    public Map<String, Integer> getSkinBonus() { return skinBonus; }
}
