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

package com.lyloou.demo.presenter;

import android.app.Activity;

import com.lyloou.demo.bean.UserBean;
import com.lyloou.demo.model.IUserModel;
import com.lyloou.demo.model.UserModel;
import com.lyloou.demo.view.IUserView;
import com.lyloou.lou.util.Utoast;

/**
 * Author:    Lou
 * Version:   V1.0
 * Date:      2016.11.18 14:33
 * <p>
 * Description:
 */
public class UserPresenter {
    private IUserModel mModel;
    private IUserView mView;

    public UserPresenter(IUserView view) {
        mView = view;
        mModel = new UserModel();
    }

    public void saveUser(int id, String firstName, String lastName) {
        mModel.setId(id);
        mModel.setFirstName(firstName);
        mModel.setLastName(lastName);
    }

    public void loadUser(int id) {
        UserBean userBean = mModel.load(id);
        mView.setFirstName(userBean.getFirstName());
        mView.setLastName(userBean.getLastName());
    }

    public void showCurrent(Activity activity) {
        String msg = "id:" + mModel.getId() + "\n" +
                "firstName:" + mModel.load(mModel.getId()).getFirstName() + "\n" +
                "lastName:" + mModel.load(mModel.getId()).getLastName();
        Utoast.show(activity, msg);
    }
}
