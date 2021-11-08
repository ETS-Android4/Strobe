package com.example.controller;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.graphics.Color;
import android.view.View;

import java.util.UUID;

public class Data {
    static int[] colors = new int[6];
    static boolean[] chosen = new boolean[6];
    static int[][] HSV =  new int[6][3];
    static int[] count_hsv = new int[6];
    static int[] add_hsv = new int[6];
    static int add_color;

    static int[] count_value = new int[6], delay_value = new int[6], ontime_value = new int[6];
    static int global_value;


    @SuppressLint("StaticFieldLeak")
    static View view;
    @SuppressLint("StaticFieldLeak")
    static View homeView;
    @SuppressLint("StaticFieldLeak")
    static View paletteView;

    static int[] menu_value =  new int[3];
    static boolean[] menu_boolean = new boolean[3];
    static int brightness= 192, magValue=49, pumpValue=0;

    static boolean[] attach = new boolean[8];
    static int[] attached = new int[8];
    static int selected;
    static boolean color_model;

    static String address;
    static BluetoothAdapter myBluetooth = null;
    static BluetoothSocket btSocket = null;
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    static boolean isBtConnected = false;
    static boolean btDelay =false;
    static int btTime = 400;

    static Context appContext;
}
