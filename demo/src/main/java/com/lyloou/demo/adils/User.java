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

package com.lyloou.demo.adils;


import android.os.Parcel;
import android.os.Parcelable;

/**
 * Author:    Lou
 * Version:   V1.0
 * Date:      2017.07.13 09:52
 * <p>
 * Description:
 */
public class User implements Parcelable {
    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
    public int userId;
    public String userName;
    public boolean isMail;
    public Book book;

    public User(int userId, String userName, boolean isMail) {
        this.userId = userId;
        this.userName = userName;
        this.isMail = isMail;
    }

    protected User(Parcel in) {
        userId = in.readInt();
        userName = in.readString();
        isMail = in.readInt() == 1;
        book = in.readParcelable(Thread.currentThread().getContextClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(userId);
        dest.writeString(userName);
        dest.writeInt(isMail ? 1 : 0);
        dest.writeParcelable(book, 0);
    }
}
