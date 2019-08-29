# 【重回基础】序列化、序列化攻击与序列化代理

## 目录

[TOC]

## 一、what、why、how 序列化

**什么是序列化？**简单讲就是将对象按照序列化协议编码成字节流，相反的过程就称为反序列化。譬如我们常见的JSON序列化：

```java
public class A {
	   private int x = 1;
	   private String y = "2";
}
```

经过JSON序列化为：

```json
{
	"x" : 1,
	"y" : "2"
}
```

**为什么需要序列化？**简单讲就是在对象进行传输、存储时压缩空间，并且做到语言无关。通讯双方只需按照约定的序列化协议进行序列化/反序列化，而无需关注对方用的是什么变成语言。

**怎么序列化？**现有的序列化协议很多，比如xml、json、fastjson、protobuf、protostuff等。除此之外，还有Java经常接触的JDK序列化（JDK序列化是无法跨语言）。这些序列化方式各有长短，非本篇重点，不再赘述，感兴趣可以看 [几种流行的序列化协议比较](https://www.cnblogs.com/wkcode/p/10431096.html)。

## 二、JDK 序列化并不简单

JDK 序列化很简单，只要在类的声明中增加 `implements Serializable` ，实现可序列化接口即可。但是正因为简单，经常可以看到被随处滥用。实际上JDK序列化是复杂的，并且为了序列化的开销是长期的。

为什么？

**第一，降低了类的灵活性，类的演变受到限制**。一旦实现可序列化，其序列化的字节流就像是API的一部分，你必须一直支持序列化/反序列化，如果其中某个通讯方修改了该类结构并发布出去，将会出现不兼容，进而导致错误。

另外，类中还有个序列版本UID(serial version UID)，反序列化时，会首先根据UID进行版本确认，若版本不一致则反序列化失败，抛出InvalidClassException异常。该UID若无显示提供，则会在运行时结合类名、所有公有和受保护的成员名称计算生成。这就是建议实现可序列化显示提供UID的原因，因为倘若其中某个通讯方在该类上增加了某个无关的变量或方法，同样会使得隐式生成的UID不一致，进而导致不兼容异常，其次隐式生成的计算也是一笔不小的开销。

**第二，增加出现BUG和安全漏洞可能性**。反序列化机制就像一个“隐式构造器”，若没有采用一定措施保证，很容易被攻击者利用，构造出违反“真正构造器”的约束关系。

**第三，随着实现可序列化类的新版本发布，相关测试负担增加**。

## 三、序列化攻击

既然序列化是将对象转换成字节流，反序列化将该字节流恢复为对象，那中间的字节流是否可以伪造？答案是肯定的：

比如，我们的对象`Period`限制了成员日期变量`start`必须要在`end`之前：

```java
public class Period implements Serializable {
    private static final long serialVersionUID = 4647424730390249716L;
    private Date start;
    private Date end;
    public Period(Date start, Date end) {
        if (start.after(end)) {
            throw new IllegalArgumentException();
        }
        this.start = start;
        this.end = end;
    }
    @Override
    public String toString() {
        return "PeriodA{" +
                "start=" + start +
                ", end=" + end +
                '}';
    }
}

```

现在我们伪造了如下字节流：

```java
public class SerializeTest {
    private static final byte[] serializedForm = new byte[] {
            (byte)0xac, (byte)0xed, 0x00, 0x05, 0x73, 0x72, 0x00, 0x06,
            0x50, 0x65, 0x72, 0x69, 0x6f, 0x64, 0x40, 0x7e, (byte)0xf8,
            0x2b, 0x4f, 0x46, (byte)0xc0, (byte)0xf4, 0x02, 0x00, 0x02,
            0x4c, 0x00, 0x03, 0x65, 0x6e, 0x64, 0x74, 0x00, 0x10, 0x4c,
            0x6a, 0x61, 0x76, 0x61, 0x2f, 0x75, 0x74, 0x69, 0x6c, 0x2f,
            0x44, 0x61, 0x74, 0x65, 0x3b, 0x4c, 0x00, 0x05, 0x73, 0x74,
            0x61, 0x72, 0x74, 0x71, 0x00, 0x7e, 0x00, 0x01, 0x78, 0x70,
            0x73, 0x72, 0x00, 0x0e, 0x6a, 0x61, 0x76, 0x61, 0x2e, 0x75,
            0x74, 0x69, 0x6c, 0x2e, 0x44, 0x61, 0x74, 0x65, 0x68, 0x6a,
            (byte)0x81, 0x01, 0x4b, 0x59, 0x74, 0x19, 0x03, 0x00, 0x00,
            0x78, 0x70, 0x77, 0x08, 0x00, 0x00, 0x00, 0x66, (byte)0xdf,
            0x6e, 0x1e, 0x00, 0x78, 0x73, 0x71, 0x00, 0x7e, 0x00, 0x03,
            0x77, 0x08, 0x00, 0x00, 0x00, (byte)0xd5, 0x17, 0x69, 0x22,
            0x00, 0x78
    };
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Period p = (Period) deserialize(serializedForm);
        System.out.println(p);
    }
    public static Object deserialize(byte[] sf) {
        try {
            InputStream is = new ByteArrayInputStream(sf);
            ObjectInputStream ois = new ObjectInputStream(is);
            return ois.readObject();
        } catch (Exception e) {
            throw new IllegalArgumentException(e.toString());
        }
    }
}
```

通过反序列化结果为：

```
PeriodA{start=Sat Jan 02 04:00:00 CST 1999, end=Mon Jan 02 04:00:00 CST 1984}
```

已然出现前面所言的反序列化这个“隐式构造器”构建出了一个违反我们构造约束关系的对象，`start`晚于`end`，这对于程序来说可能十分危险。至于这个字节流如何伪造，可以看看《Java Object Serialization Specification》，其中有关于序列化格式的描述。

因此，effetive java中多次强调，实现可序列化的类一定要编写 `readObject` 方法，并且确保约束关系。

```java
private void readObject(ObjectInputStream stream) throws IOException,ClassNotFoundException{
   stream.defaultReadObject();
   if (start.after(end)) {
     throw new IllegalArgumentException();
   }
}
```

但是，尽管这么做了依然可以通过伪造字节流去打破约束关系，就是字节流除了提供一个有效的Period对象，额外加上两个引用，这两个引用分别指向两个成员变量的实例，这样在实例化后就可以通过这两个引用肆意操作对象。下面演示：

```java
public class MutablePeriod {
    // 有效period对象
    public final Period period;
    // 两个额外的引用
    public final Date start;
    public final Date end;

    public MutablePeriod() {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(bos);
            out.writeObject(new Period(new Date(), new Date()));
            // 附上额外引用
            byte[] ref = { 0x71, 0, 0x7e, 0, 5 };
            bos.write(ref);
            ref[4] = 4;
            bos.write(ref);

            ObjectInputStream in = new ObjectInputStream(
                    new ByteArrayInputStream(bos.toByteArray()));
            period = (Period) in.readObject();
            start = (Date) in.readObject();
            end = (Date) in.readObject();
        } catch (Exception e) {
            throw new AssertionError(e);
        }
    }

    public static void main(String[] args) {
        MutablePeriod mp = new MutablePeriod();
        Period p = mp.period;
        Date pEnd = mp.end;
      
        pEnd.setYear(78);
        System.out.println(p);
        pEnd.setYear(69);
        System.out.println(p);
    }
}
```

结果为：

```
PeriodA{start=Fri Aug 23 12:26:53 CST 2019, end=Wed Aug 23 12:26:53 CST 1978}
PeriodA{start=Fri Aug 23 12:26:53 CST 2019, end=Sat Aug 23 12:26:53 CST 1969}
```

发生上面问题的根源在于readObject方法没有进行保护性拷贝，即构造时，新建成员变量对象，并将反序列化出来的对象进行保护性拷贝到新建成员变量对象，这样，攻击者额外的两个引用修改的就不是实例化对象中的变量：

```java
 private void readObject(ObjectInputStream stream) throws IOException,ClassNotFoundException {
   stream.defaultReadObject();
   // 保护性拷贝
   start = new Date(start.getTime());
   end = new Date(end.getTime());
   if (start.after(end)) {
     throw new IllegalArgumentException();
   }
}
```

需要注意的是，保护性拷贝要在检验约束关系之前，并且不是使用clone等浅拷贝方式。

除此之外，还有更常用的方法，那就是序列化代理模式，见下文。

## 四、序列化代理模式

序列化代理十分简单，即套上了一个可序列化的私有静态类的壳，这个壳就叫做序列化代理，其拥有一个构造器，该构造器的参数即被代理类，在构造时复制被代理类的参数。序列化时通过提供`writeReplace`，实际上序列化的是代理类，并且在`readObject`接口拒绝直接序列化，只允许通过代理反序列化。而代理类通过提供`readResolve`反序列化为被代理类。具体见代码：

```java
public class Period implements Serializable {

    private static final long serialVersionUID = 4647424730390249716L;
    private Date start;
    private Date end;

    public Period(Date start, Date end) {
        if (start.after(end)) {
            throw new IllegalArgumentException();
        }
        this.start = start;
        this.end = end;
    }

    @Override
    public String toString() {
        return "PeriodA{" +
                "start=" + start +
                ", end=" + end +
                '}';
    }
    public Date getStart() {
        return start;
    }
    public Date getEnd() {
        return end;
    }

    private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
        // 不允许直接反序列化，只能通过反序列化代理实例化
        throw new InvalidObjectException("只允许通过代理反序列化");
    }
    private Object writeReplace() {
        // 序列化代理
        return new SerializeProxy(this);
    }
		
    // 序列化代理类
    private class SerializeProxy implements Serializable {
        private final Date start;
        private final Date end;
        // 通过构造复制代理类变量
        public SerializeProxy(Period period) {
            this.start = period.getStart();
            this.end = period.getEnd();
        }
        // 反序列化为被代理类
        private Object readResolve() {
            return new Period(start, end);
        }
    }

}
```

## 参考

[1] Effective Java 第十一章

