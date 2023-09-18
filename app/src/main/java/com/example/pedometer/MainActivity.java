package com.example.pedometer;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;


import android.content.Intent;
import android.content.pm.PackageManager;
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

import java.util.List;

public class MainActivity extends AppCompatActivity {
    TextView textViewCurrentSteps, textViewGoalSteps, textViewPercents;
    ProgressBar progressBarCountSteps;
    private SensorManager sensorManager;
    Sensor sensor;
    double percents;
    int currentSteps = 6300, goalSteps = 6000;
    ImageView badge100, badge5000, badge10000, badge15000, badge20000, badge25000;

    int[] countStepsBadges = {100, 5000, 10000, 15000, 20000, 25000};

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textViewCurrentSteps = findViewById(R.id.textViewCurrentSteps);
        textViewGoalSteps = findViewById(R.id.textViewGoalSteps);
        textViewPercents = findViewById(R.id.textViewPercents);
        progressBarCountSteps = findViewById(R.id.progressBarCountSteps);
        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACTIVITY_RECOGNITION)
                == PackageManager.PERMISSION_DENIED)
                requestPermissions(new String[]{ android.Manifest.permission.ACTIVITY_RECOGNITION }, 31);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        badge100 = findViewById(R.id.imageViewBadge100);
        badge5000 = findViewById(R.id.imageViewBadge5000);
        badge10000 = findViewById(R.id.imageViewBadge10000);
        badge15000 = findViewById(R.id.imageViewBadge15000);
        badge20000 = findViewById(R.id.imageViewBadge20000);
        badge25000 = findViewById(R.id.imageViewBadge25000);
        updateValues();
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
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //sensorManager.unregisterListener(listener);
    }

    public void updateValues(){
        percents = (double) currentSteps / goalSteps;
        progressBarCountSteps.setProgress(currentSteps);
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