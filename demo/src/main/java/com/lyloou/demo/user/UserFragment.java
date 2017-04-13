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
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.lyloou.demo.R;
import com.lyloou.demo.data.User;
import com.lyloou.demo.setting.SettingActivity;
import com.lyloou.lou.fragment.LouFragment;
import com.lyloou.lou.util.Uview;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Author:    Lou
 * Version:   V1.0
 * Date:      2017.04.13 12:31
 * <p>
 * Description:
 */
public class UserFragment extends LouFragment implements UserContract.View {
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

    public static UserFragment newInstance() {

        Bundle args = new Bundle();

        UserFragment fragment = new UserFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, container, false);
        Log.d(TAG, "onCreateView: created");
        unbinder = ButterKnife.bind(this, view);
        initView(view);
        return view;
    }

    private void initView(View view) {
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
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}
