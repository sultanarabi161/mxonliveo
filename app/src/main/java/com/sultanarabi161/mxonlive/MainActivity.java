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
        
        // Enable JavaScript execution inside WebView
        webSettings.setJavaScriptEnabled(true);
        // Enable DOM storage API for modern web apps
        webSettings.setDomStorageEnabled(true);
        // Allow file access from WebView
        webSettings.setAllowFileAccess(true);
        // Allow content access from WebView
        webSettings.setAllowContentAccess(true);
        // Allow media playback without requiring user gesture
        webSettings.setMediaPlaybackRequiresUserGesture(false);
        
        // Ensure links open inside the app instead of an external browser
        myWebView.setWebViewClient(new WebViewClient());
        // Load the local HTML file from assets folder
        myWebViewandroid_asset/index.html");
    }

    // 1. When the Home button is pressed, PiP (Picture-in-Picture) mode will be activated
    @Override
    protected void onUserLeaveHint() {
        super.onUserLeaveHint();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            PictureInPictureParams params = new PictureInPictureParams.Builder()
                    // Set PiP window aspect ratio to 16:9
                    .setAspectRatio(new Rational(16, 9))
                    .build();
            enterPictureInPictureMode(params);
        }
    }

    // 2. PiP mode control logic (for new HTML integration)
    @Override
    public void onPictureInPictureModeChanged(boolean isInPictureInPictureMode, Configuration newConfig) {
        super.onPictureInPictureModeChanged(isInPictureInPictureMode, newConfig);
        
        if (isInPictureInPictureMode) {
            // Hide the Action Bar when in PiP mode
            if (getSupportActionBar() != null) getSupportActionBar().hide();
            
            // Notify HTML: "Enable PiP mode" (e.g., hide lists or UI elements)
            myWebView.evaluateJavascript("togglePiPMode(true);", null);
            
        } else {
            // Show the Action Bar when returning to full screen
            if (getSupportActionBar() != null) getSupportActionBar().show();
            
            // Notify HTML: "Disable PiP mode" (e.g., restore lists or UI elements)
            myWebView.evaluateJavascript("togglePiPMode(false);", null);
        }
    }

    @Override
    public void onBackPressed() {
        // Navigate back inside WebView if possible
        if (myWebView.canGoBack()) {
            myWebView.goBack();
        } else {
            // Otherwise, perform default back action
            super.onBackPressed();
        }
    }
}
