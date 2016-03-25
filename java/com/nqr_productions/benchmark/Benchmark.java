package com.nqr_productions.benchmark;

import android.util.Base64;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import android.os.Handler;
import android.os.Message;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Benchmark extends Activity {


    private TextView result;
    private Button compute;
    private String teststring;
    private String HashValue;
    private String MD5Value;
    private Long ttShared;
    private Long tt2Shared;
    private Long tt3Shared;
    private Integer jobsdone;
    private String output;
    private Integer floor;
    private Integer floor2;
    private Integer floor3;
    private Integer code = 9999999;
    //private long tsLong;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            final Long tt3 = (Long) msg.obj;
            result.setText("Brute Force Complete! Time Taken: " + tt3.toString());
            jobsdone++;
            tt3Shared = tt3;
            if (jobsdone==3) {
                printout();
            }

        }
    };
    Handler handler2 = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            final Long tt2 = (Long) msg.obj;
            result.setText("MD5 Complete! Time Taken: " + tt2.toString());
            jobsdone++;
            tt2Shared = tt2;
            if (jobsdone==3) {
                printout();
            }
        }
    };

    Handler handler3 = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            final Long tt = (Long) msg.obj;
            result.setText("SHA-1 Complete! Time Taken: " + tt.toString());
            jobsdone++;
            ttShared = tt;
            if (jobsdone==3) {
                printout();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_benchmark);
        compute = (Button) findViewById(R.id.btn1);
        result = (TextView) findViewById(R.id.textView2);
        teststring = getResources().getString(R.string.teststring);


    }


    public void printout() {
        compute.setText("BEGIN SEQUENCE...");
        Long average = (tt2Shared + tt3Shared + ttShared) / 3;
        Integer score = Math.round(average / 100000000);
        result.setText("SHA-1 Time Taken: " + ttShared.toString() + "\n MD5 Time Taken: " + tt2Shared.toString() + "\n Brute Force Time Taken: " + tt3Shared.toString() + "\n \n Score: " + score.toString());

    }

    public void onBeginClick(View view) {

        jobsdone = 0;
        computeSHAHash(teststring);
        computeMD5Hash(teststring);
        compute.setText("CALCULATING...");
        bruteForce();


    }



    public void bruteForce() {

        Runnable r = new Runnable() {
            public void run() {
                Long tsLong3 = System.nanoTime();
                Integer successfulcode = 0;
                for (Integer i = 0; i < code; i++) {
                    successfulcode = i;
                }
                Long ttLong3 = System.nanoTime() - tsLong3;
                Message msg = Message.obtain();
                msg.obj = ttLong3;
                msg.setTarget(handler);
                if (successfulcode != 0) {
                    msg.sendToTarget();
                }

            }
        };
        Thread newThread = new Thread(r);
        newThread.start();

    }


    public void computeMD5Hash(String password) {
        Runnable r = new Runnable() {
            public void run() {
                Long tsLong = System.nanoTime();
                for (Integer s = 0; s < 20000; s++) {
                    try {
                        MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
                        digest.update("The big bad wolf".getBytes());
                        byte messageDigest[] = digest.digest();

                        StringBuffer MD5Hash = new StringBuffer();
                        for (int i = 0; i < messageDigest.length; i++) {
                            String h = Integer.toHexString(0xFF & messageDigest[i]);
                            while (h.length() < 2)
                                h = "0" + h;
                            MD5Hash.append(h);
                        }

                        MD5Value = MD5Hash.toString();

                    } catch (NoSuchAlgorithmException e) {
                        Log.e("Benchmark", "Error initializing MD5");
                    }
                }
                Long ttLong2 = System.nanoTime() - tsLong;
                Message msg = Message.obtain();
                msg.obj = ttLong2;
                msg.setTarget(handler2);
                msg.sendToTarget();

            }
        };
        Thread newThread = new Thread(r);
        newThread.start();

    }


    public void computeSHAHash(String password) {
        Runnable r = new Runnable() {
            public void run() {
                Long tsLong = System.nanoTime();
                for (Integer i = 0; i < 20000; i++) {
                    MessageDigest mdSha1 = null;
                    try {
                        mdSha1 = MessageDigest.getInstance("SHA-1");
                    } catch (NoSuchAlgorithmException e1) {
                        Log.e("Benchmark", "Error initializing SHA1");
                    }
                    try {
                        mdSha1.update("The big bad wolf".getBytes("ASCII"));
                    } catch (UnsupportedEncodingException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    byte[] data = mdSha1.digest();
                    StringBuffer sb = new StringBuffer();
                    String hex = null;
                    hex = Base64.encodeToString(data, 0, data.length, 0);

                    sb.append(hex);
                    HashValue = sb.toString();
                }

                Long ttLong3 = System.nanoTime() - tsLong;
                Message msg = Message.obtain();
                msg.obj = ttLong3;
                msg.setTarget(handler3);
                msg.sendToTarget();

            }

        };
        Thread newThread = new Thread(r);
        newThread.start();


    }

}


