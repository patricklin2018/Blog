# [设计模式] 单例模式

本文内容：

```
一、前言
二、实现
   1. 懒汉式
   2. 饿汉式 - 静态常量 和 静态代码块
   3. 静态内部类
   4. 内部枚举
三、代码
```

## 一、前言

**单例模式**：即确保某个类只有一个实例，且通过自行实例化，并用静态方法提供该实例。

**应用场景**：比如专门管理配置信息的类，通常仅允许有单个实例，大家都对该实例进行修改，而不允许自行实例化。

**实现思路**：

1. 构造方法私有化，防止其它方法实例化该对象。
2. 内部自行实例化，并通过静态方法提供其它方法使用该实例。

## 二、实现

### 1. 懒汉式单例

```java
public class Singleton1 {

    private static Singleton1 singleton = null;

    private Singleton1() {}

    // 懒汉式 1 - 锁粒度过大，效率低
    public static synchronized Singleton1 getInstance1() {
        if (singleton == null) {
            singleton = new Singleton1();
        }
        return singleton;
    }

    // 懒汉式 2 - 本质与 懒汉式1 相同，锁粒度大，效率低
    public static Singleton1 getInstance2() {
        synchronized (Singleton1.class) {
            if (singleton == null) {
                singleton = new Singleton1();
            }
        }
        return singleton;
    }

    // 懒汉式 3 - double check locking，减小了锁粒度
    public static Singleton1 getInstance3() {
        if (singleton == null) {
            synchronized (Singleton1.class) {
                if (singleton == null) {
                    singleton = new Singleton1();
                }
            }
        }
        return singleton;
    }
}
```

其中 懒汉式3 - double check locking，具备 lazy-loading，线程安全，效率较高。

### 2. 饿汉式 - 静态常量 和 静态代码块

静态常量：

```java
public class Singleton2 {

    private static Singleton2 singleton = new Singleton2();

    private Singleton2() {}

    public static Singleton2 getInstance() {
        return singleton;
    }

}
```

静态代码块：

```java
public class Singleton3 {

    private static Singleton3 singleton;

    static {
        singleton = new Singleton3();
    }

    private Singleton3() {}

    public static Singleton3 getInstance() {
        return singleton;
    }

}
```

静态常量和静态代码块两个方法本质一样，都是在类加载时，完成实例化，因此缺点也一样，即没有 lazy-loading ，当该实例未使用时，造成了内存的浪费。

### 3. 静态内部类

```java
public class Singleton4 {
    private Singleton4() {}

    private static class SingletonInstance {
        private static final Singleton4 instance = new Singleton4();
    }

    public static Singleton4 getInstance() {
        return SingletonInstance.instance;
    }
}
```

静态内部类与饿汉式的机制类似，不同的是，饿汉式在 Singleton 类加载时就实例化，没有 lazy-loading，而静态内部类在该类加载时并不会立即加载，而是在需要运用到实例化时，即调用 getInstance 时才会装载静态内部类，从而完成实例化。

但是遇到序列化对象时，该得到的结果是多例的。

### 4. 内部枚举

```java
public class Singleton5 {

    private enum EnumSingleton {
        INSTANCE;

        private Singleton5 singleton;

        private EnumSingleton() {
            singleton = new Singleton5();
        }

        public Singleton5 getSingleton() {
            return singleton;
        }
    }

    public static Singleton5 getInstance() {
        return EnumSingleton.INSTANCE.getSingleton();
    }
}
```

解决了多线程同步的问题，还能防止反序列化重新创建新的对象。

## 三、代码：

| 单例模式                | 代码 | 链接                        |
| ----------------------- | ---- | --------------------------- |
| 1.懒汉式                | Java | [link](src/Singleton1.java) |
| 2.饿汉式 1 - 静态变量   | Java | [link](src/Singleton2.java) |
| 2.饿汉式 2 - 静态代码块 | Java | [link](src/Singleton3.java) |
| 3.静态内部类            | Java | [link](src/Singleton4.java) |
| 4.内部枚举              | Java | [link](src/Singleton5.java) |



