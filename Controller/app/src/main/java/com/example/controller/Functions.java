package com.example.controller;

import android.graphics.Color;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.util.Arrays;

class Functions {
    static void ActionBar(View view, String message) {

        Snackbar.make(view, message, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }


    static boolean BtSend(String string){
        ImageButton button;
        button = Data.homeView.findViewById(R.id.Bluetooth_button);
        Data.btDelay=false;
        if (Data.btSocket!=null) {
                try {
                    Data.btSocket.getOutputStream().write(string.getBytes());
                    Data.btSocket.getOutputStream().write("x".getBytes());
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Data.btDelay = true;
                        }
                    }, Data.btTime);
                } catch (IOException e) {
                    Data.address = null;
                    button.setColorFilter(Color.RED);
                    Data.isBtConnected = false;
                    return false;
                }
            return true;
        }
        return false;
    }

    static void BtSync(){
        int wait = 300;
        String chosenString = "1";
        for(int j=0; j<6; j++){
            if(Data.chosen[j]){
                chosenString+="1";
            }else{
                chosenString+="0";
            }
        }
        try {
            Thread.sleep(wait);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String attachString = "1";
        for(int j=0; j<8;j++){
            if(Data.attach[j])
                attachString+=Data.attached[j];
            else
                attachString+='9';
        }

        BtSend("6Q");
        try {
            Thread.sleep(400);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        BtSend(Data.global_value+"gx");
        BtSend(Data.add_color+"Ox");
        for(int i=0; i<6;i++){
            BtSend(i+"Sx");
            try {
                Thread.sleep(wait);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(Data.chosen[i]){
                try {
                    Thread.sleep(wait);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Functions.BtSend("1"+i+"1Ax");
                BtSend(Data.count_value[i]+"cx");
                BtSend(Data.delay_value[i]+"dx");
                BtSend(Data.ontime_value[i]+"ox");
                if(Data.color_model){
                    Functions.BtSend(Data.HSV[i][0]+"Rx"+Data.HSV[i][1]+"Gx"+Data.HSV[i][2]+"Bx");
                    BtSend(Data.count_hsv[i]+"hx");
                    BtSend(Data.add_hsv[i]+"vx");
                }else{
                    Functions.BtSend(Color.red(Data.colors[i])+"Rx"+Color.green(Data.colors[i])+"Gx"+Color.blue(Data.colors[i])+"Bx");
                }
            }else{
                try {
                    Thread.sleep(wait);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Functions.BtSend("1"+i+"0Ax");
                BtSend(Data.count_value[i]+"cx");
                BtSend(Data.delay_value[i]+"dx");
                BtSend(Data.ontime_value[i]+"ox");
                if(Data.color_model){
                    Functions.BtSend(Data.HSV[i][0]+"Rx"+Data.HSV[i][1]+"Gx"+Data.HSV[i][2]+"Bx");
                    BtSend(Data.count_hsv[i]+"hx");
                    BtSend(Data.add_hsv[i]+"vx");
                }else{
                    Functions.BtSend(Color.red(Data.colors[i])+"Rx"+Color.green(Data.colors[i])+"Gx"+Color.blue(Data.colors[i])+"Bx");
                }
            }
        }
        for(int i = 0; i<3; i++){
            if(Data.attach[0]){
                Functions.BtSend(Data.attached[i]+"1Cx");
            } else {
                Functions.BtSend(Data.attached[i]+"0Cx");
            }
        }
        BtSend(chosenString+"Ax");
        BtSend(attachString+"Cx");
        BtSend(Data.selected+"Sx");
        try {
            Thread.sleep(wait);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        BtSend("7Q");
        BtSend(Data.brightness+"bx");
    }

    static void Startup(){
        Arrays.fill(Data.chosen, false);
        Arrays.fill(Data.colors, Color.BLACK);
        Data.selected = 0;
        Data.color_model = true;
        Data.chosen[0] = true;
        Data.colors[0] = Color.WHITE;
        Arrays.fill(Data.HSV[0], 0);
        Arrays.fill(Data.HSV[1], 0);
        Arrays.fill(Data.HSV[2], 0);
        Arrays.fill(Data.HSV[3], 0);
        Arrays.fill(Data.HSV[4], 0);
        Arrays.fill(Data.HSV[5], 0);
        Data.HSV[0][2]=255;
        Arrays.fill(Data.count_value, 2);
        Arrays.fill(Data.delay_value, 0);
        Arrays.fill(Data.ontime_value, 0);
        Arrays.fill(Data.attach, false);
        Data.global_value = 0;
        Arrays.fill(Data.attached, 0);
        Arrays.fill(Data.menu_boolean, true);
        Data.menu_value[0]=Data.brightness;
        Data.menu_value[1]=Data.magValue;
        Data.menu_value[2]=Data.pumpValue;
    }

    static boolean Pokaz(){
        BtSend("0Sx");
        BtSend("3Qx");
        BtSend("0bx");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        BtSend("2Qx");
        BtSend("4Qx");
        BtSend("0Px");
        try {
            Thread.sleep(12000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        BtSend("1Px");
        try {
            Thread.sleep(8000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        BtSend("2Px");
        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        BtSend("3Px");
        try {
            Thread.sleep(8000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        BtSend("4Px");
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        BtSend("5Px");
        try {
            Thread.sleep(13000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        BtSend("6Px");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        BtSend("7Px");
        //BtSend("100Px");
        /*BtSend("0Rx255Gx255Bx");
        BtSend("43Ox");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        BtSend("12hx");
        BtSend("8vx");
        BtSend("70bx");*/
        return true;
    }
}
