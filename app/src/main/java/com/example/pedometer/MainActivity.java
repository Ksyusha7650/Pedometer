package com.example.pedometer;

import static com.example.pedometer.Options.options;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;


import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.eazegraph.lib.charts.BarChart;
import org.eazegraph.lib.models.BarModel;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    TextView textViewCurrentSteps, textViewGoalSteps, textViewPercents;
    ProgressBar progressBarCountSteps;
    private SensorManager sensorManager;
    Sensor sensor;
    double percents;
    int currentSteps = 6300;
    ImageView badge100, badge5000, badge10000, badge15000, badge20000, badge25000;

    int[] countStepsBadges = {100, 5000, 10000, 15000, 20000, 25000};

    BarChart barChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getOptions();
        connectUIElementsToCodeBehind();
        addSensor();
        updateValues();
        setChart();
    }

    private void addSensor() {
        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACTIVITY_RECOGNITION)
                == PackageManager.PERMISSION_DENIED)
                requestPermissions(new String[]{ android.Manifest.permission.ACTIVITY_RECOGNITION }, 31);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
    }

    private String nameOfDay(int dayOfWeek){
        switch (dayOfWeek){
            case 1: return "Понедельник";
            case 2: return "Вторник";
            case 3: return "Среда";
            case 4: return "Четверг";
            case 5: return "Пятница";
            case 6: return "Суббота";
            case 7: return "Воскресенье";
            default: return "Неверный день недели";
        }
    }
    private void setChart(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        barChart.addBar(new BarModel(nameOfDay(calendar.get(Calendar.DAY_OF_WEEK + 3)), 5400, Color.parseColor("#000000")));
        barChart.addBar(new BarModel(nameOfDay(calendar.get(Calendar.DAY_OF_WEEK + 2)), 6789, Color.parseColor("#000000")));
        barChart.addBar(new BarModel(nameOfDay(calendar.get(Calendar.DAY_OF_WEEK + 1)), 8234, Color.parseColor("#000000")));
        barChart.addBar(new BarModel(nameOfDay(calendar.get(Calendar.DAY_OF_WEEK)), 5600, Color.parseColor("#000000")));
        barChart.addBar(new BarModel("Вчера", 4050, Color.parseColor("#000000")));
        barChart.addBar(new BarModel("Сегодня", currentSteps, Color.parseColor("#66BB6A")));

    }
    private void connectUIElementsToCodeBehind() {
        textViewCurrentSteps = findViewById(R.id.textViewCurrentSteps);
        textViewGoalSteps = findViewById(R.id.textViewGoalSteps);
        textViewPercents = findViewById(R.id.textViewPercents);
        progressBarCountSteps = findViewById(R.id.progressBarCountSteps);
        badge100 = findViewById(R.id.imageViewBadge100);
        badge5000 = findViewById(R.id.imageViewBadge5000);
        badge10000 = findViewById(R.id.imageViewBadge10000);
        badge15000 = findViewById(R.id.imageViewBadge15000);
        badge20000 = findViewById(R.id.imageViewBadge20000);
        badge25000 = findViewById(R.id.imageViewBadge25000);
        barChart = findViewById(R.id.chart);
    }

    private void getOptions(){
        Options options = new Options(this);
        options.load();
    }

    SensorEventListener listener = new SensorEventListener() {
        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }

        @Override
        public void onSensorChanged(SensorEvent event) {
            currentSteps = (int)event.values[0]/10;
            updateValues();
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(listener, sensor,
                SensorManager.SENSOR_DELAY_UI);
        updateValues();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //sensorManager.unregisterListener(listener);
    }

    public void updateValues(){
        percents = (double) currentSteps / options.goalSteps;
        progressBarCountSteps.setProgress(currentSteps);
        textViewGoalSteps.setText(String.format("/%d", options.goalSteps));
        textViewCurrentSteps.setText(String.valueOf(currentSteps));
        textViewPercents.setText((double) Math.round(percents * 10000) / 100 + "%");
        for (int countSteps: countStepsBadges) {
            setBadges(countSteps);
        }
    }

    private void setBadges(int steps) {
        String uri = "@drawable/badge_" + steps;
        int imageResource = getResources().getIdentifier(uri, null, getPackageName());
        if (currentSteps > steps) {
            Drawable res = getResources().getDrawable(imageResource);
            switch (steps){
                case 100: {
                    badge100.setImageDrawable(res);
                    break;
                }
                case 5000: {
                    badge5000.setImageDrawable(res);
                    break;
                }
                case 10000: {
                    badge10000.setImageDrawable(res);
                    break;
                }
                case 15000: {
                    badge15000.setImageDrawable(res);
                    break;
                }
                case 20000: {
                    badge20000.setImageDrawable(res);
                    break;
                }
                case 25000: {
                    badge25000.setImageDrawable(res);
                    break;
                }
            }

        }
    }

    public void goToSettings(View view){
        Intent myIntent = new Intent(MainActivity.this, SettingsActivity.class);
        MainActivity.this.startActivity(myIntent);
    }
}