package au.stav;

public class Main {

    public static void main(String[] args) throws Exception {
        
        new Startup().run();
        
        Thread server = new Thread(new Server());
        server.start();
        server.join();
    }

}
