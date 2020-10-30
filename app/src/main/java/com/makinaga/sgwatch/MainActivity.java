package com.makinaga.sgwatch;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import static android.view.Window.FEATURE_NO_TITLE;

public class MainActivity extends Activity {

    TextView tvTime, tvDate, tvBattery;

    final int INTERVAL_PERIOD = 1000;
    Timer timer = new Timer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_main);

        tvTime = this.findViewById(R.id.tvTime);
        tvDate = this.findViewById(R.id.tvDate);
        tvBattery = this.findViewById(R.id.tvBattery);
    }

    @Override
    public void onResume() {
        super.onResume();

        final Handler handler = new Handler();
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask(){
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        tvTime.setText(getNowTime());
                        tvDate.setText(getNowDate());
                        tvBattery.setText(String.valueOf(getBattery())+"%");
                    }
                });
            }
        }, 0, INTERVAL_PERIOD);
    }

    @Override
    public void onPause() {
        //timer.cancel();
        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                timer.cancel();
                timer.purge();
            }
        });
        super.onPause();
    }

    @Override
    public void onDestroy() {
        //timer.cancel();
        //timer.purge();
        super.onDestroy();
    }

    @Override
    protected void onStop() {

        super.onStop();
    }


    public static String getNowTime(){
        final DateFormat df = new SimpleDateFormat("HH:mm");
        final Date date = new Date(System.currentTimeMillis());
        return df.format(date);
    }

    public static String getNowDate(){
        final DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
        final Date date = new Date(System.currentTimeMillis());
        return df.format(date);
    }

    public int getBattery(){
        IntentFilter intentfilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = this.registerReceiver(null, intentfilter );
        int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

        float batteryPct = level * 100 / (float)scale;

        return (int)batteryPct;
    }
}
