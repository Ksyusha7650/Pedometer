package com.example.pedometer;

public class ModelOptions {
    int goalSteps = 0;
    double height = 0;
    double weight = 0;
    String name = "";


    public ModelOptions(int goalSteps, double height, double weight, String name) {
        this.goalSteps = goalSteps;
        this.height = height;
        this.weight = weight;
        this.name = name;
    }
}
