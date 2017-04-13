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

import com.lyloou.demo.data.User;

import java.util.HashMap;

/**
 * Author:    Lou
 * Version:   V1.0
 * Date:      2017.04.13 10:47
 * <p>
 * Description:
 */
public class UserPresenter implements UserContract.Presenter {
    public static final HashMap<String, User> REPO = new HashMap<>();
    private UserContract.View mView;

    public UserPresenter(UserContract.View view) {
        mView = view;
        mView.setPresenter(this);
    }

    @Override
    public void start() {
        load("0");
    }


    @Override
    public void save(User user) {
        REPO.put(user.getId(), user);
    }

    // load的过程包括加载数据 和 显示到视图上；
    @Override
    public User load(String id) {

        User user = REPO.get(id);
        if(user == null){
            user = new User("-1", "f", "l");
        }

        mView.showId(user.getId());
        mView.showFirstName(user.getFirstName());
        mView.showLastName(user.getLastName());
        return user;
    }
}
