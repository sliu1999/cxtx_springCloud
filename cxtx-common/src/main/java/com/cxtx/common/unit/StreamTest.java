package com.cxtx.common.unit;




import org.springframework.util.StringUtils;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class StreamTest {

   public static void main(String[] args) throws IOException, InterruptedException, ParseException {

       Long timeStamp = System.currentTimeMillis();
       System.out.println(timeStamp);

       SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
       String time="2021-10-28 14:30:00";
       Date date = format.parse(time);
       //日期转时间戳（毫秒）
       long times=date.getTime();
       System.out.print("Format To times:"+times);

       String str= "苏a235g4";
       String str3=str.toUpperCase();//转换成大写
       System.out.println(str3);

    }

    public static void testStream() {

        List<Integer> listFilter = Arrays.asList(1,2,4,5,3,4);
        Stream<Integer> stream = listFilter.stream(); //获取一个顺序流
        stream.filter(s -> s>3).forEach(System.out::println); //filter过滤数据
        System.out.println("*************");
        Arrays.asList(1,2,4,5,3,4).stream().distinct().forEach(System.out::println);//distinct去重
        System.out.println("*************");
        Arrays.asList(1,2,4,5,3,4).stream().skip(2).forEach(System.out::println);//skip跳过前两个
        System.out.println("*************");
        Arrays.asList(1,2,4,5,3,4).stream().limit(2).forEach(System.out::println);//limit前2个
        System.out.println("*************");
        Arrays.asList("a,b,c", "1,2,3").stream().map(s ->
                s.replaceAll(",", "")).forEach(System.out::println);//map：接收一个函数作为参数，该函数会被应用到每个元素上，并将其映射成一个新的元素。
        System.out.println("*************");
        Arrays.asList("a,b,c", "1,2,3").stream().flatMap(s -> {
            //将每个元素转换成一个stream
            String[] split = s.split(",");
            Stream<String> s2 = Arrays.stream(split);
            return s2;
        }).forEach(System.out::println);//flatMap：接收一个函数作为参数，将流中的每个值都换成另一个流，然后把所有流连接成一个流。
        System.out.println("*************");
        Arrays.asList("aa", "ff", "dd").stream().sorted().forEach(System.out::println);// sorted()：自然排序，流中元素需实现Comparable接口

        List<Map> result = new ArrayList<>();
        Collections.sort(result, new Comparator<Map>() {
            @Override
            public int compare(Map o1, Map o2) {
                if(o1.get("createDate")!=null && o2.get("createDate")!=null){
                    return String.valueOf(o2.get("createDate")).compareTo(String.valueOf(o1.get("createDate")));
                }else {
                    return String.valueOf(o2.get("id")).compareTo(String.valueOf(o1.get("id")));
                }

            }
        });
        System.out.println("*************");

        Student s1 = new Student("aa", 10);
        Student s2 = new Student("bb", 20);
        Student s3 = new Student("aa", 30);
        Student s4 = new Student("dd", 40);
        List<Student> studentList = Arrays.asList(s1, s2, s3, s4);

        //自定义排序：先按姓名升序，姓名相同则按年龄升序
        studentList.stream().sorted(
                (o1, o2) -> {
                    if (o1.getName().equals(o2.getName())) {
                        return o1.getAge() - o2.getAge();
                    } else {
                        return o1.getName().compareTo(o2.getName());
                    }
                }
        ).forEach(o->System.out.println(o.getAge()));

        System.out.println("*************");

        List<Student> studentListTwo = Arrays.asList(s1, s2);

        studentListTwo.stream()
                .peek(o -> o.setAge(100))
                .forEach(System.out::println);

        System.out.println("*************");
        /*匹配、聚合操作
        allMatch：接收一个 Predicate 函数，当流中每个元素都符合该断言时才返回true，否则返回false
        noneMatch：接收一个 Predicate 函数，当流中每个元素都不符合该断言时才返回true，否则返回false
        anyMatch：接收一个 Predicate 函数，只要流中有一个元素满足该断言则返回true，否则返回false
        findFirst：返回流中第一个元素
        findAny：返回流中的任意元素
        count：返回流中元素的总个数
        max：返回流中元素最大值
        min：返回流中元素最小值*/

        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5);

        boolean allMatch = list.stream().allMatch(e -> e > 10); //false
        boolean noneMatch = list.stream().noneMatch(e -> e > 10); //true
        boolean anyMatch = list.stream().anyMatch(e -> e > 4);  //true

        Integer findFirst = list.stream().findFirst().get(); //1
        Integer findAny = list.stream().findAny().get(); //1

        long count = list.stream().count(); //5
        Integer max = list.stream().max(Integer::compareTo).get(); //5
        Integer min = list.stream().min(Integer::compareTo).get(); //1
        System.out.println("*************");
       /*  Optional<T> reduce(BinaryOperator<T> accumulator)：第一次执行时，accumulator函数的第一个参数为流中的第一个元素，第二个参数为流中元素的第二个元素；第二次执行时，第一个参数为第一次函数执行的结果，第二个参数为流中的第三个元素；依次类推。
        T reduce(T identity, BinaryOperator<T> accumulator)：流程跟上面一样，只是第一次执行时，accumulator函数的第一个参数为identity，而第二个参数为流中的第一个元素。
        <U> U reduce(U identity,BiFunction<U, ? super T, U> accumulator,BinaryOperator<U> combiner)：在串行流(stream)中，该方法跟第二个方法一样，即第三个参数combiner不会起作用。在并行流(parallelStream)中,我们知道流被fork join出多个线程进行执行，此时每个线程的执行流程就跟第二个方法reduce(identity,accumulator)一样，而第三个参数combiner函数，则是将每个线程的执行结果当成一个新的流，然后使用第一个方法reduce(accumulator)流程进行规约。
*/

        //经过测试，当元素个数小于24时，并行时线程数等于元素个数，当大于等于24时，并行时线程数为16
        List<Integer> listthree = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24);

        Integer v = listthree.stream().reduce((x1, x2) -> x1 + x2).get();
        System.out.println(v);   // 300

        Integer v1 = listthree.stream().reduce(10, (x1, x2) -> x1 + x2);
        System.out.println(v1);  //310

        System.out.println("*************");
        /*Collector 工具库：Collectors*/

        Student s5 = new Student("aa", 10);
        Student s6 = new Student("bb", 20);
        Student s7 = new Student("cc", 10);
        List<Student> listFour = Arrays.asList(s5, s6, s7);


//age装成list
        List<Integer> ageList = listFour.stream().map(Student::getAge).collect(Collectors.toList()); // [10, 20, 10]

//age转成set,被去重了
        Set<Integer> ageSet = listFour.stream().map(Student::getAge).collect(Collectors.toSet()); // [20, 10]
        //转成map,注:key不能相同，否则报错
        Map<String, Integer> studentMap = listFour.stream().collect(Collectors.toMap(Student::getName, Student::getAge)); // {cc=10, bb=20, aa=10}
        //字符串分隔符连接
        String joinName = listFour.stream().map(Student::getName).collect(Collectors.joining(",", "(", ")")); // (aa,bb,cc)
        //聚合操作
//1.学生总数
        Long countt = listFour.stream().collect(Collectors.counting()); // 3
//2.最大年龄 (最小的minBy同理)
        Integer maxAge = listFour.stream().map(Student::getAge).collect(Collectors.maxBy(Integer::compare)).get(); // 20
//3.所有人的年龄
        Integer sumAge = listFour.stream().collect(Collectors.summingInt(Student::getAge)); // 40
//4.平均年龄
        Double averageAge = listFour.stream().collect(Collectors.averagingDouble(Student::getAge)); // 13.333333333333334
// 带上以上所有方法
        DoubleSummaryStatistics statistics = listFour.stream().collect(Collectors.summarizingDouble(Student::getAge));
        System.out.println("count:" + statistics.getCount() + ",max:" + statistics.getMax() + ",sum:" + statistics.getSum() + ",average:" + statistics.getAverage());





//分组
        Map<Integer, List<Student>> ageMap = listFour.stream().collect(Collectors.groupingBy(Student::getAge));

        List<Map<String, String>> listR = new ArrayList<>();
        //按kind分组
        Map<String, List<Map<String, String>>> list2 = listR.stream().collect(Collectors.groupingBy(item -> item.get("kind")));

        Function<Map<String,Object>, String> s = new Function<Map<String,Object>, String>() {

            @Override
            public String apply(Map<String, Object> t) {
                Object object = t.get("merchantsName");
                String string = object.toString();
                return string;
            }
        };
        List<HashMap<String, Object>> formatList = new ArrayList<>();
        Map<String, List<Map<String, Object>>> collect = formatList.stream().collect(Collectors.groupingBy(s));
//多重分组,先根据类型分再根据年龄分
        /*Map<Integer, Map<Integer, List<Student>>> typeAgeMap = listFour.stream().collect(Collectors.groupingBy(Student::getType, Collectors.groupingBy(Student::getAge)));*/

//分区
//分成两部分，一部分大于10岁，一部分小于等于10岁
        Map<Boolean, List<Student>> partMap = listFour.stream().collect(Collectors.partitioningBy(f -> f.getAge() > 10));

//规约
        Integer allAge = listFour.stream().map(Student::getAge).collect(Collectors.reducing(Integer::sum)).get(); //40

    }

}
