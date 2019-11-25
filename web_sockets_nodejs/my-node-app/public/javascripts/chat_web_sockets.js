let webSocket;

let user = {};

function connect(id, name) {
    document.cookie = 'X-Authorization=' + id + ';path=/';
    webSocket = new WebSocket('ws://localhost:8080/chat');

    webSocket.onmessage = function receiveMessage(response) {
        let data = response['data'];
        let json = JSON.parse(data);
        $('#messagesList').first().after("<li>" + json['username'] + " " +  json['message'] + "</li>");
    };

    webSocket.onerror = function errorShow() {
        alert('Ошибка авторизации');
    };
    webSocket.onopen = () => sendMessage(name, "Hello!")
}

function sendMessage(from, text) {
    let message = {
        "username": from,
        "message": text
    };

    webSocket.send(JSON.stringify(message));
}

function getId(username) {
    $.ajax({
        type: 'get',
        url: 'http://localhost:8080/register',
        data: {"username": username}
    }).done(function (data) {
        user = data;
        connect(data.token, username);
        document.getElementById("id").innerHTML = 'Ваш id ' + data.token;
    })
}

function getHistory() {
    $.ajax({
        type: 'get',
        url: 'http://localhost:8080/getHistory',
        data: { }
    }).done(function (data) {
        for (let i = 0; i < data.length; i++) {
            $('#messagesList').first().after("<li>" + data[i].username + " " +  data[i].message + "</li>")
        }

    })
}