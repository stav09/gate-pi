
var ws = new WebSocket("ws://stav.space:8080/ws/events/");

ws.onopen = function() {
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
    	$('#button-hold').hide();
        $('#button-release').show();
        $('#gate-holding').show();
    }
    
    if (status.lights_on) {
        $('#lights-on').show();
        $('#button-lights-off').show();
    } else {
        $('#lights-off').show();
        $('#button-lights-on').show();
    }
};

ws.onclose = function() {
    update();
    $('#not-connected').show();
};

ws.onerror = function(err) {
    //console.log("Error: " + err);
    update();
    $('#not-connected').show();
};

var update = function() {
    $('.label').hide();
    $('button').hide();    
}

var send = function(action) {
    $.get("https://stav.space/action/" + action);
}


$(function() {
    
    $('#button-open').click(function() {
        send("GATE_OPEN"); 
    });
    $('#button-close').click(function() {
        send("GATE_CLOSE"); 
    });
    $('#button-lights-on').click(function() {
    	send("LIGHTS_ON");
    });
    $('#button-lights-off').click(function() {
    	send("LIGHTS_OFF");
    });
    $('#button-hold').click(function() {
    	send("GATE_HOLD");
    });
    $('#button-release').click(function() {
    	send("GATE_RELEASE");
    });
    
    update();
    send("STATUS_UPDATE");
});

 
