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
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.StringJoiner;
import java.util.concurrent.ForkJoinPool;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

/**
 * Author:    Lou
 * Version:   V1.0
 * Date:      2017.06.13 11:08
 * <p>
 * Description:
 */
public class Main {

    public static void main(String[] args) {

        String[] strs = {"a1", "b3", "C1", "d2", "C2", "b2", "e4", "E3", "E3", "e1", "a2"};

        Stream.of(strs)
                .map(String::toUpperCase)
                .peek(s -> {
                    System.out.println("peek:" + s);
                })
                .filter(s -> s.startsWith("A"))
                .forEach(System.out::println);
        ;

    }

    private static void sampleD() {
        String[] strs = {"a1", "b3", "C1", "d2", "C2", "b2", "e4", "E3", "E3", "e1", "a2"};

        Arrays.parallelSort(strs);
        System.out.println(Arrays.toString(strs));

        Arrays.asList("a1", "b3", "C1", "d2", "C2", "b2", "e4", "E3", "E3", "e1", "a2")
                .parallelStream()
                .filter(s -> {
                    System.out.printf("filter: %s [%s]\n", s, Thread.currentThread().getName());
                    return true;
                })
                .map(s -> {
                    System.out.printf("map: %s [%s]\n", s, Thread.currentThread().getName());
                    return s.toUpperCase();
                })
                .sorted((o1, o2) -> {
                    System.out.printf("sorted: %s <> %s [%s]\n", o1, o2, Thread.currentThread().getName());
                    return o1.compareTo(o2);
                })
                .forEachOrdered(s -> {
                    System.out.printf("forEach: %s [%s]\n", s, Thread.currentThread().getName());
                });
//                .forEach(s -> {
//                    System.out.printf("forEach: %s [%s]\n", s, Thread.currentThread().getName());
//                });

        ForkJoinPool forkJoinPool = ForkJoinPool.commonPool();
        System.out.println(forkJoinPool.getParallelism());
    }

    private static void sampleC() {
        List<Person> persons = Arrays.asList(new Person("Max", 18)
                , new Person("Peter", 23)
                , new Person("Pamela", 23)
                , new Person("Davida", 12)
        );

        Integer r = persons
                .parallelStream()
                .reduce(0
                        , (sum, person) -> {
                            System.out.printf("accumulate sum=%s; person=%s\n", sum, person);
                            return sum += person.age;
                        }
                        , ((sum, sum2) -> {
                            System.out.printf("combiner: sum=%s, sum1=%s\n", sum, sum2);
                            return sum + sum2;
                        }));
        System.out.println(r);


        Person result = persons.stream()
                .reduce(new Person("", 0), ((person, person2) -> {
                    person.age += person2.age;
                    person.name += person2.name;
                    return person;
                }));
        System.out.println(result.name);
        System.out.println(result.age);


        persons.stream()
                .reduce(((person, person2) -> person.age > person2.age ? person : person2))
                .ifPresent(System.out::println);
    }

    private static void sampleA() {
        // 如果遇到了 null， 就会停止向下执行
        Optional.of(new Outer())
                .flatMap(outer -> {
                    outer.nested = new Nested();
                    System.out.println("1");
                    return Optional.ofNullable(outer.nested);
                })
                .flatMap(nested -> {
//                    nested.inner = new Inner();
                    System.out.println("2");
                    return Optional.ofNullable(nested.inner);

                })
                .flatMap(inner -> {
                    inner.foo = "hello, world";
                    System.out.println("3");
                    return Optional.ofNullable(inner.foo);
                })
                .ifPresent(System.out::println);
    }

    private static void sample9() {
        String d = null;
        Optional.ofNullable(d).ifPresent(System.out::println);
    }

    private static void sample8() {
        // 感觉这些代码像机关枪一样 😄
        IntStream.range(1, 4)
                .mapToObj(value -> new Foo("Foo" + value))
                .peek(foo ->
                        IntStream.range(1, 4)
                                .mapToObj(value -> new Bar("Bar" + value + " <- " + foo.name))
                                .forEach(bar -> foo.bars.add(bar)))
                .flatMap(foo -> foo.bars.stream())
                .forEach(bar -> System.out.println(bar.name));
    }

    private static void sample7() {
        List<Foo> foos = new ArrayList<>();
        IntStream.range(1, 4)
                .forEach(value -> foos.add(new Foo("Foo" + value)));
        foos.forEach(foo ->
                IntStream.range(1, 4)
                        .forEach(value -> foo.bars.add(new Bar("Bar" + value + " <- " + foo.name))));
        System.out.println(foos);

        foos.stream()
                .flatMap(foo -> {
                    System.out.println(foo.name);
                    return foo.bars.stream();
                })
                .forEach(bar -> System.out.println(bar.name));
    }

    private static void sample6() {
        List<Person> persons = Arrays.asList(new Person("Max", 18)
                , new Person("Peter", 23)
                , new Person("Pamela", 23)
                , new Person("Davida", 12)
        );

        Collector<Person, StringJoiner, String> collector = Collector.of(
                () -> new StringJoiner(" | ")
                , (j, p) -> j.add(p.name.toUpperCase())
                , (j1, j2) -> j1.merge(j2)
                , StringJoiner::toString
        );
        System.out.println(persons.stream().collect(collector));

        String legalPerson = persons.stream()
                .filter(person -> person.age >= 18)
                .map(person -> person.name)
                .collect(Collectors.joining(", ", "In Germany, ", " are of legal age."));

        System.out.println(legalPerson);

        System.out.println(persons.stream().collect(Collectors.averagingInt(person -> person.age)));

        System.out.println(persons);

        Map<Integer, List<Person>> collect = persons.stream()
                .collect(Collectors.groupingBy(person -> person.age));
        collect.forEach((age, p) -> System.out.printf("age %s : %s \n", age, p));

        System.out.println(persons.stream()
                .collect(Collectors.groupingBy(person -> person.age))
        );

        System.out.println(persons.stream()
                .filter(person -> person.name.startsWith("P"))
                .collect(Collectors.toList())
        );
    }

    private static void smaple5() {
        Supplier<Stream<String>> supplier = () -> {
            System.out.println("---------->");
            return Stream.of("d2", "b1", "b3");
        };
        supplier.get().forEach(System.out::println);
        supplier.get().forEach(System.out::println);
        System.out.println(supplier.get().anyMatch(s -> s.startsWith("b")));
        System.out.println(supplier.get().noneMatch(s -> s.startsWith("z")));
    }

    private static void sample4() {
        Stream.of("d2", "b1", "b3", "a3", "c", "a2")
                .filter(s -> s.startsWith("a"))
                .map(s -> {
                    System.out.println(s);
                    return s.toUpperCase();
                })
                .anyMatch(s -> {
                    System.out.println(s);
                    return s.startsWith("A");
                })
        ;
    }

    private static void sample3() {
        Double d = 3.56;
        System.out.println(d.intValue());

        IntStream.range(1, 4)
                .mapToObj(value -> "a" + value)
                .forEach(System.out::println);

        Stream.of("a1", "a2", "a3")
                .map(s -> s.substring(1))
                .mapToInt(Integer::valueOf)
                .max()
                .ifPresent(System.out::println);
    }

    private static void sample2() {
        LongStream.range(1, 10).forEach(System.out::println);
        LongStream.rangeClosed(1, 10).forEach(System.out::println);

        LongStream.of(1, 3, 5, 2, 4, 9, 8, 7)
                .sorted()
                .forEach(System.out::println);

        Stream.of("a1", "a2", "a3")
                .findFirst()
                .ifPresent(System.out::println);
    }

    private static void sample1() {
        List<String> strings = Arrays.asList("a1", "a2", "b1", "c1", "c2");
        strings.stream()
                .filter(s -> s.startsWith("c"))
                .map(String::toUpperCase)
                .sorted((s1, s2) -> s2.compareTo(s1))
                .forEach(System.out::println);

        int i = 1;
        Object[] cs = strings.stream()
                .filter(s -> s.startsWith("c"))
                .map(String::toUpperCase)
                .sorted((s1, s2) -> s2.compareTo(s1 + i))
                .toArray();
//        i = 3; //error, can't be modified.
        System.out.println(Arrays.toString(cs));
    }
}
