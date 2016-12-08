package com.example.xiong.browserdemo;

import android.app.ProgressDialog;
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
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    //声明各组件
    private Button btn_main;
    private Button btn_back;
    private Button btn_forward;
    private Button btn_search;
    private WebView wVmain;
    private ProgressDialog pDialog1;
    private EditText eText;
    private String url_home="http:/www.baidu.com/";
    private String url_input;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }
    //构造init方法集中初始化
    private void init() {
        btn_main = (Button) findViewById(R.id.rbtn_home);
        btn_back = (Button) findViewById(R.id.rbtn_back);
        btn_search = (Button) findViewById(R.id.search);
        btn_forward = (Button) findViewById(R.id.rbtn_forward);
        eText = (EditText) findViewById(R.id.editText);
        btn_main.setOnClickListener(this);
        btn_back.setOnClickListener(this);
        btn_forward.setOnClickListener(this);
        btn_search.setOnClickListener(this);
        wVmain = (WebView) findViewById(R.id.wView);
        //设置好内置浏览器的属性
        WebSettings webSettings = wVmain.getSettings();
        webSettings.setJavaScriptEnabled(true);//启用支持javascript
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
                    //网页加载完毕
                    closeDialog();
                }
                else{
                    //网页正在加载
                    openDialog(newProgress);
                }
            }

            private void openDialog(int newProgress) {
                if (pDialog1==null){
                    pDialog1 = new ProgressDialog(MainActivity.this);
                    pDialog1.setTitle("正在加载中，Hold On!");
                    pDialog1.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                    pDialog1.setProgress(newProgress);
                    pDialog1.show();
                }
                else{
                    pDialog1.setProgress(newProgress);
                }
            }

            private void closeDialog() {
                if (pDialog1!=null&& pDialog1.isShowing()){
                    pDialog1.dismiss();
                    pDialog1=null;
                }
            }
        });
    }

    //业务逻辑实现
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            //首页按钮功能
            case R.id.rbtn_home:{
                wVmain.loadUrl(url_home);//加载web资源
                break;
            }
            //通过EditText转到指定网址功能
            case R.id.search:{
                url_input = "http:/" + eText.getText().toString() +"/";
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
        }

    }
    //改写物理按键--返回的逻辑
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            //弹出实际网址信息
            Toast.makeText(this,wVmain.getUrl(),Toast.LENGTH_SHORT).show();
            if (wVmain.canGoBack()){
                wVmain.goBack();//返回上一页
                return true;
            }
            else {
                System.exit(0);//退出程序
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
