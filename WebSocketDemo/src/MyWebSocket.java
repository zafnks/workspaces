import java.io.IOException;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

//该注解用来指定一个URI，客户端可以通过这个URI来连接到WebSocket。类似Servlet的注解mapping。无需在web.xml中配置。
@ServerEndpoint("/websocket")
public class MyWebSocket {
	// 静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
	private static int onlineCount = 0;

	// concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。若要实现服务端与单一客户端通信的话，可以使用Map来存放，其中Key可以为用户标识
	private static ConcurrentHashMap<String,Session> webSocketMap = new ConcurrentHashMap<String,Session>();

	// 与某个客户端的连接会话，需要通过它来给客户端发送数据
	private Session session;

	/**
	 * 连接建立成功调用的方法
	 * 
	 * @param session
	 *            可选的参数。session为与某个客户端的连接会话，需要通过它来给客户端发送数据
	 * @throws IOException 
	 */
	@OnOpen
	public void onOpen(Session session) throws IOException {
		//this.session = session;
		//webSocketSet.add(this); // 加入set中
		addOnlineCount(); // 在线数加1
		//System.out.println("有新连接加入！当前在线人数为" + getOnlineCount());
		
	}
	

	/**
	 * 连接关闭调用的方法
	 */
	@OnClose
	public void onClose() {
//		webSocketSet.remove(this); // 从set中删除
//		subOnlineCount(); // 在线数减1
//		System.out.println("有一连接关闭！当前在线人数为" + getOnlineCount());
	}

	/**
	 * 收到客户端消息后调用的方法
	 * 
	 * @param message
	 *            客户端发送过来的消息
	 * @param session
	 *            可选的参数
	 * @throws IOException 
	 */
	@OnMessage
	public void onMessage(String message, Session session) throws IOException {
		if(message.startsWith("_login:")){
			webSocketMap.put(message.substring(7), session);
			System.out.println("有新连接加入:"+message.substring(7));
			String msg="_user:";
			for (Entry<String, Session> entry : webSocketMap.entrySet()) {
				msg+= entry.getKey()+">>";
			}
			msg = msg.substring(0,msg.length()-2);
			for (Entry<String, Session> entry : webSocketMap.entrySet()) {
				sendMessage(entry.getValue(),msg);
			}
		}
		if(message.startsWith("_msg:")){
			String fromUser = message.split(">>")[0].substring(5);
			String touser = message.split(">>")[1];
			if("total".equals(touser)){
				for (Entry<String, Session> entry : webSocketMap.entrySet()) {
					sendMessage(entry.getValue(), "_msg:"+fromUser+"对大家说："+message.split(">>")[2]);
				}		
			}else{
				if(webSocketMap.get(touser)==null){
					sendMessage(session,"_err:此用户不在线!");
				}else{
					sendMessage((Session)webSocketMap.get(touser), "_msg:"+fromUser+"对你说："+message.split(">>")[2]);
				}
			}
		}
		System.out.println("来自客户端的消息:" + message);

		// 群发消息
//		for (MyWebSocket item : webSocketSet) {
//			try {
//				item.sendMessage(message);
//			} catch (IOException e) {
//				e.printStackTrace();
//				continue;
//			}
//		}
	}

	/**
	 * 发生错误时调用
	 * 
	 * @param session
	 * @param error
	 */
	@OnError
	public void onError(Session session, Throwable error) {
		System.out.println("发生错误");
		error.printStackTrace();
	}

	/**
	 * 这个方法与上面几个方法不一样。没有用注解，是根据自己需要添加的方法。
	 * 
	 * @param message
	 * @throws IOException
	 */
	public void sendMessage(Session session,String message) throws IOException {
		session.getBasicRemote().sendText(message);
		// this.session.getAsyncRemote().sendText(message);
	}

	public static synchronized int getOnlineCount() {
		return onlineCount;
	}

	public static synchronized void addOnlineCount() {
		MyWebSocket.onlineCount++;
	}

	public static synchronized void subOnlineCount() {
		MyWebSocket.onlineCount--;
	}
}
