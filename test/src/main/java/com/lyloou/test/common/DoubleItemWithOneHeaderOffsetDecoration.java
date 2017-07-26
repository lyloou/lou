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

package com.lyloou.test.common;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class DoubleItemWithOneHeaderOffsetDecoration extends RecyclerView.ItemDecoration {
    private int offset;

    public DoubleItemWithOneHeaderOffsetDecoration(int offset) {
        this.offset = offset;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildLayoutPosition(view);
        if (position == 0) {
            return;
        }

        if (position == 1 || position == 2) {
            outRect.top = offset;
        }

        outRect.bottom = offset;
        if (position % 2 == 1) {
            outRect.left = offset;
            outRect.right = offset / 2;
        } else {
            outRect.left = offset / 2;
            outRect.right = offset;
        }
    }
}
