/*
 * *****************************************************************************************
 * Copyright  (c) 2016. Lou Li
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

package com.lyloou.demo.presenter;

import com.lyloou.demo.model.IConnectionStatus;
import com.lyloou.demo.model.impl.ConnectionStatus;
import com.lyloou.demo.view.ISplashView;

/**
 * Author:    Lou
 * Version:   V1.0
 * Date:      2016.11.17 19:41
 * <p>
 * Description:
 */
public class SplashPresenter {
    private IConnectionStatus mStatus;
    private ISplashView mView;

    public SplashPresenter(IConnectionStatus status) {
        mStatus = status;
    }

    public SplashPresenter() {
        this(new ConnectionStatus());
    }

    public ISplashView getView() {
        return mView;
    }

    public void setView(ISplashView view) {
        mView = view;
    }

    public void didFinishLoading(){
        ISplashView view = getView();
        if(mStatus.isOnLine()){
            view.moveToMainView();
        } else {
            view.hideProgress();
            view.showNoInetErrorMsg();
        }
    }
}
