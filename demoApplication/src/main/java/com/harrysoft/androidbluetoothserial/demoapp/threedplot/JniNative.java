package com.harrysoft.androidbluetoothserial.demoapp.threedplot;

import android.os.Handler;
import android.webkit.JavascriptInterface;

public class JniNative {

    private String mStrTest= "Java method called!!";

    private volatile ModelFingerData modelFingerData = new ModelFingerData();

    public int count = 0;

    @JavascriptInterface
    public synchronized String getSensorRawData() {
        setModelFingerData(ActivityThreeDPlot.getModelFingerData().copy());
        return modelFingerData.toString();
    }

    public void setModelFingerData(ModelFingerData modelFingerData) {
        this.modelFingerData = modelFingerData;
    }

    public ModelFingerData getModelFingerData() {
        return modelFingerData;
    }
}
