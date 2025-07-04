## Springboot 引入 WebSocket

### 1. maven

```xml
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-websocket</artifactId>
        </dependency>
```

### 2. 添加 EnableSocket 注解

```java
@SpringBootApplication
@EnableWebSocket
public class RadarApplication {
    public static void main(String[] args) {
        SpringApplication.run(RadarApplication.class, args);
    }
}

```

### 3. 添加 WebSocket config

```java
@Configuration
public class WebSocketConfig {

    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }
}

```

### 4. 编写 WebSocket Endpoint

```java
import javax.websocket.OnClose;  
import javax.websocket.OnError;  
import javax.websocket.OnMessage;  
import javax.websocket.OnOpen;  
import javax.websocket.Session;  
import javax.websocket.server.ServerEndpoint;  
  
@ServerEndpoint(value = "/websocketendpoint", configurator = MyConfigurator.class) // 使用自定义配置器（可选）  
public class MyWebSocketServer {  
  
    // 静态变量，用来记录当前在线连接数。应该使用线程安全的实现，比如AtomicInteger。  
    private static int onlineCount = 0;  
  
    // 与某个客户端的连接会话在这个会话中有效  
    private Session session;  
  
    @OnOpen  
    public void onOpen(Session session) {  
        this.session = session;  
        onlineCount++;  
        System.out.println("有新连接加入！ 当前在线人数为：" + onlineCount);  
    }  
  
    @OnClose  
    public void onClose(Session session) {  
        onlineCount--;  
        System.out.println("有一连接关闭！ 当前在线人数为：" + onlineCount);  
    }  
  
    @OnMessage  
    public String onMessage(String message, Session session) {  
        // 可以在这里处理从客户端接收到的消息  
        // 并将处理结果返回给客户端，或者做其他事情  
        System.out.println("来自客户端的消息: " + message);  
        String echoMsg = "服务器收到消息并返回： " + message;  
        try {  
            // 同步发送消息给客户端  
            session.getBasicRemote().sendText(echoMsg);  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        return null; // 注意：onMessage方法可以有返回值，但这取决于是否使用@ServerEndpoint的subprotocol参数  
    }  
  
    @OnError  
    public void onError(Session session, Throwable throwable) {  
        // 处理错误  
        System.err.println("发生错误 " + throwable.getMessage());  
    }  
  
    // 自定义配置器类（可选），可以配置WebSocket的某些参数  
    // public static class MyConfigurator extends ServerEndpointConfig.Configurator {  
    //     // 自定义配置代码...  
    // }  
}
```

@ServerEndpoint 注解定义了一个WebSocket端点，客户端可以连接到这个端点进行通信。端点的URL路径是/websocketendpoint。

onOpen 方法在WebSocket连接打开时被调用。

onClose 方法在WebSocket连接关闭时被调用。

onMessage 方法在收到客户端发送的消息时被调用。在这个例子中，它只是简单地回显了接收到的消息。

onError 方法在WebSocket连接过程中发生错误时被调用。

注意，这个示例是一个简单的样例，没有包含复杂的并发处理或错误处理逻辑。在实际应用中，可能需要添加更多的逻辑来处理并发连接、消息广播、会话管理等问题。

另外，由于WebSocket通信是异步的，因此通常不需要担心多个线程同时向同一个WebSocket连接发送消息的问题，因为WebSocket API本身已经处理了这些并发问题。然而，如果需要在服务器端进行复杂的并发处理，可能需要使用线程安全的数据结构和并发控制机制来确保线程安全。

## WebSocket API
### session.getAsyncRemote 和 session.getBasicRemote 的区别

在Java WebSocket API（JSR 356）中，`Session` 对象提供了两个方法来获取远程端点（即客户端）的接口：`getAsyncRemote()` 和 `getBasicRemote()`。这两个方法的主要区别在于它们提供的通信模式的同步性和异步性。

1. **getBasicRemote()**：
   这个方法返回的是 `BasicRemote` 接口的实例，它提供了同步发送消息到客户端的方法。当使用 `BasicRemote` 发送消息时，发送操作会阻塞直到消息被发送或发送失败。这种阻塞行为可能会阻止服务器继续处理其他事情，直到发送操作完成。

   示例：
   ```java
   @OnMessage
   public void onMessage(Session session, String message) {
       try {
           session.getBasicRemote().sendText("Echo: " + message);
       } catch (IOException e) {
           e.printStackTrace();
       }
   }
   ```

2. **getAsyncRemote()**：
   这个方法返回的是 `RemoteEndpoint.Async` 接口的实例，它提供了异步发送消息到客户端的方法。使用 `Async` 接口，可以在不阻塞当前线程的情况下发送消息。这对于需要保持高吞吐量和低延迟的应用程序特别有用。

   示例：
   ```java
   @OnMessage q
   public void onMessage(Session session, String message, boolean last) {
       session.getAsyncRemote().sendText("Echo: " + message, new SendHandler() {
           @Override
           public void onResult(Session session, Void result) {
               // 发送操作完成时的回调
           }

           @Override
           public void onFailure(Session session, Throwable t) {
               // 发送操作失败时的回调
           }
       });
   }
   ```

   在上面的例子中，`sendText` 方法接受一个 `SendHandler` 参数，它允许定义发送操作完成或失败时的回调。这样，服务器可以在等待发送操作完成的同时继续处理其他事情。

**总结**：
- `getBasicRemote()` 提供同步通信，发送操作会阻塞直到完成。
- `getAsyncRemote()` 提供异步通信，发送操作不会阻塞当前线程，允许服务器在等待发送完成的同时继续处理其他事情。

选择哪种方式取决于的应用程序的具体需求。如果对性能要求不高，或者的服务器能够容忍阻塞操作，那么 `BasicRemote` 可能是一个更简单、更直接的选择。然而，如果的应用程序需要处理大量的并发连接或需要保持低延迟，那么 `AsyncRemote` 可能是一个更好的选择。