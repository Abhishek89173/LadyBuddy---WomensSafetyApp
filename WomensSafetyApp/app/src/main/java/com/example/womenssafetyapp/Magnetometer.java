package com.example.womenssafetyapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import static java.lang.Math.sqrt;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * SensorEventListener :- it is used for receiving notifications from the SensorManager when sensor values have changed
 */
public class Magnetometer extends AppCompatActivity implements SensorEventListener {


    private TextView magR, show_conditions, x_cor, y_cor, z_cor ;
    SpeedometerView Speed;

    MediaPlayer mediaPlayer;


    private double magD;

    /**
     * Sensors :- Android sensors are virtual devices that provide data coming from a set of physical sensors:
     * accelerometers, gyroscopes, magnetometers, barometer, humidity, pressure, light, proximity and heart rate sensors.
     */
    private Sensor magnetometer ;

    private SensorManager sensorManager; // read about SensorManger just by ttapping on where it is used.
    Boolean flag = false;
    double prevx = 0f;