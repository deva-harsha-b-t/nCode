package com.dtl.ncode;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;

import com.andrognito.patternlockview.PatternLockView;
import com.andrognito.patternlockview.listener.PatternLockViewListener;
import com.andrognito.patternlockview.utils.PatternLockUtils;

import java.util.List;

public class lockScreen extends AppCompatActivity {
    PatternLockView mPatternLockView;
    private PatternLockViewListener mPatternLockViewListener = new PatternLockViewListener() {
        @Override
        public void onStarted() {
            Log.d("oi", "Pattern drawing started");
        }
        @Override
        public void onProgress(List<PatternLockView.Dot> progressPattern) {
            Log.d("oi","Pattern progress: " +
                    PatternLockUtils.patternToString(mPatternLockView, progressPattern));
        }

        @Override
        public void onComplete(List<PatternLockView.Dot> pattern) {
            if(PatternLockUtils.patternToString(mPatternLockView, pattern).equals("678")){
                Intent intent  = new Intent(lockScreen.this , MainActivity.class);
                startActivity(intent);
                finish();
            }
            else{
                mPatternLockView.setWrongStateColor(Color.RED);
            }
        }

        @Override
        public void onCleared() {
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock_screen);
        mPatternLockView = (PatternLockView) findViewById(R.id.pattern_lock_view);
        mPatternLockView.addPatternLockListener(mPatternLockViewListener);
    }
}