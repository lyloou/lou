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

import com.lyloou.demo.presenter.UserPresenter;
import com.lyloou.demo.view.IUserView;

/**
 * Author:    Lou
 * Version:   V1.0
 * Date:      2016.11.18 17:08
 * <p>
 * Description:
 */
public class TestView implements IUserView {
    UserPresenter mPresenter;

    public void setPresenter(UserPresenter presenter) {
        mPresenter = presenter;
    }

    public static void main(String[] args) {
        TestView testView = new TestView();
        testView.setPresenter(new UserPresenter(testView));

        System.out.println("First Name:" + testView.getFirstName());
    }

    @Override
    public int getId() {
        return 0;
    }

    @Override
    public String getFirstName() {
        return "Lou";
    }

    @Override
    public String getLastName() {
        return null;
    }

    @Override
    public void setFirstName(String firstName) {

    }

    @Override
    public void setLastName(String lastName) {

    }
}
