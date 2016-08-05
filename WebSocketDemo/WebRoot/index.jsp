<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">

<title>WebSocketClient</title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
</head>

<body>
	<br />
	服务器地址：<input id="address" type="text" /><br />
	用户名：<input id="name" type="text" />
	<button onclick="login()">连接</button>
	<br />
	在线用户：
	<div style="border:1px dashed #000" id="user"></div>
	<br/>
	发送对象:
	<input id="toUser" type="text" />
	<br/>
	发送内容:
	<input id="text" type="text" />
	<button onclick="send()">发送</button>
	<button onclick="closeWebSocket()">断开连接</button>
	<div style="border:1px dashed #000" id="msg" ></div>
</body>

<script type="text/javascript" src="<%=basePath%>/js/jquery.min.js"></script>
<script type="text/javascript" src="<%=basePath%>/js/ws.js"></script>
</html>
