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

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;

interface Hourse {
    /**
     * @deprecated use of open is discouraged
     */
    @Deprecated
    void open();

    void openFrontDoor();

    void openBackDoor();
}

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@interface Meals {
    Meal[] value();
}


@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(Meals.class)
@interface Meal {
    String value();

    String mainDish();
}

public class Exercise implements Hourse {
    public static void main(String[] args) throws ClassNotFoundException {
//        new Exercise().open();

        Class<?> aClass = Class.forName("com.lyloou.annotation.Exercise");
        Method[] methods = aClass.getDeclaredMethods();

        for (Method method : methods) {
            if (method.isAnnotationPresent(Meals.class)) {
                Meals annotation = method.getAnnotation(Meals.class);
                Meal[] value1 = annotation.value();
                for (Meal meal : value1) {
                    String value = meal.value();
                    String mainDish = meal.mainDish();
                    System.out.println("value=" + value);
                    System.out.println("mainDish=" + mainDish);
                    System.out.println();
                }

            }
        }
    }

    @Override
    public void open() {
        System.out.println("open");
    }

    @Override
    public void openFrontDoor() {
        System.out.println("openFrontDoor");
    }

    @Override
    public void openBackDoor() {
        System.out.println("openBackDoor");
    }

    @Meal(value = "breakfast", mainDish = "cereal")
    @Meal(value = "launch", mainDish = "pizza")
    @Meal(value = "dinner", mainDish = "salad")
    void evaluateDiet() {

    }
}
