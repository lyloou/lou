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

package com.lyloou.test.contact;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.lyloou.test.R;
import com.lyloou.test.common.LouAdapter;

import java.util.ArrayList;

public class ContactActivity extends AppCompatActivity {

    private Activity mContext;
    private LouAdapter<LinkMan> mAdapter;

    private SwipeRefreshLayout mRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_contact);
        setMyTitle("联系人");

        initListView();
        loadData();
    }

    private void initListView() {
        mRefreshLayout = findViewById(R.id.srl_contact);
        mRefreshLayout.setEnabled(true);
        mRefreshLayout.setOnRefreshListener(() -> {
            mAdapter.clear();
            loadData();
        });

        ListView listView = findViewById(R.id.lv_contact);
        mAdapter = new LouAdapter<LinkMan>(listView, android.R.layout.simple_list_item_2) {
            @Override
            protected void assign(ViewHolder holder, LinkMan s) {
                holder.putText(android.R.id.text1, String.valueOf(s.getName()));
                holder.putText(android.R.id.text2, String.valueOf(s.getNumber()));
            }

            @Override
            protected boolean contain(LinkMan o) {
                return getList().contains(o);
            }
        };
        mAdapter.initList(new ArrayList<>());
    }

    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;

    private void loadData() {
        mRefreshLayout.setRefreshing(true);
        // [Permission Denial: opening provider com.android.providers.contacts.ContactsProvider2 from ProcessRecord in Android Studio - Stack Overflow](https://stackoverflow.com/questions/29915919/permission-denial-opening-provider-com-android-providers-contacts-contactsprovi)
        // Check the SDK version and whether the permission is already granted or not.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
            //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method
        } else {
            try (Cursor cursor = getContentResolver()
                    .query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null)) {
                while (cursor != null && cursor.moveToNext()) {
                    String displayName = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                    String number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    mAdapter.addItem(new LinkMan(displayName, number), true);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        mRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted
                loadData();
            } else {
                Toast.makeText(this, "Until you grant the permission, we cannot display the names", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void setMyTitle(String title) {
        ActionBar supportActionBar = getSupportActionBar();
        if (null != supportActionBar) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
            supportActionBar.setTitle(title);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
