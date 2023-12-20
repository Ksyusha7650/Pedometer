// Класс ModelOptions для хранения настроек
package com.example.pedometer;

public class ModelOptions {
    int goalSteps = 0; // Целевое количество шагов
    double height = 0; // Рост
    double weight = 0; // Вес
    String name = ""; // Имя пользователя

    int countSteps1 = 0; // Количество шагов в 1 день
    int countSteps2 = 0; // Количество шагов во 2 день
    int countSteps3 = 0; // Количество шагов в 3 день
    int countSteps4 = 0; // Количество шагов в 4 день
    int countSteps5 = 0; // Количество шагов в 5 день
    int countSteps6 = 0; // Количество шагов в 6 день

    // Конструктор класса
    public ModelOptions(
            int goalSteps,
            double height,
            double weight,
            String name,
            int countSteps1,
            int countSteps2,
            int countSteps3,
            int countSteps4,
            int countSteps5,
            int countSteps6) {
        this.goalSteps = goalSteps;
        this.height = height;
        this.weight = weight;
        this.name = name;
        this.countSteps1 = countSteps1;
        this.countSteps2 = countSteps2;
        this.countSteps3 = countSteps3;
        this.countSteps4 = countSteps4;
        this.countSteps5 = countSteps5;
        this.countSteps6 = countSteps6;
    }
}