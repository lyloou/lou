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
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.lyloou.test.R;
import com.lyloou.test.common.LouAdapter;
import com.lyloou.test.util.Utoast;

import java.util.ArrayList;
import java.util.List;

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

        initView();
        initListView();
        reloadData();
    }


    private void initView() {
        EditText etContent = findViewById(R.id.et_content);

        findViewById(R.id.iv_search).setOnClickListener(v -> {
            String content = etContent.getText().toString();
            if (TextUtils.isEmpty(content)) {
                Utoast.show(mContext, "请输入名称或电话");
                reloadData();
                return;
            }
            mAdapter.clear();
            List<LinkMan> manList = readLinkMans();
            for (LinkMan man : manList) {
                if (man.getNumber().contains(content) || man.getName().contains(content)) {
                    mAdapter.addItem(man, true);
                }
            }
        });
    }

    private void initListView() {
        mRefreshLayout = findViewById(R.id.srl_contact);
        mRefreshLayout.setEnabled(true);
        mRefreshLayout.setOnRefreshListener(this::reloadData);

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
        listView.setOnItemClickListener((parent, view, position, id) -> {
            LinkMan item = mAdapter.getItem(position);
            String number = item.getNumber();
            startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:".concat(number))));
        });
    }

    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;

    private void reloadData() {
        mRefreshLayout.setRefreshing(true);
        mAdapter.clear();
        List<LinkMan> manList = readLinkMans();
        for (LinkMan man : manList) {
            mAdapter.addItem(man, true);
        }
        mRefreshLayout.setRefreshing(false);
    }

    private List<LinkMan> readLinkMans() {
        List<LinkMan> manList = new ArrayList<>();
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
                    manList.add(new LinkMan(displayName, number));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return manList;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted
                reloadData();
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
