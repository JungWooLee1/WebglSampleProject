package com.example.jw910911.myapplication;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MainActivity extends AppCompatActivity  implements View.OnTouchListener, Handler.Callback {


    // webview와 webviewClient 변수를 선언합니다.
    // WebViewClient는 url이 로드되었을 때 해야할 작업을 정의 할 수 있습니다.

    private WebView mWebView;
    private WebViewClient client;

    WebSettings webSettings;

    // 이벤트 전달을 위한 핸들러 등록
    private final Handler handler = new Handler(this);


    // 이벤트 상수 선언
    // 0 : 손 뗐을 때
    // 1 : 손 처음에 터치했을 때
    // 2 : 드래그 할 때
    // 4 : 아무것도 정의되지 않았을 때
    public final static int FINGER_RELEASED = 0;
    public final static int FINGER_TOUCHED = 1;
    public final static int FINGER_DRAGGING = 2;
    public final static int FINGER_UNDEFINED = 3;

    private int fingerState = FINGER_RELEASED;

    // 이벤트 상수 선언 2
    private static final int CLICK_ON_URL = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // 웹뷰 설정 (Url , 이벤트)
        mWebView = (WebView) findViewById(R.id.activity_main_webview);
        mWebView.setVerticalScrollBarEnabled(false);
        mWebView.setHorizontalScrollBarEnabled(false);


        mWebView.setOnTouchListener(this);
        mWebView.getSettings().setJavaScriptEnabled(true);


        //webview의 옵션을 설정합니다.
        //webSettings = mWebView.getSettings();
        //webSettings.setJavaScriptEnabled(true);


        client = new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                handler.sendEmptyMessage(CLICK_ON_URL);
                return false;
            }
        };

        mWebView.setWebViewClient(client);
        mWebView.loadUrl("file:///android_asset/drawLine2.html");


    }


    @Override
    public boolean handleMessage(Message msg) {
        return false;
    }


    // 안드로이드에서 motionEvent가 발생하면 해당 상수로 이벤트를 전달합니다.
    // 안드로이드에서 전달받은 이벤트를 webview의 onTouch리스너로 전달하여
    // 스크립트(html , javascript)에 이벤트를 전달합니다.
    @Override
    public boolean onTouch(View v, MotionEvent motionEvent) {

        Log.e("onTouch",""+motionEvent.getAction());

        switch (motionEvent.getAction()) {


            // 처음에 손가락으로 화면을 터치했을때 이벤트 발생
            case MotionEvent.ACTION_DOWN:
                if (fingerState == FINGER_RELEASED) fingerState = FINGER_TOUCHED;
                else fingerState = FINGER_UNDEFINED;
                break;

            // 드래그 이후 화면에서 손가락을 땠을때 일어나는 이벤트

            case MotionEvent.ACTION_UP:
                if(fingerState != FINGER_DRAGGING) {
                    fingerState = FINGER_RELEASED;
                }
                else if (fingerState == FINGER_DRAGGING) fingerState = FINGER_RELEASED;
                else fingerState = FINGER_UNDEFINED;
                break;

            // 손가락을 드래그 할때 발생하는 이벤트
            case MotionEvent.ACTION_MOVE:
                if (fingerState == FINGER_TOUCHED || fingerState == FINGER_DRAGGING) fingerState = FINGER_DRAGGING;
                else fingerState = FINGER_UNDEFINED;
                break;

            default:
                fingerState = FINGER_UNDEFINED;

        }

        return false;
    }
}
