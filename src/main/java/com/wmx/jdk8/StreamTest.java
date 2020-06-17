package com.wmx.jdk8;

import org.junit.Test;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * 1、jdk 1.8 的 java.util.Stream 表示能应用在一组元素上一次执行的操作序列。
 * 2、Stream 操作分为中间操作或者最终操作两种，最终操作返回一特定类型的计算结果，而中间操作继续返回 Stream，方便链式操作。
 * 3、Stream 的创建需要指定一个数据源，比如 java.util.Collection 的子类，List 或者 Set， Map不支持。
 * 4、Stream 的操作可以串行 stream() 执行或者并行 parallelStream() 执行。
 *
 * @author wangMaoXiong
 * @version 1.0
 * @date 2020/6/10 20:00
 */
public class StreamTest {

    /**
     * forEach(Consumer<? super T> action) 对该流中的每个元素执行最终操作
     */
    @Test
    public void forEach() {
        List<String> list = Arrays.asList("长沙", "深圳", "武汉", "伊犁", "洛阳", "开封");
        // 方式一：增强 for 循环
        for (String item : list) {
            System.out.print(item + "\t");
        }
        System.out.println();
        // 方式二：JDK 1.8 java.util.stream.Stream.forEach(Consumer<? super T> action)
        //长沙	深圳	武汉	伊犁	洛阳	开封
        list.stream().forEach(item -> System.out.print(item + "\t"));
    }

    /**
     * Stream<T> filter(Predicate<? super T> predicate) 元素过滤，返回符合条件的元素，这是一个中间操作，返回结果流
     */
    @Test
    public void filter() {
        List<Integer> integerList = Arrays.asList(8, 12, 28, 19, 22, 39, 33, 44, 54, 33, 23);
        integerList.stream().filter(integer -> integer >= 18 && integer < 45).forEach(integer -> {
            //28	19	22	39	33	44	33	23
            System.out.print(integer + "\t");
        });
    }

    /**
     * Stream<R> map(Function<? super T, ? extends R> mapper): 元素映射，实质就是用于处理元素，然后返回结果，这是一个中间操作，返回一个结果流
     */
    @Test
    public void map() {
        List<String> list = Arrays.asList("how", "are", "you", ",", "I", "am", "fine", "!");
        //对每个单词的首字母转大写
        Stream<String> stringStream = list.stream().map(item -> {
            if (item.length() > 1) {
                return item.substring(0, 1).toUpperCase() + "" + item.substring(1);
            } else {
                return item;
            }
        });
        stringStream.forEach(item -> System.out.print(item + " "));
    }

    /**
     * Stream<T> of(T... values) ：将集合转为有顺序的流，可将多个集合合成一个流
     * collect(Collector<? super T, A, R> collector)：将流转为集合
     */
    @Test
    public void of() {
        List<Integer> a = Arrays.asList(1, 2, 3);
        List<Integer> b = Arrays.asList(41, 52, 63);
        List<List<Integer>> collect = Stream.of(a, b).collect(Collectors.toList());
        //[[1, 2, 3], [41, 52, 63]]
        System.out.println(collect);
    }

    /**
     * Stream<T> of(T... values) ：参数可以是集合，数组，基本类型，也可以是任何 POJO 对象
     */
    @Test
    public void of2() {
        Stream<String> stream = Stream.of("覆巢之下", "安有完卵", "天下攘攘", "皆为利往");
        stream.forEach(item -> System.out.print(item + "\t"));
    }

    /**
     * Stream<T> distinct() ：对元素进行去重
     */
    @Test
    public void distinct() {
        Stream<String> stream = Stream.of("秦汗", "武汉", "汉武", "武汉", "大楚");
        //秦汗	武汉	汉武	大楚
        stream.distinct().forEach(item -> System.out.print(item + "\t"));
    }

    /**
     * Stream<R> flatMap(Function<? super T, ? extends Stream<? extends R>> mapper) 将多个集合中的元素合并成一个集合
     */
    @Test
    public void flatMap() {
        List<Integer> a = Arrays.asList(1, 2, 3);
        List<Integer> b = Arrays.asList(41, 51, 61);
        Stream<Integer> stream = Stream.of(a, b).flatMap(list -> list.stream());
        //2 4 6 82 102 122
        stream.forEach(item -> {
            item = 2 * item;
            System.out.print(item + " ");
        });
    }

    /**
     * Builder<T> builder(): 创建构建器，用于构建 java.util.stream.Stream
     * Builder<T> add(T t): 向正在生成的流中添加元素。
     * Stream<T> build(): 生成流
     * collect(Collector<? super T, A, R> collector): 将流转为集合
     */
    @Test
    public void builder() {
        Stream<Object> stream = Stream.builder().add("大秦").add("大商").add("大魏").build();
        List<Object> objectList = stream.collect(Collectors.toList());
        //[大秦, 大商, 大魏]
        System.out.println(objectList);
    }


    /**
     * java.util.stream.Collectors :一个有实现{@link Collector} 有用的工具类
     * toList(): 流转 List，还有 toSet()、toMap 等
     */
    @Test
    public void collectors() {
        List<String> list = Arrays.asList("a", "b", "c", "d", "e");
        Set<String> collect = list.stream().collect(Collectors.toSet());
        //a b c d e
        collect.stream().forEach(item -> System.out.print(item + " "));
    }

    /**
     * Stream<T> sorted(Comparator<? super T> comparator) :排序操作
     * {@link Comparator} 用于比较的函数，naturalOrder 顺序排序，reverseOrder 倒序排序
     */
    @Test
    public void sorted() {
        List<String> list = Arrays.asList("c", "e", "a", "d", "b");
        // a	b	c	d	e
        list.stream().sorted(Comparator.naturalOrder()).forEach(item -> System.out.print(item + "\t"));
        System.out.println("\n倒序：");
        // e	d	c	b	a
        list.stream().sorted(Comparator.reverseOrder()).forEach(item -> System.out.print(item + "\t"));
    }

    /**
     * long count() :统计流中元素个数
     */
    @Test
    public void stu7() {
        Stream<String> stream = Stream.of("c", "e", "a", "d", "b");
        long count = stream.count();
        System.out.println(count);
    }

    /**
     * Optional<T> min(Comparator<? super T> comparator) ：获取元素中的最小值
     * Optional<T> max(Comparator<? super T> comparator) ：获取元素中的最大值
     */
    @Test
    public void minAndMax() {
        List<Integer> list = Arrays.asList(31, 22, 133, 465, 125);
        // Optional<T> min(Comparator<? super T> comparator);
        Optional<Integer> optional = list.stream().min(Comparator.naturalOrder());
        Optional<Integer> optional2 = list.stream().max(Comparator.naturalOrder());
        Integer value1 = optional.get();
        Integer value2 = optional2.get();
        //22	465
        System.out.println(value1 + "\t" + value2);
    }

    /**
     * Stream<T> skip(long n) : 表示跳过 n 个元素，单 n 超过实际个数时，返回空的流
     * Stream<T> limit(long maxSize): 表示截取 maxSize 个元素，当 maxSize 超过实际个数时，则最多截取实际长度
     */
    @Test
    public void skipAndLimit() {
        List<String> list = Arrays.asList("a", "b", "c", "d", "e", "f", "g");
        //c d e
        list.stream().skip(2).limit(3).forEach(item -> System.out.print(item + " "));
    }

    /**
     * Stream<T> concat(Stream<? extends T> a, Stream<? extends T> b): 拼接两个流为一个流
     */
    @Test
    public void concat() {
        List<String> list = Arrays.asList("a", "b");
        List<Integer> list2 = Arrays.asList(110, 120);
        Stream<Object> concatStream = Stream.concat(list.stream(), list2.stream());
        //a b 110 120
        concatStream.forEach(item -> System.out.print(item + " "));
    }

    /**
     * Stream<E> parallelStream() 可以并行计算，速度比 stream 更快
     * boolean anyMatch(Predicate<? super T> predicate): 只要流中的任意一个元素满足条件，则返回 true
     */
    @Test
    public void anyMatch() {
        List<String> list = Arrays.asList("长沙", "长安", "常州", "昌平");
        boolean result1 = list.parallelStream().anyMatch(item -> item.equals("长安"));
        boolean result2 = list.parallelStream().anyMatch(item -> item.equals("西安") || item.contains("安"));
        //true	true
        System.out.println(result1 + "\t" + result2);
    }

    /**
     * boolean allMatch(Predicate<? super T> predicate): 流中的元素全部满足条件时，返回 true
     */
    @Test
    public void allMatch() {
        List<Integer> list = Arrays.asList(22, 34, 55, 43, 28);
        boolean allMatch1 = list.stream().allMatch(item -> item > 18);
        boolean allMatch2 = list.parallelStream().allMatch(item -> item > 28);
        //true,false
        System.out.println(allMatch1 + "," + allMatch2);
    }


    @Test
    public void reduce() {
        Stream<String> stream = Stream.of("you", "give", "me", "stop");
        // Optional<T> reduce(BinaryOperator<T> accumulator);
        Optional<String> optional = stream.reduce((before, after) -> before + "," + after);
        // you,give,me,stop
        optional.ifPresent(System.out::println);
    }

    @Test
    public void reduce2() {
        List<BigDecimal> list = Arrays.asList(
                new BigDecimal("11.11"),
                new BigDecimal("22.22"),
                new BigDecimal("33.33")
        );
        // 66.66
        BigDecimal sum = list.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
        System.out.println(sum);
    }

    @Test
    public void findFirst() {
        Stream<String> stream = Stream.of("you", "give", "me", "stop");
        String value = stream.findFirst().get();
        System.out.println(value);
    }

    @Test
    public void findAny() {
        Stream<String> stream = Stream.of("you", "give", "me", "stop");
        String value2 = stream.findAny().get();
        System.out.println(value2);
    }

    /**
     * Stream API 提供数字流 numbers Stream，包括 IntStream、DoubleStream、
     * 和LongStream我们通过创建一个数字流来来搞清楚它们是如何工作的。然后，我们用 [IntStream#sum] 计算它的总数
     */
    @Test
    public void test1() {
        IntStream intNumbers = IntStream.range(1, 5);
        int sum = intNumbers.sum();
        System.out.println(sum);
    }

    /**
     * 使用 mapToDouble 将对象流转换为Double stream
     */
    @Test
    public void test2() {
        List<Double> doubleNumbers = Arrays.asList(23.48, 52.26, 13.5);
        double result = doubleNumbers.stream()
                .mapToDouble(Double::doubleValue)
                .sum();
        System.out.println(result);
    }

    @Test
    public void test3() {
        Stream<Integer> intNumbers = Stream.of(5, 1, 100);
        int result = intNumbers.reduce(0, Integer::sum);
        System.out.println(result);
    }

    /**
     * reduce方法有两个参数：
     * <p>
     * Identity – 等于0–它是还原的起始值
     * Accumulator function – 接受两个参数，目前为止的结果，以及流的下一个元素
     */
    @Test
    public void test4() {
        Stream<BigDecimal> bigDecimalNumber =
                Stream.of(BigDecimal.ZERO, BigDecimal.ONE, BigDecimal.TEN);
        BigDecimal result = bigDecimalNumber.reduce(BigDecimal.ZERO, BigDecimal::add);
        System.out.println(result);
    }
}
