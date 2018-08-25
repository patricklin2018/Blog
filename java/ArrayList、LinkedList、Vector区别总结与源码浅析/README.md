# ArrayList、LinkedList、Vector 区别总结与源码浅析

> 写在前面，本文为笔者学习整理总结，因此，若有任何不严谨或错误之处，还望不吝指教！

**本文内容：**

```
一、区别总结
二、源码浅析
	1. ArrayList
	2. LinkedList
	3. Vector
```

## 一、区别总结

* `ArrayList`，底层采用数组构建，因此决定了擅随机访问，弱增删改的特性。不具线程安全。
* `LinkedList`，底层采用链表构建，同时实现了`List`和`Deque`接口，因此还可作双向链表使用。链表决定了其擅增删改，不具随机快速访问的特性。不具线程安全。
* `Vector`，与`ArrayList`同样采用数组构建，并无本质区别，不同的是具备线程安全，相应地降低了执行效率。

## 二、源码浅析

### 1. ArrayList

查看源码，可以看到，`ArrayList` 实现了`List`、`RandomAccess` 等接口，且其底层存储采用的是数组 `Object[] elementData` 构建。因此，决定了 `ArrayList` 善于随机存储，但是增删改效率较低的特点。

```java
public class ArrayList<E> extends AbstractList<E>
        implements List<E>, RandomAccess, Cloneable, java.io.Serializable
{
	/**
     * The array buffer into which the elements of the ArrayList are stored.
     * The capacity of the ArrayList is the length of this array buffer. Any
     * empty ArrayList with elementData == DEFAULTCAPACITY_EMPTY_ELEMENTDATA
     * will be expanded to DEFAULT_CAPACITY when the first element is added.
     */
    transient Object[] elementData; // non-private to simplify nested class access
    
    .......
}
```

`ArrayList` 是个可扩展容器，每次插入时，会去验证剩余空间，当空间不足时进行扩容，源码如下：

```java
private void grow(int minCapacity) {
    // overflow-conscious code
    int oldCapacity = elementData.length;
    int newCapacity = oldCapacity + (oldCapacity >> 1);
    if (newCapacity - minCapacity < 0)
        newCapacity = minCapacity;
    if (newCapacity - MAX_ARRAY_SIZE > 0)
        newCapacity = hugeCapacity(minCapacity);
    // minCapacity is usually close to size, so this is a win:
    elementData = Arrays.copyOf(elementData, newCapacity);
}
```

可以看到，`ArrayList` 每次扩大 1.5 倍，如果扩大后仍比给定的最小容量小，则容量等于给定最小容量。

### 2. LinkedList

查看源码，可以看见 `LinkedList` 实现了 `List` 、`Deque` 双向链表等接口。且其底层的存储结构由链表构建，因此，决定了 `LinkedList` 善于增删改的特性，不善于遍历的特性。

```java
public class LinkedList<E>
    extends AbstractSequentialList<E>
    implements List<E>, Deque<E>, Cloneable, java.io.Serializable
{
    transient int size = 0;

    transient Node<E> first;

    transient Node<E> last;
    
    ......
}
```

`LinkedList` 实现了 `List` 和 `Deque` 两个接口，因此有两套常用操作 `list: add、remove` 和 `deque：offer、poll`，其本质并无区别，除了，队列为空时，`add` 方法会报错，而 `offer` 方法返回 false。

但是，为了规范使用，作为 `List` 用时，一般采用 `add、remove` 等；作为 `Deque` 使用时，则采用 `offer、poll` 方法。

### 3. Vector

首先查看 `Vector` 原码，可见 `Vector` 实现了随机访问接口，且其底层存储由数组构成。

```java
public class Vector<E>
    extends AbstractList<E>
    implements List<E>, RandomAccess, Cloneable, java.io.Serializable
{
    protected Object[] elementData;

    protected int elementCount;
    
    .......
    
}
```

不同的是，在以下方法中：

```java
public synchronized void trimToSize() {...}
public synchronized void ensureCapacity(int minCapacity) {...}
public synchronized int size() {...}
.......
```

增加了 `synchronized`，相较于 `ArrayList` 增加了线程安全，但是降低了计算效率。因此，若单线程程序，采用高效率的 `ArrayList` ，而多线程采用 `Vector`。

