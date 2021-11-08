package com.example.controller;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.database.Cursor;
import android.widget.Toast;

import java.util.ArrayList;


public class TemplateFragment extends Fragment {
    TemplateDatabase myTemplate;

    private ListView templateList;
    private EditText nameEdit;
    private Button[] buttons = new Button[3];
    private String booleansString , colorsString, hsvString, countString, delayString, ontimeString, attachedString, globalString;
    ArrayList list = new ArrayList();
    private float[] HSV_buffer = new float[3];

    private View view;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_template, container, false);
        Data.view = view;
        templateList = view.findViewById(R.id.Templates);
        nameEdit = view.findViewById(R.id.EditName);
        buttons[0] = view.findViewById(R.id.Open);
        buttons[1] = view.findViewById(R.id.Save);
        buttons[2] = view.findViewById(R.id.Delete);
        myTemplate = new TemplateDatabase(getContext());

        final ArrayAdapter adapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, list);
        templateList.setAdapter(adapter);
        templateList.setOnItemClickListener(Templates);
        Buttons();
        templates_refresh();

        buttons[2].setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if(Functions.Pokaz()){
                    Functions.ActionBar(view, "Finished");
                }
                return true;
            }
        });

        return view;
    }

    private void CompressData(){
        booleansString=""; colorsString=""; hsvString=""; countString=""; delayString=""; ontimeString=""; attachedString="";
        booleansString = Data.chosen[0]+" "+Data.chosen[1]+" "+Data.chosen[2]+" "+Data.chosen[3]+" "+Data.chosen[4]+" "+Data.chosen[5]+" "+Data.attach[0]+
                " "+Data.attach[1]+" "+Data.attach[2]+" "+Data.attach[3]+" "+Data.attach[4]+" "+Data.attach[5]+" "+Data.attach[6]+" "+Data.attach[7]+" "+Data.color_model;
        colorsString = Data.colors[0]+" "+Data.colors[1]+" "+Data.colors[2]+" "+Data.colors[3]+" "+Data.colors[4]+" "+Data.colors[5];
        hsvString = Data.add_hsv[0]+" "+Data.add_hsv[1]+" "+Data.add_hsv[2]+" "+Data.add_hsv[3]+" "+Data.add_hsv[4]+" "+Data.add_hsv[5]+" "+Data.count_hsv[0]+
                " "+Data.count_hsv[1]+" "+Data.count_hsv[2]+" "+Data.count_hsv[3]+" "+Data.count_hsv[4]+" "+Data.count_hsv[5]+" "+Data.add_color;
        countString = Data.count_value[0]+" "+Data.count_value[1]+" "+Data.count_value[2]+" "+Data.count_value[3]+" "+Data.count_value[4]+" "+Data.count_value[5];
        delayString = Data.delay_value[0]+" "+Data.delay_value[1]+" "+Data.delay_value[2]+" "+Data.delay_value[3]+" "+Data.delay_value[4]+" "+Data.delay_value[5];
        ontimeString = Data.ontime_value[0]+" "+Data.ontime_value[1]+" "+Data.ontime_value[2]+" "+Data.ontime_value[3]+" "+Data.ontime_value[4]+" "+Data.ontime_value[5];
        globalString = Data.global_value+"";
        attachedString = Data.attached[0]+" "+Data.attached[1]+" "+Data.attached[2]+" "+Data.attached[3]+" "+Data.attached[4]+" "+Data.attached[5]+" "+Data.attached[6]+" "+Data.attached[7];
    }

    private void Buttons(){
        buttons[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cursor res = myTemplate.getAllData();
                if(res.getCount()==0){
                    Functions.ActionBar(view, "Nothing found");
                    return;
                }
                while(res.moveToNext()){
                    if(res.getString(1).equals(nameEdit.getText().toString())) {
                        String[] array_booleans = res.getString(2).split(" ");
                        String[] array_colors = res.getString(3).split(" ");
                        String[] array_hsv = res.getString(4).split(" ");
                        String[] array_count = res.getString(5).split(" ");
                        String[] array_delay = res.getString(6).split(" ");
                        String[] array_ontime = res.getString(7).split(" ");
                        Data.global_value = res.getInt(8);
                        String[] array_attached = res.getString(9).split(" ");
                        for(int i=0; i<6; i++){
                            Data.chosen[i] = Boolean.parseBoolean(array_booleans[i]);
                            Data.colors[i] = Integer.parseInt(array_colors[i]);
                            Data.add_hsv[i] = Integer.parseInt(array_hsv[i]);
                            Data.count_value[i] = Integer.parseInt(array_count[i]);
                            Data.delay_value[i] = Integer.parseInt(array_delay[i]);
                            Data.ontime_value[i] = Integer.parseInt(array_ontime[i]);
                            Color.RGBToHSV(Color.red(Data.colors[i]), Color.green(Data.colors[i]), Color.blue(Data.colors[i]), HSV_buffer);
                            Data.HSV[i][0] = (int)(HSV_buffer[0]/1.41);
                            Data.HSV[i][1] = (int)(HSV_buffer[1]*255);
                            Data.HSV[i][2] = (int)(HSV_buffer[2]*255);
                        }
                        for(int i=6; i<12;i++){
                            Data.count_hsv[i-6] = Integer.parseInt(array_hsv[i]);
                        }
                        Data.add_color = Integer.parseInt((array_hsv[12]));
                        for(int i=0;i<8;i++){
                            Data.attach[i] = Boolean.parseBoolean(array_booleans[i+6]);
                            Data.attached[i] = Integer.parseInt(array_attached[i]);
                        }
                        Data.color_model=Boolean.parseBoolean(array_booleans[14]);
                    }
                }
                Functions.ActionBar(view, "Template opened");
                Functions.BtSync();
            }
        });
        buttons[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CompressData();
                if (nameEdit.getText().toString().trim().length() > 0) {
                    boolean completed = myTemplate.updateData(nameEdit.getText().toString(), booleansString, colorsString, hsvString, countString, delayString,
                            ontimeString, globalString, attachedString);
                    if (completed) {
                        templates_refresh();
                    }
                } else{
                    Functions.ActionBar(getView(), "Name is empty");
                }
            }
        });
        buttons[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                Integer deletedRows = myTemplate.deleteData(nameEdit.getText().toString());
                                if(deletedRows > 0) {
                                    Functions.ActionBar(getView(), "Data deleted");
                                    templates_refresh();
                                }else{
                                    Functions.ActionBar(getView(),"Data not deleted");}
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                break;
                        }
                    }
                };
                ContextThemeWrapper ctw = new ContextThemeWrapper(getActivity(), R.style.Theme_AppCompat_DayNight);
                AlertDialog.Builder builder = new AlertDialog.Builder(ctw);
                builder.setMessage("Are you sure?")
                        .setTitle("Delete")
                        .setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
            }
        });
    }

    public void templates_refresh(){
        Cursor res = myTemplate.getAllData();
        StringBuffer buffer = new StringBuffer();
        list.clear();
        int i=0;
        while(res.moveToNext()){
            list.add(i, res.getString(1));
            i++;
        }

        final ArrayAdapter adapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, list);
        templateList.setAdapter(adapter);
    }

    private AbsListView.OnItemClickListener Templates = new AdapterView.OnItemClickListener() {
        public void onItemClick (AdapterView<?> av, View v, int arg2, long arg3)
        {
            String name = ((TextView) v).getText().toString();
            nameEdit.setText(name);
        }
    };

}
