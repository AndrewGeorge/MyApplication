package com.example.administrator.mycamera;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    public final static int REQ_1 = 1;
    public final static int REQ_2 = 2;
    private Button btn_statr;
    private ImageView iv_carema;
    private String mFilePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initData();
        initView();
    }

    private void initView() {
        LinearLayout layout = (LinearLayout) findViewById(R.id.conten_layout);
        btn_statr = (Button) layout.findViewById(R.id.btn_start);
        iv_carema = (ImageView) layout.findViewById(R.id.iv_camera);
        btn_statr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, REQ_1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (REQ_1 == requestCode) {
                //使用相机默认路径获取缩率图
                Log.i("onActivityResult", "requestCode:" + requestCode);
                Bundle bundle = data.getExtras();
                Bitmap bitmap = (Bitmap) bundle.get("data");
                iv_carema.setImageBitmap(bitmap);
            } else if (REQ_2 == requestCode) {
                //通过指定路径获取照片原图
                FileInputStream inputStream = null;
                try {
                    inputStream = new FileInputStream(mFilePath);
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    iv_carema.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (inputStream != null) {
                            inputStream.close();
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private void initData() {
        mFilePath = Environment.getExternalStorageDirectory().getPath();
        mFilePath = mFilePath + "/" + "temp.png";
    }

    public void startCameraBig(View v) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Uri phototUri = Uri.fromFile(new File(mFilePath));
        //指定照片存放路径
        intent.putExtra(MediaStore.EXTRA_OUTPUT, phototUri);
        startActivityForResult(intent, REQ_2);
    }

    public void customCamera(View V) {
        Intent intent = new Intent(this, CustomCameraAvtivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
