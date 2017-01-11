/*
 * *****************************************************************************************
 * Copyright  (c) 2016 Lou
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
 * *****************************************************************************************
 */

package com.lyloou.demo;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.lyloou.demo.presenter.UserPresenter;
import com.lyloou.demo.view.IUserView;
import com.lyloou.lou.activity.LouActivity;

import java.io.File;
import java.util.concurrent.ExecutionException;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends LouActivity implements IUserView {


    @BindView(R.id.et_id)
    EditText mEtId;
    @BindView(R.id.et_first_name)
    EditText mEtFirstName;
    @BindView(R.id.et_last_name)
    EditText mEtLastName;

    UserPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mPresenter = new UserPresenter(this);
    }

    @Override
    public int getId() {
        int i = 0;
        try {
            i = Integer.parseInt(mEtId.getText().toString());
        } catch (NumberFormatException e) {
            i=-1;
        }

        return i;
    }

    @Override
    public String getFirstName() {
        return mEtFirstName.getText().toString();
    }

    @Override
    public String getLastName() {
        return mEtLastName.getText().toString();
    }

    @Override
    public void setFirstName(String firstName) {
        mEtFirstName.setText(firstName);
    }

    @Override
    public void setLastName(String lastName) {
        mEtLastName.setText(lastName);
    }

    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btn_save:
                mPresenter.saveUser(getId(), getFirstName(), getLastName());
                break;
            case R.id.btn_load:
//                mPresenter.loadUser(getId());
                downloadImg("");
                break;
        }

        mPresenter.showCurrent(this);
    }

    private void downloadImg(String url) {
        try {
            File file = Glide.with(this).load(url).downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}