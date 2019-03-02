package com.harrysoft.androidbluetoothserial.demoapp.threedplot;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.harrysoft.androidbluetoothserial.demoapp.CommunicateActivity;
import com.harrysoft.androidbluetoothserial.demoapp.CommunicateViewModel;
import com.harrysoft.androidbluetoothserial.demoapp.R;

public class ActivityThreeDPlot extends CommunicateActivity {


    private WebView mWebView;
    private JniNative mJavascriptInterface = new JniNative();

    @Override
    protected void setContentViewWrapper() {
        setContentView(R.layout.activity_threedplot);
    }
    private volatile static Boolean mBoolStartReadingSentenceTrue=false;
    private volatile static ModelFingerData modelFingerData = new ModelFingerData();

    public static synchronized ModelFingerData getModelFingerData() {
        return modelFingerData;
    }
    private boolean mBoolUpdate=true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Setup our activity
        super.onCreate(savedInstanceState);

        viewModel.getMessagesRecieved().observe(this, message -> {
            // Only update the message if the ViewModel is trying to reset it

            Log.d("3dplot","************************" +
                    "**********************");
            Log.d("3dplotmsg",message);
            Log.d("3dplot","************************" +
                    "**********************");
//            handleMsgRecievedOld(message);



            //////////////////////////////////////////////////////////////////////////////////////////
//            if(mBoolUpdate){
//                mBoolUpdate=false;
                handleMsgRecieved(message);
//                refreshFlag();
//            }
        });

        //////////////////////webview funcitoning
        mWebView = findViewById(R.id.webview);

        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        mWebView.getSettings().setDomStorageEnabled(true);
        mWebView.getSettings().setAllowFileAccess(true);
        mWebView.getSettings().setAllowFileAccessFromFileURLs(true);
        mWebView.addJavascriptInterface(mJavascriptInterface, "jsinterface");
        mWebView.setWebContentsDebuggingEnabled(true);
        mWebView.setWebViewClient(new WebViewClient()
        {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url)
            {
                Toast.makeText(ActivityThreeDPlot.this, url, Toast.LENGTH_SHORT).show(); //Debugging purposes
                if (url.endsWith(".mp4")){
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.parse(url), "video/mp4");
                    view.getContext().startActivity(intent);
                }
                else{
                    view.loadUrl(url);
                }
                return true;
            }

            public void onPageFinished(WebView view, String url)
            {
                //Toast.makeText(myActivity.this, "Oh no!", Toast.LENGTH_SHORT).show();
            }

            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl)
            {
                Toast.makeText(ActivityThreeDPlot.this, description, Toast.LENGTH_SHORT).show();
                String summary = "<html><body><strong>" + "lost connection" + "</body></html>";
                mWebView.loadData(summary, "text/html", "utf-8");
            }

        }); //
//        mWebView.loadUrl("https://www.journaldev.com");
        mWebView.loadUrl("file:///android_asset/web/index_surfaceplot.html");
//        mWebView.loadUrl("file:///android_asset/web/index_mesh.html");
    }

    private void refreshFlag() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mBoolUpdate=true;
            }
        },50);
    }

    private void handleMsgRecieved(String message) {
        getModelFingerData().addData(message);
    }

    private void handleMsgRecievedOld(String message) {

        if(mBoolStartReadingSentenceTrue ){
            modelFingerData.addData(message);

        }

        if (!TextUtils.isEmpty(message)) {
            if(message.equals("*")){
                mBoolStartReadingSentenceTrue=true;
                modelFingerData= new ModelFingerData();
//                    Log.d("3dplot","************************" +
//                            "**********************");
//                    Log.d("3dplot","Star intercepted , from here start reading ");
//                    Log.d("3dplot","************************" +
//                            "**********************");
            }

            if(message.equals("$")){

//                    Log.d("3dplot","************************" +
//                            "**********************");
//                    Log.d("3dplot","dollar  intercepted , from here stop  reading ");
//                    Log.d("3dplot","************************" +
//                            "**********************");
                mBoolStartReadingSentenceTrue=false;
                triggerEndOfStatement();
                modelFingerData= new ModelFingerData();
            }
        }

    }

    Boolean mBoolEnoughDelaySinceLastUpdate =true;

    private void triggerEndOfStatement() {
        mJavascriptInterface.setModelFingerData(modelFingerData.copy());

        if(mBoolEnoughDelaySinceLastUpdate ){
            String mStrUrlToLoad = "javascript:updateChart()";
            mWebView.loadUrl(mStrUrlToLoad);
            mBoolEnoughDelaySinceLastUpdate= false;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mBoolEnoughDelaySinceLastUpdate= true;
                }
            },4000);
        }
    }


    public void clearMessages(View v){
            viewModel.clearMessage("");
    }

    public void clearEverything(View v){
        viewModel.clearMessage("");
        modelFingerData.clearData();
    }


    @Override
    public void doOnMsgRecieved(String message) {
//        super.doOnMsgRecieved(message);
    }
}
