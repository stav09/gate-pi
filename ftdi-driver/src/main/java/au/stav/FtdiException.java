package au.stav;

public class FtdiException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public FtdiException(int status) {
        super("Failed with status: " + status);
    }

}
