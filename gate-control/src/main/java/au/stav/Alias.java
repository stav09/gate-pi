package au.stav;


public class Alias {

    public class Input {
    
        public static final String RC_CHANNEL_1 = "GPIO 5";
        public static final String RC_CHANNEL_2 = "GPIO 6";
        public static final String GATE_FULLY_CLOSED = "GPIO 7";
        public static final String GATE_OPENED = "GPIO 14";
        
    }
    
    public class Output {
        
        public static final int GATE_OPEN = 1;
        public static final int GATE_HOLD = 2;
        public static final int LIGHTS = 3;
        public static final int UNUSED = 4;
        
    }    
}
