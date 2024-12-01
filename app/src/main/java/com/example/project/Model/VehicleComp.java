package com.example.project.Model;

import java.util.Comparator;

public class VehicleComp implements Comparator<Vehicle> {
    @Override
    public int compare(Vehicle o1, Vehicle o2) {
       return Double.compare(o1.getPrice(), o2.getPrice());
    }

    @Override
    public Comparator<Vehicle> reversed() {
        return Comparator.super.reversed();
    }
}
