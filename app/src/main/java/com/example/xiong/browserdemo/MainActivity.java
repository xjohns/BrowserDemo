package com.example.xiong.browserdemo;

import android.content.Intent;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    //声明各组件
    private Button btn_menu;
    private Button btn_refresh;
    private Button btn_home;
    private Button btn_back;
    private Button btn_forward;
    private Button btn_search;
    private Button btn_javaScriptEnabled;
    private Button btn_imageLoad;
    private WebView wVmain;
    private ProgressBar progressBar;
    private EditText eText;
    private String url_home="http:/www.baidu.com/";
    private String url_input;
    private Boolean javaScriptEnabled = false;
    private Boolean imageCanLoad = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        //设置好内置浏览器的属性
        WebSettings webSettings = wVmain.getSettings();
        webSettings.setJavaScriptEnabled(javaScriptEnabled);//启用支持javascript
        webSettings.supportMultipleWindows();//支持多窗口
        webSettings.setSupportZoom(true);//启用支持缩放
        webSettings.setLoadsImagesAutomatically(imageCanLoad);//设置自动加载图片
        wVmain.getSettings().setCacheMode(webSettings.LOAD_CACHE_ELSE_NETWORK);//优先使用缓存
        wVmain.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                wVmain.loadUrl(url);
                eText.setText(wVmain.getUrl());
                return true;
            }

        });
        wVmain.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (newProgress==100){
                    //网页加载完毕，关闭进度条
                    progressBar.setVisibility(View.GONE);
                }
                else{
                    //网页正在加载，更新进度条
                    progressBar.setVisibility(View.VISIBLE);
                    progressBar.setProgress(newProgress);
                }
            }

        });
    }
    //构造init方法集中初始化
    private void init() {
        btn_menu = (Button) findViewById(R.id.rbtn_menu);
        btn_refresh = (Button) findViewById(R.id.rbtn_refresh);
        btn_home = (Button) findViewById(R.id.rbtn_home);
        btn_back = (Button) findViewById(R.id.rbtn_back);
        btn_search = (Button) findViewById(R.id.search);
        btn_forward = (Button) findViewById(R.id.rbtn_forward);
        btn_javaScriptEnabled = (Button) findViewById(R.id.rbtn_javaScriptEnabled);
        btn_imageLoad = (Button) findViewById(R.id.rbtn_imageLoad);
        eText = (EditText) findViewById(R.id.editText);
        wVmain = (WebView) findViewById(R.id.wView);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        btn_menu.setOnClickListener(this);
        btn_refresh.setOnClickListener(this);
        btn_home.setOnClickListener(this);
        btn_back.setOnClickListener(this);
        btn_forward.setOnClickListener(this);
        btn_search.setOnClickListener(this);
        btn_javaScriptEnabled.setOnClickListener(this);
        btn_imageLoad.setOnClickListener(this);
        eText.setOnClickListener(this);
    }

    //业务逻辑实现
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            //设置按钮功能
            case R.id.rbtn_menu: {
                Intent intent = new Intent(MainActivity.this, ActivityMenu.class);
                startActivity(intent);
                break;
            }
            //刷新按钮功能
            case R.id.rbtn_refresh: {
                wVmain.reload();
                break;
            }
            //首页按钮功能
            case R.id.rbtn_home:{
                wVmain.loadUrl(url_home);//加载web资源
                break;
            }
            //通过EditText转到指定网址功能，点击之后调用搜索结束方法改变控件显示
            case R.id.search:{
                url_input = "http://" + eText.getText().toString() +"/";
                search_close();
                wVmain.loadUrl(url_input);
                break;
            }
            //后退按钮功能
            case R.id.rbtn_back:{
                if (wVmain.canGoBack()) {
                    wVmain.goBack();
                }
                else {
                    finish();
                }
                break;
            }
            //前进按钮功能
            case R.id.rbtn_forward:{
                if (wVmain.canGoForward()) {
                    wVmain.goForward();
                }
                break;
            }
            //点击搜索框时改变控件显示
            case R.id.editText: {
                search_start();
                break;
            }
            case R.id.rbtn_javaScriptEnabled: {
                if (javaScriptEnabled == false){
                    javaScriptEnabled = true;
                }else {
                    javaScriptEnabled = false;
                }
                break;
            }
            case R.id.rbtn_imageLoad: {
                if (imageCanLoad = false){
                    imageCanLoad = true;
                }else {
                    imageCanLoad = false;
                }
                break;
            }

        }

    }

    private void search_start () {
        eText.selectAll();
        eText.setCursorVisible(true);
        btn_search.setVisibility(View.VISIBLE);
    }

    private void search_close () {
        eText.setCursorVisible(false);
        btn_search.setVisibility(View.INVISIBLE);
        eText.setText(wVmain.getUrl());
    }

    //改写物理按键--返回的逻辑
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
                wVmain.goBack();
            exit_doubleClick();
        }
        return false;
    }
    private static Boolean isExit = false;
    //构造双击返回键退出方法
    private void exit_doubleClick() {
        Timer tExit = null;
        if (isExit == false){
            isExit = true;//准备退出
            Toast.makeText(MainActivity.this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            tExit = new Timer();
            tExit.schedule(new TimerTask() {
                @Override
                public void run() {
                    isExit = false; //取消退出
                }
            }, 1000);//如果1秒内没有再按一次则取消任务
        }else {
            finish();
            System.exit(0);
        }
    }

}
