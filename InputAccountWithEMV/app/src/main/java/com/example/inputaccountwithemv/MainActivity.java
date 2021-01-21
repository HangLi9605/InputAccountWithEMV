package com.example.inputaccountwithemv;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.telephony.TelephonyDisplayInfo;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.pax.poslink.CommSetting;
import com.pax.poslink.ProcessTransResult;
import com.pax.poslink.aidl.BasePOSLinkCallback;
import com.pax.poslink.fullIntegration.Init;
import com.pax.poslink.fullIntegration.InputAccount;

import java.io.File;
import java.io.RandomAccessFile;
import java.util.List;

public class MainActivity extends AppCompatActivity implements InputAccount.InputAccountCallback{

    String amt, swipeFlag, manualFlag, tapFlag, chipFlag, encryptFlag, keyslot, timeout;
    ProcessTransResult TransResult;
    String resultcode, resulttxt;
    int result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn = findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        for(int i = 0; i < 2000; i ++){
                            result = processTrans(view, i);
                            if(result == 1){
                                break;
                            }
                        }
                    }
                });
                thread.start();
            }
        });
    }

    private CommSetting commSet(){
        CommSetting commSetting = new CommSetting();
        commSetting.setType(CommSetting.AIDL);
        commSetting.setTimeOut("-1");
        return commSetting;
    }

    public InputAccount.InputAccountRequest doInputAccount(){

        InputAccount.InputAccountRequest inputReq = new InputAccount.InputAccountRequest();

        amt = "1000";
        swipeFlag = "1";
        manualFlag = "1";
        tapFlag = "1";
        chipFlag = "1";
        encryptFlag = "1";
        keyslot = "1";
        timeout = "1000";

        inputReq.setEdcType("CREDIT");
        inputReq.setTransType("SALE");
        inputReq.setAmount(amt);
        inputReq.setMagneticSwipeEntryFlag(swipeFlag);
        inputReq.setManualEntryFlag(manualFlag);
        inputReq.setContactlessEntryFlag(tapFlag);
        inputReq.setContactEMVEntryFlag(chipFlag);
        inputReq.setEncryptionFlag(encryptFlag);
        inputReq.setKeySLot(keyslot);
        inputReq.setTimeOut(timeout);

        return inputReq;
    }

    public int processTrans(View view, int count){
        InputAccount.inputAccountWithEMV(this, doInputAccount(), commSet(), new BasePOSLinkCallback<InputAccount.InputAccountResponse>() {
            @Override
            public void onFinish(InputAccount.InputAccountResponse inputAccountResponse) {
                Log.d("CocaCola", String.valueOf(count));

                resultcode = "Result Code: " + inputAccountResponse.getResultCode();
                Log.d("CocaCola", resultcode);

                resulttxt = "Result Text: " + inputAccountResponse.getResultTxt();
                Log.d("CocaCola", resulttxt);
            }
        }, (InputAccount.InputAccountCallback) this);
        if(resultcode != "000000"){
            return 0;
        }else {
            Log.d("CocaCola", "Continue");
            return 1;
        }
    }

    @Override
    public void onInputAccountStart() {

    }

    @Override
    public void onEnterExpiryDate() {

    }

    @Override
    public void onEnterZip() {

    }

    @Override
    public void onEnterCVV() {

    }

    @Override
    public void onSelectEMVApp(List<String> list) {

    }

    @Override
    public void onProcessing(String s, String s1) {

    }

    @Override
    public void onWarnRemoveCard() {

    }
//
//    private void initData(){
//        String filePath = "/Phone storage/DCIM/";
//        String fileName = "log.txt";
//
//        writeTxtToFile("txt content", filePath, fileName);
//    }
//
//    public void writeTxtToFile(String strcontent, String filePath, String fileName) {
//        //生成文件夹之后，再生成文件，不然会出错
//        makeFilePath(filePath, fileName);
//
//        String strFilePath = filePath+fileName;
//        // 每次写入时，都换行写
//        String strContent = strcontent + "\r\n" ;
//        try {
//            File file = new File(strFilePath);
//            if (!file.exists()) {
//                Log.d( "TestFile" , "Create the file:" + strFilePath);
//                file.getParentFile().mkdirs();
//                file.createNewFile();
//            }
//            RandomAccessFile raf = new RandomAccessFile(file, "rwd" );
//            raf.seek(file.length());
//            raf.write(strContent.getBytes());
//            raf.close();
//        } catch (Exception e) {
//            Log.e( "TestFile" , "Error on write File:" + e);
//        }
//    }
//
//    public File makeFilePath(String filePath, String fileName) {
//        File file = null ;
//        makeRootDirectory(filePath);
//        try {
//            file = new File(filePath + fileName);
//            if (!file.exists()) {
//                file.createNewFile();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return file;
//    }
//
//    public static void makeRootDirectory(String filePath) {
//        File file = null ;
//        try {
//            file = new File(filePath);
//            if (!file.exists()) {
//                file.mkdir();
//            }
//        } catch (Exception e) {
//            Log.i( "error:" , e+ "" );
//        }
//    }
}