package com.example.oop2022finalp2.Activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.ThemeUtils;
import androidx.core.content.res.ResourcesCompat;

import android.app.WallpaperColors;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.oop2022finalp2.Data.WordBase;
import com.example.oop2022finalp2.Data.WordBaseManager;
import com.example.oop2022finalp2.R;
import com.example.oop2022finalp2.databinding.ActivityChangeWordbaseBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class UISwitchActivity extends AppCompatActivity {



    /**
     * @author Haotian Zhang
     * @description:
     * @date :2022/12/16 17:11
     *
     **/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ui_switch);

//        获取switch的id
        Switch day_night;
        day_night = findViewById(R.id.day_night);
        int now = AppCompatDelegate.getDefaultNightMode();
        //判断日夜模式
        if(now ==AppCompatDelegate.MODE_NIGHT_YES)
        {
            day_night.setChecked(true);
        }
        else
        {
            day_night.setChecked(false);
        }
        //switch切换后修改模式
        day_night.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked)
            {
                if(isChecked)
                {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    restartNow();
                }
                else
                {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    restartNow();
                }

            }
        });
    }

    private void restartNow(){
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }


}