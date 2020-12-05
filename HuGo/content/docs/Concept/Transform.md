# Transform 算子操作



## Operator

### map 类型转换

- 输入一个参数，产生一个参数
- 对输入的参数进行转换



### flatMap 数据拆分

- 输入一个参数，产生 0、1、n 个参数
- 多用于拆分



### filter 数据过滤

- 对每个元素进行判断，范围 Boolean 值



### [Set] groupBy / [Stream] keyBy 分组 

- 



### distinct

- 去重



### join / outJoin / cross



### reduce





## FAQ 常见问题

### Specifying keys via field positions is only valid for tuple data types. 

#### 错误的代码

```java
// ..
import scala.Tuple2;

// ..

MapOperator<String, Tuple2<String, Integer>> operator =
  dataSet.map((MapFunction<String, Tuple2<String, Integer>>) word -> new Tuple2<>(word, 1));

operator = operator.returns(new TypeHint<Tuple2<String, Integer>>() { });

// ..
```

#### 原因

> 注意导包：java api 使用 `org.apache.flink.api.java.tuple.Tuple2`

```java
import org.apache.flink.api.java.tuple.Tuple2;
// ..
```



### The generic type parameters of xxx are missing

> 完整错误消息
>
> - The generic type parameters of 'Tuple2' are missing. 
> - In many cases lambda methods don't provide enough information for automatic type extraction when Java generics are involved. 
> - An easy workaround is to use an (anonymous) class instead that implements the 'org.apache.flink.api.common.functions.MapFunction' interface. 
> - Otherwise the type has to be specified explicitly using type information.

- 大致含义是，Tuple2 没有泛型信息，这是因为 Java 的 （非定义类型的）泛型在编译后会消除
- 提供了两种解决方案

		1. 使用 匿名类 代替 Lambda 表达式
		2. 明确指定类型

#### 报错的代码

```java
// ..
MapOperator<String, Tuple2<String, Integer>> operator = 
  dataSet.map((MapFunction<String, Tuple2<String, Integer>>) word -> new Tuple2<>(word, 1));

operator.groupBy(0).sum(1).print();
// ..
```

#### 1. 使用 匿名类

```java
// ..
MapOperator<String, Tuple2<String, Integer>> operator =
  dataSet.map(new MapFunction<String, Tuple2<String, Integer>>() {
    @Override
    public Tuple2<String, Integer> map(String word) throws Exception {
      return new Tuple2<>(word, 1);
    }
  });

operator.groupBy(0).sum(1).print();
// ..
```

#### 2. 明确指定类型

> 使用匿名类 的方式，在 IDEA 中会显示为 灰色，使用 Alt + Enter 会自动优化为 Lambda

```java
// ..
MapOperator<String, Tuple2<String, Integer>> operator =
  dataSet.map((MapFunction<String, Tuple2<String, Integer>>) word -> new Tuple2<>(word, 1));

// 1. 读取 TypeHint 的泛型信息
operator = operator.returns(new TypeHint<Tuple2<String, Integer>>() { });
// 2. 或者 Types.TUPLE(xx, xx)
// operator = operator.returns(Types.TUPLE(Types.STRING, Types.INT));

operator.groupBy(0).sum(1).print();
// ..
```

#### Read More

- [flink中使用lambda表达式](https://blog.csdn.net/fu_huo_1993/article/details/103108847)



### No new data sinks have been defined since the last execution

> 完整错误信息
>
> - No new data sinks have been defined since the last execution. 
> - The last execution refers to the latest call to 'execute()', 'count()', 'collect()', or 'print()'.

#### 错误代码

```java
// ...
operator.groupBy(0).sum(1).print();

bEnv.execute("start...");
```

#### 原因

- 对于离线批处理的算子，如：`count()`、 `collect()` 、 `print()` 等既有 Sink 功能，还有触发的功能
- 调用了 `print()` 方法，会自动触发 `execute`，所以最后面的一行执行器没有数据可以执行
- 删掉则不会报错



## Read More

- [Flink 的 Source端 和 Sink端 大全](https://blog.csdn.net/youAreRidiculous/article/details/101192497)