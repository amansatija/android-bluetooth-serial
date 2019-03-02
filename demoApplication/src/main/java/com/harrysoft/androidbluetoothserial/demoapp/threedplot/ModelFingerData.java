package com.harrysoft.androidbluetoothserial.demoapp.threedplot;

import android.opengl.Matrix;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ModelFingerData {

    private ArrayList<JSONArray> mArrLisStingsFingerData = new ArrayList<>();

    private JSONArray mArrLisStringX = new JSONArray();
    private JSONArray mArrLisStringy = new JSONArray();
    private JSONArray mArrLisStringz = new JSONArray();
    private String mStrData;

    private IBECLog  mLog =  new IBECLog ();
    public static  float mAmpFactor = 5f;

    public ModelFingerData() {
        initArrayList();
    }

    public void initArrayList(){
        mArrLisStingsFingerData = new ArrayList<>();
        for(int i=0;i<80;i++){
            JSONArray mJsonArr = new JSONArray();
            mJsonArr.put(0);
            mJsonArr.put(0);
            mJsonArr.put(0);
            mArrLisStingsFingerData.add(mJsonArr);

            try {
                mArrLisStringX.put(i,0d);
                mArrLisStringy.put(i,0d);
                mArrLisStringz.put(i,0d);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }
    public ModelFingerData(String mStrFingerData) {

        addData(mStrFingerData);
    }

    public void addData(String mStrArgData){

        if(mStrArgData==null){
            return;
        }
        mStrData = mStrArgData;

        String[] mStrArrDataSplit = mStrData.split(" ");
        if(mStrArrDataSplit!=null&&mStrArrDataSplit.length<=0){
            Log.d("Hola","split array empty ");
            return;
        }
        try {
            if((mStrArrDataSplit[0] != "V") ||
                    (mStrArrDataSplit[0] != "#") ||
                    (mStrArrDataSplit[0] != "*")||
                    (mStrArrDataSplit[0] != "!") ){
                if((mStrArrDataSplit[0].length() == 2) && (mStrArrDataSplit[1].length() == 4) ){
                    if((Integer.valueOf(mStrArrDataSplit[0]) % 10) < 9 ){

                        JSONArray mJsonArr = new JSONArray();
                        int h0 = Integer.valueOf(mStrArrDataSplit[0]);
                        int h1 = Integer.valueOf(mStrArrDataSplit[1]);

                        int x = (8 - h0 % 10);
                        int y = (h0/10);

                        mJsonArr.put(x);
                        mJsonArr.put(y);

                        try {
                            float z = h1*mAmpFactor;
                            mJsonArr.put(z);
                            mArrLisStringX.put(h0,x);
                            mArrLisStringy.put(h0,y);

                            if(z<20){
                                z=0;
                            }

                            mArrLisStringz.put(h0,z);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        mArrLisStingsFingerData.set(Integer.valueOf(mStrArrDataSplit[0]),mJsonArr);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        ///////////////////////////////////////////////////////////////////////////
        /////////////////////////Surface plot data //////////////////////////////////////////////////
        ///////////////////////////////////////////////////////////////////////////
//        JSONArray mJsonArr = new JSONArray();
//        for (JSONArray mJsonArrData:
//            mArrLisStingsFingerData) {
//            mJsonArr.put(mJsonArrData.toString());
//        }
//        return mJsonArr.toString();

        ///////////////////////////////////////////////////////////////////////////
        /////////////////////////mesh data //////////////////////////////////////////////////
        ///////////////////////////////////////////////////////////////////////////
        JSONObject mJsonObject = new JSONObject();
        try {
            mJsonObject.put("x",mArrLisStringX);
            mJsonObject.put("y",mArrLisStringy);
            mJsonObject.put("z",mArrLisStringz);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mJsonObject.toString();

    }

    public ModelFingerData copy(){
        try {
            return this.clone();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ModelFingerData();

    }

    @Override
    protected ModelFingerData clone() throws CloneNotSupportedException {

        ModelFingerData mModelFingerData = new ModelFingerData();
        mModelFingerData.mArrLisStingsFingerData= this.mArrLisStingsFingerData;

        mModelFingerData.mArrLisStringX = this.mArrLisStringX;
        mModelFingerData.mArrLisStringy = this.mArrLisStringy;
        mModelFingerData.mArrLisStringz = this.mArrLisStringz;
        return mModelFingerData ;
    }

    public void clearData() {
        initArrayList();
    }
}

