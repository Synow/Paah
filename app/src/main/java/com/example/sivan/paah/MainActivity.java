package com.example.sivan.paah;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity  {

    Stopwatch stopwatch = new Stopwatch();
    TextView tvTextView;
    Button btnStart;
    Timer timer;

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

        tvTextView = findViewById(R.id.timerTextView);
        btnStart = findViewById(R.id.timerButton);

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
}
