package com.sultanarabi161.mxonlive;

import androidx.appcompat.app.AppCompatActivity;
import android.app.PictureInPictureParams;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.util.Rational;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MainActivity extends AppCompatActivity {

    private WebView myWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myWebView = findViewById(R.id.webview);
        WebSettings webSettings = myWebView.getSettings();
        
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setAllowContentAccess(true);
        webSettings.setMediaPlaybackRequiresUserGesture(false);
        
        // অ্যাপের লিংকগুলো যাতে ব্রাউজারে ওপেন না হয়ে অ্যাপেই থাকে
        myWebView.setWebViewClient(new WebViewClient());
        
        // নিচের এই লাইনটিতেই ভুল ছিল, এখন ঠিক করে দেওয়া হয়েছে
        myWebView.loadUrl("file:///android_asset/index.html");
    }

    // ১. হোম বাটনে চাপ দিলে PiP মোড চালু হবে
    @Override
    protected void onUserLeaveHint() {
        super.onUserLeaveHint();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            PictureInPictureParams params = new PictureInPictureParams.Builder()
                    .setAspectRatio(new Rational(16, 9))
                    .build();
            enterPictureInPictureMode(params);
        }
    }

    // ২. PiP মোড কন্ট্রোল লজিক (নতুন HTML এর জন্য)
    @Override
    public void onPictureInPictureModeChanged(boolean isInPictureInPictureMode, Configuration newConfig) {
        super.onPictureInPictureModeChanged(isInPictureInPictureMode, newConfig);
        
        if (isInPictureInPictureMode) {
            // অ্যাকশন বার হাইড করুন
            if (getSupportActionBar() != null) getSupportActionBar().hide();
            
            // HTML কে বলুন: "PiP মোড অন করো" (লিস্ট লুকিয়ে ফেলবে)
            myWebView.evaluateJavascript("togglePiPMode(true);", null);
            
        } else {
            // অ্যাকশন বার শো করুন
            if (getSupportActionBar() != null) getSupportActionBar().show();
            
            // HTML কে বলুন: "PiP মোড অফ করো" (লিস্ট ফিরিয়ে আনবে)
            myWebView.evaluateJavascript("togglePiPMode(false);", null);
        }
    }

    @Override
    public void onBackPressed() {
        if (myWebView.canGoBack()) {
            myWebView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}
