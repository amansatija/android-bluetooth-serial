package com.harrysoft.androidbluetoothserial.demoapp.threedplot;

import java.util.ArrayList;

public class IBECLog {

    private static volatile ArrayList<ModelFingerData> modelFingerDataArrayList = new ArrayList<>();

    public static synchronized  ArrayList<ModelFingerData> getModelFingerDataArrayList() {
        return modelFingerDataArrayList;
    }

    public static synchronized void  setModelFingerDataArrayList(ArrayList<ModelFingerData> modelFingerDataArrayList) {
        IBECLog .modelFingerDataArrayList = modelFingerDataArrayList;
    }
}
