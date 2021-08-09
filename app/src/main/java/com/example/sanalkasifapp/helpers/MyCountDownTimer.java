package com.example.sanalkasifapp.helpers;

import android.graphics.Color;
import android.os.CountDownTimer;
import android.widget.TextView;

public class MyCountDownTimer extends CountDownTimer {
private TextView timerText;
private long timerLong;
    public MyCountDownTimer(TextView timerText,long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval);
        this.timerLong =timerLong;
        this.timerText = timerText;
    }

    @Override
    public void onTick(long millisUntilFinished) {
        timerLong=millisUntilFinished;
        timerText.setText(String.valueOf(millisUntilFinished/1000));
        if ((millisUntilFinished/1000 )<=10 && (millisUntilFinished/1000 )>0)
            timerText.setTextColor(Color.RED);
        else if ((millisUntilFinished/1000 )== 0) {
            timerText.setTextColor(Color.BLACK);
            timerText.setText("Süreniz Bitti");
        }

    }

    @Override
    public void onFinish() {
        timerText.setText("Timer Finished");
    }
}