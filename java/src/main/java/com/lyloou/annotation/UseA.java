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

package com.lyloou.annotation;

/**
 * Author:    Lou
 * Version:   V1.0
 * Date:      2017.05.19 14:12
 * <p>
 * Description:
 */
@AnnotationA(name = "class name", id = 0, gid = Long.class)
public class UseA {

    @AnnotationA(name = "field", id = -1, gid = Long.class)
    private String field1;
    @AnnotationA(name = "field", id = -2, gid = Long.class)
    protected String field2;
    @AnnotationA(name = "field", id = -3, gid = Long.class)
    String field3;
    @AnnotationA(name = "field", id = -4, gid = Long.class)
    public String field4;

    @AnnotationA(name = "constructor", id = -0, gid = Long.class)
    public UseA() {

    }

    @AnnotationA(name = "method private", id = 1, gid = Long.class)
    private void method1() {

    }

    @AnnotationA(name = "method protected", id = 2, gid = Long.class)
    protected void method2() {

    }

    @AnnotationA(name = "method default", id = 3, gid = Long.class)
    void method3() {

    }

    @AnnotationA(name = "method public", id = 4, gid = Long.class)
    public void method4() {

    }

    public void method5() {

    }
}
