package com.example.controller;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Set;

public class HomeFragment extends Fragment{
    private BluetoothDatabase myBluetooth;

    private Button[] chooses = new Button[6];
    private Button[] selected = new Button[6];
    private ImageButton[] menu_button = new ImageButton[4];
    private SeekBar[] value_bar = new SeekBar[4];
    private Button[] value_buttons = new Button[4];
    private Button[] attach_buttons = new Button[3];
    private View view_selected;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        ID(view);
        myBluetooth = new BluetoothDatabase(getContext());
        Data.view = view;
        Data.homeView = view;
        Data.myBluetooth = BluetoothAdapter.getDefaultAdapter();
        Startup();
        Value_bar();
        StartHome();

        if(Data.isBtConnected)
            menu_button[3].setColorFilter(Color.GREEN);
        else
            menu_button[3].setColorFilter(Color.RED);
        return view;
    }

    private void ID(View view){
        selected[0] = view.findViewById(R.id.select_1);
        selected[1] = view.findViewById(R.id.select_2);
        selected[2] = view.findViewById(R.id.select_3);
        selected[3] = view.findViewById(R.id.select_4);
        selected[4] = view.findViewById(R.id.select_5);
        selected[5] = view.findViewById(R.id.select_6);
        chooses[0] = view.findViewById(R.id.choose_1);
        chooses[1] = view.findViewById(R.id.choose_2);
        chooses[2] = view.findViewById(R.id.choose_3);
        chooses[3] = view.findViewById(R.id.choose_4);
        chooses[4] = view.findViewById(R.id.choose_5);
        chooses[5] = view.findViewById(R.id.choose_6);
        value_bar[0] = view.findViewById(R.id.Count_bar);
        value_bar[1] = view.findViewById(R.id.Delay_bar);
        value_bar[2] = view.findViewById(R.id.Ontime_bar);
        value_bar[3] = view.findViewById(R.id.Global_bar);
        value_buttons[0] = view.findViewById(R.id.Count_value);
        value_buttons[1] = view.findViewById(R.id.Delay_value);
        value_buttons[2] = view.findViewById(R.id.Ontime_value);
        value_buttons[3] = view.findViewById(R.id.Global_value);
        attach_buttons[0] = view.findViewById(R.id.Count_attach);
        attach_buttons[1] = view.findViewById(R.id.Delay_attach);
        attach_buttons[2] = view.findViewById(R.id.Ontime_attach);
        menu_button[0] = view.findViewById(R.id.LED_button);
        menu_button[1] = view.findViewById(R.id.Magnet_button);
        menu_button[2] = view.findViewById(R.id.Pump_button);
        menu_button[3] = view.findViewById(R.id.Bluetooth_button);
        view_selected = view.findViewById(R.id.View_selected);

    }

    private void Startup(){
        for(int i=0; i<6; i++){
            if(Data.chosen[i])
                chooses[i].setBackgroundColor(Data.colors[i]);
        }
        for(int i=0; i<4;i++){
            value_buttons[i].setText(""+value_bar[i].getProgress());
        }
        selected[Data.selected].setBackgroundColor(getResources().getColor(R.color.colorThird, null));
        view_selected.setBackgroundColor(Data.colors[Data.selected]);
        value_buttons[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ValueDialog(0);
            }
        });
        value_buttons[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ValueDialog(1);
            }
        });
        value_buttons[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ValueDialog(2);
            }
        });
        value_buttons[3].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ValueDialog(3);
            }
        });

        menu_button[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BarDialog(0);
            }
        });

        menu_button[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BarDialog(1);
            }
        });
        menu_button[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BarDialog(2);
            }
        });
        menu_button[3].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Data.myBluetooth == null) {
                    Toast.makeText(Data.appContext, "Bluetooth Device Not Available", Toast.LENGTH_LONG).show();
                } else {
                    if (Data.myBluetooth.isEnabled()) {
                        btDialog();
                    } else {
                        Intent turnBTon = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                        startActivityForResult(turnBTon, 1);
                    }
                }

            }
        });
        menu_button[0].setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Functions.BtSync();
                return true;
            }
        });
        menu_button[1].setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Data.menu_boolean[1]=!Data.menu_boolean[1];
                if(Data.menu_boolean[1]){
                    Functions.BtSend("2Qx");
                    Functions.ActionBar(getView(), "ON");}
                else{
                    Functions.BtSend("3Qx");
                    Functions.ActionBar(getView(), "OFF");}
                return true;
            }
        });
        menu_button[2].setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Data.menu_boolean[2]=!Data.menu_boolean[2];
                if(Data.menu_boolean[2]){
                    Functions.BtSend("4Qx");
                    Functions.ActionBar(getView(), "ON");}
                else{
                    Functions.BtSend("5Qx");
                    Functions.ActionBar(getView(), "OFF");}
                return true;
            }
        });
        menu_button[3].setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Cursor res = myBluetooth.getAllData();
                if(res.getCount()==0){
                    Toast.makeText(Data.appContext, "Nothing found", Toast.LENGTH_LONG).show();
                    return false;
                }
                while(res.moveToNext()){
                    Data.address = res.getString(1);
                }
                if (Data.address != null) {
                    new Bluetooth().execute();
                    Functions.ActionBar(getView(), "Connecting...");
                }
                return true;
            }
        });
    }

    private void ValueDialog(final int x){
        LayoutInflater li = LayoutInflater.from(getContext());
        ContextThemeWrapper ctw = new ContextThemeWrapper(getActivity(), R.style.CustomHoloDialog);
        View dialogView = li.inflate(R.layout.value_dialog, null);
        final AlertDialog.Builder builder = new AlertDialog.Builder(ctw);
        final EditText userInput = dialogView.findViewById(R.id.editText);
        builder.setView(dialogView);
        builder.setCancelable(true)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(userInput.getText()!=null){
                            String string = userInput.getText().toString();
                            if(!string.matches(""))
                                value_bar[x].setProgress(Integer.parseInt(string));
                        }
                        else
                            dialogInterface.cancel();
                    }
                })
                .setNegativeButton("Cancel", null);

        builder.create()
                .show();
        userInput.requestFocus();
    }

    @SuppressLint("SetTextI18n")
    private void BarDialog(final int x){
        LayoutInflater li = LayoutInflater.from(getContext());
        ContextThemeWrapper ctw = new ContextThemeWrapper(getActivity(), R.style.CustomHoloDialog);
        View dialogView = li.inflate(R.layout.bar_dialog, null);
        final SeekBar inputBar = dialogView.findViewById(R.id.Dialog_bar);
        final TextView inputValue = dialogView.findViewById(R.id.Dialog_txt);
        final AlertDialog.Builder builder = new AlertDialog.Builder(ctw);
        switch (x){
            case 0:
                inputBar.setMax(255);
                inputBar.setProgress(Data.brightness);
                break;
            case 1:
                inputBar.setMin(32);
                inputBar.setMax(96);
                inputBar.setProgress(Data.magValue);
                break;
            case 2:
                inputBar.setMax(40);
                inputBar.setMin(0);
                inputBar.setProgress(Data.pumpValue);
                break;
        }
        inputValue.setText(""+Data.menu_value[x]);
        inputBar.setProgress(Data.menu_value[x]);
        inputBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                inputValue.setText(""+i);
                switch(x){
                    case 0:
                        if (Data.btDelay){
                            Data.brightness = i;
                            Functions.BtSend(i+"bx");
                        }
                        break;
                    case 1:
                        if (Data.btDelay) {
                            Data.magValue = i;
                            Functions.BtSend(i + "mx");
                        }
                        break;
                    case 2:
                        if (Data.btDelay) {
                            Data.pumpValue = i;
                            Functions.BtSend(i + "px");
                        }
                        break;
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        builder.setView(dialogView)
                .setCancelable(true)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setNegativeButton("ON/OFF", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Data.menu_boolean[x]=!Data.menu_boolean[x];
                        if(Data.menu_boolean[x]) {
                            Functions.ActionBar(getView(), "ON");
                        }
                        else {
                            Functions.ActionBar(getView(), "OFF");
                        }
                        switch(x){
                            case 0:
                                if(Data.menu_boolean[0]) {
                                    Functions.BtSend("0Qx"); }
                                else{
                                    Functions.BtSend("1Qx");}
                                break;
                            case 1:
                                if(Data.menu_boolean[1]){
                                    Functions.BtSend("2Qx");}
                                else{
                                    Functions.BtSend("3Qx");}
                                break;
                            case 2:
                                if(Data.menu_boolean[2]){
                                    Functions.BtSend("4Qx");}
                                else{
                                    Functions.BtSend("5Qx");}
                                break;
                        }


                    }
                });
        builder.create()
                .show();
    }

    private void btDialog(){
        LayoutInflater li = LayoutInflater.from(getContext());
        ContextThemeWrapper ctw = new ContextThemeWrapper(getActivity(), null);
        final View dialogView = li.inflate(R.layout.bluetooth_dialog, null);
        final AlertDialog.Builder builder = new AlertDialog.Builder(ctw);
        final ListView devices = dialogView.findViewById(R.id.Devices_list);
        Set<BluetoothDevice> pairedDevices = Data.myBluetooth.getBondedDevices();
        ArrayList list = new ArrayList();
        if (pairedDevices.size()>0)
        {
            for(BluetoothDevice bt : pairedDevices)
            {
                list.add(bt.getName() + "\n" + bt.getAddress());
            }
        }
        else
        {
            Functions.ActionBar(getView(), "No Paired Bluetooth Devices Found");
        }
        final ArrayAdapter adapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, list);
        devices.setAdapter(adapter);
        builder.setView(dialogView);
        builder.setCancelable(true)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (Data.address != null) {
                            new Bluetooth().execute();
                            myBluetooth.updateData(Data.address);
                            Functions.ActionBar(getView(), "Connecting...");
                        }
                    }
                })
                .setNegativeButton("Cancel",null)
                .create()
                .show();
        devices.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String info = ((TextView) view).getText().toString();
                Data.address = info.substring(info.length() - 17);
                Toast.makeText(getContext(), Data.address, Toast.LENGTH_LONG).show();
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void StartHome(){
        for(int i=0; i<3;i++){
            if(Data.attach[i]){
                attach_buttons[i].setText("Attached "+(Data.attached[i]+1));
                attach_buttons[i].setBackgroundColor(getResources().getColor(R.color.colorThird, null));
            } else {
                attach_buttons[i].setText("Attach");
                attach_buttons[i].setBackgroundColor(getResources().getColor(R.color.colorPrimary, null));
            }
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

    @SuppressLint("SetTextI18n")
    private void Value_bar(){
        //Count
        value_bar[0].setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                value_buttons[0].setText(i+"");
                Data.count_value[Data.selected]=i;
                if (Data.btDelay)
                    Functions.BtSend(i+"cx");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                try {
                    Thread.sleep(Data.btTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //Functions.BtSend(Data.count_value[Data.selected]+"cx");
            }
        });

        //Delay
        value_bar[1].setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                value_buttons[1].setText(i+"");
                Data.delay_value[Data.selected]=i;
                if (Data.btDelay)
                    Functions.BtSend(i+"dx");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                try {
                    Thread.sleep(Data.btTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //Functions.BtSend(Data.count_value[Data.selected]+"dx");
            }
        });

        //OnTime
        value_bar[2].setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                value_buttons[2].setText(i+"");
                Data.ontime_value[Data.selected]=i;
                if (Data.btDelay)
                    Functions.BtSend(i+"ox");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                try {
                    Thread.sleep(Data.btTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //Functions.BtSend(Data.count_value[Data.selected]+"ox");
            }
        });

        //Global Delay
        value_bar[3].setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                Data.global_value=i;
                value_buttons[3].setText(i+"");
                if (Data.btDelay)
                    Functions.BtSend(i+"gx");

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                try {
                    Thread.sleep(Data.btTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //Functions.BtSend(Data.count_value[Data.selected]+"gx");
            }
        });
    }

}
