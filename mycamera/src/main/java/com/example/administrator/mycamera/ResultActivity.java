package com.example.administrator.mycamera;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class ResultActivity extends AppCompatActivity {

    private LinearLayout result_layout;
    private ImageView iv_pic;
    private Intent intent;
    private String path;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        initData();
        initView();
    }

    private void initView() {
        result_layout = (LinearLayout) findViewById(R.id.content_result);
        iv_pic = (ImageView) findViewById(R.id.pic);
        Bitmap bitmap = BitmapFactory.decodeFile(path);
        iv_pic.setImageBitmap(bitmap);
    }

    private void initData() {
        intent = getIntent();
        path = intent.getStringExtra("picPath");
    }
}
