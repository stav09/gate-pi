package au.stav;

public class FtdiException extends RuntimeException {

    public FtdiException(int status) {
        super("Failed with status: " + status);
    }

}
