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

package com.lyloou.java8;

import java.time.Clock;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * Author:    Lou
 * Version:   V1.0
 * Date:      2017.07.10 13:17
 * <p>
 * Description:
 */
public class DateTime {
    public static void main(String[] args) {
        Clock clock = Clock.systemDefaultZone();
        System.out.println(Date.from(clock.instant()));

        ZoneId zone1 = ZoneId.of("Europe/Berlin");
        ZoneId zone2 = ZoneId.of("Asia/Shanghai");
        System.out.println(zone1.getRules());
        System.out.println(zone2.getRules());

        System.out.println(LocalTime.now(zone1));
        System.out.println(LocalTime.now(zone2));

        System.out.println(String.join("", "d", "d", "k"));

        Stream<String> stream = Stream.of("lou@gmail.com", "abc@gmail.com", "ddk@hotmail.com")
                .filter(Pattern.compile(".*@gmail\\.com").asPredicate());
        stream.forEach(System.out::println);
    }
}
