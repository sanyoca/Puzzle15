package com.example.sanya.puzzle15;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;

public class Showrules extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_rules);
        // no lolligaggin with the screen !!!
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // reads the rules from the strings.xmls and convert them into the webview
        WebView rulesWebView = (WebView) findViewById(R.id.rules_webview);
        String intro = "<h1 style='text-align:center'>The rules</h1><p align='justify'>" + getString(R.string.rules1) + "</p>";
        intro = intro + "<h1 style='text-align:center'>History</h1><p align='justify'>" + getString(R.string.rules2) + "</p>";
        intro = intro + "<p align='justify'>Source: https://en.wikipedia.org/wiki/15_puzzle</p>";
        rulesWebView.loadData(intro, "text/html", null);
    }
}
