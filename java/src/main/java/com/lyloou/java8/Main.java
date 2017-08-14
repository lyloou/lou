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
import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.ChronoUnit;
import java.time.zone.ZoneRules;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static java.util.Optional.of;

/**
 * Author:    Lou
 * Version:   V1.0
 * Date:      2017.06.12 10:31
 * <p>
 * Description:
 */
public class Main {

    public static void main(String[] args) {
        String joinStr = String.join(":", "a", "b", "c");
        System.out.println(joinStr);
    }

    private static void sampleI() {
        Hints annotation = Person1.class.getAnnotation(Hints.class);
        Hint[] hints1 = annotation.value();
        System.out.println(hints1.length);

        Hint[] hints = Person1.class.getAnnotationsByType(Hint.class);
        System.out.println(hints.length);
    }

    private static void sampleH() {
        Clock clock = Clock.systemDefaultZone();
        System.out.println(clock.millis());
        Date from = Date.from(clock.instant());
        System.out.println(from);

        Set<String> availableZoneIds = ZoneId.getAvailableZoneIds();
        System.out.println(availableZoneIds);
        ZoneId zongShanghai = ZoneId.of("Asia/Shanghai");
        ZoneRules rules = zongShanghai.getRules();
        System.out.println(rules);
        LocalTime now1 = LocalTime.now(zongShanghai);
        System.out.println(now1);

        ZoneId zongMonaco = ZoneId.of("Europe/Monaco");
        LocalTime now2 = LocalTime.now(zongMonaco);
        System.out.println(now2);

        System.out.println(now1.isBefore(now2));

        System.out.println(ChronoUnit.HOURS.between(now1, now2));
        System.out.println(ChronoUnit.MINUTES.between(now1, now2));

        System.out.println(LocalTime.of(23, 29));

        DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT).withLocale(Locale.GERMAN);
        LocalTime leetTime = LocalTime.parse("13:37", formatter);
        System.out.println(leetTime);

        LocalDate independenceDay = LocalDate.of(1992, Month.NOVEMBER, 16);
        DayOfWeek dayOfWeek = independenceDay.getDayOfWeek();
        System.out.println(dayOfWeek);
        System.out.println(independenceDay.getDayOfYear());
        System.out.println(ChronoUnit.DAYS.between(independenceDay, LocalDate.now().minusDays(0)));

        LocalDateTime localDateTime = LocalDateTime.now();
        System.out.println(localDateTime.toString());
        Instant instant = localDateTime.atZone(ZoneId.systemDefault()).toInstant();
        System.out.println(Date.from(instant));
    }

    private static void sampleG() {
        Map<Integer, String> map = new HashMap<>();
        for (int i = 0; i < 10; i++) {
            map.putIfAbsent(i, "#" + i);
        }

        map.forEach((i, s) -> System.out.println(s));
        map.computeIfPresent(3, (i, s) -> i + s);
        map.forEach((i, s) -> System.out.println(s));

        map.computeIfPresent(9, (i, s) -> null);
        map.forEach((i, s) -> System.out.println(s));
        System.out.println(map.containsKey(9));

        map.computeIfAbsent(23, i -> "#" + i);
        map.forEach((i, s) -> System.out.println(s));
        System.out.println(map.containsKey(23));

        System.out.println(map.get(3));
        map.put(3, null);
        map.computeIfAbsent(3, i -> "wowowo");
        System.out.println(map.get(3));

        map.getOrDefault(13, "not found!");

        map.merge(9, "#9", (s1, s2) -> s1.concat(s2)); // 如果没有元素9，则直接put进去，否则进行concat操作
        System.out.println(map.get(9));
        map.merge(9, "#9", (s1, s2) -> s1.concat(s2)); // 如果没有元素9，则直接put进去，否则进行concat操作
        System.out.println(map.get(9));

        Map<Integer, Float> floatMap = new HashMap<>();
        System.out.println(floatMap.getOrDefault(3, 3.0f));
    }

    private static void sampleF() {
        int max = 1000000; // 当max比较小的时候，适合使用stream， 当max比较大的时候时候使用parallelStream；
        List<String> list = new ArrayList<>(max);
        for (int i = 0; i < max; i++) {
            list.add(UUID.randomUUID().toString());
        }
        long time1 = System.currentTimeMillis();
        long count = list.stream().sorted().count();
        System.out.println(count);
        long time2 = System.currentTimeMillis();
        System.out.println("TIME:" + (time2 - time1));


        long time3 = System.currentTimeMillis();
        long countParallel = list.parallelStream().sorted().count();
        System.out.println(countParallel);
        long time4 = System.currentTimeMillis();
        System.out.println("TIME:" + (time4 - time3));
    }

    private static void sampleE() {
        List<String> list = new ArrayList<>();
        list.add("dd2");
        list.add("bd2");
        list.add("sd2");
        list.add("cd2");
        list.add("ad2");
        list.add("qd2");
        list.add("rd2");
        list.add("qd2");
        list.add("qd2");
        list.add("cd2");
        list.add("pd2");
        list.add("od2");
        list.add("jd2");

        list.stream()
                .filter(s -> s.startsWith("q"))
                .map(String::toUpperCase)
                .sorted((s1, s2) -> s2.compareTo(s1))
                .forEach(System.out::println);

        list.forEach(System.out::println);

        System.out.println(list.stream().anyMatch(s -> s.startsWith("j")));
        System.out.println(list.stream().allMatch(j -> j.endsWith("d2")));
        System.out.println(list.stream().noneMatch(j -> j.endsWith("z")));
        System.out.println(list.stream().filter(s -> s.startsWith("q")).count());

        list.stream().reduce((s1, s2) -> s1 + s2).ifPresent(System.out::println);
    }

    private static void sampleD() {
        Optional<String> optional = of("boom");
        System.out.println(optional.isPresent());
        System.out.println(optional.isPresent() ? optional.get() : "null");
        System.out.println(optional.orElse("hi"));
        optional.ifPresent((s) -> System.out.println(s.charAt(0)));

        Optional<String> empty = Optional.<String>empty();
        System.out.println(empty);
        System.out.println(empty.isPresent());
        empty.ifPresent(System.out::println); // 不会执行

        System.out.println(Optional.<String>of("ddk"));
        System.out.println(Optional.<String>of(""));
    }

    private static void sampleC() {
        Comparator<Person> comparator = (p1, p2) -> p1.firstName.compareTo(p2.firstName);
        comparator = comparator.reversed();
        int compare = comparator.compare(new Person("Luke", "Sky"), new Person("Li", "lou"));
        System.out.println(compare);


        Comparator<String> comparator1 = (s1, s2) -> s2.compareTo(s1);
        List<String> list = Arrays.asList("3", "2", "b", "a", "A", "C", "1");
        Collections.sort(list, comparator1);
        System.out.println(Arrays.toString(list.toArray()));

        Collections.sort(list, comparator1.reversed());
        System.out.println(Arrays.toString(list.toArray()));
    }

    private static void sampleB() {
        Consumer<Person> consumer = person -> System.out.println(person.firstName);
        consumer.accept(new Person("Luke", "Skywalking"));
        consumer.accept(new Person("Luke1", "Skywalking"));
        consumer.accept(new Person("Luke2", "Skywalking"));
    }

    private static void sampleA() {
        Supplier<Person> supplier = () -> new Person("F", "L");
        Person x = supplier.get();
        System.out.println(x);
        x.lastName = "Lou";
        x.firstName = "Li";
        System.out.println(x);
    }

    private static void sample9() {
        Function<String, Integer> function = Integer::valueOf;
        Function<String, String> andThen = function.andThen(String::valueOf);
        System.out.println(andThen.andThen(Float::new).apply("223"));
        System.out.println(andThen.apply("111"));
    }

    private static void sample8() {
        Predicate<String> s = s1 -> s1.length() > 2;
        System.out.println(s.test("jj"));
        System.out.println(s.negate().test("jj"));
        System.out.println(Predicate.<String>isEqual("jj").test("jj"));
    }

    private static void sample7() {
        Formula f = (a -> a * a);
        System.out.println(f.calculate(3));
        Formula f1 = new Formula() {
            @Override
            public double calculate(int a) {
                return sqrt(a * 100);
            }
        };
        System.out.println(f1.calculate(9));
    }

    private static void sample6() {
        new Lambda6().testScope();
    }

    private static void sample5() {
        int num = 1;
        Converter<Integer, String> converter = (from) -> String.valueOf(from + num); // num 隐含着是final，如果想要对其修改就会编译错误
        System.out.println(converter.convert(32));
    }

    private static void sample4() {
        PersonFactory<Person> factory = Person::new;
        Person person = factory.create("Li", "Lou");
        System.out.println(person);
    }

    private static void sample3() {
        Converter<Integer, String> s = (from) -> from.toString();
        System.out.println(s.convert(123));

        Converter<String, Integer> s2 = (from) -> Integer.valueOf(from);
        System.out.println(s2.convert("332"));

        Converter<String, Float> s3 = Float::valueOf;
        System.out.println(s3.convert("33.2"));

        class Something {
            String startWith(String str) {
                return String.valueOf(str.charAt(0));
            }
        }
        Something sth = new Something();
        Converter<String, String> s4 = sth::startWith;
        System.out.println(s4.convert("java"));
    }

    private static void sample2() {
        List<String> list = Arrays.asList("peter", "anna", "mike", "xenia");
        System.out.println("before:" + Arrays.toString(list.toArray()));
        Collections.sort(list);
        System.out.println("after:" + Arrays.toString(list.toArray()));
        Collections.sort(list, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o2.compareTo(o1);
            }
        });
        System.out.println("after2:" + Arrays.toString(list.toArray()));

        ArrayList<String> strings = new ArrayList<>(list);
        strings.add("alou");
        strings.add("jim");
        Collections.sort(strings, (s1, s2) -> s2.compareTo(s1));
        System.out.println("after3:" + Arrays.toString(strings.toArray()));
    }

    private static void sample1() {
        Formula formula = new Formula() {
            @Override
            public double calculate(int a) {
                return sqrt(a * 100);
            }
        };

        System.out.println(formula.calculate(9));
        System.out.println(formula.sqrt(9));
    }

}
