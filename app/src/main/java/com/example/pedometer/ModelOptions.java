// Класс ModelOptions для хранения настроек
package com.example.pedometer;

public class ModelOptions {
    int goalSteps = 0; // Целевое количество шагов
    double height = 0; // Рост
    double weight = 0; // Вес
    String name = ""; // Имя пользователя

    // Конструктор класса
    public ModelOptions(int goalSteps, double height, double weight, String name) {
        this.goalSteps = goalSteps;
        this.height = height;
        this.weight = weight;
        this.name = name;
    }
}