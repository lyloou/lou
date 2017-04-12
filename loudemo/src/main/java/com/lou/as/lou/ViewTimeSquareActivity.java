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

package com.lou.as.lou;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.squareup.timessquare.CalendarPickerView;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ViewTimeSquareActivity extends AppCompatActivity {

    @BindView(R.id.cpv_timesquare)
    CalendarPickerView mCpvTimesquare;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_time_square);
        ButterKnife.bind(this);
        initView();
    }

    // [TimesSquare for Android](https://github.com/square/android-times-square)
    private void initView() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, 2);

        Date today = new Date();
        mCpvTimesquare.init(today, calendar.getTime(), Locale.CHINA).withSelectedDate(today);
    }

}
