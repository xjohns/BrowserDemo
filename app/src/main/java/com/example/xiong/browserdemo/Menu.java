package com.example.xiong.browserdemo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;


/**
 * Created by xiong on 2016/12/12.
 */

public class Menu extends AppCompatActivity implements View.OnClickListener{
    private Button btn_saveSettings;
    private CheckBox cBox_imageLoad;
    private CheckBox cBox_javaScriptEnabled;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);
        //读取存储的设置数据
        btn_saveSettings = (Button) findViewById(R.id.btn_saveSettings);
        cBox_imageLoad = (CheckBox) findViewById(R.id.cBox_imageLoad);
        cBox_javaScriptEnabled = (CheckBox) findViewById(R.id.cBox_javaScriptEnabled);
        btn_saveSettings.setOnClickListener(this);
        SharedPreferences pref = getSharedPreferences("data_settings", MODE_PRIVATE);
        cBox_imageLoad.setChecked(pref.getBoolean("cBox_imageLoad", true));
        cBox_javaScriptEnabled.setChecked(pref.getBoolean("cBox_javaScriptEnabled", true));
    }
    //设置界面的功能实现
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_saveSettings:{
                Intent intent = new Intent(Menu.this, MainActivity.class);
                MainActivity mainActivity = new MainActivity();
                mainActivity.imageCanLoad = cBox_imageLoad.isChecked();
                mainActivity.javaScriptEnabled = cBox_javaScriptEnabled.isChecked();
                //保存所需的数据
                SharedPreferences.Editor editor = getSharedPreferences("data_settings", MODE_PRIVATE).edit();
                editor.putBoolean("cBox_imageLoad", cBox_imageLoad.isChecked());
                editor.putBoolean("cBox_javaScriptEnabled", cBox_javaScriptEnabled.isChecked());
                editor.putBoolean("imageCanLoad", mainActivity.imageCanLoad);
                editor.putBoolean("javaScriptEnabled", mainActivity.javaScriptEnabled);
                editor.commit();
                Toast.makeText(this, "设置已保存", Toast.LENGTH_SHORT).show();
                startActivity(intent);
                break;
            }
            default:
                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
