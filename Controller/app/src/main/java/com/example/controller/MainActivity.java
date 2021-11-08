package com.example.controller;


import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;


import com.example.controller.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;


public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding mainBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mainBinding.getRoot());
        BottomNavigationView navView = findViewById(R.id.nav_view);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_template, R.id.navigation_home, R.id.navigation_palette)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        Data.appContext = getApplicationContext();
        if (Data.address != null) {
            new Bluetooth().execute();
        }

        Functions.Startup();

    }


    public void OnSelect(View view){
        boolean flag = true;
        int i = 0;
        Button[] selected = new Button[6];
        Button[] selected_palette = new Button[6];
        View view_selected = findViewById(R.id.View_selected);;
        selected[0] = findViewById(R.id.select_1);
        selected[1] = findViewById(R.id.select_2);
        selected[2] = findViewById(R.id.select_3);
        selected[3] = findViewById(R.id.select_4);
        selected[4] = findViewById(R.id.select_5);
        selected[5] = findViewById(R.id.select_6);
        selected_palette[0] = findViewById(R.id.select_1p);
        selected_palette[1] = findViewById(R.id.select_2p);
        selected_palette[2] = findViewById(R.id.select_3p);
        selected_palette[3] = findViewById(R.id.select_4p);
        selected_palette[4] = findViewById(R.id.select_5p);
        selected_palette[5] = findViewById(R.id.select_6p);
        switch (view.getId()){
            case R.id.select_1:
                i = 0;
                break;
            case R.id.select_2:
                i = 1;
                break;
            case R.id.select_3:
                i = 2;
                break;
            case R.id.select_4:
                i = 3;
                break;
            case R.id.select_5:
                i = 4;
                break;
            case R.id.select_6:
                i = 5;
                break;
            case R.id.select_1p:
                flag = false;
                i = 0;
                break;
            case R.id.select_2p:
                flag = false;
                i = 1;
                break;
            case R.id.select_3p:
                flag = false;
                i = 2;
                break;
            case R.id.select_4p:
                flag = false;
                i = 3;
                break;
            case R.id.select_5p:
                flag = false;
                i = 4;
                break;
            case R.id.select_6p:
                flag = false;
                i = 5;
                break;
        }
        Data.selected = i;
        Functions.BtSend(i+"Sx");
        view.setClickable(false);
        if(flag){
            for(int j=0; j<6; j++){
                selected[j].setClickable(true);
                selected[j].setBackgroundColor(getResources().getColor(R.color.colorPrimary, null));
            }
            view_selected.setBackgroundColor(Data.colors[Data.selected]);
            view.setBackgroundColor(getResources().getColor(R.color.colorThird, null));
            UpdateMain();
        }else{
            for(int j=0; j<6; j++){
                selected_palette[j].setClickable(true);
                selected_palette[j].setBackgroundColor(getResources().getColor(R.color.colorPrimary, null));
            }
            view.setBackgroundColor(getResources().getColor(R.color.colorThird, null));
            UpdatePalette();
        }

    }


    public void OnChoose(View view) {
        int i=0;
        switch (view.getId()){
            case R.id.choose_1:
                i=0;
                break;
            case R.id.choose_2:
                i=1;
                break;
            case R.id.choose_3:
                i=2;
                break;
            case R.id.choose_4:
                i=3;
                break;
            case R.id.choose_5:
                i=4;
                break;
            case R.id.choose_6:
                i=5;
                break;
            case R.id.choose_1p:
                i=0;
                break;
            case R.id.choose_2p:
                i=1;
                break;
            case R.id.choose_3p:
                i=2;
                break;
            case R.id.choose_4p:
                i=3;
                break;
            case R.id.choose_5p:
                i=4;
                break;
            case R.id.choose_6p:
                i=5;
                break;
        }
        Data.chosen[i] = !Data.chosen[i];
        String chosenString = "1";
        for(int j=0; j<6; j++){
            if(Data.chosen[j]){
                chosenString+="1";
            }else{
                chosenString+="0";
            }
        }
        if(Data.chosen[i]){
            view.setBackgroundColor(Data.colors[i]);
        }
        else{
            view.setBackgroundColor(getResources().getColor(R.color.colorPrimary, null));
        }
        Functions.BtSend(chosenString+"Ax");
    }

    @SuppressLint("SetTextI18n")
    public void homeButtons(View view){
        SeekBar[] value_bar = new SeekBar[4];
        Button[] attach = new Button[3];
        String attachString = "1";
        boolean flag=false;
        value_bar[0] = findViewById(R.id.Count_bar);
        value_bar[1] = findViewById(R.id.Delay_bar);
        value_bar[2] = findViewById(R.id.Ontime_bar);
        value_bar[3] = findViewById(R.id.Global_bar);
        attach[0] = findViewById(R.id.Count_attach);
        attach[1] = findViewById(R.id.Delay_attach);
        attach[2] = findViewById(R.id.Ontime_attach);
        switch (view.getId()){
            case R.id.Count_value:
                break;
            case R.id.Count_add:
                if(Data.count_value[Data.selected]<value_bar[0].getMax()) {
                    Data.count_value[Data.selected]++;
                }
                break;
            case R.id.Count_subtract:
                if(Data.count_value[Data.selected]>0)
                    Data.count_value[Data.selected]--;
                break;
            case R.id.Count_attach:
                Data.attach[0] = !Data.attach[0];
                Data.attached[0] = Data.selected;
                flag=true;
                if(Data.attach[0]){
                    attach[0].setText("Attached "+(Data.selected+1));
                    attach[0].setBackgroundColor(getResources().getColor(R.color.colorThird, null));
                } else {
                    attach[0].setText("Attach");
                    attach[0].setBackgroundColor(getResources().getColor(R.color.colorPrimary, null));
                }
                break;
            case R.id.Delay_add:
                if(Data.delay_value[Data.selected]<value_bar[1].getMax())
                    Data.delay_value[Data.selected]++;

                break;
            case R.id.Delay_subtract:
                if(Data.delay_value[Data.selected]>0)
                    Data.delay_value[Data.selected]--;

                break;
            case R.id.Delay_attach:
                Data.attach[1] = !Data.attach[1];
                Data.attached[1] = Data.selected;
                flag=true;
                if(Data.attach[1]){
                    attach[1].setText("Attached "+(Data.selected+1));
                    attach[1].setBackgroundColor(getResources().getColor(R.color.colorThird, null));
                } else {
                    attach[1].setText("Attach");
                    attach[1].setBackgroundColor(getResources().getColor(R.color.colorPrimary, null));
                }
                break;
            case R.id.Ontime_add:
                if(Data.ontime_value[Data.selected]<value_bar[2].getMax())
                    Data.ontime_value[Data.selected]++;

                break;
            case R.id.Ontime_subtract:
                if(Data.ontime_value[Data.selected]>0)
                    Data.ontime_value[Data.selected]--;

                break;
            case R.id.Ontime_attach:
                Data.attach[2] = !Data.attach[2];
                Data.attached[2] = Data.selected;
                flag=true;
                if(Data.attach[2]){
                    attach[2].setText("Attached "+(Data.selected+1));
                    attach[2].setBackgroundColor(getResources().getColor(R.color.colorThird, null));
                } else {
                    attach[2].setText("Attach");
                    attach[2].setBackgroundColor(getResources().getColor(R.color.colorPrimary, null));
                }
                break;
            case R.id.Global_add:
                if(Data.global_value<value_bar[3].getMax())
                    Data.global_value++;
                break;
            case R.id.Global_subtract:
                if(Data.global_value>0)
                    Data.global_value--;
                break;
        }
        if(flag){
            for(int j=0; j<8;j++){
                if(Data.attach[j])
                    attachString+=Data.attached[j];
                else
                    attachString+=9;
            }
            Functions.BtSend(attachString+"Cx");
        }

        if(Data.attach[0])
            value_bar[0].setProgress(Data.count_value[Data.attached[0]]);
        else
            value_bar[0].setProgress(Data.count_value[Data.selected]);
        if(Data.attach[1])
            value_bar[1].setProgress(Data.delay_value[Data.attached[1]]);
        else
            value_bar[1].setProgress(Data.delay_value[Data.selected]);
        if(Data.attach[2])
            value_bar[2].setProgress(Data.ontime_value[Data.attached[2]]);
        else
            value_bar[2].setProgress(Data.ontime_value[Data.selected]);
        value_bar[3].setProgress(Data.global_value);
    }

    public void UpdateMain(){
        SeekBar[] value_bar = new SeekBar[4];
        value_bar[0] = findViewById(R.id.Count_bar);
        value_bar[1] = findViewById(R.id.Delay_bar);
        value_bar[2] = findViewById(R.id.Ontime_bar);
        value_bar[3] = findViewById(R.id.Global_bar);
        if(Data.attach[0])
            value_bar[0].setProgress(Data.count_value[Data.attached[0]]);
        else
            value_bar[0].setProgress(Data.count_value[Data.selected]);
        if(Data.attach[1])
            value_bar[1].setProgress(Data.delay_value[Data.attached[1]]);
        else
            value_bar[1].setProgress(Data.delay_value[Data.selected]);
        if(Data.attach[2])
            value_bar[2].setProgress(Data.ontime_value[Data.attached[2]]);
        else
            value_bar[2].setProgress(Data.ontime_value[Data.selected]);
        value_bar[3].setProgress(Data.global_value);
    }

    public void UpdatePalette(){
        SeekBar[] colors_bar = new SeekBar[5];
        colors_bar[0] = findViewById(R.id.Red_bar);
        colors_bar[1] = findViewById(R.id.Green_bar);
        colors_bar[2] = findViewById(R.id.Blue_bar);
        colors_bar[3] = findViewById(R.id.Count_hsv_bar);
        colors_bar[4] = findViewById(R.id.Add_hsv_bar);
        if(Data.color_model){
            colors_bar[0].setProgress(Data.HSV[Data.selected][0]);
            colors_bar[1].setProgress(Data.HSV[Data.selected][1]);;
            colors_bar[2].setProgress(Data.HSV[Data.selected][2]);
            colors_bar[3].setProgress(Data.count_hsv[Data.selected]);
            colors_bar[4].setProgress(Data.add_hsv[Data.selected]);
        } else{
            colors_bar[0].setProgress(Color.red(Data.colors[Data.selected]));
            colors_bar[1].setProgress(Color.green(Data.colors[Data.selected]));
            colors_bar[2].setProgress(Color.blue(Data.colors[Data.selected]));
        }
    }

}
