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

import com.lyloou.test.kingsoftware.NetWork;

import org.junit.Test;

import java.util.concurrent.CountDownLatch;

/**
 * Author:    Lou
 * Version:   V1.0
 * Date:      2017.06.26 19:04
 * <p>
 * Description:
 */
public class KingsoftwareTest {

    @Test
    public void test() {
        CountDownLatch latch = new CountDownLatch(1);
        NetWork.getKingsoftwareApi()
                .getDaily("2017-06-26")
                .subscribe(daily -> {
                    System.out.println(daily.getFenxiang_img());
                    latch.countDown();
                }, throwable -> {
                    throwable.printStackTrace();
                    latch.countDown();
                });

        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
