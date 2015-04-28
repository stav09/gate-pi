var ws = new WebSocket("ws://localhost:8080/events/");

ws.onopen = function() {
    document.write("WebSocket opened <br>");
    ws.send("Hello Server");
};

ws.onmessage = function(evt) {
    document.write("Message: " + evt.data);
};

ws.onclose = function() {
    document.write("<br>WebSocket closed");
};

ws.onerror = function(err) {
    document.write("Error: " + err);
};