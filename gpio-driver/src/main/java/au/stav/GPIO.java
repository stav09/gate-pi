package au.stav;

import java.util.HashMap;
import java.util.function.Consumer;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;

public class GPIO {

    private static GPIO instance = new GPIO();
    
    private final GpioController controller = GpioFactory.getInstance();
    
    private HashMap<Pin, GpioPinDigitalInput> inputs = new HashMap<>();
    
    private GPIO() {
    }

    public static void initialise(String[] ... args) {
        for (String[] arg : args) {
            Pin pin = RaspiPin.getPinByName(arg[0]);
            GpioPinDigitalInput input = instance.controller.provisionDigitalInputPin(
                pin, PinPullResistance.valueOf(arg[1]));
            instance.inputs.put(pin, input);
        }
    }
 
    public static void onChange(String pinName, Consumer<Boolean> listener) {
        Pin pin = RaspiPin.getPinByName(pinName);
        instance.addListener(pin, (event) -> {
            listener.accept(event.getState().isHigh());
        });
    }
    
    private void addListener(Pin pin, GpioPinListenerDigital listener) {
        inputs.get(pin).addListener(listener);
    }
}
