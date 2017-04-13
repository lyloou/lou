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

package com.lyloou.demo.data;

/**
 * Author:    Lou
 * Version:   V1.0
 * Date:      2017.04.13 10:20
 * <p>
 * Description:
 */
public class User {
    private final String mId;
    private String mFirstName;
    private String mLastName;

    public User(String mId, String mFirstName, String mLastName) {
        this.mId = mId;
        this.mFirstName = mFirstName;
        this.mLastName = mLastName;
    }

    public String getId() {
        return mId;
    }

    public String getFirstName() {
        return mFirstName;
    }

    public String getLastName() {
        return mLastName;
    }

    public void setFirstName(String firstName) {
        mFirstName = firstName;
    }

    public void setLastName(String lastName) {
        mLastName = lastName;
    }
}
