package com.example.controller;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.graphics.Color;
import android.os.AsyncTask;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.IOException;

public class Bluetooth extends AsyncTask<Void, Void, Void> {

    private boolean ConnectSuccess = true;

    @Override
    protected void onPreExecute()
    {
    }

    @Override
    protected Void doInBackground(Void... devices)
    {
        try
        {
            if (Data.btSocket == null || !Data.isBtConnected)
            {
                Data.myBluetooth = BluetoothAdapter.getDefaultAdapter();
                BluetoothDevice devicE = Data.myBluetooth.getRemoteDevice(Data.address);
                Data.btSocket = devicE.createInsecureRfcommSocketToServiceRecord(Data.myUUID);
                BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                Data.btSocket.connect();
            }
        }
        catch (IOException e)
        {
            ConnectSuccess = false;
        }
        return null;
    }
    @Override
    public void onPostExecute(Void result)
    {
        ImageButton button;
        button = Data.homeView.findViewById(R.id.Bluetooth_button);

        super.onPostExecute(result);

        if (ConnectSuccess)
        {
            button.setColorFilter(Color.GREEN);
            Functions.ActionBar(Data.view, "Connection successful");
            Data.isBtConnected = true;
        } else {
            button.setColorFilter(Color.RED);
            Functions.ActionBar(Data.view, "Connection failed");
        }
    }
}
