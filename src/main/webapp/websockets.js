/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
var ws;
var ws2;

function connect() {
    var username = document.getElementById("username").value;
    
    var host = document.location.host;
    var pathname = document.location.pathname;
    
    ws = new WebSocket("ws://" +host  + pathname + "chat/" + username);

    ws.onmessage = function(event) {
    var log = document.getElementById("log");
        console.log(event.data);
        var message = JSON.parse(event.data);
        log.innerHTML += message.from + " : " + message.content + "\n";
    };
}

function send() {
    var content = document.getElementById("msg").value;
    
    var json = JSON.stringify({
        "content":content
    });
    
    

    ws.send(json);
}

function connect2() {
    var username = document.getElementById("username2").value;
    
    var host = document.location.host;
    var pathname = document.location.pathname;
    
    ws2 = new WebSocket("ws://" +host  + pathname + "chat2/" + username);

    ws2.onmessage = function(event) {
    var log = document.getElementById("log2");
        console.log(event.data);
        var message = JSON.parse(event.data);
        log.innerHTML += message.from + " : " + message.content + "\n";
    };
}

function send2() {
    var content = document.getElementById("msg2").value;
    
    var json = JSON.stringify({
        "content":content
    });
    
    

    ws2.send(json);
}

