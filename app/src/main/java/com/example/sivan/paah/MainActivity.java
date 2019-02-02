package com.example.sivan.paah;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity  {

    Stopwatch stopwatch;
    TextView tvTextView;
    TextView tvVolumeView;
    ImageView ivCircle;
    Button btnStart;
    Timer timer;
    Soundmeter soundmeter;
    SeekBar sbSeekBar;
    boolean lock = false;

    private void setTimer(Timer t){
        t.scheduleAtFixedRate(new TimerTask(){
            @Override
            public void run(){
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        long millis = stopwatch.getElapsedTime();
                        int seconds = (int) (millis / 1000);
                        int minutes = seconds / 60;
                        seconds     = seconds % 60;
                        millis      = millis % 100;
                        tvTextView.setText(String.format("%02d:%02d:%02d", minutes, seconds, millis));
                    }
                });
            }
        },0,10);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //checking the permission status
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) !=
                    PackageManager.PERMISSION_GRANTED) {
                //request the permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.RECORD_AUDIO},
                        1);
            } else {
                startApp();
            }
        }


    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode,
            String[] permissions,
            int[] grantResults
    ){
        startApp();
    }


    public void startApp(){

        tvTextView = findViewById(R.id.timerTextView);
        btnStart = findViewById(R.id.timerButton);
        tvVolumeView = findViewById(R.id.volumeTextView);
        ivCircle = findViewById(R.id.imageView);
        sbSeekBar = findViewById(R.id.SensitivityBar);

        Timer tmrVolChecker = new Timer();
        tmrVolChecker.scheduleAtFixedRate(new TimerTask(){
            @Override
            public void run(){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        double volumeLevel = soundmeter.getAmplitude();
                        tvVolumeView.setText("" + volumeLevel);
                        if(lock) {
                            lock = false;
                            return;
                        }
                        if (volumeLevel > 400 * (100 - sbSeekBar.getProgress())) {
                            ivCircle.setVisibility(View.VISIBLE);
                            btnStart.performClick();
                            lock = true;
                        } else {
                            ivCircle.setVisibility(View.INVISIBLE);
                        }
                    }
                });
            }
        },0,200);




        stopwatch = new Stopwatch();
        soundmeter = new Soundmeter();
        soundmeter.start();

        btnStart.setOnClickListener(new OnClickListener() { // Previusly - ' setOnClickListener(this);'
            @Override
            public void onClick(View v) {
                Button b = (Button) v;
                if (b.getText().equals("Start")) {
                    timer = new Timer();

                    setTimer(timer);
                    b.setText("Stop");
                    stopwatch.start(); //start stopwatch

                } else if (b.getText().equals("Stop")) {
                    b.setText("Start");
                    stopwatch.stop();//stop stopwatch
                    timer.cancel();
                }
            }
        });
    }

    @Override
    protected void onPause(){
        super.onPause();
        Toast.makeText(this, "onPause", Toast.LENGTH_SHORT).show();
        stopwatch.stop();
    }

    @Override
    protected void onResume(){
        super.onResume();
        Toast.makeText(this, "onResume", Toast.LENGTH_SHORT).show();
        stopwatch.stop();
    }
}
