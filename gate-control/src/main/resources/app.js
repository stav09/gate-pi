
var ws = new WebSocket("ws://" + window.location.host + "/ws/events/");

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

$(function() {
    $('#button-open').click(function() {
        ws.send("GATE_OPEN"); 
    });
    $('#button-close').click(function() {
        ws.send("GATE_CLOSE"); 
    });
    $('#button-lights-on').click(function() {
    	ws.send("LIGHTS_ON");
    });
    $('#button-lights-off').click(function() {
    	ws.send("LIGHTS_OFF");
    });
    $('#button-hold').click(function() {
    	ws.send("GATE_HOLD");
    });
    $('#button-release').click(function() {
    	ws.send("GATE_RELEASE");
    });
    update();
});

 
