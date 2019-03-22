package com.nhomappmobile.musicplayer;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    protected  void onStart(){
        super.onStart();
    }

    @Override
    protected void onStop(){
        super.onStop();
    }
}
