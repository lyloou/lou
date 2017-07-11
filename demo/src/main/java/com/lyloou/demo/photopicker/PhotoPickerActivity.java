package com.lyloou.demo.photopicker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.lyloou.demo.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.iwf.photopicker.PhotoPicker;

public class PhotoPickerActivity extends AppCompatActivity {

    @BindView(R.id.tv_photos)
    TextView tvPhotos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photopicker);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_pick)
    public void onClick(View view) {
        PhotoPicker.builder()
                .setPhotoCount(9)
                .setShowCamera(true)
                .setShowGif(false)
                .setPreviewEnabled(true)
                .start(this, PhotoPicker.REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PhotoPicker.REQUEST_CODE) {
            if (data != null) {
                ArrayList<String> stringArrayExtra = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
                String text = TextUtils.join("\n\n", stringArrayExtra.toArray());
                tvPhotos.setText(text);
            }
        }
    }
}
