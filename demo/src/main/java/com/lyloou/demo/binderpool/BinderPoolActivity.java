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

package com.lyloou.demo.binderpool;

import android.content.Context;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.lyloou.demo.R;

public class BinderPoolActivity extends AppCompatActivity {

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_binderpool);

        new Thread(new Runnable() {
            @Override
            public void run() {
                testBinderPool();
            }
        }).start();
    }

    private void testBinderPool() {
        BinderPool binderPool = BinderPool.getInstance(mContext);
        IBinder computeBinder = binderPool.queryBinder(BinderPool.BINDER_COMPUTE);
        ICompute compute = ICompute.Stub.asInterface(computeBinder);

        IBinder securityCenterBinder = binderPool.queryBinder(BinderPool.BINDER_SECURITY_CENTER);
        ISecurityCenter securityCenter = ISecurityCenter.Stub.asInterface(securityCenterBinder);

        try {
            System.out.println(compute.add(3, 4));
            String encrypt = securityCenter.encrypt("你好啊");
            System.out.println("加密后："+encrypt);
            String decrypt = securityCenter.decrypt(encrypt);
            System.out.println("解密后："+decrypt);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
