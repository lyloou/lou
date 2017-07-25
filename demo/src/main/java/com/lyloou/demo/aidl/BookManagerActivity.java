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

package com.lyloou.demo.aidl;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.TextView;

import com.lyloou.demo.R;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class BookManagerActivity extends AppCompatActivity {
    TextView mTextView;
    IBookManager mIBookManager;
    IOnNewBookArrivedListener mIOnNewBookArrivedListener = new IOnNewBookArrivedListener.Stub() {

        @Override
        public void onNewBookArrived(final Book book) throws RemoteException {
            mTextView.post(new Runnable() {
                @Override
                public void run() {
                    System.out.println("新书来了：" + book);
                    String s = mTextView.getText().toString();
                    mTextView.setText(s + "\n\n" + book);
                }
            });
        }
    };
    ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            mIBookManager = IBookManager.Stub.asInterface(iBinder);
            if (mIBookManager == null) {
                return;
            }

            try {
                mIBookManager.registerListener(mIOnNewBookArrivedListener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }

            Flowable
                    .fromCallable(new Callable<List<Book>>() {
                        @Override
                        public List<Book> call() throws Exception {
                            return mIBookManager.getBookList();
                        }
                    })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<List<Book>>() {
                        @Override
                        public void accept(@NonNull List<Book> books) throws Exception {
                            System.out.println(Arrays.toString(books.toArray()));

                            String oldText = mTextView.getText().toString() + "\n\n";
                            String newText = TextUtils.join("\n\n", books);
                            String result = oldText + newText;
                            mTextView.setText(result);
                        }
                    });
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mIBookManager = null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmanager);
        mTextView = findViewById(R.id.tv_book);
        bindService(new Intent(this, BookManagerService.class), mServiceConnection, BIND_AUTO_CREATE);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 如果在 onServiceDisconnected 中添加下面代码，那么就来不及注销
        if (mIBookManager != null && mIBookManager.asBinder().isBinderAlive()) {
            try {
                mIBookManager.unregisterListener(mIOnNewBookArrivedListener);
                System.out.println("unregister listener:" + mIOnNewBookArrivedListener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        unbindService(mServiceConnection);
    }

}
