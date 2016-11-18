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

package com.lyloou.demo.model;

import android.util.SparseArray;

import com.lyloou.demo.bean.UserBean;

/**
 * Author:    Lou
 * Version:   V1.0
 * Date:      2016.11.18 14:34
 * <p>
 * Description:
 */
public class UserModel implements IUserModel {
    private int mId;
    private String mFirstName;
    private String mLastName;

    // 注意SparseArray的使用，需要Android支持，所以不能在只运行java的时候用这个类
    private SparseArray<UserBean> mArray = new SparseArray<>();

    @Override
    public void setId(int id) {
        mId = id;
    }

    @Override
    public void setFirstName(String firstName) {
        mFirstName = firstName;
    }

    @Override
    public void setLastName(String lastName) {
        mLastName = lastName;
        UserBean userBean = new UserBean(mFirstName, mLastName);
        mArray.append(mId, userBean);
    }

    @Override
    public int getId() {
        return mId;
    }

    @Override
    public UserBean load(int id) {
        mId = id;
        return mArray.get(mId, new UserBean("not found", "not found"));
    }
}
