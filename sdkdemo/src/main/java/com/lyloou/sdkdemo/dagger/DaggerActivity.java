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

package com.lyloou.sdkdemo.dagger;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.lyloou.sdkdemo.R;

import javax.inject.Inject;

public class DaggerActivity extends AppCompatActivity {
    @Inject
    UserModel mUserModel;

    @Inject
    UserModel mUserModel2;

    @Inject
    ShopModel mShopModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dagger);
        ActivityComponent component = DaggerActivityComponent.builder().activityModule(new ActivityModule("WhoA")).build();
        component.inject(this);

        TextView tv = findViewById(R.id.tv_dagger);
        tv.setText(mUserModel.id + "\n" + mUserModel.name + "\n" + mUserModel.gender + "\n" + mShopModel.shopping()
                + "\n mUserModel1:" + mUserModel.hashCode()

                + "\n\n\n"
                + mUserModel2.id + "\n" + mUserModel2.name + "\n" + mUserModel2.gender + "\n" + mShopModel.shopping()
                + "\n mUserModel2:" + mUserModel2.hashCode());

    }
}
