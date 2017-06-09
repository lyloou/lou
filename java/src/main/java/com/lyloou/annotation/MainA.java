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

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Author:    Lou
 * Version:   V1.0
 * Date:      2017.05.19 11:35
 * <p>
 * Description:
 */
public class MainA {

    @SuppressWarnings("deprecation")
    public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException {
        parseTypeAnnotation();
        System.out.println("==================");
        parseMethodAnnotation();
        System.out.println("==================");
        parseConstructorAnnotation();
        System.out.println("==================");
        parseFieldAnnotation();
    }

    public static void parseTypeAnnotation() throws ClassNotFoundException {
        Class clazz = Class.forName("com.lyloou.annotation.UseA");
        Annotation[] annotations = clazz.getAnnotations();
        for (Annotation annotation : annotations) {
            AnnotationA a = (AnnotationA) annotation;
            System.out.println("name=" + a.name() + " id=" + a.id() + " gid=" + a.gid());
        }
    }


    public static void parseMethodAnnotation() throws ClassNotFoundException {

//        Method[] methods = UseA.class.getMethods();
        Method[] methods = UseA.class.getDeclaredMethods();
        for (Method method : methods) {
            if (method.isAnnotationPresent(AnnotationA.class)) {
                AnnotationA a = method.getAnnotation(AnnotationA.class);
                System.out.println("name=" + a.name() + " id=" + a.id() + " gid=" + a.gid());
            }

        }
    }

    @Deprecated
    public static void parseConstructorAnnotation() throws ClassNotFoundException, NoSuchMethodException {

        Constructor[] constructors = UseA.class.getConstructors();
//        Constructor[] constructors = UseA.class.getDeclaredConstructors();
        for (Constructor constructor : constructors) {
            boolean annotationPresent = constructor.isAnnotationPresent(AnnotationA.class);
            if (annotationPresent) {
                AnnotationA a = (AnnotationA) constructor.getAnnotation(AnnotationA.class);
                System.out.println("name=" + a.name() + " id=" + a.id() + " gid=" + a.gid());
            }
        }

    }

    public static void parseFieldAnnotation() throws ClassNotFoundException, NoSuchMethodException {

//        Field[] fields = UseA.class.getFields();
        Field[] fields = UseA.class.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(AnnotationA.class)) {
                AnnotationA a = field.getAnnotation(AnnotationA.class);
                System.out.println("name=" + a.name() + " id=" + a.id() + " gid=" + a.gid());
            }
        }
    }
}
