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

package com.lyloou.stream;

import java.util.ArrayList;
import java.util.List;

/**
 * Author:    Lou
 * Version:   V1.0
 * Date:      2017.06.14 11:14
 * <p>
 * Description:
 */
public class Bar {
    String name;

    public Bar(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}

class Foo {
    String name;
    List<Bar> bars = new ArrayList<>();

    public Foo(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name + ":" + bars;
    }
}
