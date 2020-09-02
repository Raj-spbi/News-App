package com.aaratechnologies.lawtrend;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

import com.skydoves.elasticviews.ElasticFloatingActionButton;
import com.tuyenmonkey.mkloader.MKLoader;

public class HostScreen extends AppCompatActivity {
    WebView webView;
    MKLoader progress;
    ElasticFloatingActionButton fab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host_screen);

        progress = findViewById(R.id.progress);

        webView = findViewById(R.id.webView);
        fab=findViewById(R.id.fab);
        progress.setVisibility(View.GONE);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.getAllowFileAccessFromFileURLs();
        webSettings.setPluginState(WebSettings.PluginState.ON);

        MywebViewClient mywebViewClient = new MywebViewClient();

        webView.setWebViewClient(mywebViewClient);

        if (isInternetConnected()) {
            webView.loadUrl("https://lawtrend.in/");
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SharedPreferences sharedPreferences=getSharedPreferences("url_to_send",MODE_PRIVATE);
                    String url1=sharedPreferences.getString("Data","https://lawtrend.in/");
                    Log.d("lateurl", "onClick: "+url1);
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, ""+url1+" https://play.google.com/store/apps/details?id="+ BuildConfig.APPLICATION_ID);
                    sendIntent.setType("text/plain");
                    startActivity(sendIntent);
                }
            });
        } else {
            Intent i = new Intent(getApplicationContext(), NextActivity.class);
            startActivity(i);
            finish();
        }

    }
    private class MywebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView webView, String url) {
//            Toast.makeText(getApplicationContext(), ""+url, Toast.LENGTH_SHORT).show();
            if (url.contains("https://lawtrend.in/")){
            SharedPreferences sharedPreferences=getSharedPreferences("url_to_send",MODE_PRIVATE);
            SharedPreferences.Editor editor=sharedPreferences.edit();
            editor.putString("Data",url);
            editor.apply();
            editor.commit();
            }
//

            Log.d("lasturl", "shouldOverrideUrlLoading: "+url);

            if (isInternetConnected()) {
                if (url.indexOf("lawtrend.in") > -1)
                    return false;
            } else {
                Intent i = new Intent(getApplicationContext(), NextActivity.class);
                startActivity(i);
                finish();
            }


            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
            return true;

        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            progress.setVisibility(View.VISIBLE);
           

        }

        @Override
        public void onPageFinished(WebView view, String url) {

            progress.setVisibility(View.GONE);
        }

        @Override
        public void onReceivedError(WebView view, int errCode, String errDescription, String failingUrl ) {
            progress.setVisibility(View.GONE);
            view.clearView();



//            Toast.makeText(getApplicationContext(), "Error code is "+errCode, Toast.LENGTH_SHORT).show();
            if(errCode == -2 || errCode == -8|| errCode==-6) {
//                view.loadData("There seems to be a problem with your Internet connection. Please try later", "text/html", "UTF-8");
                Intent i=new Intent(getApplicationContext(),NextActivity.class);
                startActivity(i);
                finish();
            }

            if(errCode == -14) {
//                view.loadData("Page cannot be found on server", "text/html", "UTF-8");
                Intent i=new Intent(getApplicationContext(),NextActivity.class);
                startActivity(i);
                finish();
            }
            if (errCode==-10){
                Intent i=new Intent(getApplicationContext(), PageNotFound.class);
                startActivity(i);
                finish();
                }
            }

        }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && this.webView.canGoBack()) {
            this.webView.goBack();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
    public boolean isInternetConnected() {
        // At activity startup we manually check the internet status and change
        // the text status
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;

    }


}