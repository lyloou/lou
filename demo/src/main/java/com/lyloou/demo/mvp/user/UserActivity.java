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

package com.lyloou.demo.mvp.user;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.lyloou.demo.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UserActivity extends AppCompatActivity implements UserContract.View {

    UserContract.Presenter mPresenter;
    @BindView(R.id.et_id)
    EditText mEtId;
    @BindView(R.id.et_first_name)
    EditText mEtFirstName;
    @BindView(R.id.et_last_name)
    EditText mEtLastName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        ButterKnife.bind(this);
        new UserPresenter(this);
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
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://lyloou.com"));
        startActivity(intent);
    }

    @OnClick({R.id.btn_save, R.id.btn_load, R.id.btn_enter})
    public void onClick(View v) {
        String firstName = mEtFirstName.getText().toString();
        String lastName = mEtLastName.getText().toString();
        String id = mEtId.getText().toString();

        switch (v.getId()) {
            case R.id.btn_load:
                mPresenter.load(id);
                break;
            case R.id.btn_save:
                mPresenter.save(new User(id, firstName, lastName));
                break;
            case R.id.btn_enter:
                mPresenter.setting();
                break;
        }
    }
}