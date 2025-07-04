### 1. mybatis-plus 打印执行sql配置
```yaml
mybatis-plus:
  global-config:
    banner: off
    enable-sql-runner: true
  configuration:
    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl
    map-underscore-to-camel-case: false
  mapper-locations: mapper/*.xml
```

### 2. 同一个类中保证事务不失效
```java

AopContext.currentProxy().method(...arg)

```

### 3. 通过 classloader 读取 classpath 下的文件

```java

    InputStream getInputStream(String fileName) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        return classLoader.getResourceAsStream(fileName);
    }
```



### 4. 文件写入工具类

```java
import java.io.FileWriter;  
import java.io.IOException;  
import java.nio.file.Paths;  
  
public class WriteToFileInUserHome {  
  
    public static void main(String[] args) {  
        // 获取用户主目录  
        String userHome = System.getProperty("user.home");  
  
        // 定义要在用户主目录下创建的文件名  
        String fileName = "myfile.txt";  
  
        // 构建文件的完整路径  
        String filePath = Paths.get(userHome, fileName).toString();  
  
        try (FileWriter writer = new FileWriter(filePath, true)) { // 追加模式  
            // 假设你有一个变量data，它包含要写入的数据  
            String data = "这是写入到用户主目录下的新内容";  
              
            // 始终在数据前添加换行符，以确保在新的一行开始  
            writer.write("\n" + data);  
            System.out.println("数据已成功写入文件：" + filePath);  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
    }  
}
```

### 5. swagger 配置

(https://blog.csdn.net/m0_58943936/article/details/136814282)[swagger配置]


### 5. git 查看代码提交行数

```bash
git log --author="lichangxin" --pretty=tformat: --numstat | grep -E "(\d+)" | awk '{ add += $1; subs += $2; loc += $1 - $2 } END { printf "added lines: %s, removed lines: %s, total lines: %s\n", add, subs, loc }'
```

### 6. wait() 和 notify() 必须在同步块或者同步方法中调用

是的，`wait()` 和 `notify()`（以及 `notifyAll()`）方法必须在同步块（synchronized block）或同步方法（synchronized method）内部被调用。这是因为这些方法设计用于在多线程环境下协调线程之间的通信，而同步块或同步方法正是用来保证在同一时刻只有一个线程可以访问某个特定资源（比如一个对象）的机制。

**为什么必须在同步块或同步方法中调用？**

1. **确保线程拥有对象的锁**：`wait()` 方法会让当前线程释放锁，并进入等待状态，直到其他线程在同一对象上调用 `notify()` 或 `notifyAll()` 方法。因此，只有持有该对象锁的线程才能调用 `wait()` 方法。类似地，调用 `notify()` 或 `notifyAll()` 的线程也必须是持有该对象锁的线程，因为这两个方法用于唤醒在该对象上等待的线程。

2. **避免竞态条件**：如果 `wait()` 和 `notify()` 方法可以在没有锁保护的情况下被调用，那么可能会导致竞态条件，即多个线程可能同时修改共享数据或状态，从而引发不一致或错误的结果。

**示例**

```java
public class SharedObject {
    private int value;

    public synchronized void waitForValue() throws InterruptedException {
        while (value == 0) {
            wait(); // 必须在同步块或同步方法中调用
        }
        // 处理 value
    }

    public synchronized void setValue(int value) {
        this.value = value;
        notify(); // 唤醒一个等待的线程
        // 或者 notifyAll(); 唤醒所有等待的线程
    }
}
```

在这个例子中，`waitForValue()` 和 `setValue()` 方法都是同步的，这意味着在同一时刻只有一个线程可以执行这些方法中的任何一个。`wait()` 方法在 `waitForValue()` 方法中被调用，以确保只有持有 `SharedObject` 对象锁的线程才能等待，而 `notify()` 方法在 `setValue()` 方法中被调用，以唤醒在 `waitForValue()` 方法中等待的线程。这种方式确保了线程之间的正确通信和同步。


### 7. 大JSON数组文件,分批读取

jackson 对java8 LocalTimeStamp 处理需要引入
```xml
        <dependency>
            <groupId>com.fasterxml.jackson.datatype</groupId>
            <artifactId>jackson-datatype-jsr310</artifactId>
            <version>2.13.4</version>
        </dependency>
```

```java
File inputFile = new File("");
        JsonFactory factory = new JsonFactory();
        try {
            JsonParser parser = factory.createParser(inputFile);
            // 跳过任何前置内容，直到我们到达数组的开始
            while (!parser.isExpectedStartArrayToken()) {
                parser.nextToken();
            }
            exerciseReplay.setIsBegin(1);
            updateById(exerciseReplay);
            // 现在我们在数组的开头
            while (parser.nextToken() != JsonToken.END_ARRAY) {
                // 假设数组中的每个元素都是一个JSON对象，直接映射到YourEntity
                if (parser.getCurrentToken() == JsonToken.START_OBJECT && ExerciseCommonData.replayStatus) {
                    // 使用ObjectMapper来读取每个对象到YourEntity实例
                    // 注意：这里我们假设不需要自定义反序列化器
                    WsExerciseDto entity = new ObjectMapper().registerModule(new JavaTimeModule()).readValue(parser, WsExerciseDto.class);
                    // 跳过当前对象的其余部分，直到下一个对象或数组结束
                    parser.skipChildren();
                }
            }

        } catch (Exception e) {
            log.error(e.getMessage());
        }
```


### 深拷贝
#### 1. 使用构造函数或拷贝方法

最直接的方式是为需要深拷贝的类提供一个拷贝构造函数或一个拷贝方法，手动复制每个字段。如果类包含其他对象，那么这些对象也需要被深拷贝。

```java
class Address {
    private String street;
    private String city;

    // 构造函数、getter、setter省略

    // 拷贝构造函数
    public Address(Address other) {
        this.street = other.street;
        this.city = other.city;
    }
}

class Person {
    private String name;
    private Address address;

    // 构造函数、getter、setter省略

    // 拷贝方法
    public Person copy() {
        Person newPerson = new Person();
        newPerson.setName(this.name);
        newPerson.setAddress(new Address(this.address)); // 注意这里使用拷贝构造函数
        return newPerson;
    }
}
```

#### 2. 使用序列化

对于复杂对象，特别是包含循环引用的对象，可以通过序列化（Serialization）和反序列化（Deserialization）来实现深拷贝。这种方法相对简单，但要求对象实现`Serializable`接口。

```java
import java.io.*;

public class DeepCopyViaSerialization implements Serializable {

    private static final long serialVersionUID = 1L;

    // 类的字段、方法...

    @SuppressWarnings("unchecked")
    public static <T> T deepCopy(T object) {
        try {
            // 写入到流中
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(bos);
            out.writeObject(object);

            // 从流中读出来
            ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
            ObjectInputStream in = new ObjectInputStream(bis);
            @SuppressWarnings("rawtypes")
            T copiedObj = (T) in.readObject();
            return copiedObj;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}
```

#### 3. 使用第三方库

例如，Apache Commons Lang库中的`SerializationUtils`类提供了基于序列化的深拷贝实现。使用方法类似于上面通过序列化实现的深拷贝，但更为简洁。

```java
import org.apache.commons.lang3.SerializationUtils;

public class DeepCopyWithCommonsLang {

    public static <T> T deepCopy(T object) {
        return SerializationUtils.clone(object);
    }
}
```

注意，使用序列化进行深拷贝时，对象及其所有成员都必须实现`Serializable`接口，且要注意序列化的开销。此外，某些特殊类型的对象（如包含本地方法实现的类）可能不支持序列化。

总的来说，选择哪种深拷贝方式取决于你的具体需求和对象结构。对于简单对象，直接编写拷贝方法可能更直观；对于复杂对象，特别是包含循环引用的对象，使用序列化可能更方便。