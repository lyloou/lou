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

package com.lyloou.test;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.observers.TestObserver;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void testObserver() {
        TestObserver<Integer> test = Observable.range(1, 5).test();
        List<Integer> values = test.values();
        assertEquals(values.size(), 5);
    }

    @Test
    public void testTwoLists() {
        User user1 = new User("U1-F", "U1-L");
        User user2 = new User("U2-F", "U2-L");
        List<User> users1 = new ArrayList<>();
        users1.add(user1);
        users1.add(user2);
        System.out.println(Arrays.toString(users1.toArray()));

        List<User> users2 = new ArrayList<>();
        users2.addAll(users1);
        System.out.println(Arrays.toString(users2.toArray()));
        User user1Changed = users1.get(0);
        user1Changed.firstName = "U1-FC";
        user1Changed.lastName = "U1-LC";

        System.out.println();
        System.out.println(Arrays.toString(users1.toArray()));
        System.out.println(Arrays.toString(users2.toArray()));
    }


}

class User {
    String firstName;
    String lastName;

    public User(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    @Override
    public String toString() {
        return firstName + " " + lastName;
    }
}