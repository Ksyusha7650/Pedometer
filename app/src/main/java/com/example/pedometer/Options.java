package com.example.pedometer;

import android.content.Context;
;
import androidx.datastore.preferences.core.MutablePreferences;
import androidx.datastore.preferences.core.Preferences;
import androidx.datastore.preferences.core.PreferencesKeys;
import androidx.datastore.preferences.rxjava2.RxPreferenceDataStoreBuilder;
import androidx.datastore.rxjava2.RxDataStore;

import io.reactivex.Single;

public class Options {
    public static ModelOptions options;
    public static RxDataStore<Preferences> dataStoreRX;

    // Конструктор класса
    public Options(Context context) {
        dataStoreRX = new RxPreferenceDataStoreBuilder(context,"options").build();
    }

    // Метод для сохранения настроек
    public static void save(ModelOptions options) {
        putIntValue("GOAL", options.goalSteps);
        putDoubleValue("HEIGHT", options.height);
        putDoubleValue("WEIGHT", options.weight);
        putStringValue("NAME", options.name);
    }

    // Метод для сохранения настроек шагов
    public static void saveCountSteps(ModelOptions options) {
       putIntValue("1DAY", options.countSteps1);
        putIntValue("2DAY", options.countSteps2);
        putIntValue("3DAY", options.countSteps3);
        putIntValue("4DAY", options.countSteps4);
        putIntValue("5DAY", options.countSteps5);
        putIntValue("6DAY", options.countSteps6);
    }

    // Метод для сохранения значения типа String
    public static void putStringValue(String Key, String value) {
        Preferences.Key<String> PREF_KEY = PreferencesKeys.stringKey(Key);
        dataStoreRX.updateDataAsync(prefsIn -> {
            MutablePreferences mutablePreferences = prefsIn.toMutablePreferences();
            mutablePreferences.set(PREF_KEY, value);
            return Single.just(mutablePreferences);
        });
    }

    // Метод для сохранения значения типа int
    public static void putIntValue(String Key, int value) {
        Preferences.Key<Integer> PREF_KEY = PreferencesKeys.intKey(Key);
        dataStoreRX.updateDataAsync(prefsIn -> {
            MutablePreferences mutablePreferences = prefsIn.toMutablePreferences();
            mutablePreferences.set(PREF_KEY, value);
            return Single.just(mutablePreferences);
        });
    }

    // Метод для сохранения значения типа double
    public static void putDoubleValue(String Key, double value) {
        Preferences.Key<Double> PREF_KEY = PreferencesKeys.doubleKey(Key);
        dataStoreRX.updateDataAsync(prefsIn -> {
            MutablePreferences mutablePreferences = prefsIn.toMutablePreferences();
            mutablePreferences.set(PREF_KEY, value);
            return Single.just(mutablePreferences);
        });
    }

    // Метод для получения значения типа String
    static String getStringValue(String Key) {
        Preferences.Key<String> PREF_KEY = PreferencesKeys.stringKey(Key);
        Single<String> value = dataStoreRX.data().firstOrError().map(prefs -> prefs.get(PREF_KEY)).onErrorReturnItem("Машка");
        return value.blockingGet();
    }

    // Метод для получения значения типа int
    static int getIntValue(String Key) {
        Preferences.Key<Integer> PREF_KEY = PreferencesKeys.intKey(Key);
        Single<Integer> value = dataStoreRX.data().firstOrError().map(prefs -> prefs.get(PREF_KEY)).onErrorReturnItem(6000);
        return value.blockingGet();
    }

    // Метод для получения значения типа double
    static Double getDoubleValue(String Key) {
        Preferences.Key<Double> PREF_KEY = PreferencesKeys.doubleKey(Key);
        Single<Double> value = dataStoreRX.data().firstOrError().map(prefs -> prefs.get(PREF_KEY)).onErrorReturnItem(0.0);
        return value.blockingGet();
    }

    // Метод для загрузки настроек
    public static void load() {
        options = new ModelOptions(
                getIntValue("GOAL"),
                getDoubleValue("HEIGHT"),
                getDoubleValue("WEIGHT"),
                getStringValue("NAME"),
                getIntValue("1DAY"),
                getIntValue("2DAY"),
                getIntValue("3DAY"),
                getIntValue("4DAY"),
                getIntValue("5DAY"),
                getIntValue("6DAY")
        );
    }
}
