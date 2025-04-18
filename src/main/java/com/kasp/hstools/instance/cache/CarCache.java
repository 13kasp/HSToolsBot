package com.kasp.hstools.instance.cache;

import com.kasp.hstools.instance.Car;
import com.kasp.hstools.instance.HSUser;

import java.util.HashMap;
import java.util.Map;

public class CarCache {

    private static HashMap<String, Car> cars = new HashMap<>();

    public static Car getCar(String name) {
        return cars.get(name);
    }

    public static void addCar(Car car) {
        cars.put(car.getName(), car);

        System.out.println("Car " + car.getName() + " has been loaded into memory");
    }

    public static void removeCar(Car car) {
        cars.remove(car.getName());
    }

    public static boolean containsCar(Car car) {
        return cars.containsValue(car);
    }

    public static void initializeCar(Car car) {
        if (!containsCar(car))
            addCar(car);
    }

    public static Map<String, Car> getCars() {
        return cars;
    }
}
