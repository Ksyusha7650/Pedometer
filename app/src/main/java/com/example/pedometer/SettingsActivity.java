package com.example.pedometer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class SettingsActivity extends AppCompatActivity {

    EditText editTextGoalSteps, editTextName, editTextHeight, editTextWeight;
    TextView textViewName;
    int goalSteps;
    double height, weight;
    String name;
    Button buttonEdit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        connectUIElementsToCodeBehind();
        getOptions();
        updateValues();
    }

    // Метод для связывания элементов пользовательского интерфейса с переменными класса
    private void connectUIElementsToCodeBehind() {
        setContentView(R.layout.activity_settings);
        editTextGoalSteps = findViewById(R.id.editTextGoalCountSteps);
        editTextName = findViewById(R.id.editTextName);
        textViewName = findViewById(R.id.textViewName);
        editTextHeight = findViewById(R.id.editTextHeight);
        editTextWeight = findViewById(R.id.editTextWeight);
        buttonEdit = findViewById(R.id.buttonEdit);
    }

    // Метод для обновления значений элементов пользовательского интерфейса
    private void updateValues() {
        editTextGoalSteps.setText(String.valueOf(goalSteps));
        editTextHeight.setText(String.valueOf(height));
        editTextWeight.setText(String.valueOf(weight));
        textViewName.setText(name);
    }

    // Метод для сохранения настроек
    public void save(View view) {
        goalSteps = (int) Double.parseDouble(editTextGoalSteps.getText().toString());
        height = Double.parseDouble(editTextHeight.getText().toString());
        weight = Double.parseDouble(editTextWeight.getText().toString());
        if (editTextName.getVisibility() == View.VISIBLE)
            name = editTextName.getText().toString();
        else
            name = textViewName.getText().toString();
        editTextName.setVisibility(View.INVISIBLE);
        textViewName.setVisibility(View.VISIBLE);
        Options.save(new ModelOptions(
                goalSteps,
                height,
                weight,
                name
        ));
        updateValues();
        buttonEdit.setVisibility(View.VISIBLE);
    }

    // Метод для редактирования имени пользователя
    public void edit(View view) {
        buttonEdit.setVisibility(View.INVISIBLE);
        editTextName.setVisibility(View.VISIBLE);
        editTextName.setText(name);
        textViewName.setVisibility(View.INVISIBLE);
    }

    // Метод для получения настроек
    private void getOptions() {
        Options.load();
        goalSteps = Options.options.goalSteps;
        name = Options.options.name;
        height = Options.options.height;
        weight = Options.options.weight;
    }
}
