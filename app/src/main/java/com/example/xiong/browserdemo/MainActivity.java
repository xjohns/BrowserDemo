package com.example.xiong.browserdemo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
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
    private WebView wVmain;
    private ProgressBar progressBar;
    private EditText eText;
    private String url_input;
    public Boolean imageCanLoad;
    public Boolean javaScriptEnabled;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        SharedPreferences pref = getSharedPreferences("data_settings", MODE_PRIVATE);
        imageCanLoad = pref.getBoolean("imageCanLoad", true);
        javaScriptEnabled = pref.getBoolean("javaScriptEnabled", true);
        //设置好内置浏览器的属性
        WebSettings webSettings = wVmain.getSettings();
        webSettings.setBlockNetworkImage(!imageCanLoad.booleanValue());//是否禁止从网络（通过http和https URI schemes访问的资源）下载图片资源
        webSettings.setJavaScriptEnabled(javaScriptEnabled);//启用支持javascript
        webSettings.setSupportZoom(true);//启用支持缩放
        webSettings.setBuiltInZoomControls(true);//设置内置的缩放控件
        webSettings.setUseWideViewPort(true);//将图片调整到适合webview的大小
        webSettings.setLoadWithOverviewMode(true);//缩放至屏幕的大小，与setUseWideViewPort合用，实现自适应屏幕
        webSettings.supportMultipleWindows();//支持多窗口
        wVmain.getSettings().setCacheMode(webSettings.LOAD_CACHE_ELSE_NETWORK);//优先使用缓存
        wVmain.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                wVmain.loadUrl(url);
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
            /*
            重写创建新窗口方法，view :请求新窗口的WebView;
            isDialog :如果是true，代表这个新窗口只是个对话框，如果是false，则是一个整体的大小的窗口;
            isUserGesture :如果是true，代表这个请求是用户触发的，例如点击一个页面上的一个连接;
            resultMsg :当一个新的WebView被创建时这个只被传递给他，resultMsg.obj是一个WebViewTransport的对象，它被用来传送给新创建的WebView;
            返回值 :这个方法如果返回true，代表这个主机应用会创建一个新的窗口，否则应该返回fasle.
             */
            @Override
            public boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg) {
                WebView wV_new = new WebView(view.getContext());
                wV_new.setWebViewClient(new WebViewClient(){
                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        startActivity(browserIntent);
                        return true;
                    }
                });
                WebView.WebViewTransport webViewTransport = (WebView.WebViewTransport) resultMsg.obj;
                webViewTransport.setWebView(wV_new);
                resultMsg.sendToTarget();
                return true;
            }
            //重写返回网站标题的方法，在搜索框显示当前网站标题
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                eText.setText(title);
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
        eText = (EditText) findViewById(R.id.editText);
        wVmain = (WebView) findViewById(R.id.wView);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        btn_menu.setOnClickListener(this);
        btn_refresh.setOnClickListener(this);
        btn_home.setOnClickListener(this);
        btn_back.setOnClickListener(this);
        btn_forward.setOnClickListener(this);
        btn_search.setOnClickListener(this);
        eText.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences pref = getSharedPreferences("data_settings", MODE_PRIVATE);
        imageCanLoad = pref.getBoolean("imageCanLoad", true);
        javaScriptEnabled = pref.getBoolean("javaScriptEnabled", true);
        WebSettings webSettings = wVmain.getSettings();
        webSettings.setBlockNetworkImage(!imageCanLoad.booleanValue());//是否禁止从网络（通过http和https URI schemes访问的资源）下载图片资源
        webSettings.setJavaScriptEnabled(javaScriptEnabled);//启用支持javascript
    }

    //业务逻辑实现
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            //设置按钮功能
            case R.id.rbtn_menu: {
                Intent intent = new Intent(MainActivity.this, Menu.class);
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
                wVmain.loadUrl("http:/www.baidu.com/");//加载web资源
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

        }

    }

    private void search_start () {
        eText.selectAll();
        eText.setCursorVisible(true);
        btn_search.setVisibility(View.VISIBLE);
    }

    private void search_close () {
        eText.setCursorVisible(false);
        btn_search.setVisibility(View.GONE);
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

