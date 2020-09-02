package com.aaratechnologies.lawtrend.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.aaratechnologies.lawtrend.R;

public class PdfViewActivity extends AppCompatActivity {
    WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_view);

        webView=findViewById(R.id.webView);

        Intent i=getIntent();
        String url=i.getStringExtra("pdf");
//        Toast.makeText(this, ""+url, Toast.LENGTH_SHORT).show();
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.getAllowFileAccessFromFileURLs();
        webSettings.setPluginState(WebSettings.PluginState.ON);

        Toast.makeText(this, ""+url, Toast.LENGTH_SHORT).show();
        MywebViewClient1 mywebViewClient = new MywebViewClient1();

        if (url.contains("https://drive")){
            webView.setWebViewClient(mywebViewClient);
            webView.loadUrl(url);
        }
//        else {
//
//        }

    }
    private class MywebViewClient1 extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView webView, String url) {
//            Toast.makeText(getApplicationContext(), ""+url, Toast.LENGTH_SHORT).show();
//            if (url.contains("https://lawtrend.in/")){
//                SharedPreferences sharedPreferences=getSharedPreferences("url_to_send",MODE_PRIVATE);
//                SharedPreferences.Editor editor=sharedPreferences.edit();
//                editor.putString("Data",url);
//                editor.apply();
//                editor.commit();
//            }
////

            Log.d("lasturl", "shouldOverrideUrlLoading: "+url);

            if (isInternetConnected()) {
                if (url.indexOf("drive.google.com") > -1)
                    return false;
            } else {
                Toast.makeText(PdfViewActivity.this, "Sorry", Toast.LENGTH_SHORT).show();
            }


            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
            return true;

        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {



        }

        @Override
        public void onPageFinished(WebView view, String url) {

//            progress.setVisibility(View.GONE);
        }

//        @Override
//        public void onReceivedError(WebView view, int errCode, String errDescription, String failingUrl ) {
//            progress.setVisibility(View.GONE);
//            view.clearView();
//
//
//
////            Toast.makeText(getApplicationContext(), "Error code is "+errCode, Toast.LENGTH_SHORT).show();
//            if(errCode == -2 || errCode == -8|| errCode==-6) {
////                view.loadData("There seems to be a problem with your Internet connection. Please try later", "text/html", "UTF-8");
//                Intent i=new Intent(getApplicationContext(),NextActivity.class);
//                startActivity(i);
//                finish();
//            }
//
//            if(errCode == -14) {
////                view.loadData("Page cannot be found on server", "text/html", "UTF-8");
//                Intent i=new Intent(getApplicationContext(),NextActivity.class);
//                startActivity(i);
//                finish();
//            }
//            if (errCode==-10){
//                Intent i=new Intent(getApplicationContext(), PageNotFound.class);
//                startActivity(i);
//                finish();
//            }
//        }

    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && this.webView.canGoBack()) {
            this.webView.goBack();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    private class MywebViewClient2 extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView webView, String url) {
//            Toast.makeText(getApplicationContext(), ""+url, Toast.LENGTH_SHORT).show();
//            if (url.contains("https://lawtrend.in/")){
//                SharedPreferences sharedPreferences=getSharedPreferences("url_to_send",MODE_PRIVATE);
//                SharedPreferences.Editor editor=sharedPreferences.edit();
//                editor.putString("Data",url);
//                editor.apply();
//                editor.commit();
//            }
////

            Log.d("lasturl", "shouldOverrideUrlLoading: "+url);

            if (isInternetConnected()) {
                if (url.indexOf("lawtrend.in/wp-content") > -1)
                    return false;
            } else {
                Toast.makeText(PdfViewActivity.this, "Sorry", Toast.LENGTH_SHORT).show();
            }


            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
            return true;

        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {



        }

        @Override
        public void onPageFinished(WebView view, String url) {

//            progress.setVisibility(View.GONE);
        }

//        @Override
//        public void onReceivedError(WebView view, int errCode, String errDescription, String failingUrl ) {
//            progress.setVisibility(View.GONE);
//            view.clearView();
//
//
//
////            Toast.makeText(getApplicationContext(), "Error code is "+errCode, Toast.LENGTH_SHORT).show();
//            if(errCode == -2 || errCode == -8|| errCode==-6) {
////                view.loadData("There seems to be a problem with your Internet connection. Please try later", "text/html", "UTF-8");
//                Intent i=new Intent(getApplicationContext(),NextActivity.class);
//                startActivity(i);
//                finish();
//            }
//
//            if(errCode == -14) {
////                view.loadData("Page cannot be found on server", "text/html", "UTF-8");
//                Intent i=new Intent(getApplicationContext(),NextActivity.class);
//                startActivity(i);
//                finish();
//            }
//            if (errCode==-10){
//                Intent i=new Intent(getApplicationContext(), PageNotFound.class);
//                startActivity(i);
//                finish();
//            }
//        }

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

    @Override
    public void onBackPressed() {
        Intent i=new Intent(getApplicationContext(),MainActivity.class);
        startActivity(i);
        finish();
    }
}