package com.example.sanya.puzzle15;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;

public class Showrules extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_rules);

        WebView rulesWebView = (WebView) findViewById(R.id.rules_webview);
        String intro = "<h1 style='text-align:center'>The rules</h1><p align='justify'>"+getString(R.string.rules1)+"</p>";
        intro = intro + "<h1 style='text-align:center'>History</h1><p align='justify'>"+getString(R.string.rules2)+"</p>";
        rulesWebView.loadData(intro, "text/html", null);
    }
}
