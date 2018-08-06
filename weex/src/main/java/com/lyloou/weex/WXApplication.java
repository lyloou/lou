/*
 * Copyright  (c) 2018 Lyloou
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

package com.lyloou.weex;

import android.app.Application;
import com.lyloou.weex.extend.ImageAdapter;
import com.taobao.weex.InitConfig;
import com.taobao.weex.WXSDKEngine;

public class WXApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        initWeexConfig();
    }

    private void initWeexConfig() {
        InitConfig config = new InitConfig.Builder().setImgAdapter(new ImageAdapter()).build();
        WXSDKEngine.initialize(this, config);
    }
}