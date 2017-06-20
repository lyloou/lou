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

package com.lyloou.test;

import android.app.Activity;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.io.IOException;

public class MediaRecorderActivity extends Activity {

    private String mFileName;
    private ToggleButton mTbtnRecord;
    private ToggleButton mTbtnPlay;
    private MediaPlayer mPlayer;
    private MediaRecorder mRecorder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initStorageEnv();
    }

    private void initStorageEnv() {
        mFileName = getExternalCacheDir().getAbsolutePath();
        mFileName += "/audiorecordtest.3gp";
        System.out.println(mFileName);
    }

    private void initView() {
        mTbtnRecord = (ToggleButton) findViewById(R.id.tbtn_record);
        mTbtnPlay = (ToggleButton) findViewById(R.id.tbtn_play);

        mTbtnRecord.setOnCheckedChangeListener((buttonView, isOn) -> onRecord(isOn));
        mTbtnPlay.setOnCheckedChangeListener((buttonView, isOn) -> onPlay(isOn));
    }

    private void onPlay(boolean isOn) {
        if (isOn) {
            startPlaying();
        } else {
            stopPlaying();
        }
    }

    private void stopPlaying() {
        mPlayer.stop();
        mPlayer.release();
        mPlayer = null;
    }

    private void startPlaying() {
        mPlayer = new MediaPlayer();
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
        } catch (IOException e) {
            Toast.makeText(this, "prepare() failed in recording", Toast.LENGTH_SHORT).show();
        }
        mRecorder.start();
    }

    private void stopRecording() {
        mRecorder.stop();
        mRecorder.release();
        mRecorder = null;
    }
}
