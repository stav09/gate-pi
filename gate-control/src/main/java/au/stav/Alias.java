package au.stav;


public class Alias {

    public class Input {
    
        public static final String RC_CHANNEL_1 = "GPIO_05";
        public static final String RC_CHANNEL_2 = "GPIO_06";
        public static final String GATE_FULLY_CLOSED = "GPIO_07";
        public static final String GATE_OPENED = "GPIO_14";
        
    }
    
    public class Output {
        
        public static final int GATE_OPEN = 1;
        public static final int GATE_HOLD = 2;
        public static final int LIGHTS = 3;
        public static final int UNUSED = 4;
        
    }    
}
