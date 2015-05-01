
var ws = new WebSocket("ws://" + window.location.host + "/ws/events/");

ws.onopen = function() {
    //document.write("WebSocket opened <br>");
    //ws.send("Hello Server");
    update();
    $('#connected').show();    
};

ws.onmessage = function(evt) {
    var status = $.parseJSON(evt.data);
    update();
    
    if (status.gate_closed) {
        $('#gate-closed').show();
        $('#button-open').show();
    } else {
        $('#gate-open').show();
        $('#button-hold').show();
    }
    
    if (status.gate_opened) {
        $('#button-close').show();
    }
    
    if (status.gate_hold) {
        $('#button-release').show();
    }
    
    if (status.lights_on) {
        $('#light-on').show();
        $('#button-lights-off').show();
    } else {
        $('#light-off').show();
        $('#button-lights-on').show();
    }
};

ws.onclose = function() {
    update();
    $('#not-connected').show();
};

ws.onerror = function(err) {
    //document.write("Error: " + err);
    update();
    $('#not-connected').show();
};

var update = function() {
    $('.label').hide();
    $('button').hide();    
    $('button-open').click(function() {
        ws.send("GATE_OPEN"); 
    });
}

$(update);

 
