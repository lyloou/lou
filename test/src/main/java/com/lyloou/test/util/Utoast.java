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

package com.lyloou.test.util;

import android.content.Context;
import android.widget.Toast;

public class Utoast {
    private static Toast mToast;

    public static void show(Context context, CharSequence text) {
        show(context, text, 0);
    }

    public static void show(Context context, int resId) {
        show(context, resId, 0);
    }

    public static void show(Context context, CharSequence text, int mode) {
        if (mToast == null) {
            mToast = Toast.makeText(context.getApplicationContext(), text, mode);
        } else {
            mToast.setText(text);
            mToast.setDuration(mode);
        }
        mToast.show();
    }

    public static void show(Context context, int resId, int mode) {

        if (mToast == null) {
            mToast = Toast.makeText(context.getApplicationContext(), resId, mode);
        } else {
            mToast.setText(resId);
            mToast.setDuration(mode);
        }

        mToast.show();
    }

    public void cancel() {
        if (mToast != null) {
            mToast.cancel();
        }
    }
}
