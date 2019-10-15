package com.lyloou.test.media.pic;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.lyloou.test.R;
import com.lyloou.test.util.Uview;

public class PictureActivity extends AppCompatActivity {
    private static final int REQUEST_CODE = 100;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_media_picture);
        initView();
    }

    private ImageView mIvContent;

    private void initView() {
        mIvContent = findViewById(R.id.iv_content);

        Glide.with(this).load(R.mipmap.loading).asGif().into(mIvContent);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("拍照和裁剪");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        Uview.initStatusBar(this, R.color.colorAccent);
    }

    public void takePhoto(View view) {
        startActivityForResult(new Intent(this, CropImageActivity.class), REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode) {
            case REQUEST_CODE:
                handleResultCode(resultCode, data);
                break;
        }
    }

    private void handleResultCode(int resultCode, Intent data) {
        switch (resultCode) {
            case RESULT_OK:
                mIvContent.setImageURI(null);
                mIvContent.setImageURI(data.getData());
                break;
            case RESULT_FIRST_USER:
                Toast.makeText(this, data.getStringExtra("error"), Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
