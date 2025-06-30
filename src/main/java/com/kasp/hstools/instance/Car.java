package com.kasp.hstools.instance;

import com.kasp.hstools.CarType;
import com.kasp.hstools.instance.cache.CarCache;

import java.util.HashMap;
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

    private Map<String, Integer> skinBonus = new HashMap<>();

    public Car(String name, CarType carType, int EarlyAcc, int midAcc, int topSpeed, int handling) {
        this.name = name;
        this.carType = carType;
        this.earlyAcc = EarlyAcc;
        this.midAcc = midAcc;
        this.topSpeed = topSpeed;
        this.handling = handling;
        this.drift = carType == CarType.STREET ? 15 : 10;
        this.grip = carType == CarType.SUPER ? 15 : 10;
        this.offroad = carType == CarType.SUV ? 15 : 10;
        this.boostDur = 10;
        this.boostStr = carType == CarType.MUSCLE ? 15 : 10;
        switch (carType) {
            case STREET -> skinBonus = Map.of("midacc", 6);
            case SUPER -> skinBonus = Map.of("handling", 6);
            case SUV -> skinBonus = Map.of("earlyacc", 6);
            case MUSCLE -> skinBonus = Map.of("topspeed", 6);
        }

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

    public String getName() {
        return name;
    }

    public String getNameFormatted() {
        return (name.substring(0, 1).toUpperCase() + name.substring(1)).replaceAll("_", " ");
    }

    public CarType getCarType() {
        return carType;
    }

    public int getEarlyAcc() {
        return earlyAcc;
    }

    public int getMidAcc() {
        return midAcc;
    }

    public int getTopSpeed() {
        return topSpeed;
    }

    public int getHandling() {
        return handling;
    }

    public int getDrift() {
        return drift;
    }

    public int getGrip() {
        return grip;
    }

    public int getOffroad() {
        return offroad;
    }

    public int getBoostDur() {
        return boostDur;
    }

    public int getBoostStr() {
        return boostStr;
    }

    public Map<String, Integer> getSkinBonus() {
        return skinBonus;
    }

    // for upgrades

    public int getEarlyAcc(int level, boolean skinBonus) {
        return getStat("earlyacc", earlyAcc, level, skinBonus);
    }

    public int getMidAcc(int level, boolean skinBonus) {
        return getStat("midacc", midAcc, level, skinBonus);
    }

    public int getTopSpeed(int level, boolean skinBonus) {
        return getStat("topspeed", topSpeed, level, skinBonus);
    }

    public int getHandling(int level, boolean skinBonus) {
        return getStat("handling", handling, level, skinBonus);
    }

    private int getStat(String statName, int baseValue, int level, boolean skinBonus) {
        double bonus = (skinBonus && this.skinBonus.containsKey(statName))
                ? (1 + this.skinBonus.get(statName) / 100.0)
                : 1;
        return (int) ((baseValue + 5 * level - 1) * bonus);
    }
}
