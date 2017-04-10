package com.example.sanya.puzzle15;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;

public class Showrules extends AppCompatActivity implements View.OnClickListener{
    int[] creditViews = {R.id.music_credits, R.id.horse_credits, R.id.thor_credits, R.id.vic_bg_credits, R.id.sp_bg_credits, R.id.sp_frame_credits, R.id.vic_frame_credits, R.id.vic_font_credit, R.id.sp_font_credit};
    String[] webIntents = {"https://www.youtube.com/channel/UCjMZjGhrFq_4llVS_x2XJ_w", "https://images-na.ssl-images-amazon.com/images/I/91yPXjGrH7L._SL1500_.jpg", "http://marvel-movies.wikia.com/wiki/File:Thor_111.jpg", "http://wallpaper-gallery.net/single/victorian-wallpapers/victorian-wallpapers-14.html", "http://image.hotdog.hu/user/bilberry/magazin/steampunk_hatterek08_1280x800.jpg", "http://dswilliams.com/2016/08/30/steampunk-transparent-png-picture-frame/", "https://openclipart.org/detail/240423/victorian-frame", "http://www.1001freefonts.com/harrington.font", "http://www.1001freefonts.com/sancreek.font"};

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

        for(int i=0; i<creditViews.length; i++) {
            findViewById(creditViews[i]).setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        for(int i=0; i<creditViews.length; i++) {
            if(v.getId() == creditViews[i]) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(webIntents[i]));
                startActivity(intent);
            }
        }
    }
}
