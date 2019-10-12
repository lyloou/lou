/*
 * Copyright  (c) 2017 Lyloou
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.lyloou.test.media.recoder;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.gyf.immersionbar.ImmersionBar;
import com.lyloou.test.R;

import java.io.IOException;

// [MediaRecorder | Android Developers](https://developer.android.com/guide/topics/media/mediarecorder.html)

public class RecorderActivity extends AppCompatActivity {

    private static final int REQUEST_PERMISION_RECORD_AUDIO = 868;
    private String[] permission = {Manifest.permission.RECORD_AUDIO};
    private String mFileName;
    private ToggleButton mTbtnRecord;
    private ToggleButton mTbtnPlay;
    private MediaPlayer mPlayer;
    private MediaRecorder mRecorder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mediarecorder);

        requestPermission();
        initStorageEnv();
        initToolbar();
        initView();

    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("录音和播放");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        ImmersionBar.with(this)
                .statusBarDarkFont(true)
                .navigationBarDarkIcon(true)
                .statusBarColor(R.color.colorAccent)
                .statusBarAlpha(0.1f)
                .init();
    }

    private void requestPermission() {
        System.out.println("请求授权");
        ActivityCompat.requestPermissions(this, permission, REQUEST_PERMISION_RECORD_AUDIO);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean permissionToRecordAccepted = false;
        switch (requestCode) {
            case REQUEST_PERMISION_RECORD_AUDIO:
                permissionToRecordAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                break;
        }
        if (!permissionToRecordAccepted) {
            finish();
        }
    }

    private void initStorageEnv() {
        mFileName = getExternalCacheDir().getAbsolutePath();
        mFileName += "/audiorecordtest.3gp";
    }

    private void initView() {
        mTbtnRecord = (ToggleButton) findViewById(R.id.tbtn_record);
        mTbtnPlay = (ToggleButton) findViewById(R.id.tbtn_play);

        mTbtnRecord.setOnCheckedChangeListener((buttonView, isOn) -> onRecord(isOn));
        mTbtnPlay.setOnCheckedChangeListener((buttonView, isOn) -> onPlay(isOn));

        findViewById(R.id.btn_grant).setOnClickListener(view -> requestPermission());
    }


    private void onPlay(boolean isOn) {
        if (isOn) {
            startPlaying();
        } else {
            stopPlaying();
        }
    }

    private void stopPlaying() {
        if (mPlayer != null) {
            mPlayer.stop();
            mPlayer.release();
            mPlayer = null;
        }
    }

    private void startPlaying() {
        mPlayer = new MediaPlayer();
        mPlayer.setOnCompletionListener(mp -> {
            stopPlaying();
            mTbtnPlay.setChecked(false);
        });

        try {
            mPlayer.setDataSource(mFileName);
            mPlayer.prepare();
            mPlayer.start();
        } catch (IOException e) {
            Toast.makeText(this, "prepare failed in playing", Toast.LENGTH_SHORT).show();
        }
    }

    private void onRecord(boolean isOn) {
        if (isOn) {
            startRecording();
        } else {
            stopRecording();
        }
    }

    private void startRecording() {
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRecorder.setOutputFile(mFileName);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        try {
            mRecorder.prepare();
            mRecorder.start();
        } catch (IOException e) {
            Toast.makeText(this, "prepare() failed in recording", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void stopRecording() {
        mRecorder.stop();
        mRecorder.release();
        mRecorder = null;
    }
}
