package com.example.pedometer;

import static com.example.pedometer.Options.options;
import static java.lang.Thread.sleep;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import org.eazegraph.lib.charts.BarChart;
import org.eazegraph.lib.models.BarModel;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    // Переменная для отображения текущих шагов
    TextView textViewCurrentSteps;

    // Переменная для отображения целевого количества шагов
    TextView textViewGoalSteps;

    // Переменная для отображения процента выполнения цели
    TextView textViewPercents;

    // Прогресс бар для отображения количества текущих шагов
    ProgressBar progressBarCountSteps;

    // Менеджер сенсоров для управления сенсорными данными
    private SensorManager sensorManager;

    // Сенсор для отслеживания количества шагов
    Sensor sensor;

    // Процент выполнения цели
    double percents;

    // Текущее количество шагов
    int currentSteps = 0;

    // Общее текущее количество шагов
    int totalCurrentSteps = 0;

    // Изображения значков за достижения определенных количеств шагов (100, 5000, 10000 и т.д.)
    ImageView badge100, badge5000, badge10000, badge15000, badge20000, badge25000;

    // Изображение фона значка
    static ImageView backgroundBadge;

    // Текстовое поле для отображения текста значка
    static TextView textViewBadge;

    // Флаг для определения, запущен ли поток
    static boolean isLaunchedThread = false;

    // Массив с количеством шагов для каждого значка (100, 5000, 10000 и т.д.)
    int[] countStepsBadges = {100, 5000, 10000, 15000, 20000, 25000};

    // Диаграмма для отображения статистики шагов по дням недели
    BarChart barChart;

    // Текущий день недели
    int currentDay; // 1 - Вс, 2 - Пн, 3 - Вт и т.д.

    int orientation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            connectUIElementsToCodeBehindInLandOrientation();
        } else {
            connectUIElementsToCodeBehind(); // связывает элементы пользовательского интерфейса с переменными класса
        }
        addSensor(); // добавляет сенсор и запрашивает разрешение для мониторинга активности
        getOptions(); // загружает настройки из SharedPreferences
        updateValues(); // обновляет значения элементов пользовательского интерфейса
    }

    // Метод для добавления сенсора и запроса разрешения на доступ к активности
    private void addSensor() {
        // Проверяем, есть ли у приложения разрешение на доступ к активности
        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACTIVITY_RECOGNITION)
                == PackageManager.PERMISSION_DENIED)
            requestPermissions(new String[]{ android.Manifest.permission.ACTIVITY_RECOGNITION }, 31); // Запрашиваем разрешение у пользователя на доступ к активности

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE); // Получаем доступ к сервису SensorManager
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER); // Получаем доступ к сенсору типа TYPE_STEP_COUNTER
    }

    // Метод для получения имени дня недели на основе его номера
    private String nameOfDay(int dayOfWeek) {
        switch (dayOfWeek) {
            case 2: return getString(R.string.monday);
            case 3: return getString(R.string.tuesday);
            case 4: return getString(R.string.wensday);
            case 5: return getString(R.string.thursday);
            case 6: return getString(R.string.friday);
            case 7: return getString(R.string.saturday);
            case 1: return getString(R.string.sunday);
            default: return getString(R.string.empty);
        }
    }

    // Метод для получения имени дня недели на основе его номера
    private int colorOfBarModel(int countSteps) {
        if (countSteps > options.goalSteps){
            return Color.parseColor("#32ade6");
        }
        else return Color.parseColor("#00c7be");
    }

    // Метод для установки данных на диаграмму для текущего дня
    private void setChart(int today) {
        barChart.clearChart(); // Очищаем диаграмму
        barChart.addBar(new BarModel(nameOfDay(today + 1), 9367, colorOfBarModel(9367))); // Добавляем бар для дня, следующего за текущим днем
        barChart.addBar(new BarModel(nameOfDay(today + 2), 14596, colorOfBarModel(14596))); // Добавляем бар для дня, через два дня от текущего дня
        barChart.addBar(new BarModel(nameOfDay(today + 3), 9770, colorOfBarModel(9770))); // Добавляем бар для дня, через три дня от текущего дня
        barChart.addBar(new BarModel(nameOfDay(today - 3), 10205, colorOfBarModel(10205))); // Добавляем бар для дня, через три дня до текущего дня
        barChart.addBar(new BarModel(nameOfDay(today - 2), 11847, colorOfBarModel(11847))); // Добавляем бар для дня, через два дня до текущего дня
        barChart.addBar(new BarModel(nameOfDay(today - 1), 5181, colorOfBarModel(5181))); // Добавляем бар для дня, предыдущего текущему дню
        barChart.addBar(new BarModel(nameOfDay(today), currentSteps, Color.parseColor(getString(R.string.white)))); // Добавляем бар для текущего дня с текущим количеством шагов
    }

    // Метод для связывания элементов пользовательского интерфейса с переменными класса
    private void connectUIElementsToCodeBehind() {
        textViewCurrentSteps = findViewById(R.id.textViewCurrentSteps);
        textViewGoalSteps = findViewById(R.id.textViewGoalSteps);
        textViewPercents = findViewById(R.id.textViewPercents);
        textViewBadge = findViewById(R.id.textViewBadge);
        progressBarCountSteps = findViewById(R.id.progressBarCountSteps);
        badge100 = findViewById(R.id.imageViewBadge100);
        badge5000 = findViewById(R.id.imageViewBadge5000);
        badge10000 = findViewById(R.id.imageViewBadge10000);
        badge15000 = findViewById(R.id.imageViewBadge15000);
        badge20000 = findViewById(R.id.imageViewBadge20000);
        badge25000 = findViewById(R.id.imageViewBadge25000);
        backgroundBadge = findViewById(R.id.imageViewBadge);
        barChart = findViewById(R.id.chart);
    }

    // Метод для связывания элементов пользовательского интерфейса с переменными класса при повороте
    private void connectUIElementsToCodeBehindInLandOrientation() {
        textViewCurrentSteps = findViewById(R.id.textViewCurrentSteps);
        textViewGoalSteps = findViewById(R.id.textViewGoalSteps);
        textViewPercents = findViewById(R.id.textViewPercents);
        progressBarCountSteps = findViewById(R.id.progressBarCountSteps);
    }

    // Метод для получения настроек из DataStore
    private void getOptions() {
        Options options = new Options(this);
        options.load();
    }

    // Создание слушателя событий сенсора
    SensorEventListener listener = new SensorEventListener() {
        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }

        @Override
        public void onSensorChanged(SensorEvent event) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            int today = calendar.get(Calendar.DAY_OF_WEEK);
            if (currentDay != today) {
                currentSteps = 0; // Если текущий день отличается от предыдущего сохраненного дня, обнуляем текущие шаги
                currentDay = today; // Устанавливаем текущий день
            }
            currentSteps += (int) event.values[0] - currentSteps; // Обновляем текущее количество шагов
            updateValues(); // Обновляем значения в интерфейсе
        }
    };

    // Метод, вызываемый при возобновлении активити
    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_UI); // Регистрируем слушателя событий сенсора
        updateValues(); // Обновляем значения в интерфейсе
    }

    // Метод, вызываемый при приостановке активити
    @Override
    protected void onPause() {
        super.onPause();
    }

    // Метод для обновления значений в интерфейсе
    public void updateValues() {
        percents = (double) currentSteps / options.goalSteps; // Вычисляем процент выполнения цели
        progressBarCountSteps.setProgress((int) currentSteps); // Устанавливаем прогресс бар с количеством текущих шагов
        textViewGoalSteps.setText(String.format("/%d", options.goalSteps)); // Устанавливаем текст с целевым количеством шагов
        textViewCurrentSteps.setText(String.valueOf(currentSteps)); // Устанавливаем текст с текущим количеством шагов
        textViewPercents.setText((double) Math.round(percents * 10000) / 100 + "%"); // Устанавливаем текст с процентом выполнения цели, округленным до двух знаков после запятой
        if (orientation == Configuration.ORIENTATION_LANDSCAPE)
            return;
        // Устанавливаем значки для заданного количества шагов
        for (int countSteps : countStepsBadges) {
            setBadges(countSteps);
        }

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        int today = calendar.get(Calendar.DAY_OF_WEEK);
        setChart(today); // Устанавливаем диаграмму для текущего дня
    }

    // Метод для установки значка для заданного количества шагов
    private void setBadges(int steps) {
        String uri = "@drawable/badge_" + steps;
        int imageResource = getResources().getIdentifier(uri, null, getPackageName());
        if (currentSteps > steps) {
            Drawable res = getResources().getDrawable(imageResource);
            switch (steps) {
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

    // Метод для перехода на страницу настроек
    public void goToSettings(View view) {
        Intent myIntent = new Intent(MainActivity.this, SettingsActivity.class);
        MainActivity.this.startActivity(myIntent);
    }

    // Метод для перехода на страницу датчиков
    public void goToSensors(View view) {
        Intent myIntent = new Intent(MainActivity.this, SensorsActivity.class);
        MainActivity.this.startActivity(myIntent);
    }

    // Метод для открытия значка 100 шагов
    public void openBadge100(View view) {
        launchBadgeThread("100 шагов");
    }

    // Метод для открытия значка 5000 шагов
    public void openBadge5000(View view) {
        launchBadgeThread("5000 шагов");
    }

    // Метод для открытия значка 10000 шагов
    public void openBadge10000(View view) {
        launchBadgeThread("10000 шагов");
    }

    // Метод для открытия значка 15000 шагов
    public void openBadge15000(View view) {
        launchBadgeThread("15000 шагов");
    }

    // Метод для открытия значка 20000 шагов
    public void openBadge20000(View view) {
        launchBadgeThread("20000 шагов");
    }

    // Метод для открытия значка 25000 шагов
    public void openBadge25000(View view) {
        launchBadgeThread("25000 шагов");
    }

    // Метод для запуска потока отображения значка
    public void launchBadgeThread(String text) {
        try {
            if (isLaunchedThread) return; // Если поток уже запущен, то выходим из метода
            VisibleBadgeThread badgeThread = new VisibleBadgeThread(); // Создаем новый поток
            Thread myThready = new Thread(badgeThread); // Создаем новый поток исполнения для созданного потока
            myThready.start(); // Запускаем поток исполнения
            isLaunchedThread = true; // Устанавливаем флаг "isLaunchedThread" в значение true
            backgroundBadge.setVisibility(View.VISIBLE); // Показываем изображение значка в интерфейсе
            MainActivity.textViewBadge.setText(text); // Устанавливаем текст значка
            MainActivity.textViewBadge.setVisibility(View.VISIBLE); // Показываем текст значка в интерфейсе
            myThready.join(2); // Ожидаем завершения потока исполнения
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}

// Объявление класса VisibleBadgeThread, реализующего интерфейс Runnable
class VisibleBadgeThread implements Runnable {
    // Метод run, выполняющийся в отдельном потоке
    @Override
    public void run() {
        try {
            sleep(1500); // Приостанавливаем выполнение потока на 1500 миллисекунд (1.5 секунды)
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        // Скрываем изображение и текст значка в интерфейсе
        MainActivity.backgroundBadge.setVisibility(View.INVISIBLE);
        MainActivity.textViewBadge.setVisibility(View.INVISIBLE);
        MainActivity.isLaunchedThread = false; // Устанавливаем флаг "isLaunchedThread" в значение false
    }
}