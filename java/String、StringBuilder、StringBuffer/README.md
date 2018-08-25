

# String、StringBuilder、StringBuffer

**写在前面：** 本文权当笔者学习 Java 过程的记录和总结，因此，若有不严谨、错误之处，还望不吝指出。

**内容：**

1. String
2. StringBuffer 和 StringBuffer
3. 三者比较

## 1. String

先说结论：

> 1. String 类 final 修饰，并且其通过 final char[] 进行存储，因此存储于运行时常量中。故而，String 变量定义时，JVM 会去运行时常量池中检查是否有该字符串，有则指向该字符串，否则开辟空间存储该字符串并将该对象指向该字符串。
> 2. String 字符串的改变，其实质是重新构建了 String 对象，并将该对象指向计算所得的字符串常量。

首先看 `String` 的源码：

```java
public final class String
    implements java.io.Serializable, Comparable<String>, CharSequence {
    /** The value is used for character storage. */
    private final char value[];

    /** Cache the hash code for the string */
    private int hash; // Default to 0

    /** use serialVersionUID from JDK 1.0.2 for interoperability */
    private static final long serialVersionUID = -6849794470754667710L;
    
    ......
    }    
```

可以看到，`String` 为 `final` 类，其存储通过 `private final char value[]` 实现，因此 `String` 为字符串常量，其数值被存储于运行时常量池。

故而执行 `String str = "hello";` 时，JVM 会在运行时常量池中查找 hello 常量是否存在，如果存在则将 str 指向 hello。否则开辟空间存储该 hello 字符串，并将 str 指向 hello。

接下来看 `String` 的成员函数：

```java
public String substring(int beginIndex) {
    if (beginIndex < 0) {
        throw new StringIndexOutOfBoundsException(beginIndex);
    }
    int subLen = value.length - beginIndex;
    if (subLen < 0) {
        throw new StringIndexOutOfBoundsException(subLen);
    }
    return (beginIndex == 0) ? this : new String(value, beginIndex, subLen);
}

public String concat(String str) {
    int otherLen = str.length();
    if (otherLen == 0) {
        return this;
    }
    int len = value.length;
    char buf[] = Arrays.copyOf(value, len + otherLen);
    str.getChars(buf, len);
    return new String(buf, true);
}

......
```

`String` 的成员函数中可以看到，其字符串的改变，无论是 `substring`、`concat` 还是 `str1 + str2` ，其本质上都是重新构造了一个 `String` 对象，并将该对象指向新的字符串常量。而原来的对象则由 JVM 的垃圾回收机制自动回收了。

综合以上两点，就出现了以下常见的程序：

```java
 public static void main(String[] args) {

     String str1 = "hello";
     String str2 = new String("hello");
     String str3 = "hello";
     String str4 = new String("hello");

     System.out.println("str1 == str2 : " + (str1 == str2));
     System.out.println("str1 == str3 : " + (str1 == str3));
     System.out.println("str2 == str3 : " + (str2 == str3));
     System.out.println("str2 == str4 : " + (str2 == str4));

 }
```

得到结果：

```
str1 == str2 : false
str1 == str3 : true
str2 == str3 : false
str2 == str4 : false
```

`str1 == str3` 是因为指向运行时常量池的同一常量。而 `String str2 = new String("hello");` 通过 `new` 操作生成的变量存储于程序的堆区，并且，创建时并不会去检查堆区是否有相同变量。

## 2. StringBuilder 和 StringBuffer

先说结论：

> 1. 与 String 常量字符串不同的是，StringBuilder 和 StringBuffer 皆为变量，并且无论字符串更改多少次，至始至终只 new 了一个对象，因此占用资源更少。
> 2. 两者皆继承 AbstractStringBuilder，StringBuilder 支持 StringBuffer 所有相同操作，并且由于其单线程设计，速度更快。而 StringBuffer 虽慢，但是线程安全。



由于 `String` 的实现方式，造成了如果该字符串改变 100 次，那么就 `new` 了 100 个新的对象出来，这极大耗费了内存。因此便有了 `StringBuilder` 和 `StringBuffer`，无论字符串更改多少次，总共只 `new` 一个对象，因此对于资源的消耗大大减少。

以下为 `StringBuilder` 源码：

```java
public final class StringBuilder
    extends AbstractStringBuilder
    implements java.io.Serializable, CharSequence
{

    /** use serialVersionUID for interoperability */
    static final long serialVersionUID = 4383685877147921099L;
    
    ......
    
    @Override
    public StringBuilder append(Object obj) {
        return append(String.valueOf(obj));
    }

    @Override
    public StringBuilder append(String str) {
        super.append(str);
        return this;
    }
    
    ......
}
```

以下为 `StringBuffer` 源码及注释：

```java
public final class StringBuffer
    extends AbstractStringBuilder
    implements java.io.Serializable, CharSequence
{
 	/**
     * A cache of the last value returned by toString. Cleared
     * whenever the StringBuffer is modified.
     */
    private transient char[] toStringCache;

    /** use serialVersionUID from JDK 1.0.2 for interoperability */
    static final long serialVersionUID = 3388685877147921107L;
    
    @Override
    public synchronized StringBuffer append(Object obj) {
        toStringCache = null;
        super.append(String.valueOf(obj));
        return this;
    }

    @Override
    public synchronized StringBuffer append(String str) {
        toStringCache = null;
        super.append(str);
        return this;
    }
    
    ......
}
```

以上可见，两者皆是继承 `AbstractStringBuilder` ，但是 `StringBuffer` 的成员方法都修饰了 `synchronized`，因此，正如以下 `StringBuffer` 的一段注释所言：

```
/* 
 * ......
 *
 * As of  release JDK 5, this class has been supplemented with an equivalent
 * class designed for use by a single thread, {@link StringBuilder}.  The
 * {@code StringBuilder} class should generally be used in preference to
 * this one, as it supports all of the same operations but it is faster, as
 * it performs no synchronization. 
*/
```

即，`StringBuilder` 类能够支持 `StringBuffer` 的所有相同操作，由于其单线程设计，还更快。而 `StringBuffer` 速度慢，但是线程安全。

## 3. 三者比较

先写结论：

> 1. 速度： String < StringBuffer < StringBuilder （当然，这不是绝对的）
> 2. String 适用于少量字符串操作，StringBuilder 适用于单线程下字符缓冲区大量操作， StringBuffer 适用于多线程下在字符缓冲区进行大量操作。

三者效率比较程序：（摘至 http://www.importnew.com/18167.html ）

```java
public class Main {
    // 循环次数
    private static int time = 50000;
    public static void main(String[] args) {
        testString();
        testStringBuffer();
        testStringBuilder();
        test1String();
        test2String();
    }
 
    public static void testString () {
        String s="";
        long begin = System.currentTimeMillis();
        for(int i=0; i<time; i++){
            s += "java";
        }
        long over = System.currentTimeMillis();
        System.out.println("操作"+s.getClass().getName()+"类型使用的时间为："+(over-begin)+"毫秒");
    }
 
    public static void testStringBuffer () {
        StringBuffer sb = new StringBuffer();
        long begin = System.currentTimeMillis();
        for(int i=0; i<time; i++){
            sb.append("java");
        }
        long over = System.currentTimeMillis();
        System.out.println("操作"+sb.getClass().getName()+"类型使用的时间为："+(over-begin)+"毫秒");
    }
 
    public static void testStringBuilder () {
        StringBuilder sb = new StringBuilder();
        long begin = System.currentTimeMillis();
        for(int i=0; i<time; i++){
            sb.append("java");
        }
        long over = System.currentTimeMillis();
        System.out.println("操作"+sb.getClass().getName()+"类型使用的时间为："+(over-begin)+"毫秒");
    }
 
    public static void test1String () {
        long begin = System.currentTimeMillis();
        for(int i=0; i<time; i++){
            String s = "I"+"love"+"java";
        }
        long over = System.currentTimeMillis();
        System.out.println("字符串直接相加操作："+(over-begin)+"毫秒");
    }
 
    public static void test2String () {
        String s1 ="I";
        String s2 = "love";
        String s3 = "java";
        long begin = System.currentTimeMillis();
        for(int i=0; i<time; i++){
            String s = s1+s2+s3;
        }
        long over = System.currentTimeMillis();
        System.out.println("字符串间接相加操作："+(over-begin)+"毫秒");
    }
 
}
```

执行结果为：

```
操作java.lang.String类型使用的时间为：6736毫秒
操作java.lang.StringBuffer类型使用的时间为：5毫秒
操作java.lang.StringBuilder类型使用的时间为：3毫秒
字符串直接相加操作：1毫秒
字符串间接相加操作：10毫秒
```

## 参考

1, [探秘Java中String、StringBuilder以及StringBuffer](http://www.importnew.com/18167.html)

2, [Java中的String，StringBuilder，StringBuffer三者的区别](https://www.cnblogs.com/su-feng/p/6659064.html)

