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

import com.lyloou.demo.mvp.BasePresenter;
import com.lyloou.demo.mvp.BaseView;

/**
 * Author:    Lou
 * Version:   V1.0
 * Date:      2017.04.13 10:36
 * <p>
 * Description:
 */
public interface UserContract {
    interface View extends BaseView<Presenter>{
        void showId(String id);
        void showFirstName(String firstName);
        void showLastName(String lastName);

        void showSetting();
    }

    interface Presenter extends BasePresenter{
        void save(User user);
        User load(String id);
        void setting();
    }
}
