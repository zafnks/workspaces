var websocket = null;
var myUser='';
function login() {
	// 判断当前浏览器是否支持WebSocket
	if ('WebSocket' in window) {
		websocket = new WebSocket("ws://" + $('#address').val()
				+ "/WebSocketDemo/websocket");
		// 连接发生错误的回调方法
		websocket.onerror = function() {
			setMessageInnerHTML("error");
		};

		// 连接成功建立的回调方法
		websocket.onopen = function(event) {
			websocket.send('_login:' + $('#name').val());
			myUser = $('#name').val();
		};

		// 接收到消息的回调方法
		websocket.onmessage = function() {
			setMessageInnerHTML(event.data);
		};

		// 连接关闭的回调方法
		websocket.onclose = function() {
			setMessageInnerHTML("close");
		};

		// 监听窗口关闭事件，当窗口关闭时，主动去关闭websocket连接，防止连接还没断开就关闭窗口，server端会抛异常。
		window.onbeforeunload = function() {
			websocket.close();
		};
	} else {
		alert('Not support websocket');
	}
}

// 将消息显示在网页上
function setMessageInnerHTML(msg) {
	if (msg.startsWith('_user:')) {
		$('#user').html('');
		var user = msg.substring(6).split('>>');
		for (var i = 0; i < user.length; i++) {
			$('#user').html($('#user').html()+user[i] + '<br/>');
		}
	}
	if (msg.startsWith('_msg:')) {
		var msg = msg.substring(5);
		$('#msg').html($('#msg').html() + "<br/>" + msg);
	}
	if (msg.startsWith('_err:')) {
		var err = msg.substring(5);
		alert(err);
	}
}

// 关闭连接
function closeWebSocket() {
	websocket.close();
}

// 发送消息
function send() {
	var message;
	if ($('#toUser').val() == '') {
		message = '_msg:'+myUser+'>>'+'total>>';
		$('#msg').html($('#msg').html() + "<br/>" + "你对大家说:"+$('#text').val());
	} else {
		message = '_msg:' +myUser+'>>'+ $('#toUser').val() + '>>';
		$('#msg').html($('#msg').html() + "<br/>" + "你对"+$('#toUser').val()+"说:"+$('#text').val());
	}
	message += $('#text').val();
	websocket.send(message);
}