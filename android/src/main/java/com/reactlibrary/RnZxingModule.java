package com.reactlibrary;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.facebook.react.bridge.ActivityEventListener;
import com.facebook.react.bridge.BaseActivityEventListener;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;


public class RnZxingModule extends ReactContextBaseJavaModule {
    private static ReactApplicationContext reactContext;
    private Callback mCallback;

    public RnZxingModule(ReactApplicationContext context) {
        super(context);
        reactContext = context;
    }

    @ReactMethod
    public void showQrReader(Callback callback) {
        mCallback = callback;
        new IntentIntegrator(getCurrentActivity()).initiateScan();
        reactContext.addActivityEventListener(mActivityEventListener);
    }

    @ReactMethod
    public void showBoletoReader(Callback callback) {
        mCallback = callback;
        IntentIntegrator integrator = new IntentIntegrator(getCurrentActivity());
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ITF);
        // integrator.setPrompt("Scan a barcode");
        // integrator.setBeepEnabled(false);
        // integrator.setTorchEnabled(false);
        integrator.setBarcodeImageEnabled(false);
        integrator.initiateScan();
        // new IntentIntegrator(getCurrentActivity()).initiateScan();
        reactContext.addActivityEventListener(mActivityEventListener);
    }

    @Override
    public String getName() {
        return "RnZxingModule";
    }

    private final ActivityEventListener mActivityEventListener = new BaseActivityEventListener() {
        @Override
        public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
            IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            if(result != null){
                if(result.getContents() != null){
                    mCallback.invoke(result.getContents(), result.getBarcodeImagePath());
                    reactContext.removeActivityEventListener(this);
                }
            } else {
               super.onActivityResult(activity, requestCode, resultCode, data);
            }
        }
    };

}
