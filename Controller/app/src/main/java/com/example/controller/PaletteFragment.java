package com.example.controller;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

public class PaletteFragment extends Fragment {

    private Button[] chooses = new Button[6];
    private Button[] selected = new Button[6];
    private View color_image;
    private ImageView wheel_image;
    private float[] HSV_buffer = new float[3];
    private int[] color_buffer = new int[3];
    private SeekBar[] colors_bar = new SeekBar[5];
    private Button[] value_button =  new Button[5];
    private Button model_switch;
    private Bitmap bitmap;

    private final int[] r = new int[1];
    private final int[] g = new int[1];
    private final int[] b = new int[1];

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_palette, container, false);
        ID(view);
        Data.view = view;
        Data.paletteView = view;
        color_image.setBackgroundColor(Data.colors[Data.selected]);
        Startup();
        Switch_mode();
        Palette();
        Palette_bar();
        colors_bar[3].setProgress(Data.count_hsv[Data.selected]);
        colors_bar[4].setProgress(Data.add_hsv[Data.selected]);

        return view;
    }

    private void ID(View view){
        wheel_image = view.findViewById(R.id.wheel_image);
        color_image = view.findViewById(R.id.color_view);
        model_switch = view.findViewById(R.id.Model_switch);
        colors_bar[0] = view.findViewById(R.id.Red_bar);
        colors_bar[1] = view.findViewById(R.id.Green_bar);
        colors_bar[2] = view.findViewById(R.id.Blue_bar);
        colors_bar[3] = view.findViewById(R.id.Count_hsv_bar);
        colors_bar[4] = view.findViewById(R.id.Add_hsv_bar);
        value_button[0] = view.findViewById(R.id.Red_button);
        value_button[1] = view.findViewById(R.id.Green_button);
        value_button[2] = view.findViewById(R.id.Blue_button);
        value_button[3] = view.findViewById(R.id.Count_hsv_button);
        value_button[4] = view.findViewById(R.id.Add_hsv_button);
        selected[0] = view.findViewById(R.id.select_1p);
        selected[1] = view.findViewById(R.id.select_2p);
        selected[2] = view.findViewById(R.id.select_3p);
        selected[3] = view.findViewById(R.id.select_4p);
        selected[4] = view.findViewById(R.id.select_5p);
        selected[5] = view.findViewById(R.id.select_6p);
        chooses[0] = view.findViewById(R.id.choose_1p);
        chooses[1] = view.findViewById(R.id.choose_2p);
        chooses[2] = view.findViewById(R.id.choose_3p);
        chooses[3] = view.findViewById(R.id.choose_4p);
        chooses[4] = view.findViewById(R.id.choose_5p);
        chooses[5] = view.findViewById(R.id.choose_6p);
    }
    @SuppressLint("ClickableViewAccessibility")

    private void Startup(){
        for(int i=0; i<5; i++){
            if(Data.attach[i+3])
                value_button[i].setTextColor(Color.GREEN);
        }
        model_switch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Data.color_model = !Data.color_model;
                Functions.BtSend(Data.color_model+"Mx");
                NavHostFragment.findNavController(PaletteFragment.this)
                        .navigate(R.id.action_navigation_palette_self);
            }
        });
        value_button[3].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ValueDialog(3);
            }
        });
        value_button[4].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ValueDialog(4);
            }
        });
        for(int i=0; i<6; i++){
            if(Data.chosen[i])
                chooses[i].setBackgroundColor(Data.colors[i]);
        }
        selected[Data.selected].setBackgroundColor(getResources().getColor(R.color.colorThird, null));

        value_button[0].setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                String attachString = "1";
                Data.attach[3] = !Data.attach[3];
                if(Data.attach[3]){
                    BarDialog();
                    value_button[0].setTextColor(Color.GREEN);
                } else {
                    Functions.BtSend(0+"Ox");
                    value_button[0].setTextColor(Color.WHITE);}
                Data.attached[3] = 0;
                for(int j=0; j<8;j++){
                    if(Data.attach[j])
                        attachString+=Data.attached[j];
                    else
                        attachString+=9;
                }
                Functions.BtSend(attachString+"Cx");
                return true;
            }
        });
        value_button[1].setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                String attachString = "1";
                Data.attach[4] = !Data.attach[4];
                if(Data.attach[4])
                    value_button[1].setTextColor(Color.GREEN);
                else
                    value_button[1].setTextColor(Color.WHITE);
                Data.attached[4] = 0;
                for(int j=0; j<8;j++){
                    if(Data.attach[j])
                        attachString+=Data.attached[j];
                    else
                        attachString+=9;
                }
                Functions.BtSend(attachString+"Cx");
                return true;
            }
        });
        value_button[2].setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                String attachString = "1";
                Data.attach[5] = !Data.attach[5];
                if(Data.attach[5])
                    value_button[2].setTextColor(Color.GREEN);
                else
                    value_button[2].setTextColor(Color.WHITE);
                Data.attached[5] = 0;
                for(int j=0; j<8;j++){
                    if(Data.attach[j])
                        attachString+=Data.attached[j];
                    else
                        attachString+=9;
                }
                Functions.BtSend(attachString+"Cx");
                return true;
            }
        });
        value_button[3].setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                String attachString = "1";
                Data.attach[6] = !Data.attach[6];
                if(Data.attach[6])
                    value_button[3].setTextColor(Color.GREEN);
                else
                    value_button[3].setTextColor(Color.WHITE);
                Data.attached[6] = 0;
                for(int j=0; j<8;j++){
                    if(Data.attach[j])
                        attachString+=Data.attached[j];
                    else
                        attachString+=9;
                }
                Functions.BtSend(attachString+"Cx");
                return true;
            }
        });
        value_button[4].setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                String attachString = "1";
                Data.attach[7] = !Data.attach[7];
                if(Data.attach[7])
                    value_button[4].setTextColor(Color.GREEN);
                else
                    value_button[4].setTextColor(Color.WHITE);
                Data.attached[7] = 0;
                for(int j=0; j<8;j++){
                    if(Data.attach[j])
                        attachString+=Data.attached[j];
                    else
                        attachString+=9;
                }
                Functions.BtSend(attachString+"Cx");
                return true;
            }
        });
    }
    private void Palette(){
        final int size = 636;
        wheel_image.getLayoutParams().height = size;
        wheel_image.getLayoutParams().width = size;
        wheel_image.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE){
                    if((int)event.getX()<size&&(int)event.getX()>0&&(int)event.getY()<size&&(int)event.getY()>0) {
                        bitmap = ((BitmapDrawable)wheel_image.getDrawable()).getBitmap();
                        int pixel = bitmap.getPixel((int) event.getX(), (int) event.getY());
                        r[0] = Color.red(pixel);
                        g[0] = Color.green(pixel);
                        b[0] = Color.blue(pixel);
                        if (r[0] != 0 && g[0] != 0 && b[0] != 0) {
                            color_buffer[0] = r[0];
                            color_buffer[1] = g[0];
                            color_buffer[2] = b[0];

                            Color.RGBToHSV(r[0], g[0], b[0], HSV_buffer);
                            Data.HSV[Data.selected][0] = (int)(HSV_buffer[0]/1.41);
                            Data.HSV[Data.selected][1] = (int)(HSV_buffer[1]*255);
                            Data.HSV[Data.selected][2] = (int)(HSV_buffer[2]*255);

                            if(Data.color_model){
                                colors_bar[0].setProgress((int)(HSV_buffer[0]/1.41));
                                colors_bar[1].setProgress((int)(HSV_buffer[1]*255));
                                colors_bar[2].setProgress(255);
                            } else{
                                colors_bar[0].setProgress(color_buffer[0]);
                                colors_bar[1].setProgress(color_buffer[1]);
                                colors_bar[2].setProgress(color_buffer[2]);

                            }
                        }
                    }
                }
                return true;
            }
        });
    }

    private void GetColor(){
        if (Data.color_model) {
            HSV_buffer[0] = (float) (colors_bar[0].getProgress()*1.41);
            HSV_buffer[1] = (float)colors_bar[1].getProgress()/255;
            HSV_buffer[2] = (float)colors_bar[2].getProgress()/255;
            color_image.setBackgroundColor(Color.HSVToColor(HSV_buffer));
            Data.colors[Data.selected] = Color.HSVToColor(HSV_buffer);
            if(Data.chosen[Data.selected]){
                chooses[Data.selected].setBackgroundColor(Color.HSVToColor(HSV_buffer));}
        }else{
            chooses[Data.selected].setBackgroundColor(Data.colors[Data.selected]);
            if(Data.chosen[Data.selected]){
                color_image.setBackgroundColor(Data.colors[Data.selected]);}
        }

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
                                colors_bar[x].setProgress(Integer.parseInt(string));
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
    private void Switch_mode(){
        if(Data.color_model){
            model_switch.setText("HSV");
            colors_bar[3].setVisibility(View.VISIBLE);
            colors_bar[4].setVisibility(View.VISIBLE);
            value_button[3].setVisibility(View.VISIBLE);
            value_button[4].setVisibility(View.VISIBLE);
            for(int i=0; i<3; i++){
                colors_bar[i].setProgress(Data.HSV[Data.selected][i]);
            }
            value_button[0].setText("H: "+colors_bar[0].getProgress());
            value_button[1].setText("S: "+colors_bar[1].getProgress());
            value_button[2].setText("V: "+colors_bar[2].getProgress());
            value_button[3].setText("C: "+colors_bar[3].getProgress());
            value_button[4].setText("A: "+colors_bar[4].getProgress());
        } else{
            model_switch.setText("RGB");
            colors_bar[0].setProgress(Color.red(Data.colors[Data.selected]));
            colors_bar[1].setProgress(Color.green(Data.colors[Data.selected]));
            colors_bar[2].setProgress(Color.blue(Data.colors[Data.selected]));
            value_button[0].setText("R: "+colors_bar[0].getProgress());
            value_button[1].setText("G: "+colors_bar[1].getProgress());
            value_button[2].setText("B: "+colors_bar[2].getProgress());
            colors_bar[3].setVisibility(View.INVISIBLE);
            colors_bar[4].setVisibility(View.INVISIBLE);
            value_button[3].setVisibility(View.INVISIBLE);
            value_button[4].setVisibility(View.INVISIBLE);
        }
    }

    @SuppressLint("SetTextI18n")
    private void Palette_bar(){
        //RED
        colors_bar[0].setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean x) {
                GetColor();
                if(Data.color_model) {
                    Data.HSV[Data.selected][0] = i;
                    value_button[0].setText("H: "+ i);
                } else {
                    Data.colors[Data.selected] = Color.rgb(colors_bar[0].getProgress(), colors_bar[1].getProgress(), colors_bar[2].getProgress());
                    value_button[0].setText("R: "+ i);
                }
                if (Data.btDelay)
                    Functions.BtSend(colors_bar[0].getProgress()+"Rx"+colors_bar[1].getProgress()+"Gx"+colors_bar[2].getProgress()+"Bx");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                GetColor();
                try {
                    Thread.sleep(Data.btTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Functions.BtSend(colors_bar[0].getProgress()+"Rx"+colors_bar[1].getProgress()+"Gx"+colors_bar[2].getProgress()+"Bx");
            }
        });

        //GREEN
        colors_bar[1].setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean x) {
                GetColor();
                if(Data.color_model) {
                    Data.HSV[Data.selected][1] = i;
                    value_button[1].setText("S: "+ i);
                }else {
                    Data.colors[Data.selected] = Color.rgb(colors_bar[0].getProgress(), colors_bar[1].getProgress(), colors_bar[2].getProgress());
                    value_button[1].setText("G: "+ i);
                }
                if (Data.btDelay)
                    Functions.BtSend(colors_bar[0].getProgress()+"Rx"+colors_bar[1].getProgress()+"Gx"+colors_bar[2].getProgress()+"Bx");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                GetColor();
                try {
                    Thread.sleep(Data.btTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Functions.BtSend(colors_bar[0].getProgress()+"Rx"+colors_bar[1].getProgress()+"Gx"+colors_bar[2].getProgress()+"Bx");
            }
        });

        //BLUE
        colors_bar[2].setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean x) {
                GetColor();
                if(Data.color_model) {
                    Data.HSV[Data.selected][2] = i;
                    value_button[2].setText("V: "+ i);
                }else {
                    Data.colors[Data.selected] = Color.rgb(colors_bar[0].getProgress(), colors_bar[1].getProgress(), colors_bar[2].getProgress());
                    value_button[2].setText("B: "+ i);
                }
                if (Data.btDelay)
                    Functions.BtSend(colors_bar[0].getProgress()+"Rx"+colors_bar[1].getProgress()+"Gx"+colors_bar[2].getProgress()+"Bx");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                GetColor();
                try {
                    Thread.sleep(Data.btTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Functions.BtSend(colors_bar[0].getProgress()+"Rx"+colors_bar[1].getProgress()+"Gx"+colors_bar[2].getProgress()+"Bx");
            }
        });
        //COUNT
        colors_bar[3].setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean x) {
                Data.count_hsv[Data.selected] = i;
                value_button[3].setText("C: "+i);
                if (Data.btDelay)
                    Functions.BtSend(i+"hx");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        //ADD
        colors_bar[4].setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean x) {
                Data.add_hsv[Data.selected] = i;
                value_button[4].setText("A: "+i);
                if (Data.btDelay)
                    Functions.BtSend(i+"vx");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void BarDialog(){
        LayoutInflater li = LayoutInflater.from(getContext());
        ContextThemeWrapper ctw = new ContextThemeWrapper(getActivity(), R.style.CustomHoloDialog);
        View dialogView = li.inflate(R.layout.bar_dialog, null);
        final SeekBar inputBar = dialogView.findViewById(R.id.Dialog_bar);
        final TextView inputValue = dialogView.findViewById(R.id.Dialog_txt);
        final AlertDialog.Builder builder = new AlertDialog.Builder(ctw);
        inputBar.setMax(255);
        inputBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                inputValue.setText(""+i);
                Data.add_color = i;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Data.add_color = inputBar.getProgress();
            }
        });
        builder.setView(dialogView)
                .setCancelable(true)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Functions.BtSend(Data.add_color+"Ox");
                    }
                });
        builder.create()
                .show();
    }

}
