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

package com.lyloou.demo.persistence.sqlite;

import android.provider.BaseColumns;

/**
 * Author:    Lou
 * Version:   V1.0
 * Date:      2017.07.17 15:06
 * <p>
 * Description:
 */
public class PhraseEntry implements BaseColumns {
    public static final String COLEUM_NAME_ID = "id";
    public static final String COLEUM_NAME_CONTENT = "content";
    public static final String COLEUM_NAME_FAVORITE = "favorite";
}
