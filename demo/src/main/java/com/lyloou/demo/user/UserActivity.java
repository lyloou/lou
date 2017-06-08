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

package com.lyloou.demo.user;


import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.lyloou.demo.R;
import com.lyloou.demo.data.User;
import com.lyloou.demo.setting.SettingActivity;
import com.lyloou.lou.activity.LouActivity;
import com.lyloou.lou.util.Uview;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class UserActivity extends LouActivity implements UserContract.View {

    UserContract.Presenter mPresenter;
    @BindView(R.id.et_id)
    EditText mEtId;
    @BindView(R.id.et_first_name)
    EditText mEtFirstName;
    @BindView(R.id.et_last_name)
    EditText mEtLastName;
    @BindView(R.id.btn_save)
    Button mBtnSave;
    @BindView(R.id.btn_load)
    Button mBtnLoad;
    Unbinder unbinder;
    @BindView(R.id.btn_setting)
    Button mBtnSetting;
    @BindView(R.id.iv_voice)
    ImageView mIvVoice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_user);
        unbinder = ButterKnife.bind(this);
        new UserPresenter(this);
        initView();
    }

    private void initView() {
        Uview.clickEffectByNoEffect(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = mEtId.getText().toString();
                String firstName = mEtFirstName.getText().toString();
                String lastName = mEtLastName.getText().toString();

                switch (v.getId()) {
                    case R.id.btn_load:
                        mPresenter.load(id);
                        break;
                    case R.id.btn_save:
                        mPresenter.save(new User(id, firstName, lastName));
                        break;
                    case R.id.btn_setting:
                        mPresenter.setting();
                        break;
                }
            }
        }, mBtnLoad, mBtnSave, mBtnSetting);

        mIvVoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnimationDrawable animationDrawable = (AnimationDrawable) mIvVoice.getBackground();
                if (animationDrawable.isRunning()) {
                    animationDrawable.stop();
                    animationDrawable.selectDrawable(0);
                } else {
                    animationDrawable.start();
                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        mPresenter.start();
    }

    @Override
    public void setPresenter(UserContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showId(String id) {
        mEtId.setText(id);
    }

    @Override
    public void showFirstName(String firstName) {
        mEtFirstName.setText(firstName);
    }

    @Override
    public void showLastName(String lastName) {
        mEtLastName.setText(lastName);
    }

    @Override
    public void showSetting() {
        Intent intent = new Intent(mContext, SettingActivity.class);
        startActivity(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}