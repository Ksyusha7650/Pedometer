package com.example.pedometer;

import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class SensorsActivity extends AppCompatActivity {
    private TextView textViewSensors;
    List<Sensor> sensors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensors);
        textViewSensors = findViewById(R.id.textViewSensors);
        addSensor();
    }
    // Метод для добавления сенсоров и получения списка доступных сенсоров
    private void addSensor() {
        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE); // Получаем доступ к сервису SensorManager
        sensors = sensorManager.getSensorList(Sensor.TYPE_ALL); // Получаем список доступных сенсоров всех типов
        StringBuilder sb = new StringBuilder();
        for (Sensor sensor : sensors) {
            // Создаем строку с информацией о каждом сенсоре
            sb.append(getString(R.string.nameSensor)).append(sensor.getName()) // Имя сенсора
                    .append(getString(R.string.type)).append(sensor.getType()) // Тип сенсора
                    .append(getString(R.string.maufacturer)).append(sensor.getVendor()) // Производитель
                    .append(getString(R.string.maxValue)).append(sensor.getMaximumRange()) // Максимальное значение
                    .append(getString(R.string.Resolution)).append(sensor.getResolution()) // Разрешение
                    .append("\n--------------------------------------\n");
        }
        textViewSensors.setText(sb); // Устанавливаем текст с информацией о сенсорах в TextView
    }

}